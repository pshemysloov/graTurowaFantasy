package Scenes;

import Core.*;
import Core.Enemies.Zombie;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class DungeonPanel extends JPanel {
    private final AppWindow window;
    private final JTextArea logArea;
    private final MusicPlayer musicPlayer;
    private final JPanel leftPanel = new JPanel();
    private final JPanel rightPanel = new JPanel();
    private final JTextField hpField = new JTextField();
    private final JTextField energyField = new JTextField();
    private final CombatHandler combatHandler;
    private final Player player;
    private final long startTime;
    private final Runnable onBattleEndFinished; // funkcja synchronizująca dane gracza z serwerem przekazywana do GameEndPanel

    public DungeonPanel(AppWindow window, Player player, Runnable onBattleEndFinished) {
        this.window = window;
        this.player = player;
        this.onBattleEndFinished = onBattleEndFinished;
        this.startTime = System.currentTimeMillis();

        // 1. Inicjalizacja muzyki na początku (żeby przyciski mogły jej użyć)
        musicPlayer = new MusicPlayer();
        loadMusic(musicPlayer);

        setLayout(new BorderLayout(10, 10));

        //Górny panel z opcjami ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);

        // Przycisk Muzyki
        JButton musicBtn = new JButton("Wycisz");
        musicBtn.setFocusable(false);
        musicBtn.addActionListener(_ -> {
            if (musicPlayer.isPlaying()) {
                musicPlayer.pauseMusic();
                musicBtn.setText("Włącz muzykę");
            } else {
                musicPlayer.resumeMusic();
                musicBtn.setText("Wycisz");
            }
        });

        // Przycisk Wyjścia
        JButton exitBtn = new JButton("Wyjście");
        exitBtn.setFocusable(false);
        exitBtn.addActionListener(_ -> handleBattleEnd(false));

        topPanel.add(musicBtn);
        topPanel.add(exitBtn);
        add(topPanel, BorderLayout.NORTH);


        // Panel środkowy podzielony na lewo (przeciwnicy) i prawo (gracz)
        JPanel battlePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        leftPanel.setLayout(new FlowLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Przeciwnicy"));

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Gracz: " + player.name));

        // Statystyki gracza
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        statsPanel.add(new JLabel("HP:"));
        hpField.setEditable(false);
        hpField.setText(player.health + "/" + player.maxHealth);
        statsPanel.add(hpField);

        statsPanel.add(new JLabel("Energia:"));
        energyField.setEditable(false);
        energyField.setText(player.energy + "/" + player.maxEnergy);
        statsPanel.add(energyField);

        rightPanel.add(statsPanel);
        rightPanel.add(Box.createVerticalStrut(10));

        // Przyciski umiejętności
        for (Skill skill : player.skills) {
            if (skill != null) {
                JButton skillBtn = new JButton(skill.name);
                skillBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                // Używamy składni 'e ->' dla kompatybilności
                skillBtn.addActionListener(_ -> player.setSelectedSkill(skill));
                rightPanel.add(skillBtn);
                rightPanel.add(Box.createVerticalStrut(5));
            }
        }

        battlePanel.add(leftPanel);
        battlePanel.add(rightPanel);
        add(battlePanel, BorderLayout.CENTER);


        // Panel Logów (Dół)
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


        GameLogger.log("=== Witaj w grze turowej! ===");

        combatHandler = new CombatHandler();
        combatHandler.setDungeonPanel(this);
        combatHandler.addActor(player);

        // Dodanie początkowych przeciwników
        combatHandler.addActor(new Zombie("Zombie1"));
        combatHandler.addActor(new Zombie("Zombie2"));
        //combatHandler.addActor(new Zombie("Zombie3"));

        refreshEnemies();

        Thread combatThread = new Thread(combatHandler);
        combatThread.start();
    }

    public void refreshEnemies() {
        // 1. Znajdź gracza i odśwież jego statystyki w rightPanel
        for (Actor actor : combatHandler.getActorsInCombat()) {
            if (actor instanceof Player) {
                hpField.setText(actor.health + "/" + actor.maxHealth);
                energyField.setText(actor.energy + "/" + actor.maxEnergy);
                break;
            }
        }


        // 2. Odśwież listę przeciwników w leftPanel
        leftPanel.removeAll();
        for (Actor actor : combatHandler.getActorsInCombat()) {
            if (actor instanceof Enemy) {
                Enemy e = (Enemy) actor;

                JPanel enemyUnitPanel = new JPanel();
                enemyUnitPanel.setLayout(new BoxLayout(enemyUnitPanel, BoxLayout.Y_AXIS));
                enemyUnitPanel.setOpaque(false);

                JButton enemyBtn = new JButton(e.name);
                enemyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                // Używamy składni 'e ->' lub '_' w zależności od wersji Javy
                enemyBtn.addActionListener(_ -> {
                    // Znajdź gracza w walce i ustaw mu cel
                    for(Actor a : combatHandler.getActorsInCombat()) {
                        if(a instanceof Player) {
                            ((Player)a).setSelectedTarget(e);
                            break;
                        }
                    }
                });


                if (e.spritePath != null) {
                    try {
                        ImageIcon icon = new ImageIcon(getClass().getResource(e.spritePath));
                        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        enemyBtn.setIcon(new ImageIcon(img));
                    } catch (Exception ex) {
                        // Ignoruj błąd grafiki
                    }
                }

                JLabel hpLabel = new JLabel("HP: " + e.health + "/" + e.maxHealth);
                hpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                hpLabel.setForeground(Color.BLACK);

                enemyUnitPanel.add(enemyBtn);
                enemyUnitPanel.add(Box.createVerticalStrut(5));
                enemyUnitPanel.add(hpLabel);

                leftPanel.add(enemyUnitPanel);
            }
        }
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void loadMusic(MusicPlayer mp) {
        InputStream musicStream = Main.class.getResourceAsStream("/music.wav");
        if (musicStream != null) {
            mp.playMusic(musicStream);
            mp.setVolume(0.7f);
        }
    }

    public void handleBattleEnd(boolean victory) {
        int experience = 0;
        int enemiesDefeated = 0;
        if (combatHandler != null) {
            experience = combatHandler.cumulativeExperience;
            enemiesDefeated = combatHandler.enemiesDefeated;
            combatHandler.stop(); // Sygnał do zatrzymania wątku
        }

        long endTime = System.currentTimeMillis();
        long durationSeconds = (endTime - startTime) / 1000;

        if(victory){
            player.addExperience(experience);
        } else {
            // W przypadku porażki gracz nie zdobywa doświadczenia
            // zerowane dla GameEndPanel
            experience = 0;
        }

        musicPlayer.stopMusic();

        GameEndPanel endPanel = new GameEndPanel(window, player, victory, enemiesDefeated, durationSeconds, experience, onBattleEndFinished);
        window.registerScene("gameend", endPanel);
        window.showScene("gameend");
    }
}