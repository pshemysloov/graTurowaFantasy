package Core;

import java.util.Scanner;

public class Player extends Actor{
    public Skill[] skills = new Skill[4];
    public int level;
    public int experience;

    private Skill selectedSkill;
    private Actor selectedTarget;

    public Player(String name, int strength, int accuracy, int intelligence, int willpower, int constitution, Skill skill1, Skill skill2, Skill skill3, Skill skill4, int level, int experience) {
        super(name, strength, accuracy, intelligence, willpower, constitution);
        skills[0] = skill1;
        skills[1] = skill2;
        skills[2] = skill3;
        skills[3] = skill4;
        this.level = level;
        this.experience = experience;
    }


    public synchronized Skill chooseSkill() {
        selectedSkill = null;
        try {
            while (selectedSkill == null) {
                wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return selectedSkill;
    }

    public synchronized void setSelectedSkill(Skill skill) {
        this.selectedSkill = skill;
        notifyAll();
    }

    public synchronized Actor chooseTarget() {
        selectedTarget = null;
        try {
            while (selectedTarget == null) {
                wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return selectedTarget;
    }

    public synchronized void setSelectedTarget(Actor target) {
        this.selectedTarget = target;
        notifyAll();
    }

}
