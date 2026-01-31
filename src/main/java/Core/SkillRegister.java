package Core;

import Core.Skills.*;

/**
 * Rejestr wszystkich umiejętności dostępnych w grze.
 * Mapuje unikalne identyfikatory (ID) na instancje obiektów Skill.
 * Umożliwia łatwe ładowanie umiejętności z bazy danych oraz rozszerzanie gry o nowe ataki.
 */
@Author(name = "Mateusz Biskup")
public enum SkillRegister {
    ATTACK(0, Attack.getInstance()),
    HEAL(1, Heal.getInstance()),
    BLEED_ATTACK(2, BleedAttack.getInstance()),

    // Nowe umiejętności dodane w ramach rozbudowy mechaniki walki
    MEDITATION(3, Meditation.getInstance()),
    HEAVY_SMASH(4, HeavySmash.getInstance())
    ;


    private final int id;
    private final Skill skill;

    SkillRegister(int id, Skill skill) {
        this.id = id;
        this.skill = skill;
    }

    public int getId() { return id; }
    public Skill getSkill() { return skill; }

    /**
     * Pobiera obiekt umiejętności na podstawie jego ID.
     * Wykorzystywane przy wczytywaniu stanu postaci.
     */
    public static Skill getSkillById(int id) {
        for (SkillRegister register : SkillRegister.values()) {
            if (register.getId() == id) {
                return register.getSkill();
            }
        }
        return null;
    }

    /**
     * Pobiera ID dla danej instancji umiejętności.
     * Wykorzystywane przy zapisywaniu stanu postaci.
     */
    public static int getIdBySkill(Skill skill) {
        if (skill == null) return -1;
        for (SkillRegister register : SkillRegister.values()) {
            if (register.getSkill() == skill) {
                return register.getId();
            }
        }
        return -1;
    }
}