import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class StudentProfileDialog extends JDialog {
    public StudentProfileDialog(Window owner, Student student) {
        super(owner, "Student Profile", ModalityType.APPLICATION_MODAL);
        setSize(420, 320);
        setLocationRelativeTo(owner);
        setContentPane(buildUI(student));
    }

    private JPanel buildUI(Student s) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel title = new JLabel("Student Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        root.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 10, 10));

        addRow(grid, "Name", s.getStudentName());
        addRow(grid, "Marks", String.valueOf(s.getMarks()));
        addRow(grid, "Grade", s.getGrade());
        addRow(grid, "Status", s.isPass() ? "Pass" : "Fail");
        addRow(grid, "Student ID", s.getId());

        String created = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
                .withZone(ZoneId.systemDefault())
                .format(s.getCreatedAt());
        addRow(grid, "Created At", created);

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(close);

        root.add(grid, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private void addRow(JPanel grid, String label, String value) {
        JLabel l = new JLabel(label);
        l.setForeground(new Color(90, 100, 115));
        grid.add(l);
        JTextField v = new JTextField(value);
        v.setEditable(false);
        grid.add(v);
    }
}

