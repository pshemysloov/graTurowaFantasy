package Core.Skills;

import Core.Actor;
import Core.Author;
import Core.Skill;
import Core.TargetType;

import java.util.ArrayList;

@Author(name = "Mateusz Biskup")
public class Attack extends Skill {

    private Attack() {
        super("Attack", "A basic attack", 5,0, true, TargetType.SINGLE_TARGET);
    }

    private static final Attack INSTANCE = new Attack();

    public static Skill getInstance(){
        return INSTANCE;
    }

    @Override
    public String useSkill(Actor user, ArrayList<Actor> target) {
        int damage = user.attributes.strength * 2;
        String return_str = "";
        for (Actor actor : target) {
            actor.takeDamage(damage);
            return_str += user.name + ": zadano " +damage+" obrażeń postaci "+actor.name+"\n";
        }

        return return_str;
    }



}

