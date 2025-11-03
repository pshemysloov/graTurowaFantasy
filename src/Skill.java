public abstract class Skill {
    String name;
    String description;

    public Skill(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public abstract void useSkill(Actor user, Actor target);
}
