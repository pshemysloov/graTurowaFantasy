// java
package Scenes;

import Core.*;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class DungeonPanel extends JPanel {
    private final Battleground battleground;
    private final GamePanel gamePanel;
    private final ControlPanel controlPanel;
    private final JTextArea logArea;
    private final MusicPlayer musicPlayer;

    public DungeonPanel(Player player) {
        setLayout(new BorderLayout(10, 10));
        battleground = new Battleground(player);

        musicPlayer = new MusicPlayer();
        loadMusic(musicPlayer);

        gamePanel = new GamePanel(battleground);
        add(gamePanel, BorderLayout.CENTER);

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setPreferredSize(new Dimension(0, 180));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(30, 30, 40));
        logArea.setForeground(new Color(200, 200, 200));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Dziennik Walki"));
        logPanel.add(scrollPane, BorderLayout.CENTER);
        add(logPanel, BorderLayout.SOUTH);

        GameLogger.setLogArea(logArea);

        controlPanel = new ControlPanel(battleground, gamePanel, musicPlayer);
        add(controlPanel, BorderLayout.EAST);

        GameLogger.log("=== Witaj w grze turowej! ===");
    }

    private void loadMusic(MusicPlayer mp) {
        InputStream musicStream = Main.class.getResourceAsStream("/music.wav");
        if (musicStream != null) {
            mp.playMusic(musicStream);
            mp.setVolume(0.7f);
        }
    }

}
