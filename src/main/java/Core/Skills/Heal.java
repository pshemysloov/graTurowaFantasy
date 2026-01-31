package Core.Skills;

import Core.Actor;
import Core.Author;
import Core.Skill;
import Core.TargetType;

import java.util.ArrayList;

@Author(name = "Mateusz Biskup")
public class Heal extends Skill {

    private Heal() {
        super("Heal", "Heals the player", 10,0, true, TargetType.SELF);
    }

    private static final Heal INSTANCE = new Heal();

    public static Skill getInstance(){
        return INSTANCE;
    }

    @Override
    public String useSkill(Actor user, ArrayList<Actor> target) {
        String return_str = new String();
        for (Actor actor : target) {
            int healing_value = 20+2*user.attributes.intelligence;
            actor.health += healing_value;
            if (actor.health > actor.maxHealth) actor.health = actor.maxHealth;
            return_str += "Uleczono "+actor.name+" o "+healing_value+" punktów życia";
        }
        return return_str;
    }

}
