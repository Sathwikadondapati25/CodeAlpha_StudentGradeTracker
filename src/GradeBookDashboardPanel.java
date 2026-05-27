import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Main dashboard UI: sidebar + analytics cards + student CRUD table.
 * UI only; business logic is in StudentService.
 */
public class GradeBookDashboardPanel extends JPanel {
    private final StudentService service;
    private final Runnable onDataChanged;
    private final DecimalFormat df = new DecimalFormat("0.00");

    private static final Color NAVY = new Color(28, 57, 102);
    private static final Color BG = new Color(246, 248, 252);
    private static final Color CARD = Color.WHITE;
    private static final Color PRIMARY = new Color(39, 92, 186);
    private static final Color TEXT = new Color(32, 39, 52);

    private JLabel totalStudentsValue;
    private JLabel averageMarksValue;
    private JLabel passRateValue;
    private JLabel highestStudentValue;
    private JLabel lowestStudentValue;

    private DefaultPieDataset<String> gradePieDataset;
    private DefaultCategoryDataset performanceBarDataset;

    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;

    public GradeBookDashboardPanel(StudentService service, Runnable onDataChanged) {
        super(new BorderLayout());
        this.service = service;
        this.onDataChanged = onDataChanged;
        setBackground(BG);

        add(createSidebar(), BorderLayout.WEST);
        add(createMainDashboard(), BorderLayout.CENTER);
        refreshUI();
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(NAVY);
        panel.setPreferredSize(new Dimension(220, 0));
        panel.setBorder(new EmptyBorder(20, 14, 20, 14));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(NAVY);
        top.add(createBrandHeader());
        top.add(Box.createVerticalStrut(20));

        JPanel countCard = new JPanel(new BorderLayout());
        countCard.setBackground(new Color(41, 73, 124));
        countCard.setBorder(new EmptyBorder(12, 12, 12, 12));
        JLabel countLabel = new JLabel("Total Students");
        countLabel.setForeground(new Color(225, 233, 245));
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        totalStudentsValue = new JLabel("0");
        totalStudentsValue.setForeground(Color.WHITE);
        totalStudentsValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        countCard.add(countLabel, BorderLayout.NORTH);
        countCard.add(totalStudentsValue, BorderLayout.CENTER);
        top.add(countCard);

        panel.add(top, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createBrandHeader() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(NAVY);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        row.add(new GradCapIcon());

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(NAVY);

        JLabel title = new JLabel("GradeBook");
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel edition = new JLabel("PRO EDITION");
        edition.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        edition.setForeground(new Color(180, 195, 215));
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
            setPreferredSize(new Dimension(44, 44));
            setMinimumSize(new Dimension(44, 44));
            setMaximumSize(new Dimension(44, 44));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(GOLD);
            g2.fillRoundRect(0, 0, 44, 44, 10, 10);

            g2.setColor(CAP);
            g2.setStroke(new BasicStroke(2f));
            g2.fillRect(10, 22, 24, 4);
            g2.fillPolygon(new int[]{8, 14, 36, 30}, new int[]{14, 8, 8, 14}, 4);
            g2.drawLine(22, 14, 22, 8);
            g2.fillOval(19, 6, 6, 6);
            g2.dispose();
        }
    }

    private JPanel createMainDashboard() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.add(createHeaderAndStats(), BorderLayout.NORTH);
        panel.add(createCenterSplit(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHeaderAndStats() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(BG);

        JLabel title = new JLabel("Class Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT);
        wrapper.add(title);

        JLabel subtitle = new JLabel("Manage student performance and class statistics.");
        subtitle.setForeground(new Color(98, 108, 125));
        subtitle.setBorder(new EmptyBorder(0, 0, 12, 0));
        wrapper.add(subtitle);

        JPanel cards = new JPanel(new GridLayout(1, 4, 10, 10));
        cards.setBackground(BG);

        averageMarksValue = new JLabel("-", SwingConstants.LEFT);
        passRateValue = new JLabel("-", SwingConstants.LEFT);
        highestStudentValue = new JLabel("-", SwingConstants.LEFT);
        lowestStudentValue = new JLabel("-", SwingConstants.LEFT);

        cards.add(createInfoCard("Average Marks", averageMarksValue));
        cards.add(createInfoCard("Pass Rate", passRateValue));
        cards.add(createInfoCard("Highest Marks", highestStudentValue));
        cards.add(createInfoCard("Lowest Marks", lowestStudentValue));
        wrapper.add(cards);
        return wrapper;
    }

    private JPanel createCenterSplit() {
        JPanel split = new JPanel(new GridLayout(1, 2, 12, 12));
        split.setBackground(BG);
        split.add(createLeftColumn());
        split.add(createStudentsTableCard());
        return split;
    }

    private JPanel createLeftColumn() {
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(BG);
        left.add(createAddStudentCard());
        left.add(Box.createVerticalStrut(12));
        left.add(createDistributionCard());
        left.add(Box.createVerticalStrut(12));
        left.add(createPerformanceChartCard());
        return left;
    }

    private JPanel createAddStudentCard() {
        JPanel card = baseCard("Student Management");
        card.setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 7, 7, 7);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        c.gridx = 0; c.gridy = 0;
        content.add(new JLabel("Student Name"), c);
        JTextField nameField = new JTextField();
        UIUtil.styleTextField(nameField);
        c.gridy = 1;
        content.add(nameField, c);

        c.gridy = 2;
        content.add(new JLabel("Marks (0 - 100)"), c);
        JSpinner marksSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        c.gridy = 3;
        content.add(marksSpinner, c);

        JButton addBtn = new JButton("Add Student");
        UIUtil.stylePrimaryButton(addBtn);
        addBtn.addActionListener(e -> {
            String name = Validator.normalizeName(nameField.getText());
            int marks = (Integer) marksSpinner.getValue();
            if (!Validator.isValidName(name)) {
                JOptionPane.showMessageDialog(this, "Enter a valid student name.");
                return;
            }
            if (!Validator.isValidMarks(marks)) {
                JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.");
                return;
            }
            service.addStudent(name, marks);
            nameField.setText("");
            marksSpinner.setValue(0);
            dataChanged();
        });
        c.gridy = 4;
        content.add(addBtn, c);

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 8, 8));
        btnRow.setOpaque(false);
        JButton editBtn = new JButton("Edit Selected");
        UIUtil.styleNeutralButton(editBtn);
        editBtn.addActionListener(e -> editSelected());
        JButton viewBtn = new JButton("View Profile");
        UIUtil.styleNeutralButton(viewBtn);
        viewBtn.addActionListener(e -> viewSelectedProfile());
        btnRow.add(editBtn);
        btnRow.add(viewBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(btnRow, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createStudentsTableCard() {
        JPanel card = baseCard("Student Directory");
        card.setLayout(new BorderLayout(10, 10));

        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setOpaque(false);
        searchField = new JTextField();
        UIUtil.styleTextField(searchField);
        searchField.setToolTipText("Search by student name...");
        searchField.getDocument().addDocumentListener((SimpleDocumentListener) e -> applyFilter());
        topBar.add(searchField, BorderLayout.CENTER);

        JButton deleteBtn = new JButton("Delete Selected");
        UIUtil.styleNeutralButton(deleteBtn);
        deleteBtn.addActionListener(e -> deleteSelected());
        topBar.add(deleteBtn, BorderLayout.EAST);
        card.add(topBar, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"#", "Student Name", "Marks", "Grade", "Status", "ID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        UIUtil.styleTable(table);
        table.setDefaultRenderer(Object.class, new UIUtil.SearchHighlightRenderer(searchField));
        table.removeColumn(table.getColumnModel().getColumn(5)); // hide ID

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) viewSelectedProfile();
            }
        });

        card.add(new JScrollPane(table), BorderLayout.CENTER);
        return card;
    }

    private JPanel createDistributionCard() {
        JPanel card = baseCard("Grade Distribution");
        card.setLayout(new BorderLayout());

        gradePieDataset = new DefaultPieDataset<>();
        gradePieDataset.setValue("A", 0);
        gradePieDataset.setValue("B", 0);
        gradePieDataset.setValue("C", 0);
        gradePieDataset.setValue("F", 0);

        JFreeChart pieChart = ChartFactory.createPieChart("", gradePieDataset, true, true, false);
        pieChart.setBackgroundPaint(CARD);
        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        piePlot.setBackgroundPaint(CARD);
        piePlot.setSectionPaint("A", new Color(37, 99, 235));
        piePlot.setSectionPaint("B", new Color(22, 163, 74));
        piePlot.setSectionPaint("C", new Color(245, 158, 11));
        piePlot.setSectionPaint("F", new Color(239, 68, 68));
        piePlot.setLabelBackgroundPaint(CARD);
        piePlot.setLabelOutlinePaint(new Color(220, 225, 235));
        piePlot.setCircular(true);

        ChartPanel piePanel = new ChartPanel(pieChart);
        piePanel.setOpaque(false);
        piePanel.setMouseWheelEnabled(true);
        piePanel.setPreferredSize(new Dimension(420, 220));
        card.add(piePanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createPerformanceChartCard() {
        JPanel card = baseCard("Performance Statistics");
        card.setLayout(new BorderLayout());

        performanceBarDataset = new DefaultCategoryDataset();
        performanceBarDataset.addValue(0, "Marks", "Average");
        performanceBarDataset.addValue(0, "Marks", "Highest");
        performanceBarDataset.addValue(0, "Marks", "Lowest");
        performanceBarDataset.addValue(0, "Marks", "Pass %");

        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "Metric",
                "Value",
                performanceBarDataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );
        barChart.setBackgroundPaint(CARD);
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(250, 252, 255));
        plot.setRangeGridlinePaint(new Color(220, 225, 235));
        plot.getRenderer().setSeriesPaint(0, new Color(59, 130, 246));

        ChartPanel barPanel = new ChartPanel(barChart);
        barPanel.setOpaque(false);
        barPanel.setMouseWheelEnabled(true);
        barPanel.setPreferredSize(new Dimension(420, 220));
        card.add(barPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createInfoCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 231, 241)),
                new EmptyBorder(10, 12, 10, 12)
        ));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(98, 108, 125));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(TEXT);
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel baseCard(String title) {
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(223, 229, 238)),
                title
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1000));
        return card;
    }

    private void applyFilter() {
        String text = searchField.getText() == null ? "" : searchField.getText().trim();
        if (text.isEmpty()) sorter.setRowFilter(null);
        else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text), 1));
    }

    private void dataChanged() {
        refreshUI();
        if (onDataChanged != null) onDataChanged.run();
    }

    public void refreshUI() {
        refreshTable();
        refreshStats();
        refreshDistribution();
        applyFilter();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Student> students = service.getAllStudents();
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            tableModel.addRow(new Object[]{
                    i + 1,
                    s.getStudentName(),
                    s.getMarks(),
                    s.getGrade(),
                    s.isPass() ? "Pass" : "Fail",
                    s.getId()
            });
        }
    }

    private void refreshStats() {
        int total = service.getCount();
        totalStudentsValue.setText(String.valueOf(total));
        averageMarksValue.setText(total == 0 ? "-" : df.format(service.getAverageMarks()));
        passRateValue.setText(total == 0 ? "-" : df.format(service.getPassPercentage()) + "%");

        Student top = service.getTopper();
        Student low = service.getLowestScorer();
        highestStudentValue.setText(top == null ? "-" : top.getStudentName() + " (" + top.getMarks() + ")");
        lowestStudentValue.setText(low == null ? "-" : low.getStudentName() + " (" + low.getMarks() + ")");
    }

    private void refreshDistribution() {
        StudentService.GradeCounts gc = service.getGradeCounts();
        gradePieDataset.setValue("A", gc.a);
        gradePieDataset.setValue("B", gc.b);
        gradePieDataset.setValue("C", gc.c);
        gradePieDataset.setValue("F", gc.f);
        refreshPerformanceBars();
    }

    private void refreshPerformanceBars() {
        double avg = service.getAverageMarks();
        int high = service.getHighestMarks();
        int low = service.getLowestMarks();
        double pass = service.getPassPercentage();

        performanceBarDataset.setValue(avg, "Marks", "Average");
        performanceBarDataset.setValue(high, "Marks", "Highest");
        performanceBarDataset.setValue(low, "Marks", "Lowest");
        performanceBarDataset.setValue(pass, "Marks", "Pass %");
    }

    private String selectedStudentIdOrNull() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return null;
        int modelRow = table.convertRowIndexToModel(viewRow);
        return (String) tableModel.getValueAt(modelRow, 5);
    }

    private void deleteSelected() {
        String id = selectedStudentIdOrNull();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a student row first.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Delete selected student?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        List<Student> all = new ArrayList<>(service.getAllStudents());
        int idx = -1;
        for (int i = 0; i < all.size(); i++) if (all.get(i).getId().equals(id)) { idx = i; break; }
        if (idx >= 0) service.removeAt(idx);
        dataChanged();
    }

    private void editSelected() {
        String id = selectedStudentIdOrNull();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a student row first.");
            return;
        }
        Student s = service.getById(id);
        if (s == null) return;

        StudentEditDialog dlg = new StudentEditDialog(SwingUtilities.getWindowAncestor(this), s.getStudentName(), s.getMarks());
        dlg.setVisible(true);
        if (!dlg.isSaved()) return;

        String newName = Validator.normalizeName(dlg.getStudentName());
        int newMarks = dlg.getMarks();
        if (!Validator.isValidName(newName) || !Validator.isValidMarks(newMarks)) {
            JOptionPane.showMessageDialog(this, "Invalid values.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        service.updateStudent(id, newName, newMarks);
        dataChanged();
    }

    private void viewSelectedProfile() {
        String id = selectedStudentIdOrNull();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Select a student row first.");
            return;
        }
        Student s = service.getById(id);
        if (s == null) return;
        new StudentProfileDialog(SwingUtilities.getWindowAncestor(this), s).setVisible(true);
    }

    public void exportCsv(FileStore store) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("gradebook_students.csv"));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;
        Path target = chooser.getSelectedFile().toPath();
        try {
            store.saveStudents(service.getAllStudents());
            java.nio.file.Files.copy(store.getStudentsCsvPath(), target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            JOptionPane.showMessageDialog(this, "CSV exported to:\n" + target);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void exportPdf() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("gradebook_report.pdf"));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;
        Path target = chooser.getSelectedFile().toPath();

        try {
            List<String> lines = new ArrayList<>();
            lines.add("Total Students: " + service.getCount());
            lines.add("Average Marks: " + df.format(service.getAverageMarks()));
            lines.add("Pass Percentage: " + df.format(service.getPassPercentage()) + "%");
            Student top = service.getTopper();
            Student low = service.getLowestScorer();
            lines.add("Highest: " + (top == null ? "-" : top.getStudentName() + " (" + top.getMarks() + ")"));
            lines.add("Lowest: " + (low == null ? "-" : low.getStudentName() + " (" + low.getMarks() + ")"));
            lines.add("");
            lines.add("Students:");
            int i = 1;
            for (Student s : service.getAllStudents()) {
                lines.add(i++ + ". " + s.getStudentName() + " — " + s.getMarks() + " (" + s.getGrade() + ", " + (s.isPass() ? "Pass" : "Fail") + ")");
            }
            PdfExporter.exportSimpleReport(target, "GradeBook PRO EDITION — Report", lines);
            JOptionPane.showMessageDialog(this, "PDF exported to:\n" + target);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "PDF export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FunctionalInterface
    private interface SimpleDocumentListener extends javax.swing.event.DocumentListener {
        void update(javax.swing.event.DocumentEvent e);
        @Override default void insertUpdate(javax.swing.event.DocumentEvent e) { update(e); }
        @Override default void removeUpdate(javax.swing.event.DocumentEvent e) { update(e); }
        @Override default void changedUpdate(javax.swing.event.DocumentEvent e) { update(e); }
    }
}

