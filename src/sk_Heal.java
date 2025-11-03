public class sk_Heal extends SpecialSkill{
    private sk_Heal(String name, String description){
        super(name,description);
    }

    private static final sk_Heal INSTANCE = new sk_Heal("Leczenie", "Leczy gracza");

    public static sk_Heal getInstance() {
        return INSTANCE;
    }

    @Override
    public void useSkill(Actor user, Actor target) {
        // heal logic
    }
}
