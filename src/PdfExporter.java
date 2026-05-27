import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Minimal PDF exporter without external libraries.
 * Writes a single-page PDF with basic text (Helvetica).
 */
public final class PdfExporter {
    private PdfExporter() {}

    public static void exportSimpleReport(Path target, String title, List<String> lines) throws IOException {
        Files.createDirectories(target.getParent() == null ? Path.of(".") : target.getParent());

        StringBuilder content = new StringBuilder();
        content.append("BT\n/F1 14 Tf\n50 800 Td\n");
        content.append(escapePdf(title)).append(" Tj\n");
        content.append("0 -24 Td\n");
        content.append("/F1 11 Tf\n");
        for (String line : lines) {
            content.append(escapePdf(line)).append(" Tj\n0 -14 Td\n");
        }
        content.append("ET\n");
        byte[] streamBytes = content.toString().getBytes(StandardCharsets.US_ASCII);

        // Build PDF objects
        String obj1 = "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n";
        String obj2 = "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n";
        String obj3 = "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n";
        String obj4Header = "4 0 obj\n<< /Length " + streamBytes.length + " >>\nstream\n";
        String obj4Footer = "\nendstream\nendobj\n";
        String obj5 = "5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n";

        try (OutputStream raw = Files.newOutputStream(target);
             OutputStream os = counting(raw)) {
            os.write("%PDF-1.4\n".getBytes(StandardCharsets.US_ASCII));
            long o1 = writeObj(os, obj1);
            long o2 = writeObj(os, obj2);
            long o3 = writeObj(os, obj3);
            long o4 = osPosition(os);
            os.write(obj4Header.getBytes(StandardCharsets.US_ASCII));
            os.write(streamBytes);
            os.write(obj4Footer.getBytes(StandardCharsets.US_ASCII));
            long o5 = writeObj(os, obj5);

            long xref = osPosition(os);
            String xrefTable = "xref\n0 6\n" +
                    "0000000000 65535 f \n" +
                    offsetLine(o1) +
                    offsetLine(o2) +
                    offsetLine(o3) +
                    offsetLine(o4) +
                    offsetLine(o5) +
                    "trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n" + xref + "\n%%EOF\n";
            os.write(xrefTable.getBytes(StandardCharsets.US_ASCII));
        }
    }

    private static String escapePdf(String s) {
        if (s == null) return "()";
        String v = s.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
        return "(" + v + ")";
    }

    // OutputStream does not expose position; we track via wrapper-like approach with a counting stream.
    private static long osPosition(OutputStream os) throws IOException {
        if (os instanceof CountingOutputStream) return ((CountingOutputStream) os).count;
        return 0;
    }

    private static long writeObj(OutputStream os, String obj) throws IOException {
        long pos = osPosition(os);
        os.write(obj.getBytes(StandardCharsets.US_ASCII));
        return pos;
    }

    private static String offsetLine(long offset) {
        String s = String.valueOf(offset);
        String padded = "0000000000".substring(Math.min(10, s.length())) + s;
        return padded + " 00000 n \n";
    }

    public static OutputStream counting(OutputStream os) {
        return new CountingOutputStream(os);
    }

    private static final class CountingOutputStream extends OutputStream {
        private final OutputStream inner;
        private long count = 0;
        CountingOutputStream(OutputStream inner) { this.inner = inner; }
        @Override public void write(int b) throws IOException { inner.write(b); count++; }
        @Override public void write(byte[] b) throws IOException { inner.write(b); count += b.length; }
        @Override public void write(byte[] b, int off, int len) throws IOException { inner.write(b, off, len); count += len; }
        @Override public void flush() throws IOException { inner.flush(); }
        @Override public void close() throws IOException { inner.close(); }
    }
}

