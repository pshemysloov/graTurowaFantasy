package Scenes;

import javax.swing.*;
import java.awt.*;

public class CreateAccountPanel extends JPanel {
    private final AppWindow window;
    private final TopBar topBar;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton confirmButton;
    private Runnable onConfirm;

    public CreateAccountPanel(AppWindow window, Runnable onConfirm) {
        this.window = window;
        this.onConfirm = onConfirm;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 40));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        topBar = new TopBar(window, "Utwórz konto");
        add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel title = new JLabel("Rejestracja", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(title);
        center.add(Box.createVerticalStrut(20));

        usernameField = new JTextField();
        styleField(usernameField, "Nazwa użytkownika");
        center.add(usernameField);
        center.add(Box.createVerticalStrut(12));

        passwordField = new JPasswordField();
        styleField(passwordField, "Hasło");
        center.add(passwordField);
        center.add(Box.createVerticalStrut(20));

        confirmButton = new JButton("Potwierdź");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.setMaximumSize(new Dimension(260, 36));
        confirmButton.addActionListener(e -> handleConfirm());
        center.add(confirmButton);

        center.add(Box.createVerticalGlue());
        add(center, BorderLayout.CENTER);
    }

    private void styleField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(420, 36));
        field.setPreferredSize(new Dimension(420, 36));
        field.setFont(new Font("Dialog", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(50, 50, 60));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 90)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Optional small hint: setToolTipText
        field.setToolTipText(placeholder);
    }

    private void handleConfirm() {
        String username = getUsername().trim();
        String password = getPassword();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(window, "Podaj nazwę użytkownika.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(window, "Podaj hasło.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Wywołaj dostarczony Runnable — możliwe, że korzysta z getUsername()/getPassword()
        if (onConfirm != null) {
            onConfirm.run();
        } else {
            JOptionPane.showMessageDialog(window, "Brak zdefiniowanej akcji potwierdzenia.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

}
