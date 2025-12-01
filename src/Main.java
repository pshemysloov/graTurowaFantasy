import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private Battleground battleground;
    private GamePanel gamePanel;
    private ControlPanel controlPanel;
    private JTextArea logArea;
    private MusicPlayer musicPlayer;  // Dodaj odtwarzacz muzyki

    public Main() {
        setTitle("Gra Turowa Fantasy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        // Inicjalizuj odtwarzacz muzyki
        musicPlayer = new MusicPlayer();

        // Stwórz gracza
        Player player = new Player("Bohater", 150, 100, 0, 0, 10, 10, 10, 10, 10, 10, 1, 2);

        // Stwórz pole bitwy
        battleground = new Battleground(player);

        createUI();

        // Uruchom muzykę
        musicPlayer.playMusic("src/music.wav");
        musicPlayer.setVolume(0.7f);  // Głośność 70%

        setVisible(true);
    }

    private void createUI() {
        setLayout(new BorderLayout(10, 10));

        // Panel z postaciami
        gamePanel = new GamePanel(battleground);
        add(gamePanel, BorderLayout.CENTER);

        // Panel logów
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

        // Panel sterowania z przyciskami muzyki
        controlPanel = new ControlPanel(battleground, gamePanel, musicPlayer);
        add(controlPanel, BorderLayout.EAST);

        GameLogger.log("=== Witaj w grze turowej! ===");
        GameLogger.log("Pokonaj wszystkich przeciwników!\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}