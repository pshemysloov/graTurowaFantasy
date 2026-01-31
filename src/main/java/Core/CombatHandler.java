package Core;

import Scenes.DungeonPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

@Author(name = "Przemysław Błaszczyk")
public class CombatHandler implements Runnable{

    ArrayList<Actor> actorsInCombat;
    ArrayList<Actor> player;
    private DungeonPanel dungeonPanel;
    private volatile boolean running = true;
    private Thread combatThread;
    public int cumulativeExperience = 0;
    public int enemiesDefeated = 0;

    public CombatHandler(){
        actorsInCombat = new ArrayList<>();
        player = new ArrayList<>();
    }

    public void setDungeonPanel(Scenes.DungeonPanel panel) {
        this.dungeonPanel = panel;
    }

    public void addActor(Actor actor){
        actorsInCombat.add(actor);
    }

    public ArrayList<Actor> getActorsInCombat() {
        return actorsInCombat;
    }

    @Override
    public void run() {
        combatThread = Thread.currentThread();
        mainLoop();
    }

    public void stop() {
        this.running = false;
        if (combatThread != null) {
            combatThread.interrupt();
        }
    }

    private void mainLoop(){
        for (Actor actor : actorsInCombat) {
            if (actor instanceof Player){
                player.add(actor);
                break;
            }
        }

        if (player.isEmpty()) {
            throw new IllegalStateException("Player not found");
        }


        while(actorsInCombat.size() > 1 && running){
            Iterator<Actor> iterator = actorsInCombat.iterator();
            while (iterator.hasNext()) {
                Actor actor = iterator.next();

                if (actor instanceof Enemy){
                    Enemy enemyActor = (Enemy) actor;

                    // 1. Czy żyje
                    if (enemyActor.health <= 0) {
                        cumulativeExperience += enemyActor.experienceValue;
                        enemiesDefeated++;
                        iterator.remove();
                        if (dungeonPanel != null) {
                            SwingUtilities.invokeLater(() -> dungeonPanel.refreshEnemies());
                        }
                        continue;
                    }

                    // 2. Status effects
                    enemyActor.processEffects();

                    if (enemyActor.health <= 0) {
                        cumulativeExperience += enemyActor.experienceValue;
                        enemiesDefeated++;
                        iterator.remove();
                        if (dungeonPanel != null) {
                            SwingUtilities.invokeLater(() -> dungeonPanel.refreshEnemies());
                        }
                        continue;
                    }


                    // 3. Info
//                    GameLogger.log("Enemy turn: " + enemyActor.name);
//                    GameLogger.log("HP: " + enemyActor.health);
//                    GameLogger.log("Energy: " + enemyActor.energy);

                    // 4. Akcja
                    String action_msg = enemyActor.takeTurn(player);
                    GameLogger.log(action_msg);

                    // Odśwież UI po ataku przeciwnika
                    if (dungeonPanel != null) {
                        SwingUtilities.invokeLater(() -> dungeonPanel.refreshEnemies());
                    }


                } else if (actor instanceof Player) {
                    Player playerActor = (Player) actor;

                    // 1. Czy żyje
                    if (playerActor.health <= 0) {
                        dungeonPanel.handleBattleEnd(false);
                        return;
                    }

                    // 2. Status effects
                    playerActor.processEffects();

                    if (playerActor.health <= 0) {
                        dungeonPanel.handleBattleEnd(false);
                        return;
                    }

                    // 3. Info
//                    GameLogger.log("Player turn");
//                    GameLogger.log("HP: " + playerActor.health);
//                    GameLogger.log("Energy: " + playerActor.energy);

                    boolean endTurn = false;

                    // 4. Wybór skilli
                    while (!endTurn && running) {
                        try {
                            Skill skill = playerActor.chooseSkill();

                            if (skill == null) {
                                GameLogger.log("Niepoprawny wybór (null)");
                                continue;
                            }

                            if (playerActor.energy < skill.energyCost) {
                                GameLogger.log("Za mało energii");
                                continue;
                            }

                            ArrayList<Actor> targets = selectTargets(
                                    skill.targetType,
                                    playerActor
                            );

                            playerActor.energy -= skill.energyCost;
                            playerActor.energy += skill.energyGain;
                            String skill_msg = skill.useSkill(playerActor, targets);
                            GameLogger.log(skill_msg);

                            endTurn = skill.endsTurn;

                            // Odśwież UI po każdym użyciu skilla przez gracza
                            if (dungeonPanel != null) {
                                SwingUtilities.invokeLater(() -> dungeonPanel.refreshEnemies());
                            }
                        } catch (Exception e) {
                            if (e instanceof InterruptedException || !running) {
                                return;
                            }
                        }
                    }
                }

                if (dungeonPanel != null) {
                    SwingUtilities.invokeLater(() -> dungeonPanel.refreshEnemies());
                }
            }
        }

        if (running) {
            GameLogger.log("Wygrana!");
            dungeonPanel.handleBattleEnd(true);
        }
    }

    private ArrayList<Actor> selectTargets(TargetType targetType, Actor source) {
        ArrayList<Actor> targets = new ArrayList<>();
        switch (targetType) {
            case SINGLE_TARGET -> {
                if (source instanceof Player) {
                    GameLogger.log("Wybierz cel klikając na przeciwnika...");
                    Actor target = ((Player) source).chooseTarget();
                    targets.add(target);
                } else {
                    // Logika dla NPC: wybiera gracza
                    for (Actor a : actorsInCombat) {
                        if (a instanceof Player) {
                            targets.add(a);
                            break;
                        }
                    }
                }
            }
            case ALL_TARGETS -> {
                // Jeśli rzuca gracz, celujemy we wszystkich wrogów, jeśli wróg - w gracza (i ewentualnych sojuszników)
                for (Actor actor : actorsInCombat) {
                    if (source instanceof Player && actor instanceof Enemy) {
                        targets.add(actor);
                    } else if (source instanceof Enemy && actor instanceof Player) {
                        targets.add(actor);
                    }
                }
            }
            case RANDOM_TARGET -> {
                ArrayList<Actor> potentialTargets = new ArrayList<>();
                for (Actor actor : actorsInCombat) {
                    if (source instanceof Player && actor instanceof Enemy) potentialTargets.add(actor);
                    else if (source instanceof Enemy && actor instanceof Player) potentialTargets.add(actor);
                }
                if (!potentialTargets.isEmpty()) {
                    int randomIndex = (int) (Math.random() * potentialTargets.size());
                    targets.add(potentialTargets.get(randomIndex));
                }
            }
            case SELF -> targets.add(source);

            default -> throw new IllegalStateException("Unexpected value: " + targetType);
        }
        return targets;
    }


}

