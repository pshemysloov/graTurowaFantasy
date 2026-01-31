package TCPServer.Packets;

import Core.Author;

import java.io.Serializable;

@Author(name = "Przemysław Błaszczyk")
public class EquipmentInfo implements Serializable {
    public String nickname;
    public String playerData;

    public EquipmentInfo(String playerData, String nickname) {
        this.nickname = nickname;
        this.playerData = playerData;
    }


}
