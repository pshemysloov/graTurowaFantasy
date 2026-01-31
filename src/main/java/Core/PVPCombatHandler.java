package Core;

import Scenes.PlayerVSPlayerPanel;
import TCPServer.ClientToServerHandler;
import TCPServer.Packets.GameFoundInfo;
import TCPServer.Packets.StartGameInfo;
import TCPServer.Packets.TurnInfo;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

@Author(name = "Przemysław Błaszczyk")
public class PVPCombatHandler implements Runnable {

    private final Player player;
    private Actor opponent; // "Dummy" actor representing the other player
    private PlayerVSPlayerPanel pvpPanel;
    private volatile boolean running = true;

    // Networking
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final ClientToServerHandler networkHandler;
    private final String sessionCode;
    private volatile boolean myTurn = true;

    public PVPCombatHandler(Player player, String sessionCode) {
        this.player = player;
        this.sessionCode = sessionCode;
        this.networkHandler = new ClientToServerHandler();

        // Create a placeholder opponent (will be updated via network)
        this.opponent = new Enemy("Przeciwnik", 10, 10, 10, 10, 10, null, null, 0, "/rycerz.jpg") {
            @Override
            public String takeTurn(ArrayList<Actor> target) { return ""; }
        };
    }

    public void setPvpPanel(PlayerVSPlayerPanel panel) {
        this.pvpPanel = panel;
    }

    public ArrayList<Actor> getActorsInCombat() {
        ArrayList<Actor> actors = new ArrayList<>();
        actors.add(player);
        actors.add(opponent);
        return actors;
    }

    @Override
    public void run() {
        try {
            connectToServer();

            // Start listening for opponent moves
            Thread listenerThread = new Thread(this::listenForUpdates);
            listenerThread.start();

            gameLoop();

        } catch (IOException e) {
            GameLogger.log("Błąd połączenia z serwerem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void disconnect() {
        running = false; // Ustawiamy flagę, aby nie przetwarzać błędu rozłączenia u siebie
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToServer() throws IOException {
        GameLogger.log("Łączenie z serwerem...");
        socket = networkHandler.connectForGame();
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        // Wyslij info do serwera
        StartGameInfo startInfo = new StartGameInfo(player.name, sessionCode);
        networkHandler.sendStartGameInfo(oos, startInfo);
        GameLogger.log("Oczekiwanie na przeciwnika (kod: " + sessionCode + ")...");
    }

    private void listenForUpdates() {
        try {
            while (running && !socket.isClosed()) {
                Object obj = ois.readObject();
                if (obj instanceof TurnInfo) {
                    handleTurnInfo((TurnInfo) obj);
                } else if (obj instanceof GameFoundInfo) {
                    handleGameFound((GameFoundInfo) obj);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            if (running) {
                running = false;

                if (player.health > 0){
                    GameLogger.log("Przeciwnik rozłączył się. Wygrałeś!");
                    if(pvpPanel != null)
                        pvpPanel.handleBattleEnd(true);
                } else {
                    if(pvpPanel != null)
                        pvpPanel.handleBattleEnd(false);
                }
            }
        }
    }

    private void handleTurnInfo(TurnInfo info) {
        // Zaktualizuj dane o przeciwniku
        opponent.maxHealth = info.maxHealth;
        opponent.maxEnergy = info.maxEnergy;
        opponent.name = info.attackerName;

        opponent.attributes.strength = info.strength;
        opponent.attributes.accuracy = info.accuracy;
        opponent.attributes.intelligence = info.intelligence;
        opponent.attributes.willpower = info.willpower;
        opponent.attributes.constitution = info.constitution;

        Skill usedSkill;
        if (info.skillId == -1)
            usedSkill = null;
        else
            usedSkill = SkillRegister.getSkillById(info.skillId);

        if (usedSkill != null) {
            ArrayList<Actor> targets = new ArrayList<>();

            if (usedSkill.targetType == TargetType.SELF) {
                targets.add(opponent);
            } else {
                targets.add(player);
            }

            String logMsg = usedSkill.useSkill(opponent, targets);
            GameLogger.log(opponent.name + " używa " + usedSkill.name);
            GameLogger.log(logMsg);

            // Check if this skill ended the opponent's turn, giving control back to us
            if (usedSkill.endsTurn) {
                myTurn = true;
                GameLogger.log("--- Twoja tura! ---");

                player.processEffects();
            }
        }

        opponent.health = info.currentHealth;
        opponent.energy = info.currentEnergy;

        if (pvpPanel != null) {
            SwingUtilities.invokeLater(() -> pvpPanel.refreshEnemies());
        }

        // 1. Czy ja zginąłem? (Porażka
        if (player.health <= 0) {
            GameLogger.log("Zostałeś pokonany!");
            running = false;
            pvpPanel.handleBattleEnd(false);
        }
        // 2. Czy przeciwnik zginął? (Wygrana)
        else if (opponent.health <= 0) {
            GameLogger.log("Przeciwnik pokonany!");
            running = false;
            pvpPanel.handleBattleEnd(true);
        }
    }

    private void handleGameFound(GameFoundInfo info) {
        opponent.name = info.opponentName;
        GameLogger.log("Połączono z graczem: " + info.opponentName);

        try {
            TurnInfo initialStats = new TurnInfo(
                    player.health, player.maxHealth,
                    player.energy, player.maxEnergy,
                    -1, // -1 oznacza brak użycia skilla (tylko sync statystyk)
                    player.attributes.strength, player.attributes.accuracy,
                    player.attributes.intelligence, player.attributes.willpower,
                    player.attributes.constitution,
                    player.name
            );
            // Synchronizujemy wysyłanie, aby nie kolidowało z innymi wątkami
            synchronized (oos) {
                networkHandler.sendTurnInfo(oos, initialStats);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (pvpPanel != null) {
            SwingUtilities.invokeLater(() -> pvpPanel.refreshEnemies());
        }
    }

    private void gameLoop() {
        while (running && player.health > 0) {
            try {
                // Czekaj na wybranie skilla
                Skill skill = player.chooseSkill();

                if (skill == null) continue;
                if (!running) break;

                // Sprawdz czy tura jest moja
                if (!myTurn) {
                    GameLogger.log("Nie twoja tura! Czekaj na ruch przeciwnika.");
                    continue;
                }

                if (player.energy < skill.energyCost) {
                    GameLogger.log("Za mało energii!");
                    continue;
                }

                // Execute skill locally (Player attacking Opponent)
                ArrayList<Actor> targets = new ArrayList<>();

                if (skill.targetType == TargetType.SELF) {
                    targets.add(player); // Skille na siebie działają na gracza
                } else {
                    targets.add(opponent); // Ofensywne działają na przeciwnika
                }

                player.energy -= skill.energyCost;
                player.energy += skill.energyGain;

                String logMsg = skill.useSkill(player, targets);
                GameLogger.log(logMsg);

                for (Actor target : targets) {
                    target.processEffects();
                }

                // Jezeli skill konczy ture, ustaw odpowiednio flage
                if (skill.endsTurn) {
                    myTurn = false;
                    GameLogger.log("Koniec tury. Oczekiwanie na przeciwnika...");
                }

                // Send TurnInfo to server
                TurnInfo packet = new TurnInfo(
                        player.health,
                        player.maxHealth,
                        player.energy,
                        player.maxEnergy,
                        SkillRegister.getIdBySkill(skill),
                        player.attributes.strength,
                        player.attributes.accuracy,
                        player.attributes.intelligence,
                        player.attributes.willpower,
                        player.attributes.constitution,
                        player.name
                );

                networkHandler.sendTurnInfo(oos, packet);

                if (pvpPanel != null) {
                    SwingUtilities.invokeLater(() -> pvpPanel.refreshEnemies());
                }

            } catch (Exception e) {
                if (running) e.printStackTrace();
            }
        }
    }
}