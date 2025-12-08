public class sk_Heal extends SpecialSkill {
    private sk_Heal() {
        super("Leczenie", "Przywraca zdrowie", 20);
    }

    private static final sk_Heal INSTANCE = new sk_Heal();

    public static sk_Heal getInstance() {
        return INSTANCE;
    }

    @Override
    public void useSkill(Actor user, Actor target) {
        if (user.getEnergy() >= energyCost) {
            user.setEnergy(user.getEnergy() - energyCost);
            int healAmount = 30 + (int)(Math.random() * 20);
            int oldHealth = target.getHealth();
            target.setHealth(target.getHealth() + healAmount);
            int actualHeal = target.getHealth() - oldHealth;
            GameLogger.log(user.getName() + " używa " + name + " przywracając " + actualHeal + " HP!");
        }
    }
}