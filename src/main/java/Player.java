public class Player extends Actor {
    MeleeSkill meleeSkill;
    RangeSkill rangeSkill;
    SpecialSkill specialSkills[] = new SpecialSkill[4];

    int experience;
    int attributePoints;

    int strength;
    int accuracy;
    int intelligence;
    int willpower;
    int agility;
    int constitution;

    public Player(String name, int maxHealth, int maxEnergy, int experience, int attributePoints,
                  int strength, int accuracy, int intelligence, int willpower, int agility,
                  int constitution, int id_skill1, int id_skill2) {
        super(name, maxHealth, maxEnergy);
        this.experience = experience;
        this.attributePoints = attributePoints;

        this.strength = strength;
        this.accuracy = accuracy;
        this.intelligence = intelligence;
        this.willpower = willpower;
        this.agility = agility;
        this.constitution = constitution;

        this.actionValue = agility * 4;

        // Inicjalizacja umiejętności
        this.meleeSkill = sk_SwordSlash.getInstance();
        this.rangeSkill = sk_BowShot.getInstance();
        this.specialSkills[0] = SpecialSkillRegister.getSkillById(id_skill1);
        this.specialSkills[1] = SpecialSkillRegister.getSkillById(id_skill2);
    }

    @Override
    public void death() {
        GameLogger.log(name + " został pokonany!");
    }

    public MeleeSkill getMeleeSkill() { return meleeSkill; }
    public RangeSkill getRangeSkill() { return rangeSkill; }
    public SpecialSkill[] getSpecialSkills() { return specialSkills; }
}
