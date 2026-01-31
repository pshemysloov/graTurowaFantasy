package Core;

@Author(name = "Mateusz Biskup")
public class Player extends Actor{
    public Skill[] skills = new Skill[4];
    public int level;
    public int experience;
    public int attributePoints;

    private Skill selectedSkill;
    private Actor selectedTarget;

    public Player(String name, int strength, int accuracy, int intelligence, int willpower, int constitution, Skill skill1, Skill skill2, Skill skill3, Skill skill4, int level, int experience, int attributePoints) {
        super(name, strength, accuracy, intelligence, willpower, constitution);
        skills[0] = skill1;
        skills[1] = skill2;
        skills[2] = skill3;
        skills[3] = skill4;
        this.level = level;
        this.experience = experience;
        this.attributePoints = attributePoints;
    }


    public synchronized Skill chooseSkill() {
        selectedSkill = null;
        while (selectedSkill == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return selectedSkill;
    }

    public synchronized void setSelectedSkill(Skill skill) {
        this.selectedSkill = skill;
        notifyAll();
    }

    public synchronized Actor chooseTarget() {
        selectedTarget = null;
        while (selectedTarget == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return selectedTarget;
    }

    public synchronized void setSelectedTarget(Actor target) {
        this.selectedTarget = target;
        notifyAll();
    }

    public void addExperience(int exp){
        experience += exp;
    }

}
