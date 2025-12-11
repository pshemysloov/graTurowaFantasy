package Core;

public abstract class Skill {
    String name;
    String description;
    int energyCost;

    public Skill(String name, String description, int energyCost) {
        this.name = name;
        this.description = description;
        this.energyCost = energyCost;
    }

    public abstract void useSkill(Actor user, Actor target);

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getEnergyCost() { return energyCost; }
}
