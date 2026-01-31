package TCPServer.Packets;

import Core.Author;

import java.io.Serializable;

@Author(name = "Przemysław Błaszczyk")
public class StartGameInfo implements Serializable {
    public String nickname;
    public String sessionCode;

    public StartGameInfo(String nickname, String sessionCode) {
        this.nickname = nickname;
        this.sessionCode = sessionCode;
    }
}