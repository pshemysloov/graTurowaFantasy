package Core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class CombatHandler {

    ArrayList<Actor> actorsInCombat;
    ArrayList<Actor> player;

    public CombatHandler(){
        actorsInCombat = new ArrayList<>();
        player = new ArrayList<>();
    }

    void addActor(Actor actor){
        actorsInCombat.add(actor);
    }

    void removeActor(Actor actor){
        actorsInCombat.remove(actor);
    }

    void mainLoop(){
        for (Actor actor : actorsInCombat) {
            if (actor instanceof Player){
                player.add(actor);
                break;
            }
        }

        if (player.isEmpty()) {
            throw new IllegalStateException("Player not found");
        }


        OuterLoop:
        while(actorsInCombat.size() > 1){
            Iterator<Actor> iterator = actorsInCombat.iterator();
            while (iterator.hasNext()) {
                Actor actor = iterator.next();

                if (actor instanceof Enemy){
                    Enemy enemyActor = (Enemy) actor;

                    // 1. Czy żyje
                    if (enemyActor.health <= 0) {
                        iterator.remove();
                        continue;
                    }

                    // 2. Status effects


                    // 3. Info
                    System.out.println("Enemy turn: "+enemyActor.name);
                    System.out.println("HP: " + enemyActor.health);
                    System.out.println("Energy: " + enemyActor.energy);

                    // 4. Akcja
                    String action_msg = enemyActor.takeTurn(player);
                    System.out.println(action_msg);



                } else if (actor instanceof Player) {
                    Player playerActor = (Player) actor;

                    // 1. Czy żyje
                    if (playerActor.health <= 0) {
                        break OuterLoop;
                    }

                    // 2. Status effects
                    //playerActor.applyStatusEffects();

                    if (playerActor.health <= 0) {
                        break OuterLoop;
                    }

                    // 3. Info
                    System.out.println("Player turn");
                    System.out.println("HP: " + playerActor.health);
                    System.out.println("Energy: " + playerActor.energy);

                    boolean endTurn = false;

                    // 4. Wybór skilli
                    while (!endTurn) {
                        Skill skill = playerActor.chooseSkill();

                        if (skill == null) {
                            System.out.println("Invalid skill choice! (null)");
                            continue;
                        }

                        if (playerActor.energy < skill.energyCost) {
                            System.out.println("Not enough energy!");
                            continue;
                        }

                        ArrayList<Actor> targets = selectTargets(
                                skill.targetType,
                                playerActor
                        );

                        playerActor.energy -= skill.energyCost;
                        playerActor.energy += skill.energyGain;
                        String skill_msg = skill.useSkill(playerActor, targets);
                        System.out.println(skill_msg);

                        endTurn = skill.endsTurn;
                    }
                }
            }
        }

    }

    private ArrayList<Actor> selectTargets(TargetType targetType, Actor source) {
        ArrayList<Actor> targets = new ArrayList<>();
        switch (targetType) {
            case SINGLE_TARGET -> {
                System.out.println("Select target: ");
                Scanner scanner = new Scanner(System.in);
                int targetId = scanner.nextInt();
                targets.add(actorsInCombat.get(targetId));
                return targets;
            }
            case ALL_TARGETS -> {
                for (Actor actor : actorsInCombat) {
                    if (actor instanceof Enemy) {
                        targets.add(actor);
                    }
                }
            }
            case RANDOM_TARGET -> {
                int randomIndex = (int) (Math.random() * actorsInCombat.size());
                targets.add(actorsInCombat.get(randomIndex));
                return targets;
            }

            case SELF -> {
                targets.add(source);
                return targets;
            }
            default -> throw new IllegalStateException("Unexpected value: " + targetType);

        }
        return null;
    }


}

