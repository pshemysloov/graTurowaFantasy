package TCPServer;


import Core.Author;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

@Author(name = "Przemysław Błaszczyk")
public class ServerTerminal implements Runnable{
    private final DatabaseHandler dbHandler;

    public ServerTerminal(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("[TERMINAL] Terminal serwera uruchomiony. Wpisz 'help' aby zobaczyć dostępne polecenia.");

            String line;
            while ((line = br.readLine()) != null) {
                switch (line) {
                    case "create db": create_db(); break;
                    case "select users all": select_users_all(); break;
                    case "drop users": drop_users(); break;
                    case "help": help(); break;
                    case "exit": exit(); break;
                    case "reset login flags": reset_login_flags(); break;
                    case "logout user": logout_user(); break;
                    default: System.out.println("[TERMINAL] Nieznane polecenie: " + line); break;
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    private void help(){
        System.out.println("[TERMINAL] Polecenia:");
        System.out.println("[TERMINAL] create db - utworzenie bazy danych");
        System.out.println("[TERMINAL] drop users - usunięcie tabeli users");
        System.out.println("[TERMINAL] select users all - wyświetlenie wszystkich rekordów z tabeli users");
        System.out.println("[TERMINAL] exit - zamknięcie serwera");
        System.out.println("[TERMINAL] reset login flags - wyzerowanie flag logowania wszystkich użytkowników");
        System.out.println("[TERMINAL] logout user - wylogowanie konkretnego użytkownika");
    }

    private void create_db() throws SQLException {
        System.out.println("[TERMINAL] Tworzenie bazy danych...");
        dbHandler.createDatabase();
    }

    private void drop_users(){
        System.out.println("[TERMINAL] Usuwanie użytkowników...");
        dbHandler.dropUsers();
    }

    private void select_users_all(){
        System.out.println("[TERMINAL] Wszystkie rekordy z tabeli users:");
        dbHandler.selectAllUsers();
    }

    private void reset_login_flags(){
        System.out.println("[TERMINAL] Resetowanie flag logowania użytkowników...");
        dbHandler.resetLoginFlags();
    }

    private void logout_user(){
        Scanner sc = new Scanner(System.in);
        System.out.println("[TERMINAL] Podaj login użytkownika do wylogowania: ");
        String nickname = sc.nextLine();
        dbHandler.logoutUser(nickname);
    }

    private void exit(){
        System.exit(0);
    }
}
