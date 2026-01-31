package Core.Skills;

import Core.Actor;
import Core.Author;
import Core.Skill;
import Core.TargetType;

import java.util.ArrayList;

/**
 * Klasa reprezentująca umiejętność "Medytacja".
 * Jest to umiejętność wsparcia (Utility Skill), która pozwala graczowi
 * poświęcić turę na regenerację dużej ilości energii, zamiast zadawania obrażeń.
 */
@Author(name = "Mateusz Biskup")
public class Meditation extends Skill {

    private Meditation() {
        // Parametry: Nazwa, Opis, Koszt Energii (0), Zysk Energii (30), Czy kończy turę (Tak), Typ celu (Siebie)
        super("Medytacja", "Skupienie pozwalające odzyskać znaczną ilość energii (+30).", 0, 30, true, TargetType.SELF);
    }

    private static final Meditation INSTANCE = new Meditation();

    public static Skill getInstance(){
        return INSTANCE;
    }

    @Override
    public String useSkill(Actor user, ArrayList<Actor> target) {
        return user.name + " wchodzi w stan głębokiej koncentracji, odzyskując siły witalne (+30 Energii).\n";
    }
}