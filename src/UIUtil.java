import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class UIUtil {
    private UIUtil() {}

    public static final Font FONT_BOLD_14 = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_PLAIN_14 = new Font("Segoe UI", Font.PLAIN, 14);

    public static void styleTextField(JTextField f) {
        f.setFont(FONT_PLAIN_14);
        f.setForeground(new Color(33, 43, 58));
        f.setCaretColor(new Color(33, 43, 58));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 218, 230)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        f.setOpaque(true);
        f.setBackground(Color.WHITE);
    }

    public static void stylePasswordField(JPasswordField f) {
        f.setFont(FONT_PLAIN_14);
        f.setForeground(new Color(33, 43, 58));
        f.setCaretColor(new Color(33, 43, 58));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 218, 230)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        f.setOpaque(true);
        f.setBackground(Color.WHITE);
    }

    public static void stylePrimaryButton(JButton b) {
        Color normal = new Color(39, 92, 186);
        Color hover = new Color(29, 78, 166);
        b.setFont(FONT_BOLD_14);
        b.setBackground(normal);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(normal); }
        });
    }

    public static void styleBlackButton(JButton b) {
        Color normal = new Color(15, 15, 15);
        Color hover = new Color(40, 40, 40);
        b.setFont(FONT_BOLD_14);
        b.setBackground(normal);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(normal); }
        });
    }

    public static void styleNeutralButton(JButton b) {
        Color normal = new Color(241, 245, 249);
        Color hover = new Color(226, 232, 240);
        b.setFont(FONT_BOLD_14);
        b.setBackground(normal);
        b.setForeground(new Color(33, 43, 58));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { b.setBackground(normal); }
        });
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_PLAIN_14);
        table.setRowHeight(30);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 235, 243));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(new Color(33, 43, 58));

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD_14);
        header.setBackground(new Color(239, 246, 255));
        header.setForeground(new Color(33, 43, 58));
        header.setReorderingAllowed(false);
    }

    public static class SearchHighlightRenderer extends DefaultTableCellRenderer {
        private final JTextField searchField;
        public SearchHighlightRenderer(JTextField searchField) {
            this.searchField = searchField;
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String q = (searchField.getText() == null) ? "" : searchField.getText().trim().toLowerCase();
            String text = value == null ? "" : value.toString();

            boolean match = false;
            if (!q.isEmpty() && column == 1) { // student name column
                match = text.toLowerCase().contains(q);
            }

            if (isSelected) {
                c.setBackground(new Color(219, 234, 254));
                c.setForeground(new Color(33, 43, 58));
            } else if (match) {
                c.setBackground(new Color(254, 249, 195)); // light yellow highlight
                c.setForeground(new Color(33, 43, 58));
            } else {
                c.setBackground((row % 2 == 0) ? Color.WHITE : new Color(248, 250, 252));
                c.setForeground(new Color(33, 43, 58));
            }
            return c;
        }
    }
}

