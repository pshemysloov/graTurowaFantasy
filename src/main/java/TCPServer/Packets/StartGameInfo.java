package TCPServer.Packets;

import java.io.Serializable;

public class StartGameInfo implements Serializable {
    public String nickname;
    public String sessionCode;

    public StartGameInfo(String nickname, String sessionCode) {
        this.nickname = nickname;
        this.sessionCode = sessionCode;
    }
}