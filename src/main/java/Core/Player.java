package Core;

import java.util.Scanner;

public class Player extends Actor{
    public Skill[] skills = new Skill[4];
    public int level;
    public int experience;


    public Player(String name, int strength, int accuracy, int intelligence, int willpower, int constitution, Skill skill1, Skill skill2, Skill skill3, Skill skill4, int level, int experience) {
        super(name, strength, accuracy, intelligence, willpower, constitution);
        skills[0] = skill1;
        skills[1] = skill2;
        skills[2] = skill3;
        skills[3] = skill4;
        this.level = level;
        this.experience = experience;
    }

    public void takeTurn() {

    }

    public Skill chooseSkill() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose skill(1-4): ");
        int choice = scanner.nextInt();
        if (choice < 1 || choice > 4) return null;
        return skills[choice-1];
    }



}
