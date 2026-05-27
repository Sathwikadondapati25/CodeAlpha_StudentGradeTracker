import javax.swing.*;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame(Path.of(".").toAbsolutePath().normalize()).setVisible(true));
    }
}