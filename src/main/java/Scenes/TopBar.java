package Scenes;

import Core.Author;

import javax.swing.*;
import java.awt.*;

@Author(name = "Mateusz Biskup")
public class TopBar extends JPanel {
    private final JButton backButton;
    private final JLabel titleLabel;
    private final AppWindow window;

    public TopBar(AppWindow window, String title) {
        this.window = window;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        setBackground(new Color(35, 35, 45));

        backButton = new JButton("←");
        backButton.setFocusable(false);
        backButton.setPreferredSize(new Dimension(48, 28));
        backButton.addActionListener(e -> {
            window.showPreviousScene();
            updateBackEnabled();
        });

        titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        add(backButton, BorderLayout.WEST);
        add(titleLabel, BorderLayout.CENTER);

        updateBackEnabled();
    }

    // Ustaw tekst tytułu
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    // Aktualizuj stan przycisku w zależności od historii
    public void updateBackEnabled() {
        backButton.setEnabled(window != null && window.hasHistory());
    }
}
