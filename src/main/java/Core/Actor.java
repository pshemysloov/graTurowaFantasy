package Core;

public abstract class Actor {
    public String name;
    public int maxHealth;
    public int health;
    public int maxEnergy;
    public int energy;

    public Attributes attributes;


    public Actor(String name, int strength, int accuracy, int intelligence, int willpower, int constitution) {
        this.name = name;
        this.maxHealth = constitution*8;
        this.maxEnergy = willpower*10;
        this.health = maxHealth;
        this.energy = maxEnergy;
        attributes = new Attributes(strength, accuracy, intelligence, willpower, constitution);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) health = 0;
    }

    public void resetStatus() {
        this.health = maxHealth;
        this.energy = maxEnergy;
    }

}