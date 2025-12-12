package TCPServer;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerTerminal implements Runnable{
    private DatabaseHandler dbHandler;

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
                    case "help": help(); break;
                    case "exit": exit(); break;
                    default: System.out.println("[TERMINAL] Nieznane polecenie: " + line); break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void help(){
        System.out.println("[TERMINAL] Polecenia:");
        System.out.println("[TERMINAL] create db - tworzenie bazy danych");
        System.out.println("[TERMINAL] exit - zamknięcie serwera");
    }

    private void create_db(){
        System.out.println("[TERMINAL] Tworzenie bazy danych...");
        dbHandler.createDatabase();
    }

    private void exit(){
        System.exit(0);
    }
}
