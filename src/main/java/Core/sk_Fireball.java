package Core;

public class sk_Fireball extends SpecialSkill {
    private sk_Fireball() {
        super("Kula Ognia", "Magiczny atak ogniowy", 25);
    }

    private static final sk_Fireball INSTANCE = new sk_Fireball();

    public static sk_Fireball getInstance() {
        return INSTANCE;
    }

    @Override
    public void useSkill(Actor user, Actor target) {
        if (user.getEnergy() >= energyCost) {
            user.setEnergy(user.getEnergy() - energyCost);
            int damage = 35 + (int)(Math.random() * 15);
            target.takeDamage(damage);
            GameLogger.log(user.getName() + " używa " + name + " zadając " + damage + " obrażeń magicznych!");
        }
    }
}