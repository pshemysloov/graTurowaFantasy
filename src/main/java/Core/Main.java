package Core;

import Scenes.*;

import javax.swing.*;
import TCPServer.*;
import TCPServer.Packets.RegisterInfo;
import TCPServer.Packets.RegisterResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class Main  {
    static AppWindow window = new AppWindow();
    MainMenuPanel menu;
    CreateAccountPanel createAccount;
    AuthorsPanel authors;

    public static void main(String[] args) {
        new Main().start();
    }

    public void start() {
        SwingUtilities.invokeLater(() -> {
            menu = new MainMenuPanel(
                    window,
                    // onLogin
                    () -> onLoginClicked(),
                    // onCreateAccount
                    () -> onCreateAccount(),
                    // onOptions
                    () -> onOptionsClicked(),
                    // onAuthors
                    () -> onAuthorsClicked(),
                    // onExit
                    () -> onExitClicked()
            );

            window.registerScene("mainmenu", menu);
            window.showScene("mainmenu");
            window.setVisible(true);
        });
    }

    private void onLoginClicked() {
        SwingUtilities.invokeLater(() -> {
            String nick = JOptionPane.showInputDialog(null, "Podaj nick:", "Logowanie", JOptionPane.PLAIN_MESSAGE);
            if (nick != null && !nick.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Zalogowano jako: " + nick, "OK", JOptionPane.INFORMATION_MESSAGE);
                // po zalogowaniu otwórz Dungeon
                Player player = new Player(nick, 150, 100, 0, 0, 10, 10, 10, 10, 10, 10, 1, 2);
                DungeonPanel dungeon = new DungeonPanel(player);
                window.registerScene("dungeon",dungeon);
                window.showScene("dungeon");
                window.setVisible(true);
            }
        });
    }

    private void onCreateAccount() {
        SwingUtilities.invokeLater(() -> {
            createAccount = new CreateAccountPanel(window, () -> onConfirmClicked());
            window.registerScene("createaccount",createAccount);
            window.showScene("createaccount");
            window.setVisible(true);
        });
    }

    private void onOptionsClicked() {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null, "Brak opcji do ustawienia.", "Opcje", JOptionPane.INFORMATION_MESSAGE)
        );
    }

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
        System.exit(0);
    }

    private void onConfirmClicked() {
        SwingUtilities.invokeLater(() -> {
            ClientToServerHandler handler = new ClientToServerHandler();
            String username = createAccount.getUsername();
            String password = createAccount.getPassword();
            RegisterInfo registerInfo = new RegisterInfo(username, password);
            RegisterResponse response = null;
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

}