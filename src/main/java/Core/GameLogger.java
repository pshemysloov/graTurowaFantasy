package Core;

import javax.swing.*;

public class GameLogger {
    private static JTextArea logArea;

    public static void setLogArea(JTextArea area) {
        logArea = area;
    }

    public static void log(String message) {
        if (logArea != null) {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        }
    }
}