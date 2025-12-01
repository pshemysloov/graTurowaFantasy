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
        AtomicBoolean awaitingResponse = new AtomicBoolean(false);
        AtomicBoolean myTurn = new AtomicBoolean(false);
        final Object sendLock = new Object();

        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Nickname: ");
            String nickname = console.readLine().trim();
            System.out.print("Kod sesji: ");
            String code = console.readLine().trim();

            Socket socket = new Socket(HOST, PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
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
                                boolean amPlayer0 = s.endsWith("0");
                                myTurn.set(amPlayer0);
                                System.out.println("[INFO] Rozpoczęto. " + (amPlayer0 ? "Jesteś graczem 0 (zaczyna)." : "Jesteś graczem 1."));
                            }
                        } else if (obj instanceof packet_TurnInfo) {
                            packet_TurnInfo pkt = (packet_TurnInfo) obj;
                            System.out.println("[ODEBRANO] " + pkt);

                            // po otrzymaniu pakietu przeciwnika — teraz jest nasza tura
                            synchronized (sendLock) {
                                myTurn.set(true);
                                if (awaitingResponse.get()) {
                                    awaitingResponse.set(false);
                                }
                                sendLock.notifyAll();
                            }
                        } else {
                            System.out.println("[ODEBRANO] Nieznany obiekt: " + obj);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("[INFO] Połączenie zamknięte: " + e.getMessage());
                } finally {
                    // odblokuj ewentualnie czekający wątek przed zakończeniem
                    synchronized (sendLock) {
                        awaitingResponse.set(false);
                        myTurn.set(false);
                        sendLock.notifyAll();
                    }
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

            // po każdym wciśnięciu Enter wysyłaj pakiet, ale tylko jeśli jest nasza tura
            int a = 0, b = 0, c = 0;
            System.out.println("Naciśnij Enter aby wysłać pakiet (wpisz `quit` i Enter aby zakończyć).");
            while (true) {
                String line = console.readLine();
                if (line == null) break;
                if ("quit".equalsIgnoreCase(line.trim())) break;

                // jeśli nie jest nasza tura, ignoruj szybkie klikanie Enter
                synchronized (sendLock) {
                    if (!myTurn.get()) {
                        System.out.println("[INFO] Nie twoja tura");
                        continue;
                    }
                    // dodatkowo zabezpieczamy że mamy tylko jeden pakiet w locie
                    while (awaitingResponse.get()) {
                        try {
                            sendLock.wait();
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    // zaznaczamy, że wysyłamy i przekazujemy turę przeciwnikowi
                    awaitingResponse.set(true);
                    myTurn.set(false);
                }

                packet_TurnInfo pkt = new packet_TurnInfo(nickname, a++, b++, c++);
                try {
                    oos.writeObject(pkt);
                    oos.flush();
                    System.out.println("[WYSŁANO] " + pkt);
                } catch (Exception e) {
                    System.out.println("[BŁĄD] Nie można wysłać pakietu: " + e.getMessage());
                    // upewnij się, że nie zostaniemy w stanie czekać bez końca
                    synchronized (sendLock) {
                        awaitingResponse.set(false);
                        myTurn.set(true);
                        sendLock.notifyAll();
                    }
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
