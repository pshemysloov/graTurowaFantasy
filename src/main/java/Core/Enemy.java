package Core;

import java.util.ArrayList;

public abstract class Enemy extends Actor{
    public Skill basicAction;
    public Skill specialAction;
    public String spritePath;


    public Enemy (String name, int strength, int accuracy, int intelligence, int willpower, int constitution, Skill basicAction, Skill specialAction, String spritePath) {
        super(name, strength, accuracy, intelligence, willpower, constitution);
        this.basicAction = basicAction;
        this.specialAction = specialAction;
        this.spritePath = spritePath;
    }

    public abstract String takeTurn(ArrayList<Actor> target);

}
