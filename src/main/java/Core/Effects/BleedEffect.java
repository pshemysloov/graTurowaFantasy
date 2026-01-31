package Core.Effects;

import Core.Actor;
import Core.Author;
import Core.GameLogger;
import Core.StatusEffect;

@Author(name = "Mateusz Biskup")
public class BleedEffect extends StatusEffect {
    int multiplier;

    public BleedEffect(int duration, int multiplier) {
        super("Krwawienie", duration);
        this.multiplier = multiplier;
    }

    @Override
    public void onTurnStart(Actor owner) {
        int damage = (int)(owner.maxHealth * multiplier * 0.05);
        owner.takeDamage(damage);
        GameLogger.log(owner.name + " krwawi i traci " + damage);
    }
}


