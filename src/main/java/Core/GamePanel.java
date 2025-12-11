package Core;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.Timer;

public class GamePanel extends JPanel {
    private Battleground battleground;
    private ImageIcon backgroundGif;      // Animowane tło
    private BufferedImage playerImage;    // Zwykłe zdjęcie gracza
    private BufferedImage enemyImage;     // Zwykłe zdjęcie przeciwnika

    public GamePanel(Battleground battleground) {
        this.battleground = battleground;
        setBackground(new Color(40, 40, 50));
        setPreferredSize(new Dimension(700, 450));

        loadBackgroundImage();
        loadCharacterImages();

        // Timer tylko dla animacji tła
        Timer timer = new Timer(50, e -> repaint());
        timer.start();
    }

    // Wczytuje animowane tło
    private void loadBackgroundImage() {
        try {
            java.net.URL imgUrl = getClass().getResource("/background.gif");
            if (imgUrl == null) {
                throw new RuntimeException("Plik background.gif nie został znaleziony w resources!");
            }
            backgroundGif = new ImageIcon(imgUrl);
            System.out.println("Animowane tło wczytane pomyślnie!");
        } catch (Exception e) {
            System.out.println("Nie można wczytać animowanego tła: " + e.getMessage());
            System.out.println("Używam domyślnego koloru tła.");
        }
    }

    // Wczytuje zwykłe obrazki gracza i przeciwnika
    private void loadCharacterImages() {
        try (InputStream is = getClass().getResourceAsStream("/rycerz.jpg")) {
            if (is == null) {
                throw new IOException("Plik rycerz.jpg nie został znaleziony w resources!");
            }
            playerImage = ImageIO.read(is);
            System.out.println("Obrazek gracza wczytany!");
        } catch (IOException e) {
            System.out.println("Nie można wczytać obrazka gracza: " + e.getMessage());
        }

        try (InputStream is = getClass().getResourceAsStream("/goblin.jpg")) {
            if (is == null) {
                throw new IOException("Plik goblin.jpg nie został znaleziony w resources!");
            }
            enemyImage = ImageIO.read(is);
            System.out.println("Obrazek przeciwnika wczytany!");
        } catch (IOException e) {
            System.out.println("Nie można wczytać obrazka przeciwnika: " + e.getMessage());
        }
    }

    // Rysuje wszystkie elementy gry
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Narysuj animowane tło
        if (backgroundGif != null) {
            Image bgImage = backgroundGif.getImage();
            g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }

        Player player = battleground.getPlayer();
        Enemy enemy = battleground.getCurrentEnemy();

        // Oblicz pozycje i rozmiary jako procenty wymiarów okna
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int playerX = (int)(panelWidth * 0.30);
        int enemyX = (int)(panelWidth * 0.65);
        int actorY = (int)(panelHeight * 0.85);

        int actorWidth = (int)(panelHeight * 0.30);
        int actorHeight = (int)(panelHeight * 0.40);

        // Rysuj gracza
        if (playerImage != null) {
            drawActorWithImageScaled(g2d, player, playerImage, playerX, actorY, actorWidth, actorHeight);
        } else {
            drawActor(g2d, player, playerX, actorY, true);
        }

        // Rysuj przeciwnika
        if (enemy != null) {
            if (enemyImage != null) {
                drawActorWithImageScaled(g2d, enemy, enemyImage, enemyX, actorY, actorWidth, actorHeight);
            } else {
                drawActor(g2d, enemy, enemyX, actorY, false);
            }
        }
    }

    // Rysuje postać używając obrazka ze skalowaniem
    private void drawActorWithImageScaled(Graphics2D g, Actor actor, BufferedImage image,
                                          int x, int y, int width, int height) {
        // Rysuj obrazek wyśrodkowany
        g.drawImage(image, x - width/2, y - height, width, height, this);

        // Rozmiar czcionki proporcjonalny do wysokości postaci
        int fontSize = Math.max(12, height / 15);

        // Nazwa pod obrazkiem
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, fontSize));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(actor.getName(), x - fm.stringWidth(actor.getName()) / 2, y + fontSize + 5);

        // Paski HP i Energii
        drawStatusBarsScaled(g, actor, x, y + fontSize + 10, width);
    }

    // Rysuje paski HP i energii ze skalowaniem
    private void drawStatusBarsScaled(Graphics2D g, Actor actor, int x, int y, int actorWidth) {
        int barWidth = (int)(actorWidth * 1.2);
        int barHeight = Math.max(12, actorWidth / 10);

        // Pasek HP
        drawBar(g, x, y, barWidth, barHeight, actor.getHealth(), actor.getMaxHealth(),
                new Color(50, 200, 50), "HP");

        // Pasek Energii
        drawBar(g, x, y + barHeight + 5, barWidth, barHeight, actor.getEnergy(), actor.getMaxEnergy(),
                new Color(50, 150, 255), "EN");
    }

    // Rysuje postać jako proste kształty (backup)
    private void drawActor(Graphics2D g, Actor actor, int x, int y, boolean isPlayer) {
        // Ciało
        g.setColor(isPlayer ? new Color(100, 150, 255) : new Color(200, 50, 50));
        g.fillOval(x - 30, y - 80, 60, 80);

        // Głowa
        g.setColor(new Color(255, 220, 180));
        g.fillOval(x - 25, y - 120, 50, 50);

        // Miecz
        g.setColor(new Color(180, 180, 180));
        g.fillRect(isPlayer ? x + 30 : x - 50, y - 50, 20, 60);

        // Łuk
        g.setColor(new Color(139, 69, 19));
        g.drawArc(isPlayer ? x - 60 : x + 40, y - 60, 30, 60, isPlayer ? 90 : -90, 180);

        // Nazwa
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(actor.getName(), x - fm.stringWidth(actor.getName()) / 2, y + 30);

        // Paski HP i Energii
        drawStatusBars(g, actor, x, y + 45);
    }

    // Rysuje paski HP i energii (wersja ze stałym rozmiarem)
    private void drawStatusBars(Graphics2D g, Actor actor, int x, int y) {
        int barWidth = 120;
        int barHeight = 15;

        // Pasek HP
        drawBar(g, x, y, barWidth, barHeight, actor.getHealth(), actor.getMaxHealth(),
                new Color(50, 200, 50), "HP");

        // Pasek Energii
        drawBar(g, x, y + 20, barWidth, barHeight, actor.getEnergy(), actor.getMaxEnergy(),
                new Color(50, 150, 255), "EN");
    }

    // Rysuje pojedynczy pasek (HP lub energia)
    private void drawBar(Graphics2D g, int x, int y, int width, int height,
                         int current, int max, Color color, String label) {
        int barX = x - width / 2;

        // Tło paska
        g.setColor(new Color(60, 60, 60));
        g.fillRect(barX, y, width, height);

        // Wypełnienie paska proporcjonalne do wartości
        float percent = (float) current / max;
        g.setColor(color);
        g.fillRect(barX, y, (int) (width * percent), height);

        // Obramowanie paska
        g.setColor(Color.WHITE);
        g.drawRect(barX, y, width, height);

        // Tekst z wartościami
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        String text = label + ": " + current + "/" + max;
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, x - fm.stringWidth(text) / 2, y + 12);
    }
}