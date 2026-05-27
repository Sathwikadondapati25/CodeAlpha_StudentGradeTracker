import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentEditDialog extends JDialog {
    private boolean saved = false;
    private final JTextField nameField = new JTextField();
    private final JSpinner marksSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
    private JButton saveButton;

    public StudentEditDialog(Window owner, String currentName, int currentMarks) {
        super(owner, "Edit Student", ModalityType.APPLICATION_MODAL);
        setSize(420, 260);
        setLocationRelativeTo(owner);

        nameField.setText(currentName);
        marksSpinner.setValue(currentMarks);

        setContentPane(buildUI());
        getRootPane().setDefaultButton(saveButton);
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Student Name"), c);
        c.gridy = 1;
        form.add(nameField, c);

        c.gridy = 2;
        form.add(new JLabel("Marks (0 - 100)"), c);
        c.gridy = 3;
        form.add(marksSpinner, c);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());
        saveButton = createSaveButton();
        btns.add(cancel);
        btns.add(saveButton);

        root.add(form, BorderLayout.CENTER);
        root.add(btns, BorderLayout.SOUTH);
        return root;
    }

    private JButton createSaveButton() {
        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            saved = true;
            dispose();
        });
        return save;
    }

    public boolean isSaved() {
        return saved;
    }

    public String getStudentName() {
        return nameField.getText();
    }

    public int getMarks() {
        return (Integer) marksSpinner.getValue();
    }
}

