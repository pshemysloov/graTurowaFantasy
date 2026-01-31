package Core.Skills;

import Core.Actor;
import Core.Author;
import Core.Skill;
import Core.TargetType;
import Core.Effects.BleedEffect;
import java.util.ArrayList;

@Author(name = "Mateusz Biskup")
public class BleedAttack extends Skill {

    private BleedAttack() {
        super("Rozprucie", "Zadaje obrażenia i nakłada krwawienie", 15, 0, true, TargetType.SINGLE_TARGET);
    }

    private static final BleedAttack INSTANCE = new BleedAttack();

    public static Skill getInstance(){
        return INSTANCE;
    }

    @Override
    public String useSkill(Actor user, ArrayList<Actor> target) {
        StringBuilder sb = new StringBuilder();

        for (Actor actor : target) {
            // 1. Zadaj obrażenia bazowe
            int damage = user.attributes.strength + 2;
            actor.takeDamage(damage);

            // 2. Nałóż efekt statusu
            actor.applyEffect(new BleedEffect(3, user.attributes.intelligence / 2)); // 3 tury krwawienia

            sb.append(user.name).append(" rani ").append(actor.name)
                    .append(" za ").append(damage).append(" i nakłada krwawienie!\n");
        }

        return sb.toString();
    }
}