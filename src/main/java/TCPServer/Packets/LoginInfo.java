package TCPServer.Packets;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    String login;
    String password;

    public LoginInfo(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
