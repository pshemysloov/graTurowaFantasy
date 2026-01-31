package Core;

import java.util.ArrayList;
import java.util.Iterator;

@Author(name = "Mateusz Biskup")
public abstract class Actor {
    public String name;
    public int maxHealth;
    public int health;
    public int maxEnergy;
    public int energy;

    public Attributes attributes;

    public ArrayList<StatusEffect> activeEffects = new ArrayList<>();

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
        this.activeEffects.clear();
    }

    public void applyEffect(StatusEffect effect) {
        activeEffects.add(effect);
        effect.onApply(this);
    }

    public void processEffects() {
        Iterator<StatusEffect> it = activeEffects.iterator();
        while (it.hasNext()) {
            StatusEffect effect = it.next();
            effect.onTurnStart(this);
            effect.duration--;
            if (effect.duration <= 0) {
                effect.onRemove(this);
                it.remove();
            }
        }
    }


}