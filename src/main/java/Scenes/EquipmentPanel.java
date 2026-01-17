package Scenes;

import Core.Player;
import Core.Skill;
import Core.SkillRegister;

import javax.swing.*;
import java.awt.*;

public class EquipmentPanel extends JPanel {
    private final AppWindow window;
    private final Player player;

    public EquipmentPanel(AppWindow window, Player player) {
        this.window = window;
        this.player = player;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 40));

        // Top Bar
        add(new TopBar(window, "Ekwipunek i Atrybuty"), BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- LEWA STRONA: Atrybuty, Poziom, EXP ---
        JPanel leftPanel = createLeftPanel();
        mainContent.add(leftPanel);

        // --- PRAWA STRONA: Wybór umiejętności ---
        JPanel rightPanel = createRightPanel();
        mainContent.add(rightPanel);

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Postać", 0, 0, null, Color.WHITE));

        // Poziom
        JLabel levelLabel = new JLabel("Poziom: " + player.level);
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(levelLabel);
        panel.add(Box.createVerticalStrut(10));

        // Atrybuty
        panel.add(createAttributeRow("Siła:", player.attributes.strength));
        panel.add(createAttributeRow("Celność:", player.attributes.accuracy));
        panel.add(createAttributeRow("Inteligencja:", player.attributes.intelligence));
        panel.add(createAttributeRow("Wola:", player.attributes.willpower));
        panel.add(createAttributeRow("Wytrzymałość:", player.attributes.constitution));

        panel.add(Box.createVerticalGlue());

        // Pasek EXP
        JLabel expLabel = new JLabel("Doświadczenie:");
        expLabel.setForeground(Color.WHITE);
        expLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(expLabel);

        JProgressBar expBar = new JProgressBar(0, 100); // Przykładowy max 100
        expBar.setValue(player.experience);
        expBar.setStringPainted(true);
        expBar.setString(player.experience + " / 100");
        expBar.setMaximumSize(new Dimension(300, 25));
        panel.add(expBar);

        return panel;
    }

    private JPanel createAttributeRow(String label, int value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(350, 40));

        JLabel attrLabel = new JLabel(label);
        attrLabel.setForeground(Color.WHITE);
        attrLabel.setPreferredSize(new Dimension(120, 20));

        JLabel valLabel = new JLabel(String.valueOf(value));
        valLabel.setForeground(Color.CYAN);
        valLabel.setFont(new Font("Monospaced", Font.BOLD, 14));

        JButton btnPlus = new JButton("+");
        btnPlus.setMargin(new Insets(0, 5, 0, 5));
        // Tutaj logika dodawania punktów (jeśli player ma wolne punkty)

        row.add(attrLabel);
        row.add(valLabel);
        row.add(btnPlus);

        return row;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Umiejętności", 0, 0, null, Color.WHITE));

        panel.add(new JLabel("Wybrane umiejętności (Sloty 1-4):"));
        for (int i = 0; i < 4; i++) {
            Skill s = player.skills[i];
            String name = (s != null) ? s.name : "Pusty slot";
            JButton slotBtn = new JButton("Slot " + (i + 1) + ": " + name);
            slotBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            slotBtn.setMaximumSize(new Dimension(300, 40));
            panel.add(Box.createVerticalStrut(5));
            panel.add(slotBtn);
        }

        panel.add(Box.createVerticalStrut(20));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Dostępne umiejętności:"));

        // Przykładowa lista dostępnych skilli z rejestru
        for (SkillRegister reg : SkillRegister.values()) {
            JButton skillBtn = new JButton(reg.getSkill().name);
            skillBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            skillBtn.setMaximumSize(new Dimension(250, 30));
            panel.add(Box.createVerticalStrut(5));
            panel.add(skillBtn);
        }

        return panel;
    }
}