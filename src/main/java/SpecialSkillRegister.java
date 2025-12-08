public enum SpecialSkillRegister {
    HEAL(1, sk_Heal.getInstance()),
    FIREBALL(2, sk_Fireball.getInstance());

    private final int id;
    private final SpecialSkill skill;

    SpecialSkillRegister(int id, SpecialSkill skill) {
        this.id = id;
        this.skill = skill;
    }

    public int getId() { return id; }
    public SpecialSkill getSkill() { return skill; }

    public static SpecialSkill getSkillById(int id) {
        for (SpecialSkillRegister register : SpecialSkillRegister.values()) {
            if (register.getId() == id) {
                return register.getSkill();
            }
        }
        return null;
    }
}