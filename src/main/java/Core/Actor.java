package Core;

public abstract class Actor {
    String name;
    int maxHealth;
    int health;
    int maxEnergy;
    int energy;
    int shield;
    int actionValue;

    public Actor(String name, int maxHealth, int maxEnergy, int actionValue) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.maxEnergy = maxEnergy;
        this.health = maxHealth;
        this.energy = maxEnergy;
        this.shield = 0;
        this.actionValue = actionValue;
    }

    public Actor(String name, int maxHealth, int maxEnergy) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.maxEnergy = maxEnergy;
        this.health = maxHealth;
        this.energy = maxEnergy;
        this.shield = 0;
    }

    public void takeDamage(int damage) {
        int actualDamage = Math.max(0, damage - shield);
        this.health -= actualDamage;
        if (health <= 0) {
            this.health = 0;
            death();
        }
    }

    public abstract void death();

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getEnergy() { return energy; }
    public int getMaxEnergy() { return maxEnergy; }
    public int getShield() { return shield; }
    public int getActionValue() { return actionValue; }
    public void setHealth(int health) { this.health = Math.min(health, maxHealth); }
    public void setEnergy(int energy) { this.energy = Math.min(energy, maxEnergy); }
    public void setShield(int shield) { this.shield = shield; }
}
