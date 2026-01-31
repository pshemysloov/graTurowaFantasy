package TCPServer.Packets;

import Core.Author;

import java.io.Serializable;

@Author(name = "Przemysław Błaszczyk")
public class TurnInfo implements Serializable {
    public int currentHealth;
    public int maxHealth;
    public int currentEnergy;
    public int maxEnergy;
    public int skillId;

    // Attributes required for skill calculation
    public int strength;
    public int accuracy;
    public int intelligence;
    public int willpower;
    public int constitution;

    public String attackerName;

    public TurnInfo(int currentHealth, int maxHealth, int currentEnergy, int maxEnergy, int skillId,
                    int strength, int accuracy, int intelligence, int willpower, int constitution, String attackerName) {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.currentEnergy = currentEnergy;
        this.maxEnergy = maxEnergy;
        this.skillId = skillId;
        this.strength = strength;
        this.accuracy = accuracy;
        this.intelligence = intelligence;
        this.willpower = willpower;
        this.constitution = constitution;
        this.attackerName = attackerName;
    }
}