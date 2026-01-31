package Scenes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateAccountPanel extends JPanel {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public CreateAccountPanel(AppWindow window, Runnable onConfirm) {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 40));

        // --- GÓRA ---
        TopBar tb = new TopBar(window, "Tworzenie konta");
        add(tb, BorderLayout.NORTH);
        tb.updateBackEnabled();

        // --- ŚRODEK ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Dołącz do gry");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Wybierz nick:");
        userLabel.setForeground(new Color(200, 200, 200));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(300, 30));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passLabel = new JLabel("Wybierz hasło:");
        passLabel.setForeground(new Color(200, 200, 200));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton confirmBtn = createStyledButton("Utwórz konto");
        confirmBtn.addActionListener(_ -> {
            if (onConfirm != null) onConfirm.run();
        });

        centerPanel.add(Box.createVerticalGlue());
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
        centerPanel.add(confirmBtn);
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