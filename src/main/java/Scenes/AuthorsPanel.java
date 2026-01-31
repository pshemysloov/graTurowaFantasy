package Scenes;

import Core.Author;

import javax.swing.*;
import java.awt.*;

@Author(name = "Mateusz Biskup")
public class AuthorsPanel extends JPanel {
    public AuthorsPanel(AppWindow window) {
        // Używamy BorderLayout, aby TopBar był na górze, a treść w centrum
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 40));

        // Pasek górny z tytułem "Autorzy"
        TopBar tb = new TopBar(window, "Autorzy");
        add(tb, BorderLayout.NORTH);

        // Panel centralny z zawartością
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // Przezroczyste tło, by dziedziczyć kolor z AuthorsPanel
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        // Nagłówek sekcji
        JLabel headerLabel = new JLabel("Twórcy Gry");
        headerLabel.setForeground(new Color(100, 150, 255)); // Lekki niebieski akcent
        headerLabel.setFont(new Font("Serif", Font.BOLD, 32));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(headerLabel);

        contentPanel.add(Box.createVerticalStrut(40)); // Odstęp

        // Lista autorów
        addAuthorLabel(contentPanel, "Przemysław Błaszczyk");
        contentPanel.add(Box.createVerticalStrut(15));

        addAuthorLabel(contentPanel, "Mateusz Biskup");

        // Wypychacz na dół, aby napisy były bliżej środka/góry
        contentPanel.add(Box.createVerticalGlue());

        add(contentPanel, BorderLayout.CENTER);
    }

    private void addAuthorLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        // Użycie czcionki bezszeryfowej dla nazwisk wygląda nowocześniej
        label.setFont(new Font("SansSerif", Font.PLAIN, 22));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Opcjonalnie: Dodanie cienia tekstu (imitacja) lub ikony
        // label.setIcon(new ImageIcon(...));

        panel.add(label);
    }
}