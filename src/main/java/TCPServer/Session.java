package TCPServer;

import Core.Author;
import TCPServer.Packets.TurnInfo;

import java.io.IOException;

@Author(name = "Przemysław Błaszczyk")
public class Session implements Runnable {
    private final ClientConnection player1;
    private final ClientConnection player2;
    private final String code;

    public Session(ClientConnection player1, ClientConnection player2, String code) {
        this.player1 = player1;
        this.player2 = player2;
        this.code = code;
    }

    @Override
    public void run() {
        // Thread for Player 1 -> Player 2
        Thread t1 = new Thread(() -> relayPackets(player1, player2));

        // Thread for Player 2 -> Player 1
        Thread t2 = new Thread(() -> relayPackets(player2, player1));

        t1.start();
        t2.start();
    }

    private void relayPackets(ClientConnection sender, ClientConnection receiver) {
        try {
            while (true) {
                Object obj = sender.ois.readObject();
                if (obj instanceof TurnInfo) {
                    System.out.println("[SESSION " + code + "] Przesyłanie TurnInfo...");
                    receiver.oos.writeObject(obj);
                    receiver.oos.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[SESSION " + code + "] Koniec sesji - połączenie zerwane.");
            try { sender.socket.close(); } catch (IOException ignored) {}
            try { receiver.socket.close(); } catch (IOException ignored) {}
        }
    }
}