public class GoblinEnemy extends Enemy {
    public GoblinEnemy() {
        super("Goblin Łucznik", 60, 50, 30);
    }

    @Override
    public void performAction(Actor target) {
        if (Math.random() < 0.6) {
            // Strzał z łuku
            if (energy >= 10) {
                energy -= 10;
                int damage = 15 + (int)(Math.random() * 8);
                target.takeDamage(damage);
                GameLogger.log(name + " strzela z łuku zadając " + damage + " obrażeń!");
            }
        } else {
            // Atak mieczem
            if (energy >= 8) {
                energy -= 8;
                int damage = 18 + (int)(Math.random() * 6);
                target.takeDamage(damage);
                GameLogger.log(name + " atakuje mieczem zadając " + damage + " obrażeń!");
            }
        }
    }
}