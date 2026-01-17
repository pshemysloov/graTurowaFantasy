// java
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

    public DungeonPanel(AppWindow window, Player player) {
        this.window = window;

        setLayout(new BorderLayout(10, 10));

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
                skillBtn.addActionListener(_ -> player.setSelectedSkill(skill));
                rightPanel.add(skillBtn);
                rightPanel.add(Box.createVerticalStrut(5));
            }
        }

        battlePanel.add(leftPanel);
        battlePanel.add(rightPanel);
        add(battlePanel, BorderLayout.CENTER);

        musicPlayer = new MusicPlayer();
        loadMusic(musicPlayer);


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
        if(victory){
            JOptionPane.showMessageDialog(this, "Wygrana!");
        } else  {
            JOptionPane.showMessageDialog(this, "Przegrana!");
        }
        musicPlayer.stopMusic();
        window.showScene("afterlogin");
        window.setVisible(true);


    }
}
