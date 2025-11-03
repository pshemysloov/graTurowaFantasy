public abstract class Actor {
    String name;

    int maxHealth;
    int health;
    int maxEnergy;
    int energy;
    int shield;

    int actionValue;

    public Actor(String name, int maxHealth, int maxEnergy,int actionValue) {
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
        this.health -= damage;
        if (health <= 0) {
            this.health = 0;
            death();
        }
    }

    public abstract void death();
}
