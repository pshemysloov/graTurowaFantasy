package Scenes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(AppWindow window,
                         Runnable onLogin,
                         Runnable onCreateAccount,
                         Runnable onAuthors,
                         Runnable onExit) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 40)); // Ciemne tło

        // --- GÓRA ---
        TopBar tb = new TopBar(window,"");
        add(tb, BorderLayout.NORTH);
        tb.updateBackEnabled();

        // --- ŚRODEK ---
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Tytuł Gry (Bez podtytułu)
        JLabel titleLabel = new JLabel("GRA TUROWA FANTASY");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(200, 200, 220));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(titleLabel);
        center.add(Box.createVerticalStrut(60)); // Odstęp od tytułu do przycisków

        // Przyciski
        JButton btnLogin = createStyledButton("Zaloguj się");
        JButton btnCreateAccount = createStyledButton("Stwórz konto");
        JButton btnAuthors = createStyledButton("Autorzy");
        JButton btnExit = createStyledButton("Wyjście");

        // Logika przycisków
        btnLogin.addActionListener(_ -> { if (onLogin != null) onLogin.run(); });
        btnCreateAccount.addActionListener(_ -> { if (onCreateAccount != null) onCreateAccount.run(); });
        btnAuthors.addActionListener(_ -> { if (onAuthors != null) onAuthors.run(); });
        btnExit.addActionListener(_ -> { if (onExit != null) onExit.run(); });

        // Dodawanie do panelu z odstępami
        center.add(btnLogin);
        center.add(Box.createVerticalStrut(20));
        center.add(btnCreateAccount);
        center.add(Box.createVerticalStrut(20));
        center.add(btnAuthors);
        center.add(Box.createVerticalStrut(20));
        center.add(btnExit);
        center.add(Box.createVerticalGlue());

        add(center, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(60, 60, 75));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 40, 50), 1),
                BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 50));

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