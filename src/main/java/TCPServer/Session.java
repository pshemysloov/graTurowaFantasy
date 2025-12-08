package TCPServer;

import TCPServer.Packets.TurnInfo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Session implements Runnable {
    private final String code;
    private final PlayerConnection[] players = new PlayerConnection[2];
    private final AtomicInteger count = new AtomicInteger(0);

    public Session(String code) {
        this.code = code;
    }

    public static class PlayerConnection {
        public final Socket socket;
        public final ObjectOutputStream oos;
        public final ObjectInputStream ois;

        public PlayerConnection(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
            this.socket = socket;
            this.oos = oos;
            this.ois = ois;
        }
    }

    public synchronized void addPlayer(PlayerConnection conn) {
        int idx = count.getAndIncrement();
        if (idx >= 2) {
            try { conn.socket.close(); } catch (Exception ignored) {}
            return;
        }
        players[idx] = conn;
        System.out.println("TCPServer.Session " + code + " - player " + idx + " connected");
        if (count.get() == 2) {
            new Thread(this, "TCPServer.Session-" + code).start();
        }
    }

    @Override
    public void run() {
        System.out.println("TCPServer.Session " + code + " started");
        try {
            // powiadomienie kto zaczyna (0 zaczyna)
            players[0].oos.writeObject("START:0");
            players[0].oos.flush();
            players[1].oos.writeObject("START:1");
            players[1].oos.flush();

            int current = 0;
            while (true) {
                Object obj = players[current].ois.readObject();
                if (obj instanceof TurnInfo) {
                    TurnInfo pkt = (TurnInfo) obj;
                    int other = 1 - current;
                    players[other].oos.writeObject(pkt);
                    players[other].oos.flush();
                    current = other;
                    System.out.println("[TCPServer.Session " + code + "] " + pkt);
                } else {
                    // ignoruj inne obiekty
                }
            }
        } catch (Exception e) {
            System.out.println("TCPServer.Session " + code + " ended: " + e.getMessage());
        } finally {
            closeSafe(players[0]);
            closeSafe(players[1]);
        }
    }

    private void closeSafe(PlayerConnection p) {
        if (p == null) return;
        try { if (p.ois != null) p.ois.close(); } catch (Exception ignored) {}
        try { if (p.oos != null) p.oos.close(); } catch (Exception ignored) {}
        try { if (p.socket != null) p.socket.close(); } catch (Exception ignored) {}
    }
}
