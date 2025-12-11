package Core;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private Battleground battleground;
    private GamePanel gamePanel;
    private MusicPlayer musicPlayer;

    public ControlPanel(Battleground battleground, GamePanel gamePanel, MusicPlayer musicPlayer) {
        this.battleground = battleground;
        this.gamePanel = gamePanel;
        this.musicPlayer = musicPlayer;


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(280, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        createButtons();
    }

    private void createButtons() {
        Player player = battleground.getPlayer();

        add(Box.createVerticalStrut(10));
        addTitle("Umiejętności Podstawowe");
        add(Box.createVerticalStrut(10));

        // Atak mieczem
        addSkillButton(player.getMeleeSkill());
        add(Box.createVerticalStrut(8));

        // Atak łukiem
        addSkillButton(player.getRangeSkill());
        add(Box.createVerticalStrut(20));

        addTitle("Umiejętności Specjalne");
        add(Box.createVerticalStrut(10));

        // Umiejętności specjalne
        for (SpecialSkill skill : player.getSpecialSkills()) {
            if (skill != null) {
                addSkillButton(skill);
                add(Box.createVerticalStrut(8));
            }
        }

        add(Box.createVerticalStrut(20));

        // Przycisk następnej fali
        JButton nextWaveBtn = new JButton("Następna Fala");
        nextWaveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextWaveBtn.setMaximumSize(new Dimension(250, 35));
        nextWaveBtn.addActionListener(e -> {
            if (battleground.isWaveCleared()) {
                battleground.addEnemyWave();
                gamePanel.repaint();
            } else {
                GameLogger.log("Najpierw pokonaj wszystkich przeciwników!");
            }
        });
        add(nextWaveBtn);
        add(Box.createVerticalStrut(20));

// Sekcja muzyki
        addTitle("Muzyka");
        add(Box.createVerticalStrut(10));

// Przycisk Pauza/Wznów
        JButton musicToggleBtn = new JButton("Pauza");
        musicToggleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        musicToggleBtn.setMaximumSize(new Dimension(250, 35));
        musicToggleBtn.addActionListener(e -> {
            if (musicPlayer.isPlaying()) {
                musicPlayer.pauseMusic();
                musicToggleBtn.setText("Wznów");
            } else {
                musicPlayer.resumeMusic();
                musicToggleBtn.setText("Pauza");
            }
        });
        add(musicToggleBtn);

        add(Box.createVerticalGlue());
        add(Box.createVerticalGlue());
    }


    private void addTitle(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);
    }

    private void addSkillButton(Skill skill) {
        JButton btn = new JButton("<html><center><b>" + skill.getName() + "</b><br>" +
                "<small>" + skill.getDescription() + "</small><br>" +
                "<small>Koszt: " + skill.getEnergyCost() + " energii</small></center></html>");
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 70));

        btn.addActionListener(e -> {
            Player player = battleground.getPlayer();
            Enemy enemy = battleground.getCurrentEnemy();

            if (enemy == null || !(enemy.health > 0)) {
                GameLogger.log("Brak przeciwnika do ataku!");
                return;
            }

            if (player.getEnergy() < skill.getEnergyCost()) {
                GameLogger.log("Nie masz wystarczająco energii!");
                return;
            }

            // Użyj umiejętności gracza
            if (skill instanceof SpecialSkill && skill.getName().equals("Leczenie")) {
                skill.useSkill(player, player);
            } else {
                skill.useSkill(player, enemy);
            }

            gamePanel.repaint();

            // Sprawdź czy przeciwnik żyje
            if (enemy.getHealth() <= 0) {
                GameLogger.log(enemy.getName() + " został pokonany!\n");
                battleground.nextEnemy();

                if (battleground.isWaveCleared()) {
                    GameLogger.log("=== Fala pokonana! ===\n");
                }
                gamePanel.repaint();
                return;
            }

            // Tura przeciwnika
            Timer timer = new Timer(800, evt -> {
                Enemy currentEnemy = battleground.getCurrentEnemy();
                if (currentEnemy != null && currentEnemy.getHealth() > 0) {
                    currentEnemy.performAction(player);
                    gamePanel.repaint();

                    if (player.getHealth() <= 0) {
                        GameLogger.log("\n=== KONIEC GRY ===");
                        JOptionPane.showMessageDialog(this, "Przegrałeś! Zrestartuj grę.");
                    }
                }
            });
            timer.setRepeats(false);
            timer.start();
        });

        add(btn);
    }
}