package TCPServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        AtomicBoolean started = new AtomicBoolean(false);
        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Nickname: ");
            String nickname = console.readLine().trim();
            System.out.print("Kod 4-cyfrowy: ");
            String code = console.readLine().trim();

            Socket socket = new Socket(HOST, PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); // ważne: najpierw OOS
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // wysyłamy kod sesji jako pierwszy obiekt
            oos.writeObject(code);
            oos.flush();

            // wątek nasłuchujący przychodzące obiekty
            Thread listener = new Thread(() -> {
                try {
                    while (true) {
                        Object obj = ois.readObject();
                        if (obj instanceof String) {
                            String s = (String) obj;
                            System.out.println("[SERWER] " + s);
                            if (s.startsWith("START:")) {
                                started.set(true);
                                System.out.println("[INFO] Rozpoczęto. " + (s.endsWith("0") ? "Jesteś graczem 0 (zaczyna)." : "Jesteś graczem 1."));
                            }
                        } else if (obj instanceof packet_TurnInfo) {
                            packet_TurnInfo pkt = (packet_TurnInfo) obj;
                            System.out.println("[ODBIERZ] " + pkt);
                        } else {
                            System.out.println("[ODBIERZ] Nieznany obiekt: " + obj);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("[INFO] Połączenie zamknięte: " + e.getMessage());
                } finally {
                    try { ois.close(); } catch (Exception ignored) {}
                    try { oos.close(); } catch (Exception ignored) {}
                    try { socket.close(); } catch (Exception ignored) {}
                }
            }, "TCPServer.Client-Listener");
            listener.setDaemon(true);
            listener.start();

            // poczekaj na START
            while (!started.get()) {
                Thread.sleep(100);
            }

            // po każdym wciśnięciu Enter wysyłaj pakiet
            int a = 0, b = 0, c = 0;
            System.out.println("Naciśnij Enter aby wysłać pakiet (wpisz `quit` i Enter aby zakończyć).");
            while (true) {
                String line = console.readLine();
                if (line == null) break;
                if ("quit".equalsIgnoreCase(line.trim())) break;

                packet_TurnInfo pkt = new packet_TurnInfo(nickname, a++, b++, c++);
                try {
                    oos.writeObject(pkt);
                    oos.flush();
                    System.out.println("[WYSŁANO] " + pkt);
                } catch (Exception e) {
                    System.out.println("[BŁĄD] Nie można wysłać pakietu: " + e.getMessage());
                    break;
                }
            }

            // zamykanie i zakończenie
            try { oos.close(); } catch (Exception ignored) {}
            try { ois.close(); } catch (Exception ignored) {}
            try { socket.close(); } catch (Exception ignored) {}
            System.out.println("Zakończono klienta.");
        } catch (Exception e) {
            System.out.println("Błąd klienta: " + e.getMessage());
        }
    }
}
