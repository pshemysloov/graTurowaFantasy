package Core;

@Author(name = "Przemysław Błaszczyk")
public class EquipmentHandler {
    private final Player player;

    public EquipmentHandler(Player player) {
        this.player = player;
    }

    public boolean addAttribute(String attrName) {
        if (player.attributePoints <= 0) return false;

        switch (attrName) {
            case "Siła" -> player.attributes.strength++;
            case "Celność" -> player.attributes.accuracy++;
            case "Inteligencja" -> player.attributes.intelligence++;
            case "Wola" -> {
                player.attributes.willpower++;
                player.maxEnergy = player.attributes.willpower * 10;
            }
            case "Wytrzymałość" -> {
                player.attributes.constitution++;
                player.maxHealth = player.attributes.constitution * 8;
            }
            default -> { return false; }
        }
        player.attributePoints--;
        return true;
    }

    public boolean subtractAttribute(String attrName) {
        // Logika zapobiegająca zmniejszaniu poniżej poziomu bazowego (np. 1)
        boolean changed = false;
        switch (attrName) {
            case "Siła" -> { if(player.attributes.strength > 1) { player.attributes.strength--; changed = true; } }
            case "Celność" -> { if(player.attributes.accuracy > 1) { player.attributes.accuracy--; changed = true; } }
            case "Inteligencja" -> { if(player.attributes.intelligence > 1) { player.attributes.intelligence--; changed = true; } }
            case "Wola" -> {
                if(player.attributes.willpower > 1) {
                    player.attributes.willpower--;
                    player.maxEnergy = player.attributes.willpower * 10;
                    changed = true;
                }
            }
            case "Wytrzymałość" -> {
                if(player.attributes.constitution > 1) {
                    player.attributes.constitution--;
                    player.maxHealth = player.attributes.constitution * 8;
                    changed = true;
                }
            }
        }

        if (changed) {
            player.attributePoints++;
            return true;
        }
        return false;
    }

    public int getAttributeValue(String attrName) {
        return switch (attrName) {
            case "Siła" -> player.attributes.strength;
            case "Celność" -> player.attributes.accuracy;
            case "Inteligencja" -> player.attributes.intelligence;
            case "Wola" -> player.attributes.willpower;
            case "Wytrzymałość" -> player.attributes.constitution;
            default -> 0;
        };
    }

    public int getAvailablePoints() {
        return player.attributePoints;
    }

    public void assignSkillToSlot(Skill skill, int slotIndex) {
        if (slotIndex >= 0 && slotIndex < player.skills.length) {
            player.skills[slotIndex] = skill;
        }
    }


}