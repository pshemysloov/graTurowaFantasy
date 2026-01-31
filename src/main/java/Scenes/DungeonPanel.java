package Scenes;

import Core.*;
import Core.Enemies.Zombie;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

public class DungeonPanel extends JPanel {
    private final AppWindow window;
    private final JTextArea logArea;
    private final MusicPlayer musicPlayer;
    private final JPanel leftPanel = new JPanel();
    private final JPanel rightPanel = new JPanel();

    private final JProgressBar hpBar = new JProgressBar();
    private final JProgressBar energyBar = new JProgressBar();

    private final CombatHandler combatHandler;
    private final Player player;
    private final long startTime;
    private final Runnable onBattleEndFinished;
    private Image backgroundImage;

    public DungeonPanel(AppWindow window, Player player, Runnable onBattleEndFinished) {
        this.window = window;
        this.player = player;
        this.onBattleEndFinished = onBattleEndFinished;
        this.startTime = System.currentTimeMillis();

        try {
            URL bgUrl = getClass().getResource("/background.gif");
            if (bgUrl != null) {
                backgroundImage = new ImageIcon(bgUrl).getImage();
            }
        } catch (Exception e) {
            System.err.println("Nie udało się załadować tła.");
        }

        musicPlayer = new MusicPlayer();
        loadMusic(musicPlayer);

        setLayout(new BorderLayout(10, 10));

        // --- GÓRA ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);

        JButton musicBtn = createStyledButton("Wycisz");
        musicBtn.addActionListener(_ -> {
            if (musicPlayer.isPlaying()) {
                musicPlayer.pauseMusic();
                musicBtn.setText("Włącz muzykę");
            } else {
                musicPlayer.resumeMusic();
                musicBtn.setText("Wycisz");
            }
        });

        JButton exitBtn = createStyledButton("Wyjście");
        exitBtn.addActionListener(_ -> handleBattleEnd(false));

        topPanel.add(musicBtn);
        topPanel.add(exitBtn);
        add(topPanel, BorderLayout.NORTH);


        // --- ŚRODEK ---
        JPanel battlePanel = new JPanel(new GridLayout(1, 2, 20, 10));
        battlePanel.setOpaque(false);
        battlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Lewa strona
        leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(createTitledBorder("Przeciwnicy"));

        // Prawa strona
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(createTitledBorder("Gracz: " + player.name));

        // Statystyki
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hpBar.setStringPainted(true);
        hpBar.setForeground(new Color(200, 50, 50));
        hpBar.setBackground(new Color(50, 0, 0));
        hpBar.setMaximum(player.maxHealth);
        hpBar.setValue(player.health);
        hpBar.setFont(new Font("SansSerif", Font.BOLD, 12));

        energyBar.setStringPainted(true);
        energyBar.setForeground(new Color(50, 100, 200));
        energyBar.setBackground(new Color(0, 0, 50));
        energyBar.setMaximum(player.maxEnergy);
        energyBar.setValue(player.energy);
        energyBar.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel hpLabel = new JLabel("Zdrowie:");
        hpLabel.setForeground(Color.WHITE);
        hpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel enLabel = new JLabel("Energia:");
        enLabel.setForeground(Color.WHITE);
        enLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statsPanel.add(hpLabel);
        statsPanel.add(hpBar);
        statsPanel.add(Box.createVerticalStrut(10));
        statsPanel.add(enLabel);
        statsPanel.add(energyBar);

        rightPanel.add(statsPanel);
        rightPanel.add(Box.createVerticalStrut(20));

        // --- PRZYCISKI UMIEJĘTNOŚCI (WERSJA PROSTA) ---
        JPanel skillsContainer = new JPanel(new GridLayout(0, 1, 5, 5));
        skillsContainer.setOpaque(false);
        skillsContainer.setMaximumSize(new Dimension(300, 200));

        for (Skill skill : player.skills) {
            if (skill != null) {
                JButton skillBtn = createStyledButton(skill.name);
                // Prosty tooltip z kosztem energii
                skillBtn.setToolTipText("Koszt: " + skill.energyCost + " Energii");
                skillBtn.addActionListener(_ -> player.setSelectedSkill(skill));
                skillsContainer.add(skillBtn);
            }
        }
        rightPanel.add(skillsContainer);

        battlePanel.add(leftPanel);
        battlePanel.add(rightPanel);
        add(battlePanel, BorderLayout.CENTER);


        // --- DÓŁ (LOGI) ---
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setOpaque(false);
        logPanel.setPreferredSize(new Dimension(0, 180));
        logPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(0, 0, 0, 180));
        logArea.setForeground(new Color(220, 220, 220));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(createTitledBorder("Dziennik Walki"));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        logPanel.add(scrollPane, BorderLayout.CENTER);
        add(logPanel, BorderLayout.SOUTH);

        GameLogger.setLogArea(logArea);
        GameLogger.log("=== Witaj w Lochach! ===");

        combatHandler = new CombatHandler();
        combatHandler.setDungeonPanel(this);
        combatHandler.addActor(player);
        combatHandler.addActor(new Zombie("Zombie1"));
        combatHandler.addActor(new Zombie("Zombie2"));

        refreshEnemies();

        Thread combatThread = new Thread(combatHandler);
        combatThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 120), 2),
                title
        );
        border.setTitleColor(Color.WHITE);
        border.setTitleFont(new Font("Serif", Font.BOLD, 16));
        border.setTitleJustification(TitledBorder.CENTER);
        return border;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusable(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(new Color(60, 60, 70));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(20, 20, 30), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return btn;
    }

    public void refreshEnemies() {
        for (Actor actor : combatHandler.getActorsInCombat()) {
            if (actor instanceof Player) {
                hpBar.setMaximum(actor.maxHealth);
                hpBar.setValue(actor.health);
                hpBar.setString(actor.health + "/" + actor.maxHealth);

                energyBar.setMaximum(actor.maxEnergy);
                energyBar.setValue(actor.energy);
                energyBar.setString(actor.energy + "/" + actor.maxEnergy);
                break;
            }
        }

        leftPanel.removeAll();
        for (Actor actor : combatHandler.getActorsInCombat()) {
            if (actor instanceof Enemy) {
                Enemy e = (Enemy) actor;

                JPanel enemyUnitPanel = new JPanel();
                enemyUnitPanel.setLayout(new BoxLayout(enemyUnitPanel, BoxLayout.Y_AXIS));
                enemyUnitPanel.setOpaque(false);
                enemyUnitPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                JButton enemyBtn = new JButton(e.name);
                enemyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                enemyBtn.setBackground(new Color(80, 40, 40));
                enemyBtn.setForeground(Color.WHITE);
                enemyBtn.setFocusable(false);

                enemyBtn.addActionListener(_ -> {
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
                        enemyBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
                        enemyBtn.setHorizontalTextPosition(SwingConstants.CENTER);
                    } catch (Exception ex) {
                        // ignore
                    }
                }

                JProgressBar enemyHpBar = new JProgressBar(0, e.maxHealth);
                enemyHpBar.setValue(e.health);
                enemyHpBar.setString(e.health + "/" + e.maxHealth);
                enemyHpBar.setStringPainted(true);
                enemyHpBar.setForeground(Color.RED);
                enemyHpBar.setPreferredSize(new Dimension(100, 15));
                enemyHpBar.setAlignmentX(Component.CENTER_ALIGNMENT);

                enemyUnitPanel.add(enemyBtn);
                enemyUnitPanel.add(Box.createVerticalStrut(5));
                enemyUnitPanel.add(enemyHpBar);

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
            combatHandler.stop();
        }

        long endTime = System.currentTimeMillis();
        long durationSeconds = (endTime - startTime) / 1000;

        if(victory){
            player.addExperience(experience);
        } else {
            experience = 0;
        }

        musicPlayer.stopMusic();

        GameEndPanel endPanel = new GameEndPanel(window, player, victory, enemiesDefeated, durationSeconds, experience, onBattleEndFinished);
        window.registerScene("gameend", endPanel);
        window.showScene("gameend");
    }
}