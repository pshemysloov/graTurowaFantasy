package TCPServer.Packets;

import java.io.Serializable;

public class LogoutInfo implements Serializable {
    public String nickname;
    public LogoutInfo(String nickname) {
        this.nickname = nickname;
    }
}
