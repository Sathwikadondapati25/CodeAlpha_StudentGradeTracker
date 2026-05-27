import javax.swing.*;
import java.awt.*;

public final class ThemeManager {
    private ThemeManager() {}

    public enum Theme { LIGHT, DARK }

    private static Theme current = Theme.LIGHT;

    public static Theme getCurrent() {
        return current;
    }

    public static void toggle() {
        current = (current == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT;
    }

    public static void applyToWindow(Window window) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        if (current == Theme.DARK) {
            UIManager.put("control", new Color(35, 39, 47));
            UIManager.put("info", new Color(35, 39, 47));
            UIManager.put("nimbusBase", new Color(18, 20, 24));
            UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusGreen", new Color(176, 179, 50));
            UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
            UIManager.put("nimbusLightBackground", new Color(28, 30, 36));
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", Color.WHITE);
            UIManager.put("nimbusSelectionBackground", new Color(55, 95, 145));
            UIManager.put("text", Color.WHITE);
        }

        SwingUtilities.updateComponentTreeUI(window);
    }
}

