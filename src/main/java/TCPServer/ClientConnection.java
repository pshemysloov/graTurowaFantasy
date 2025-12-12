package TCPServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    public ClientConnection(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
    }
}
