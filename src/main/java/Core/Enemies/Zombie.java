package Core.Enemies;

import Core.Actor;
import Core.Author;
import Core.Enemy;
import Core.SkillRegister;

import java.util.ArrayList;

/**
 * Klasa reprezentująca przeciwnika typu Zombie.
 * Posiada teraz ulepszony moduł decyzyjny (AI).
 */
@Author(name = "Mateusz Biskup")
public class Zombie extends Enemy {

    public Zombie() {
        // Konstruktor: Zombie ma Atak (slot 0) i Leczenie (slot 1)
        super("Zombie",  5, 5, 5, 5, 5, SkillRegister.getSkillById(0), SkillRegister.getSkillById(1), 15, "/goblin.jpg");
    }

    public Zombie(String name) {
        super(name,  5, 5, 5, 5, 5, SkillRegister.getSkillById(0), SkillRegister.getSkillById(1), 15, "/goblin.jpg");
    }

    /**
     * Zaawansowana logika tury przeciwnika (AI).
     * Zamiast losowego ataku, przeciwnik analizuje sytuację na polu bitwy.
     */
    @Override
    public String takeTurn(ArrayList<Actor> players){
        // 1. Wybór Celu (Targeting Logic)
        // Zombie szuka "najsłabszego ogniwa" - gracza z najniższym poziomem zdrowia.
        Actor target = players.get(0);
        for (Actor p : players) {
            if (p.health < target.health) {
                target = p;
            }
        }

        // 2. Analiza Stanu Własnego (Self-Preservation)
        // Czy mam krytycznie mało życia? (mniej niż 30%)
        boolean isLowHealth = (this.health < this.maxHealth * 0.3);

        // --- Drzewo Decyzyjne ---

        // SCENARIUSZ A: Leczenie (Priorytet Defensywny)
        // Warunki: Mało HP + Posiada skill leczący + Ma na niego energię
        if (isLowHealth &&
                this.specialAction != null &&
                this.specialAction.name.equals("Heal") &&
                this.energy >= this.specialAction.energyCost) {

            this.energy -= this.specialAction.energyCost;

            // Tworzymy listę celów zawierającą samego siebie (Self-Cast)
            ArrayList<Actor> selfTarget = new ArrayList<>();
            selfTarget.add(this);

            return this.name + " (AI) czuje strach i próbuje się uleczyć!\n" +
                    this.specialAction.useSkill(this, selfTarget);
        }

        // SCENARIUSZ B: Atak (Priorytet Ofensywny)
        // Warunki: Ma wystarczająco energii na podstawowy atak
        if (this.energy >= this.basicAction.energyCost) {
            this.energy -= this.basicAction.energyCost;
            this.energy += this.basicAction.energyGain;

            ArrayList<Actor> attackTargets = new ArrayList<>();
            attackTargets.add(target);

            return this.basicAction.useSkill(this, attackTargets);
        }

        // SCENARIUSZ C: Odpoczynek (Regeneracja)
        // Warunki: Brak energii na jakąkolwiek akcję
        int energyRecovered = 15;
        this.energy += energyRecovered;
        if (this.energy > this.maxEnergy) this.energy = this.maxEnergy;

        return this.name + " charczy ze zmęczenia i regeneruje siły (+" + energyRecovered + " Energii).";
    }
}