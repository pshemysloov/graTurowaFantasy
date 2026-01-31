package TCPServer.Packets;

import java.io.Serializable;

public class GameFoundInfo implements Serializable {
    public String opponentName;

    public GameFoundInfo(String opponentName) {
        this.opponentName = opponentName;
    }
}
