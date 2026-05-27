import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Simple permanent storage using files (CSV + properties).
 * No external libraries and no database needed.
 */
public final class FileStore {
    private final Path dataDir;
    private final Path studentsCsv;
    private final Path adminProps;
    private final Path historyDir;

    public FileStore(Path projectRoot) {
        this.dataDir = projectRoot.resolve("data");
        this.studentsCsv = dataDir.resolve("students.csv");
        this.adminProps = dataDir.resolve("admin.properties");
        this.historyDir = dataDir.resolve("history");
    }

    public void ensureDataFiles() throws IOException {
        Files.createDirectories(dataDir);
        if (!Files.exists(studentsCsv)) {
            Files.writeString(studentsCsv, "id,name,marks,createdAt\n", StandardCharsets.UTF_8);
        }
        Files.createDirectories(historyDir);
    }

    public boolean adminExists() throws IOException {
        ensureDataFiles();
        return Files.exists(adminProps);
    }

    public void saveAdminCredentials(String username, String password) throws IOException {
        ensureDataFiles();
        Properties p = new Properties();
        String salt = SecurityUtil.newSaltHex(16);
        String hash = SecurityUtil.sha256Hex(salt + ":" + password);
        p.setProperty("username", username);
        p.setProperty("salt", salt);
        p.setProperty("passwordHash", hash);
        try (OutputStream os = Files.newOutputStream(adminProps)) {
            p.store(os, "GradeBook PRO EDITION - Admin Credentials (hashed)");
        }
    }

    public AdminRecord loadAdminRecord() throws IOException {
        ensureDataFiles();
        Properties p = new Properties();
        try (InputStream is = Files.newInputStream(adminProps)) {
            p.load(is);
        }
        return new AdminRecord(
                p.getProperty("username", "").trim(),
                p.getProperty("salt", "").trim(),
                p.getProperty("passwordHash", "").trim()
        );
    }

    public boolean validateLogin(String username, String password) throws IOException {
        if (!adminExists()) return false;
        AdminRecord r = loadAdminRecord();
        if (!username.equals(r.username)) return false;
        String computed = SecurityUtil.sha256Hex(r.salt + ":" + password);
        return computed.equalsIgnoreCase(r.passwordHash);
    }

    public List<Student> loadStudents() throws IOException {
        ensureDataFiles();
        List<String> lines = Files.readAllLines(studentsCsv, StandardCharsets.UTF_8);
        ArrayList<Student> out = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) { // skip header
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            String[] parts = CsvUtil.splitCsvLine(line);
            if (parts.length < 4) continue;
            String id = parts[0];
            String name = parts[1];
            int marks;
            try {
                marks = Integer.parseInt(parts[2]);
            } catch (NumberFormatException ex) {
                continue;
            }
            Instant createdAt;
            try {
                createdAt = Instant.parse(parts[3]);
            } catch (Exception ex) {
                createdAt = Instant.now();
            }
            out.add(new Student(id, name, marks, createdAt));
        }
        return out;
    }

    public void saveStudents(List<Student> students) throws IOException {
        ensureDataFiles();
        try (BufferedWriter bw = Files.newBufferedWriter(studentsCsv, StandardCharsets.UTF_8)) {
            bw.write("id,name,marks,createdAt");
            bw.newLine();
            for (Student s : students) {
                String line = CsvUtil.toCsv(
                        s.getId(),
                        s.getStudentName(),
                        String.valueOf(s.getMarks()),
                        s.getCreatedAt().toString()
                );
                bw.write(line);
                bw.newLine();
            }
        }
        writeHistorySnapshot();
    }

    private void writeHistorySnapshot() {
        try {
            String ts = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                    .format(LocalDateTime.now(ZoneId.systemDefault()));
            Path snap = historyDir.resolve("students_" + ts + ".csv");
            Files.copy(studentsCsv, snap);
        } catch (Exception ignored) {}
    }

    public Path getStudentsCsvPath() {
        return studentsCsv;
    }

    public Path getHistoryDir() {
        return historyDir;
    }

    public static final class AdminRecord {
        public final String username;
        public final String salt;
        public final String passwordHash;
        public AdminRecord(String username, String salt, String passwordHash) {
            this.username = username;
            this.salt = salt;
            this.passwordHash = passwordHash;
        }
    }
}

