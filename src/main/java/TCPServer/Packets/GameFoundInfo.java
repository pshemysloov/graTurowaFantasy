package TCPServer.Packets;

import Core.Author;

import java.io.Serializable;

@Author(name = "Przemysław Błaszczyk")
public class GameFoundInfo implements Serializable {
    public String opponentName;

    public GameFoundInfo(String opponentName) {
        this.opponentName = opponentName;
    }
}
