package TCPServer;

import Core.Author;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Author(name = "Przemysław Błaszczyk")
public class ClientConnection {
    public Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    public String nickname;

    public ClientConnection(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
    }
}
