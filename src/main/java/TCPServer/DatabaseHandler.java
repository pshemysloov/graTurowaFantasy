package TCPServer;

import java.sql.*;
import TCPServer.Packets.*;

public class DatabaseHandler {
    private static final String url = "jdbc:sqlite:database.db";
    private Connection conn;

    DatabaseHandler() {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("[DB HANDLER] Błąd połączenia z bazą danych.");
            e.printStackTrace();
        }
    }

    public void createDatabase(){
        System.out.println("[DB HANDLER] Baza danych utworzona.");
    }

    public boolean registerUser(RegisterInfo packet){
        System.out.println("Otrzymano pakiet z register info: "+packet.login+", "+packet.password);
        return true;

        // w przypadku niepowodzenia return false
    }

}
