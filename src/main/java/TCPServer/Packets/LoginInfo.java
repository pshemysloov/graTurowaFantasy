package TCPServer.Packets;

import Core.Author;

import java.io.Serializable;

@Author(name = "Przemysław Błaszczyk")
public class LoginInfo implements Serializable {
    public String login;
    public String password;

    public LoginInfo(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
