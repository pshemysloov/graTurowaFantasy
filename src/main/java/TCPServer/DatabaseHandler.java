package TCPServer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import TCPServer.Packets.*;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import org.mindrot.jbcrypt.BCrypt;


public class DatabaseHandler {
    private static final String url = "jdbc:sqlite:database.db";
    private Connection conn;
    private int saltValue = 12; // zakres 4 - 30

    DatabaseHandler() {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("[DB HANDLER] Błąd połączenia z bazą danych.");
            e.printStackTrace();
        }
    }

    public void createDatabase() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                user_id INTEGER PRIMARY KEY AUTOINCREMENT, 
                login TEXT UNIQUE, 
                password TEXT,
                player_data TEXT)
""";

        try(Statement stmt = conn.createStatement()){
            stmt.executeUpdate(sql);
            System.out.println("[DB HANDLER] Utworzono bazę danych");
        } catch (SQLException e){
            System.err.println("[DB HANDLER] Wystąpił błąd SQL podczas tworzenia tabeli users");
            e.printStackTrace();
        }
    }

    public void selectAllUsers(){
        String sql = "SELECT * FROM users";

        try(Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);

            ArrayList<String[]> data = new ArrayList<>();

            while(rs.next()){
                String user_id = rs.getString("user_id");
                String login = rs.getString("login");
                String password = rs.getString("password");
                String player_data = rs.getString("player_data");
                String[] row = {user_id, login, password, player_data};
                data.add(row);
            }

            System.out.println(AsciiTable.getTable(data, Arrays.asList(
                    new Column().header("user_id").with(u -> u[0]),
                    new Column().header("login").with(u -> u[1]),
                    new Column().header("password").with(u -> u[2]),
                    new Column().header("player_data").with(u -> u[3])
            )));

            System.out.println("[DB HANDLER] Pomyślnie wyświetlono wszystkie rekordy");

        } catch (SQLException e){
            System.err.println("[DB HANDLER] Wystąpił błąd SQL podczas wyświetlania tabeli users");
            e.printStackTrace();
        }

    }

    public void dropUsers() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate(sql);
            System.out.println("[DB HANDLER] Pomyślnie usunięto tabelę users");
        } catch (SQLException e){
            System.err.println("[DB HANDLER] Wystąpił błąd SQL podczas usuwania tabeli users");
            e.printStackTrace();
        }
    }

    public boolean registerUser(RegisterInfo packet) throws SQLException {
        System.out.println("[DB HANDLER] Otrzymano pakiet z register info");

        String sql = "INSERT INTO users (login, password, player_data) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            String login = packet.login;
            String hashedPassword = BCrypt.hashpw(packet.password, BCrypt.gensalt(saltValue));

            pstmt.setString(1, login);
            pstmt.setString(2, hashedPassword);
            // "strength;accuracy;intelligence;willpower;constitution;skill1;skill2;skill3;skill4;level;experience"
            pstmt.setString(3, "10;10;10;10;10;0;0;0;0;1;0");

            pstmt.executeUpdate();
            System.out.println("[DB HANDLER] Pomyślnie dodano użytkownika do bazy danych");
            return true;
        } catch (SQLException e) {
            System.err.println("[DB HANDLER] Wystąpił błąd SQL podczas rejestracji");
            e.printStackTrace();
            return false;
        }

    }

    public LoginInfoResponse loginUser(LoginInfo packet) {
        System.out.println("[DB HANDLER] Otrzymano pakiet z login info");

        String sql = "SELECT * FROM users WHERE login = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, packet.login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    if (hashedPassword != null && BCrypt.checkpw(packet.password, hashedPassword)) {
                        String message = rs.getString("player_data");
                        LoginInfoResponse response = new LoginInfoResponse(true, message);
                        System.out.println("[DB HANDLER] Zalogowano pomyślnie");
                        return response;
                    } else {
                        String message = "Nieprawidłowe hasło";
                        LoginInfoResponse response = new LoginInfoResponse(false, message);
                        System.out.println("[DB HANDLER] Nieprawidłowe hasło");
                        return response;
                    }
                } else {
                    String message = "Użytkownik nie istnieje";
                    LoginInfoResponse response = new LoginInfoResponse(false, message);
                    System.out.println("[DB HANDLER] Użytkownik nie istnieje");
                    return response;
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB HANDLER] Wystąpił błąd SQL podczas logowania");
            e.printStackTrace();
            String message = "Wystąpił błąd SQL podczas logowania";
            LoginInfoResponse response = new LoginInfoResponse(false, message);
            return response;
        }
    }
}
