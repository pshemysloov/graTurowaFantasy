package TCPServer;
import TCPServer.Packets.LoginInfo;
import TCPServer.Packets.RegisterInfo;
import TCPServer.Packets.RegisterResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = Global.PORT;
    private DatabaseHandler dbHandler;
    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        new Server().start();
    }

    public void start() throws Exception {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("[SERWER] TCPServer.Server nasłuchuje na porcie " + PORT);

            // tworzenie database handler
            dbHandler = new DatabaseHandler();

            // tworzenie wątku terminala
            ServerTerminal terminal = new ServerTerminal(dbHandler);
            Thread terminalThread = new Thread(terminal);
            terminalThread.start();

            while (true) {
                Socket socket = server.accept();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                    Object obj = ois.readObject();
                    if ((obj instanceof String)) {
                        String code = (String) obj;
                        Session.PlayerConnection conn = new Session.PlayerConnection(socket, oos, ois);

                        sessions.compute(code, (k, session) -> {
                            if (session == null) {
                                session = new Session(code);
                                session.addPlayer(conn);
                                return session;
                            } else {
                                session.addPlayer(conn);
                                return null;
                            }
                        });
                    } else if (obj instanceof LoginInfo) {

                    } else if (obj instanceof RegisterInfo) {
                        // informacje o połączeniu z klientem
                        ClientConnection client = new ClientConnection(socket, oos, ois);

                        // tworzenie nowego wątku do obsługi rejestracji
                        Thread registerThread = new Thread(() -> {
                            try {
                                handleRegisterInfo((RegisterInfo) obj, client);
                            } catch (IOException | SQLException e) {
                                e.printStackTrace();
                                //throw new RuntimeException(e);
                            }
                        });
                        registerThread.start();

                    }

                } catch (Exception e) {
                    try { socket.close(); } catch (Exception ignored) {}
                }
            }
        }
    }

    private void handleRegisterInfo(RegisterInfo info, ClientConnection client) throws IOException, SQLException {
        boolean success = dbHandler.registerUser(info);
        RegisterResponse response;
        if(success){
            response = new RegisterResponse(true,"Zarejestrowano użytkownika");
        } else {
            response = new RegisterResponse(true,"Nie udało się zarejestrować użytkownika");
        }
        client.oos.writeObject(response);
        client.oos.flush();
    }

}
