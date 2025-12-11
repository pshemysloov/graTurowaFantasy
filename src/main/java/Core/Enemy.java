package Core;

public abstract class Enemy extends Actor {
    public Enemy(String name, int maxHealth, int maxEnergy, int actionValue) {
        super(name, maxHealth, maxEnergy, actionValue);
    }

    public abstract void performAction(Actor target);

    @Override
    public void death() {
        GameLogger.log(name + " został pokonany!");
    }
}