package TCPServer;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 12345;
    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        new Server().start();
    }

    public void start() throws Exception {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("TCPServer.Server listening on port " + PORT);
            while (true) {
                Socket socket = server.accept();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                    Object obj = ois.readObject();
                    if (!(obj instanceof String)) {
                        socket.close();
                        continue;
                    }
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
                } catch (Exception e) {
                    try { socket.close(); } catch (Exception ignored) {}
                }
            }
        }
    }
}
