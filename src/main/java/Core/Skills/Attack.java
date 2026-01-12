package Core.Skills;

import Core.Actor;
import Core.Skill;
import Core.TargetType;

import java.util.ArrayList;

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
        String return_str = new String();
        for (Actor actor : target) {
            actor.takeDamage(damage);
            return_str += "Zadano "+damage+" obrażeń "+actor.name+"\n";
        }

        return return_str;
    }



}

