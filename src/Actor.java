public abstract class Actor {
    String name;

    int maxHealth;
    int health;
    int maxEnergy;
    int energy;
    int shield;

    public Actor(String name, int maxHealth, int maxEnergy) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.maxEnergy = maxEnergy;
        this.health = maxHealth;
        this.energy = maxEnergy;
        this.shield = 0;
    }

}
