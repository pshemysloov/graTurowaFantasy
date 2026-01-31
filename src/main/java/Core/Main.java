package Core;

import Scenes.*;

import javax.swing.*;
import TCPServer.*;
import TCPServer.Packets.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Main  {
    static AppWindow window = new AppWindow();
    MainMenuPanel menu;
    CreateAccountPanel createAccount;
    AuthorsPanel authors;
    AfterLoginPanel afterLogin;
    LoginPanel login;
    DungeonPanel dungeon;
    EquipmentPanel equipment;

    Player player;


    static void main() {
        new Main().start();
    }

    public void start() {
        // Zmień zachowanie przycisku X, aby nie zamykał aplikacji natychmiast
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleLogoutAndExit();
            }
        });


        SwingUtilities.invokeLater(() -> {
            menu = new MainMenuPanel(
                    window,
                    // onLogin
                    this::onLoginClicked,
                    // onCreateAccount
                    this::onCreateAccount,
                    // onAuthors (UWAGA: Usunięto onOptions, zgodnie z nowym MainMenuPanel)
                    this::onAuthorsClicked,
                    // onExit
                    this::onExitClicked
            );

            window.registerScene("mainmenu", menu);
            window.showScene("mainmenu");
            window.setVisible(true);
        });
    }

    private void onLoginClicked() {
        SwingUtilities.invokeLater(() -> {
            login = new LoginPanel(window, this::onLoginConfirmClicked);
            window.registerScene("login",login);
            window.showScene("login");
            window.setVisible(true);
        });
    }

    private void onCreateAccount() {
        SwingUtilities.invokeLater(() -> {
            createAccount = new CreateAccountPanel(window, this::onConfirmClicked);
            window.registerScene("createaccount",createAccount);
            window.showScene("createaccount");
            window.setVisible(true);
        });
    }

    // Metoda onOptionsClicked usunięta, ponieważ przycisk Opcje nie istnieje

    private void onAuthorsClicked() {
        SwingUtilities.invokeLater(() -> {
            authors = new AuthorsPanel(window);
            window.registerScene("authors",authors);
            window.showScene("authors");
            window.setVisible(true);
        });
    }

    private void onExitClicked() {
        // zamknięcie aplikacji
        handleLogoutAndExit();
    }

    private void onConfirmClicked() {
        SwingUtilities.invokeLater(() -> {
            ClientToServerHandler handler = new ClientToServerHandler();
            String username = createAccount.getUsername();
            String password = createAccount.getPassword();
            RegisterInfo registerInfo = new RegisterInfo(username, password);
            RegisterResponse response;
            try {
                response = handler.sendRegisterInfo(registerInfo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(response == null){
                JOptionPane.showMessageDialog(null, "Nie otrzymano odpowiedzi od serwera");
                return;
            }

            JOptionPane.showMessageDialog(null,response.message);

        });
    }

    private void onLoginConfirmClicked(){
        SwingUtilities.invokeLater(() -> {
            ClientToServerHandler handler = new ClientToServerHandler();
            String username = login.getUsername();
            String password = login.getPassword();
            LoginInfo loginInfo = new LoginInfo(username, password);
            LoginInfoResponse response;
            try {
                response = handler.sendLoginInfo(loginInfo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(response == null){
                JOptionPane.showMessageDialog(null, "Nie otrzymano odpowiedzi od serwera");
            }

            else if(!response.success){
                JOptionPane.showMessageDialog(null,response.message);
            }

            else {
                // Utworzenie gracza na podstawie danych z serwera
                player = createPlayerFromServerData(response);


                afterLogin = new AfterLoginPanel(
                        window,
                        this::OnWalkaKomputerClicked,
                        this::OnWalkaGraczClicked,
                        this::OnEkwipunekClicked,
                        this::onWyjscieClicked
                );

                window.registerScene("afterlogin",afterLogin);
                window.showScene("afterlogin");
                window.setVisible(true);
            }
        });
    }

    private Player createPlayerFromServerData(LoginInfoResponse response){
        ArrayList<Integer> stats = new ArrayList<>();

        if (response.message != null && !response.message.isEmpty()) {
            String[] parts = response.message.split(";");
            for (String part : parts) {
                try {
                    stats.add(Integer.parseInt(part.trim()));
                } catch (NumberFormatException e) {
                    // Ignoruj części, które nie są liczbami
                }
            }
        }

        if (stats.size() != 12) {
            System.err.println("Nieprawidłowa liczba statystyk otrzymanych z serwera");
            return null;
        }

        // stats: 0:str, 1:acc, 2:int, 3:will, 4:const, 5-8:skill_ids, 9:level, 10:exp, 11:attribute_points
        return new Player(
                response.nickname,
                stats.get(0), // strength
                stats.get(1), // accuracy
                stats.get(2), // intelligence
                stats.get(3), // willpower
                stats.get(4), // constitution
                SkillRegister.getSkillById(stats.get(5)), // skill1
                SkillRegister.getSkillById(stats.get(6)), // skill2
                SkillRegister.getSkillById(stats.get(7)), // skill3
                SkillRegister.getSkillById(stats.get(8)), // skill4
                stats.get(9), // level
                stats.get(10), // experience
                stats.get(11) // attribute points
        );
    }

    private void OnEkwipunekClicked() {
        if (player != null) {
            equipment = new EquipmentPanel(window, player, this::synchronizePlayerDataWithServer);
            window.registerScene("equipment", equipment);
            window.showScene("equipment");
        } else {
            JOptionPane.showMessageDialog(null, "Błąd: Gracz nie jest zalogowany.");
        }
    }

    private void OnWalkaGraczClicked() {
        if (player != null) {
            String sessionCode = JOptionPane.showInputDialog(window, "Podaj kod sesji (np. 1234):", "Kod Sesji", JOptionPane.PLAIN_MESSAGE);

            if (sessionCode != null && !sessionCode.trim().isEmpty()) {
                player.resetStatus();
                PlayerVSPlayerPanel pvpPanel = new PlayerVSPlayerPanel(window, player, sessionCode.trim());
                window.registerScene("pvp", pvpPanel);
                window.showScene("pvp");
                window.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Player jest null");
        }
    }

    private void OnWalkaKomputerClicked(){
        if (player != null) {
            player.resetStatus();
            dungeon = new DungeonPanel(window, player, this::synchronizePlayerDataWithServer);
            window.registerScene("dungeon",dungeon);
            window.showScene("dungeon");
            window.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Player jest null");
        }
    }

    private void onWyjscieClicked(){
        handleLogoutAndExit();
    }

    private void handleLogoutAndExit() {
        if (player != null) {
            try {
                ClientToServerHandler handler = new ClientToServerHandler();
                handler.sendLogoutInfo(player.name);
            } catch (IOException e) {
                System.err.println("Błąd podczas wysyłania informacji o wylogowaniu: " + e.getMessage());
            }
        }
        System.exit(0);
    }

    private void synchronizePlayerDataWithServer(){
        if (player == null) return;

        ClientToServerHandler handler = new ClientToServerHandler();
        EquipmentInfoResponse response = handler.sendEquipmentInfo(player);

        if(response == null){
            JOptionPane.showMessageDialog(null, "Nie otrzymano odpowiedzi od serwera");
        }

        else if(!response.success){
            JOptionPane.showMessageDialog(null,response.message);
        }

        else {
            System.out.println("Zsynchronizowano dane z serwerem");
        }
    }
}