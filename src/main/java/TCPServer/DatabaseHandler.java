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
                password TEXT)
""";

        try(Statement stmt = conn.createStatement()){
            stmt.executeUpdate(sql);
            System.out.println("[DB HANDLER] Utworzono bazę danych");
        } catch (SQLException e){
            System.err.println("[DB HANDLER] Nie udało się wykonać polecenia");
            e.printStackTrace();
        }
    }

    public boolean registerUser(RegisterInfo packet) throws SQLException {
        System.out.println("[DB HANDLER] Otrzymano pakiet z register info");

        String sql = "INSERT INTO users (login, password) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            String login = packet.login;
            String hashedPassword = BCrypt.hashpw(packet.password, BCrypt.gensalt(saltValue));

            pstmt.setString(1, login);
            pstmt.setString(2, hashedPassword);

            pstmt.executeUpdate();
            System.out.println("[DB HANDLER] Pomyślnie dodano użytkownika do bazy danych");
            return true;
        } catch (SQLException e) {
            System.err.println("[DB HANDLER] Wystąpił błąd SQL");
            e.printStackTrace();
            return false;
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
                String[] row = {user_id, login, password};
                data.add(row);
            }

            System.out.println(AsciiTable.getTable(data, Arrays.asList(
                    new Column().header("user_id").with(u -> u[0]),
                    new Column().header("login").with(u -> u[1]),
                    new Column().header("password").with(u -> u[2])
            )));

            System.out.println("[DB HANDLER] Pomyślnie wyświetlono wszystkie rekordy");

        } catch (SQLException e){
            System.err.println("[DB HANDLER] Wystąpił błąd SQL");
            e.printStackTrace();
        }

    }

    public void dropUsers() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate(sql);
            System.out.println("[DB HANDLER] Pomyślnie usunięto tabelę users");
        } catch (SQLException e){
            System.err.println("[DB HANDLER] Wystąpił błąd SQL");
            e.printStackTrace();
        }
    }
}
