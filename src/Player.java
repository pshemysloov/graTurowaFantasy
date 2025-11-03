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

    public Player(String name, int maxHealth, int maxEnergy, int experience, int attributePoints, int strength, int accuracy, int intelligence, int willpower, int constitution, int id_skill1, int id_skill2) {
        super(name, maxHealth, maxEnergy);
        this.experience = experience;
        this.attributePoints = attributePoints;
        this.strength = strength;
        this.accuracy = accuracy;
        this.intelligence = intelligence;
        this.willpower = willpower;
        this.constitution = constitution;
        
        // Initialize skills using SkillRegister
        specialSkills[0] = SkillRegister.getSkillById(id_skill1); // Heal
        specialSkills[1] = SkillRegister.getSkillById(id_skill2); // Fireball
    }

    public void death(){
        // handle death
    }
}
