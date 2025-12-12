package TCPServer.Packets;

import java.io.Serializable;

public class RegisterInfo implements Serializable {
    public String login;
    public String password;

    public RegisterInfo(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
