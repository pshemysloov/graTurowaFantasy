package TCPServer.Packets;

import Core.Author;

import java.io.Serializable;

@Author(name = "Przemysław Błaszczyk")
public class LogoutInfo implements Serializable {
    public String nickname;
    public LogoutInfo(String nickname) {
        this.nickname = nickname;
    }
}
