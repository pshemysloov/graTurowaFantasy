public class sk_SwordSlash extends MeleeSkill {
    private sk_SwordSlash() {
        super("Cięcie Mieczem", "Potężny atak wręcz regenerujący energię", 0);
    }

    private static final sk_SwordSlash INSTANCE = new sk_SwordSlash();

    public static sk_SwordSlash getInstance() {
        return INSTANCE;
    }

    @Override
    public void useSkill(Actor user, Actor target) {
        // Regeneruj energię zamiast zużywać
        int energyGain = 15; // Ile energii odzyskujesz
        int oldEnergy = user.getEnergy();
        user.setEnergy(user.getEnergy() + energyGain);
        int actualGain = user.getEnergy() - oldEnergy;

        // Zadaj obrażenia
        int damage = 25 + (int)(Math.random() * 10);
        target.takeDamage(damage);

        GameLogger.log(user.getName() + " używa " + name + " zadając " + damage +
                " obrażeń i odzyskując 15 punktów energii!");
    }
}