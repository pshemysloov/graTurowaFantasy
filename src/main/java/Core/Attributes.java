package Core;

@Author(name = "Mateusz Biskup")
public class Attributes {
    public int strength;
    public int accuracy;
    public int intelligence;
    public int willpower;
    public int constitution;

    public Attributes(int strength, int accuracy, int intelligence, int willpower, int constitution) {
        this.strength = strength;
        this.accuracy = accuracy;
        this.intelligence = intelligence;
        this.willpower = willpower;
        this.constitution = constitution;
    }
}
