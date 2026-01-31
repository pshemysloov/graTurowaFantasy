package Core;

@Author(name = "Mateusz Biskup")
public class PlayerLevelHandler {
    private final Player player;
    public int expThreshold;

    public PlayerLevelHandler(Player player) {
        this.player = player;
        calculateExpThreshold();
        checkIfLevelUp();
    }

    public void calculateExpThreshold() {
        expThreshold = (player.level)^2 + player.level + 100;
    }

    public void checkIfLevelUp() {
        if (player.experience >= expThreshold) {
            player.experience -= expThreshold;
            player.level++;
            player.attributePoints += 3;
        }
    }
}
