package Scenes;

import Core.Author;
import Core.Player;
import Core.PlayerLevelHandler;

import javax.swing.*;
import java.awt.*;

@Author(name = "Mateusz Biskup")
public class GameEndPanel extends JPanel {
    private final AppWindow window;
    private final Player player;
    private final Runnable onMainMenuClicked;

    public GameEndPanel(AppWindow window, Player player, boolean victory, int enemiesDefeated, long timeInSeconds, int expGained, Runnable onMainMenuClicked) {
        this.window = window;
        this.player = player;
        this.onMainMenuClicked = onMainMenuClicked;

        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));


        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Napis Wygrana / Przegrana
        JLabel resultLabel = new JLabel(victory ? "ZWYCIĘSTWO!" : "PORAŻKA...");
        resultLabel.setFont(new Font("Serif", Font.BOLD, 48));
        resultLabel.setForeground(victory ? Color.GREEN : Color.RED);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(resultLabel);
        content.add(Box.createVerticalStrut(30));

        // Statystyki
        content.add(createStatLabel("Pokonani przeciwnicy: " + enemiesDefeated));
        content.add(createStatLabel("Czas walki: " + formatTime(timeInSeconds)));
        content.add(createStatLabel("Zdobyte doświadczenie: +" + expGained));

        content.add(Box.createVerticalStrut(20));

        // Logika Level Up przed wyświetleniem paska
        int oldLevel = player.level;
        PlayerLevelHandler levelHandler = new PlayerLevelHandler(player);
        // expThreshold jest liczone w konstruktorze handler'a

        if (player.level > oldLevel) {
            JLabel levelUpLabel = new JLabel("POZIOM W GÓRĘ! (" + oldLevel + " -> " + player.level + ")");
            levelUpLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
            levelUpLabel.setForeground(Color.YELLOW);
            levelUpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(levelUpLabel);
            content.add(Box.createVerticalStrut(10));
        }

        // Pasek doświadczenia
        JLabel expTitle = new JLabel("Poziom " + player.level + " (" + player.experience + " / " + levelHandler.expThreshold + ")");
        expTitle.setForeground(Color.WHITE);
        expTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(expTitle);

        JProgressBar expBar = new JProgressBar(0, levelHandler.expThreshold);
        expBar.setValue(player.experience);
        expBar.setStringPainted(true);
        expBar.setForeground(new Color(50, 150, 250));
        expBar.setMaximumSize(new Dimension(400, 30));
        content.add(expBar);

        content.add(Box.createVerticalGlue());

        // Przycisk powrotu
        JButton btnMenu = new JButton("Powrót do menu");
        btnMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMenu.setPreferredSize(new Dimension(200, 40));
        btnMenu.addActionListener(_ -> goBackToMainMenu());
        content.add(btnMenu);

        add(content, BorderLayout.CENTER);
    }

    private void goBackToMainMenu() {
        if(onMainMenuClicked != null) {
            onMainMenuClicked.run();
        }
        window.showScene("afterlogin");
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private String formatTime(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}