package TCPServer;

import TCPServer.Packets.RegisterInfo;
import TCPServer.Packets.RegisterResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientToServerHandler {
    private static final String HOST = Global.HOST;
    private static final int PORT = Global.PORT;

    public RegisterResponse sendRegisterInfo(RegisterInfo info) throws IOException {
        try {
            Socket socket = new Socket(HOST, PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // wyślij pakiet
            oos.writeObject(info);
            oos.flush();

            // Odbierz odpowiedź od serwera
            Object response = ois.readObject();
            if (response instanceof RegisterResponse) {
                return (RegisterResponse) response;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("Problem z wyslaniem pakietu");
            e.printStackTrace();
            return null;
        }
    }
}
