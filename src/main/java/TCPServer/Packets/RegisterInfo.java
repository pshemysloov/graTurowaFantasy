package TCPServer.Packets;

import Core.Author;

import java.io.Serializable;

@Author(name = "Przemysław Błaszczyk")
public class RegisterInfo implements Serializable {
    public String login;
    public String password;

    public RegisterInfo(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
