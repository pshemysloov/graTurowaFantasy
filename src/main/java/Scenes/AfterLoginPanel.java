package Scenes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AfterLoginPanel extends JPanel {

    // Usunięto argument 'Player player', aby pasował do Twojego Main.java
    public AfterLoginPanel(AppWindow window,
                           Runnable onEnterDungeon,
                           Runnable onEnterPVP,
                           Runnable onEquipment,
                           Runnable onLogout) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 40));


        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Nagłówek
        JLabel header = new JLabel("Wybierz tryb gry");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(new Color(200, 200, 220));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(header);
        center.add(Box.createVerticalStrut(40));

        // Przyciski
        JButton btnDungeon = createStyledButton("Lochy (PvE)");
        JButton btnPvp = createStyledButton("Arena (PvP)");
        JButton btnEquipment = createStyledButton("Ekwipunek");
        JButton btnLogout = createStyledButton("Wyloguj");

        // Wyróżnienie przycisku wylogowania
        btnLogout.setBackground(new Color(100, 50, 50));
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(new Color(130, 60, 60));
                btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(new Color(100, 50, 50));
            }
        });


        btnDungeon.addActionListener(_ -> { if (onEnterDungeon != null) onEnterDungeon.run(); });
        btnPvp.addActionListener(_ -> { if (onEnterPVP != null) onEnterPVP.run(); });
        btnEquipment.addActionListener(_ -> { if (onEquipment != null) onEquipment.run(); });
        btnLogout.addActionListener(_ -> { if (onLogout != null) onLogout.run(); });

        center.add(btnDungeon);
        center.add(Box.createVerticalStrut(20));
        center.add(btnPvp);
        center.add(Box.createVerticalStrut(20));
        center.add(btnEquipment);
        center.add(Box.createVerticalStrut(40));
        center.add(btnLogout);
        center.add(Box.createVerticalGlue());

        add(center, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(60, 60, 75));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 40, 50), 1),
                BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 50));

        if (!text.equals("Wyloguj")) {
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(80, 80, 100));
                    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(new Color(60, 60, 75));
                }
            });
        }
        return btn;
    }
}