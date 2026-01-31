package Core;

import java.util.ArrayList;

@Author(name = "Mateusz Biskup")
public abstract class Skill {
    public String name;
    public String description;
    public int energyCost;
    public int energyGain;
    public boolean endsTurn;
    public TargetType targetType;

    public Skill(String name, String description, int energyCost, int energyGain, boolean endsTurn, TargetType targetType) {
        this.name = name;
        this.description = description;
        this.energyCost = energyCost;
        this.energyGain = energyGain;
        this.endsTurn = endsTurn;
        this.targetType = targetType;
    }

    public abstract String useSkill(Actor user, ArrayList<Actor> target);

}
