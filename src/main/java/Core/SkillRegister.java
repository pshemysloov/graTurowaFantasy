package Core;

import Core.Skill;
import Core.Skills.*;

public enum SkillRegister {
    ATTACK(0, Attack.getInstance()),
    HEAL(1, Heal.getInstance())
    ;


    private final int id;
    private final Skill skill;

    SkillRegister(int id, Skill skill) {
        this.id = id;
        this.skill = skill;
    }

    public int getId() { return id; }
    public Skill getSkill() { return skill; }

    public static Skill getSkillById(int id) {
        for (SkillRegister register : SkillRegister.values()) {
            if (register.getId() == id) {
                return register.getSkill();
            }
        }
        return null;
    }
}
