public class Player extends Actor{
    MeleeSkill meleeSkill;
    RangeSkill rangeSkill;
    SpecialSkill specialSkills[] = new SpecialSkill[4];

    int experience;
    int attributePoints;

    int strength;
    int accuracy;
    int intelligence;
    int willpower;
    int constitution;

    public Player(String name, int maxHealth, int maxEnergy) {
        super(name, maxHealth, maxEnergy);
    }
}
