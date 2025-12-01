public class sk_BowShot extends RangeSkill {
    private sk_BowShot() {
        super("Strzał z łuku", "Precyzyjny strzał na dystans", 15);
    }

    private static final sk_BowShot INSTANCE = new sk_BowShot();

    public static sk_BowShot getInstance() {
        return INSTANCE;
    }

    @Override
    public void useSkill(Actor user, Actor target) {
        if (user.getEnergy() >= energyCost) {
            user.setEnergy(user.getEnergy() - energyCost);
            int damage = 20 + (int)(Math.random() * 8);
            target.takeDamage(damage);
            GameLogger.log(user.getName() + " używa " + name + " zadając " + damage + " obrażeń!");
        }
    }
}
