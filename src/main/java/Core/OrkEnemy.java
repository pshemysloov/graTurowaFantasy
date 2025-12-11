package Core;

public class OrkEnemy extends Enemy {
    public OrkEnemy() {
        super("Ork", 90, 60, 25);
    }

    @Override
    public void performAction(Actor target) {
        if (Math.random() < 0.7) {
            // Potężne cięcie
            if (energy >= 15) {
                energy -= 15;
                int damage = 25 + (int)(Math.random() * 12);
                target.takeDamage(damage);
                GameLogger.log(name + " wykonuje potężne cięcie zadając " + damage + " obrażeń!");
            }
        } else {
            // Strzał z łuku
            if (energy >= 12) {
                energy -= 12;
                int damage = 20 + (int)(Math.random() * 8);
                target.takeDamage(damage);
                GameLogger.log(name + " strzela z łuku zadając " + damage + " obrażeń!");
            }
        }
    }
}
