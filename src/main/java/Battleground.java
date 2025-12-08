import java.util.ArrayList;
import java.util.List;

public class Battleground {
    private Player player;
    private List<Enemy> currentEnemies;
    private int currentEnemyIndex;

    public Battleground(Player player) {
        this.player = player;
        this.currentEnemies = new ArrayList<>();
        this.currentEnemyIndex = 0;
        addEnemyWave();
    }

    public void addEnemyWave() {
        currentEnemies.clear();
        currentEnemyIndex = 0;

        // Dodaj losowych przeciwników
        int enemyCount = 1 + (int)(Math.random() * 2);
        for (int i = 0; i < enemyCount; i++) {
            if (Math.random() < 0.5) {
                currentEnemies.add(new GoblinEnemy());
            }
            /*
            else {
                currentEnemies.add(new OrkEnemy());
            }

             */
        }

        GameLogger.log("\n=== Nowa fala przeciwników! ===");
        for (Enemy e : currentEnemies) {
            GameLogger.log("- " + e.getName());
        }
        GameLogger.log("");
    }

    public Enemy getCurrentEnemy() {
        if (currentEnemyIndex < currentEnemies.size()) {
            return currentEnemies.get(currentEnemyIndex);
        }
        return null;
    }

    public void nextEnemy() {
        currentEnemyIndex++;
    }

    public boolean isWaveCleared() {
        return currentEnemyIndex >= currentEnemies.size();
    }

    public Player getPlayer() {
        return player;
    }
}