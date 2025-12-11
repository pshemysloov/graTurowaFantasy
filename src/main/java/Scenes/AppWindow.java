package Scenes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class AppWindow extends JFrame {
    private final CardLayout cards = new CardLayout();
    private final Deque<String> history = new ArrayDeque<>();
    private String currentScene = null;

    public AppWindow() {
        super("Gra Turowa Fantasy");
        getContentPane().setLayout(cards);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
    }

    public void registerScene(String name, JPanel panel) {
        getContentPane().add(panel, name);
    }

    // Pokazuje scenę i zapisuje aktualną na stosie historii (jeśli istnieje)
    public void showScene(String name) {
        if (name == null || name.equals(currentScene)) return;
        if (currentScene != null) {
            history.push(currentScene);
        }
        cards.show(getContentPane(), name);
        currentScene = name;
        refreshTopBars();
    }

    // Pokazuje scenę bez zapisywania bieżącej do historii (np. start)
    public void showSceneNoHistory(String name) {
        if (name == null) return;
        cards.show(getContentPane(), name);
        currentScene = name;
        refreshTopBars();
    }

    // Powrót do poprzedniej sceny (jeśli istnieje)
    public void showPreviousScene() {
        if (history.isEmpty()) return;
        String prev = history.pop();
        cards.show(getContentPane(), prev);
        currentScene = prev;
        refreshTopBars();
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }

    // Opcjonalnie czyści historię
    public void clearHistory() {
        history.clear();
        refreshTopBars();
    }

    // Przeszukaj wszystkie komponenty i zaktualizuj TopBar'y (jeśli są)
    private void refreshTopBars() {
        SwingUtilities.invokeLater(() -> updateTopBarRecursive(getContentPane()));
    }

    private void updateTopBarRecursive(Component comp) {
        if (comp instanceof TopBar) {
            ((TopBar) comp).updateBackEnabled();
        }
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                updateTopBarRecursive(child);
            }
        }
    }
}
