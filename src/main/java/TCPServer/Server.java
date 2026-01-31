package TCPServer;
import Core.Author;
import TCPServer.Packets.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

@Author(name = "Przemysław Błaszczyk")
public class Server {
    private static final int PORT = Global.PORT;
    private DatabaseHandler dbHandler;
    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, ClientConnection> waitingClients = new ConcurrentHashMap<>();



    static void main() throws Exception {
        new Server().start();
    }

    public void start() throws Exception {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("[SERWER] TCPServer.Server nasłuchuje na porcie " + PORT);

            // tworzenie database handler
            dbHandler = new DatabaseHandler();
            // Resetowanie flag przy starcie serwera
            dbHandler.resetLoginFlags();

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

                    if (obj instanceof LoginInfo) {
                        // informacje o połączeniu z klientem
                        ClientConnection client = new ClientConnection(socket, oos, ois);

                        // tworzenie nowego wątku do obsługi logowania
                        Thread loginThread = new Thread(() -> {
                            try {
                                handleLoginInfo((LoginInfo) obj, client);
                            } catch (IOException e) {
                                e.printStackTrace();
                                //throw new RuntimeException(e);
                            }
                        });
                        loginThread.start();

                    } else if (obj instanceof RegisterInfo) {
                        // informacje o połączeniu z klientem
                        ClientConnection client = new ClientConnection(socket, oos, ois);

                        Thread registerThread = new Thread(() -> {
                            try {
                                handleRegisterInfo((RegisterInfo) obj, client);
                            } catch (IOException | SQLException e) {
                                e.printStackTrace();
                                //throw new RuntimeException(e);
                            }
                        });
                        registerThread.start();

                    } else if (obj instanceof LogoutInfo) {
                        Thread logoutThread = new Thread(() -> {
                            try {
                                handleLogoutInfo((LogoutInfo) obj);
                            } catch (IOException e) {
                                e.printStackTrace();
                                //throw new RuntimeException(e);
                            }

                        });
                        logoutThread.start();

                    } else if (obj instanceof EquipmentInfo) {
                        // informacje o połączeniu z klientem
                        ClientConnection client = new ClientConnection(socket, oos, ois);

                        Thread equipmentThread = new Thread(() -> {
                            try{
                                handleEquipmentInfo((EquipmentInfo) obj, client);
                            } catch (IOException e) {
                                e.printStackTrace();
                                //throw new RuntimeException(e);
                            }
                        });
                        equipmentThread.start();
                    } else if (obj instanceof StartGameInfo) {
                        StartGameInfo info = (StartGameInfo) obj;
                        System.out.println("[SERWER] Otrzymano StartGameInfo from " + info.nickname + " (kod: " + info.sessionCode + ")");
                        ClientConnection client = new ClientConnection(socket, oos, ois);
                        handleStartGame(client, info);
                    }

                } catch (Exception e) {
                    try { socket.close(); } catch (Exception ignored) {}
                }
            }
        }
    }

    private void handleLoginInfo(LoginInfo info, ClientConnection client) throws IOException {
        LoginInfoResponse response = dbHandler.loginUser(info);
        client.oos.writeObject(response);
        client.oos.flush();
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

    private void handleLogoutInfo(LogoutInfo info) throws IOException {
        dbHandler.logoutUser(info.nickname);
    }

    private void handleEquipmentInfo(EquipmentInfo info, ClientConnection client) throws IOException {
        EquipmentInfoResponse response = dbHandler.updatePlayerData(info);
        client.oos.writeObject(response);
        client.oos.flush();

    }

    private synchronized void handleStartGame(ClientConnection client, StartGameInfo info) {
        String code = info.sessionCode;
        client.nickname = info.nickname;

        if (waitingClients.containsKey(code)) {
            System.out.println("[SERWER] Znaleziono drugiego gracza dla kodu " + code + ". Weryfikacja połączenia hosta...");

            // Pobieramy przeciwnika i usuwamy z kolejki czekających
            ClientConnection opponent = waitingClients.remove(code);

            // Weryfikacja czy przeciwnik nadal żyje
            boolean opponentAlive = true;
            try {
                // Próbujemy wysłać informację do oczekującego gracza (hosta).
                // Jeśli socket jest zerwany, rzuci IOException.
                opponent.oos.writeObject(new GameFoundInfo(client.nickname));
                opponent.oos.flush();
            } catch (IOException e) {
                System.out.println("[SERWER] Oczekujący przeciwnik (Host) rozłączył się. Zastępowanie nowym graczem.");
                opponentAlive = false;
            }

            if (!opponentAlive) {
                // Przeciwnik nie żyje - obecny klient staje się nowym oczekującym hostem
                waitingClients.put(code, client);
                System.out.println("[SERWER] Gracz " + client.nickname + " oczekuje na przeciwnika (kod: " + code + ")...");
                return;
            }

            // Jeśli przeciwnik żyje, wysyłamy info do klienta dołączającego
            try {
                client.oos.writeObject(new GameFoundInfo(opponent.nickname));
                client.oos.flush();
            } catch (IOException e) {
                System.out.println("[SERWER] Błąd połączenia z klientem dołączającym (" + client.nickname + ").");
                return;
            }

            // Tworzenie sesji
            Session session = new Session(opponent, client, code);

            // Dodajemy sesję do mapy
            sessions.put(code, session);

            Thread sessionThread = new Thread(session);
            sessionThread.start();

        } else {
            // Nikt nie czeka - ten klient staje się hostem
            System.out.println("[SERWER] Gracz " + client.nickname + " wchodzi do lobby i oczekuje (kod: " + code + ")...");
            waitingClients.put(code, client);
        }
    }

}
