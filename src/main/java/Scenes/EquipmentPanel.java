package Scenes;

import Core.*;

import javax.swing.*;
import java.awt.*;

@Author(name = "Mateusz Biskup")
public class EquipmentPanel extends JPanel {
    private final AppWindow window;
    private final Player player;
    private final EquipmentHandler equipmentHandler;
    private final PlayerLevelHandler playerLevelHandler;
    private JLabel pointsLabel;
    private JLabel levelLabel;
    private JProgressBar expBar;
    private Skill selectedSkillToAssign = null;
    private JPanel rightPanel;
    private final Runnable onSaveEquipmentClicked;

    public EquipmentPanel(AppWindow window, Player player, Runnable onSaveEquipmentClicked) {
        this.window = window;
        this.player = player;
        this.equipmentHandler = new EquipmentHandler(player);
        this.playerLevelHandler = new PlayerLevelHandler(player);
        this.onSaveEquipmentClicked = onSaveEquipmentClicked;

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

        // Punkty umiejętności
        pointsLabel = new JLabel("Dostępne punkty: " + equipmentHandler.getAvailablePoints());
        pointsLabel.setForeground(Color.YELLOW);
        pointsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(pointsLabel);
        panel.add(Box.createVerticalStrut(10));

        // Poziom
        levelLabel = new JLabel("Poziom: " + player.level);
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(levelLabel);
        panel.add(Box.createVerticalStrut(10));

        // Atrybuty
        panel.add(createAttributeRow("Siła:"));
        panel.add(createAttributeRow("Celność:"));
        panel.add(createAttributeRow("Inteligencja:"));
        panel.add(createAttributeRow("Wola:"));
        panel.add(createAttributeRow("Wytrzymałość:"));

        panel.add(Box.createVerticalStrut(20));

        // --- SEKCJA WYBRANYCH UMIEJĘTNOŚCI ---
        JLabel selectedSkillsLabel = new JLabel("Wybrane umiejętności:");
        selectedSkillsLabel.setForeground(Color.WHITE);
        selectedSkillsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(selectedSkillsLabel);
        panel.add(Box.createVerticalStrut(5));

        for (int i = 0; i < 4; i++) {
            final int slotIndex = i;
            Skill s = player.skills[i];
            String name = (s != null) ? s.name : "Pusty slot";
            JButton slotBtn = new JButton("Slot " + (i + 1) + ": " + name);
            slotBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            slotBtn.setMaximumSize(new Dimension(250, 30));

            slotBtn.addActionListener(_ -> {
                if (selectedSkillToAssign != null) {
                    equipmentHandler.assignSkillToSlot(selectedSkillToAssign, slotIndex);
                    selectedSkillToAssign = null;
                    refreshAll(); // Odświeżamy oba panele
                } else {
                    JOptionPane.showMessageDialog(this, "Najpierw wybierz umiejętność z listy po prawej!");
                }
            });

            panel.add(slotBtn);
            panel.add(Box.createVerticalStrut(5));
        }


        panel.add(Box.createVerticalGlue());

        // Pasek EXP
        JLabel expLabel = new JLabel("Doświadczenie:");
        expLabel.setForeground(Color.WHITE);
        expLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(expLabel);

        expBar = new JProgressBar(0, playerLevelHandler.expThreshold);
        expBar.setValue(player.experience);
        expBar.setStringPainted(true);
        updateExpBarText();
        expBar.setMaximumSize(new Dimension(300, 25));
        panel.add(expBar);

        return panel;
    }

    private JPanel createAttributeRow(String label) {
        String attrName = label.replace(":", "");
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(350, 40));

        JLabel attrLabel = new JLabel(label);
        attrLabel.setForeground(Color.WHITE);
        attrLabel.setPreferredSize(new Dimension(120, 20));

        JLabel valLabel = new JLabel(String.valueOf(equipmentHandler.getAttributeValue(attrName)));
        valLabel.setForeground(Color.CYAN);
        valLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        valLabel.setPreferredSize(new Dimension(30, 20));

        JButton btnMinus = new JButton("-");
        btnMinus.setMargin(new Insets(0, 5, 0, 5));
        btnMinus.addActionListener(_ -> {
            if (equipmentHandler.subtractAttribute(attrName)) {
                refreshUI(valLabel, attrName);
            }
        });

        JButton btnPlus = new JButton("+");
        btnPlus.setMargin(new Insets(0, 5, 0, 5));
        btnPlus.addActionListener(_ -> {
            if (equipmentHandler.addAttribute(attrName)) {
                refreshUI(valLabel, attrName);
            }
        });

        row.add(attrLabel);
        row.add(valLabel);
        row.add(btnMinus);
        row.add(btnPlus);

        return row;
    }

    private void refreshUI(JLabel valLabel, String attrName) {
        valLabel.setText(String.valueOf(equipmentHandler.getAttributeValue(attrName)));
        pointsLabel.setText("Dostępne punkty: " + equipmentHandler.getAvailablePoints());

        // Odświeżenie statystyk poziomu i doświadczenia
        levelLabel.setText("Poziom: " + player.level);
        expBar.setMaximum(playerLevelHandler.expThreshold);
        expBar.setValue(player.experience);
        updateExpBarText();
    }

    private void refreshAll() {
        // Usuwamy wszystko i budujemy od nowa, aby odświeżyć oba panele (lewy i prawy)
        removeAll();
        add(new TopBar(window, "Ekwipunek i Atrybuty"), BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainContent.add(createLeftPanel());
        mainContent.add(createRightPanel());

        add(mainContent, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void updateExpBarText() {
        expBar.setString(player.experience + " / " + playerLevelHandler.expThreshold);
    }

    private JPanel createRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Dostępne Umiejętności", 0, 0, null, Color.WHITE));

        // Panel z listą skilli (Scrollable)
        JPanel skillListPanel = new JPanel();
        skillListPanel.setLayout(new BoxLayout(skillListPanel, BoxLayout.Y_AXIS));
        skillListPanel.setOpaque(false);

        for (SkillRegister reg : SkillRegister.values()) {
            Skill skill = reg.getSkill();
            JButton skillBtn = new JButton(skill.name);
            skillBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            skillBtn.setMaximumSize(new Dimension(250, 40));

            // Zaznaczenie wybranego skilla kolorem
            if (selectedSkillToAssign == skill) {
                skillBtn.setBackground(Color.DARK_GRAY);
                skillBtn.setForeground(Color.YELLOW);
            }

            skillBtn.addActionListener(_ -> {
                selectedSkillToAssign = skill;
                refreshRightPanel();
            });

            skillListPanel.add(Box.createVerticalStrut(5));
            skillListPanel.add(skillBtn);
        }

        JScrollPane scrollPane = new JScrollPane(skillListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // --- PRZYCISK ZAPISZ NA DOLE ---
        JButton saveBtn = new JButton("ZAPISZ");
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        saveBtn.setBackground(new Color(50, 100, 50));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setPreferredSize(new Dimension(0, 50));
        saveBtn.addActionListener(_ -> onSaveEquipmentClicked.run());
        rightPanel.add(saveBtn, BorderLayout.SOUTH);

        return rightPanel;
    }

    private void refreshRightPanel() {
        // Podmiana samego prawego panelu
        Container parent = rightPanel.getParent();
        if (parent != null) {
            int index = -1;
            for (int i = 0; i < parent.getComponentCount(); i++) {
                if (parent.getComponent(i) == rightPanel) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                parent.remove(rightPanel);
                parent.add(createRightPanel(), index);
                parent.revalidate();
                parent.repaint();
            }
        }
    }

}
