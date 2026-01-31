package Core.Skills;

import Core.Actor;
import Core.Author;
import Core.Skill;
import Core.TargetType;

import java.util.ArrayList;

/**
 * Klasa reprezentująca umiejętność "Potężne Uderzenie".
 * Jest to atak ofensywny o wysokim potencjale obrażeń (Burst Damage),
 * ale obarczony ryzykiem chybienia oraz wysokim kosztem energetycznym.
 * Demonstracja zaawansowanej logiki walki z wykorzystaniem atrybutów postaci (Accuracy).
 */
@Author(name = "Mateusz Biskup")
public class HeavySmash extends Skill {

    private HeavySmash() {
        // Parametry: Koszt 20 Energii, Typ celu: Pojedynczy przeciwnik
        super("Potężne Uderzenie", "Potężny atak (300% siły), ale obarczony ryzykiem chybienia.", 20, 0, true, TargetType.SINGLE_TARGET);
    }

    private static final HeavySmash INSTANCE = new HeavySmash();

    public static Skill getInstance(){
        return INSTANCE;
    }

    /**
     * Realizuje logikę ataku z uwzględnieniem testu celności.
     * Obrażenia są kalkulowane jako 300% siły użytkownika.
     * Szansa na trafienie zależy od bazowej wartości (60%) oraz atrybutu celności (Accuracy).
     */
    @Override
    public String useSkill(Actor user, ArrayList<Actor> target) {
        int damage = user.attributes.strength * 3; // Mnożnik obrażeń x3
        StringBuilder return_str = new StringBuilder();

        for (Actor actor : target) {
            // Obliczenie szansy na trafienie:
            // 0.60 (60%) to bazowa szansa. Każdy punkt statystyki Accuracy zwiększa szansę o 1%.
            double hitChance = 0.60 + (user.attributes.accuracy * 0.01);

            // Losowanie (RNG)
            if (Math.random() < hitChance) {
                // Trafienie
                actor.takeDamage(damage);
                return_str.append(user.name)
                        .append(" wykonuje POTĘŻNE UDERZENIE w ")
                        .append(actor.name)
                        .append(" zadając krytyczne ")
                        .append(damage)
                        .append(" obrażeń!\n");
            } else {
                // Chybienie
                return_str.append(user.name)
                        .append(" zamachuje się z całą siłą na ")
                        .append(actor.name)
                        .append(", ale TRACI równowagę i CHYBIA!\n");
            }
        }

        return return_str.toString();
    }
}