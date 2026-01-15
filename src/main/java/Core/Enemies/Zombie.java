package Core.Enemies;

import Core.Actor;
import Core.Enemy;
import Core.SkillRegister;

import java.util.ArrayList;

public class Zombie extends Enemy {

    public Zombie() {
        super("Zombie",  5, 5, 5, 5, 5, SkillRegister.getSkillById(0), SkillRegister.getSkillById(1), "/goblin.jpg");
    }

    public Zombie(String name) {
        super(name,  5, 5, 5, 5, 5, SkillRegister.getSkillById(0), SkillRegister.getSkillById(1), "/goblin.jpg");
    }

    public String takeTurn(ArrayList<Actor> target){
        String return_str;
        if (energy >= basicAction.energyCost) {
            energy -= basicAction.energyCost;
            energy += basicAction.energyGain;
            return_str = basicAction.useSkill(this, target);
        } else {
            return_str = "Brak energii!";
        }

        return return_str;
    }

}
