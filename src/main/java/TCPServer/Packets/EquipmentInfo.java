package TCPServer.Packets;

import java.io.Serializable;

public class EquipmentInfo implements Serializable {
    public String nickname;
    public String playerData;

    public EquipmentInfo(String playerData, String nickname) {
        this.nickname = nickname;
        this.playerData = playerData;
    }


}
