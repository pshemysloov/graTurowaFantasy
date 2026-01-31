package Scenes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginPanel(AppWindow window, Runnable onLogin) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 40)); // Ciemne tło

        // --- GÓRA  ---
        TopBar tb = new TopBar(window, "Logowanie");
        add(tb, BorderLayout.NORTH);
        tb.updateBackEnabled();

        // --- ŚRODEK (Formularz) ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        // Marginesy tylko dla środka, nie dla paska
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Tytuł
        JLabel title = new JLabel("Witaj ponownie!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Pola tekstowe
        JLabel userLabel = new JLabel("Nazwa użytkownika:");
        userLabel.setForeground(new Color(200, 200, 200));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(300, 30));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passLabel = new JLabel("Hasło:");
        passLabel.setForeground(new Color(200, 200, 200));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Przycisk
        JButton loginBtn = createStyledButton("Zaloguj się");
        loginBtn.addActionListener(_ -> {
            if (onLogin != null) onLogin.run();
        });

        // Układanie elementów
        centerPanel.add(Box.createVerticalGlue()); // Wypchnij na środek
        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(userLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(usernameField);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(passLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(passwordField);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(loginBtn);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(60, 60, 75));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 40, 50), 1),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 45));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(80, 80, 100));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(60, 60, 75));
            }
        });
        return btn;
    }
}