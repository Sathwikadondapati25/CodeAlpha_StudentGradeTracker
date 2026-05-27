import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminSetupDialog extends JDialog {
    private boolean saved = false;
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmField = new JPasswordField();

    public AdminSetupDialog(Window owner) {
        super(owner, "Create Admin Account", ModalityType.APPLICATION_MODAL);
        setSize(480, 340);
        setLocationRelativeTo(owner);
        setContentPane(buildUI());
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel title = new JLabel("First-time Setup");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Admin Username"), c);
        c.gridy = 1;
        form.add(usernameField, c);

        c.gridy = 2;
        form.add(new JLabel("Password"), c);
        c.gridy = 3;
        form.add(passwordField, c);

        c.gridy = 4;
        form.add(new JLabel("Confirm Password"), c);
        c.gridy = 5;
        form.add(confirmField, c);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());
        JButton save = new JButton("Create Admin");
        save.setBackground(new Color(39, 92, 186));
        save.setForeground(Color.WHITE);
        save.setFocusPainted(false);
        save.addActionListener(e -> onSave());
        btns.add(cancel);
        btns.add(save);

        root.add(form, BorderLayout.CENTER);
        root.add(btns, BorderLayout.SOUTH);
        return root;
    }

    private void onSave() {
        String u = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String p1 = new String(passwordField.getPassword());
        String p2 = new String(confirmField.getPassword());
        if (u.isEmpty() || p1.isEmpty() || p2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!p1.equals(p2)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    public String getUsername() {
        return usernameField.getText() == null ? "" : usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
}

