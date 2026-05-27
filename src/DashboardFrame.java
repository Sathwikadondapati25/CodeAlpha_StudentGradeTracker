import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DashboardFrame extends JFrame {
    private final FileStore store;
    private final StudentService service = new StudentService();

    private GradeBookDashboardPanel dashboardPanel;

    public DashboardFrame(FileStore store) throws IOException {
        this.store = store;
        setTitle("GradeBook PRO EDITION");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 720);
        setLocationRelativeTo(null);

        store.ensureDataFiles();
        for (Student s : store.loadStudents()) {
            service.addStudent(s);
        }

        setJMenuBar(createMenuBar());

        dashboardPanel = new GradeBookDashboardPanel(service, this::persistNow);
        setContentPane(dashboardPanel);

        ThemeManager.applyToWindow(this);
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem exportCsv = new JMenuItem("Export CSV...");
        exportCsv.addActionListener(e -> dashboardPanel.exportCsv(store));
        JMenuItem exportPdf = new JMenuItem("Export PDF...");
        exportPdf.addActionListener(e -> dashboardPanel.exportPdf());
        JMenuItem saveNow = new JMenuItem("Save Now");
        saveNow.addActionListener(e -> persistNow());
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> dispose());
        file.add(exportCsv);
        file.add(exportPdf);
        file.addSeparator();
        file.add(saveNow);
        file.addSeparator();
        file.add(exit);

        JMenu view = new JMenu("View");
        JMenuItem toggleTheme = new JMenuItem("Toggle Dark Mode");
        toggleTheme.addActionListener(e -> {
            ThemeManager.toggle();
            ThemeManager.applyToWindow(this);
        });
        view.add(toggleTheme);

        JMenu account = new JMenu("Account");
        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Logout now?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) return;
            try {
                persistNow();
            } catch (Exception ignored) {}
            LoginFrame login = new LoginFrame(java.nio.file.Path.of(".").toAbsolutePath().normalize());
            login.setVisible(true);
            dispose();
        });
        account.add(logout);

        bar.add(file);
        bar.add(view);
        bar.add(account);
        return bar;
    }

    private void persistNow() {
        try {
            store.saveStudents(service.getAllStudents());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

