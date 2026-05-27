import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.file.Path;

public class LoginFrame extends JFrame {
    private final FileStore store;
    private static final Color NAVY = new Color(28, 57, 102);
    private static final Color BG = new Color(246, 248, 252);
    private static final Color CARD = Color.WHITE;
    private static final Color PRIMARY = new Color(39, 92, 186);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame(Path projectRoot) {
        this.store = new FileStore(projectRoot);
        setTitle("GradeBook PRO EDITION — Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 460);
        setLocationRelativeTo(null);

        setContentPane(buildUI());
        getRootPane().setDefaultButton(loginButton);
        ThemeManager.applyToWindow(this);
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(BG);

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(NAVY);
        left.setBorder(new EmptyBorder(24, 24, 24, 24));
        left.add(createBrandPanel(), BorderLayout.NORTH);

        JLabel sub = new JLabel("<html>Welcome to your student analytics dashboard.<br/>Login to continue.</html>");
        sub.setForeground(new Color(214, 225, 243));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        left.add(sub, BorderLayout.CENTER);

        JPanel rightWrap = new JPanel(new GridBagLayout());
        rightWrap.setBackground(BG);
        rightWrap.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235)),
                new EmptyBorder(16, 16, 16, 16)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        JLabel title = new JLabel("User Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(33, 43, 58));
        c.gridx = 0; c.gridy = 0;
        card.add(title, c);

        c.gridy = 1;
        JLabel subtitle = new JLabel("Enter any username and password");
        subtitle.setForeground(new Color(98, 108, 125));
        card.add(subtitle, c);

        c.gridx = 0; c.gridy = 2;
        card.add(new JLabel("Username"), c);
        usernameField = new JTextField();
        UIUtil.styleTextField(usernameField);
        c.gridy = 3;
        card.add(usernameField, c);

        c.gridy = 4;
        card.add(new JLabel("Password"), c);
        passwordField = new JPasswordField();
        UIUtil.stylePasswordField(passwordField);
        c.gridy = 5;
        card.add(passwordField, c);

        loginButton = new JButton("Login");
        UIUtil.styleBlackButton(loginButton);
        loginButton.addActionListener(e -> onLogin());
        c.gridy = 6;
        card.add(loginButton, c);

        rightWrap.add(card);
        root.add(left);
        root.add(rightWrap);

        return root;
    }

    private JPanel createBrandPanel() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(NAVY);

        row.add(new GradCapIcon());

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(NAVY);

        JLabel title = new JLabel("GradeBook");
        title.setFont(new Font("Georgia", Font.BOLD, 34));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel edition = new JLabel("PRO EDITION");
        edition.setFont(new Font("Segoe UI", Font.BOLD, 20));
        edition.setForeground(new Color(190, 204, 227));
        edition.setAlignmentX(Component.LEFT_ALIGNMENT);

        text.add(title);
        text.add(edition);
        row.add(text);
        return row;
    }

    private static final class GradCapIcon extends JComponent {
        private static final Color GOLD = new Color(234, 179, 8);
        private static final Color CAP = new Color(28, 57, 102);
        GradCapIcon() {
            setPreferredSize(new Dimension(56, 56));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(GOLD);
            g2.fillRoundRect(0, 0, 56, 56, 12, 12);
            g2.setColor(CAP);
            g2.setStroke(new BasicStroke(2f));
            g2.fillRect(14, 28, 28, 5);
            g2.fillPolygon(new int[]{11, 18, 45, 38}, new int[]{20, 14, 14, 20}, 4);
            g2.drawLine(28, 20, 28, 11);
            g2.fillOval(25, 9, 6, 6);
            g2.dispose();
        }
    }

    private void onLogin() {
        String u = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String p = new String(passwordField.getPassword());

        try {
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // First run: create admin credentials from the first successful login (no default credentials).
            if (!store.adminExists()) {
                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "No admin account found.\nCreate an admin account now?",
                        "First-time Setup",
                        JOptionPane.YES_NO_OPTION
                );
                if (choice != JOptionPane.YES_OPTION) return;

                AdminSetupDialog setup = new AdminSetupDialog(this);
                setup.setVisible(true);
                if (!setup.isSaved()) return;
                store.saveAdminCredentials(setup.getUsername(), setup.getPassword());
                JOptionPane.showMessageDialog(this, "Admin account created. Please login again.", "Success", JOptionPane.INFORMATION_MESSAGE);
                passwordField.setText("");
                return;
            }

            if (!store.validateLogin(u, p)) {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DashboardFrame dash = new DashboardFrame(store);
            dash.setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Login error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

