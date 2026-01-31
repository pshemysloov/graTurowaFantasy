package Core;

import java.util.ArrayList;

@Author(name = "Mateusz Biskup")
public abstract class Enemy extends Actor{
    public Skill basicAction;
    public Skill specialAction;
    public String spritePath;
    public int experienceValue;


    public Enemy (String name, int strength, int accuracy, int intelligence, int willpower, int constitution, Skill basicAction, Skill specialAction, int experienceValue, String spritePath) {
        super(name, strength, accuracy, intelligence, willpower, constitution);
        this.basicAction = basicAction;
        this.specialAction = specialAction;
        this.spritePath = spritePath;
        this.experienceValue = experienceValue;
    }

    public abstract String takeTurn(ArrayList<Actor> target);

}
