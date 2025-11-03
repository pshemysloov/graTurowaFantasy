public class sk_Fireball extends SpecialSkill {
    private sk_Fireball(String name, String description){
        super(name,description);
    }

    private static final sk_Fireball INSTANCE = new sk_Fireball("Kula ognia", "Zadaje obrażenia");

    public static sk_Fireball getInstance() {
        return INSTANCE;
    }

    @Override
    public void useSkill(Actor user, Actor target) {
        // fireball logic
    }
}
