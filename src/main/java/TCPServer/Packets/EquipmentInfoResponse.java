package TCPServer.Packets;

import java.io.Serializable;

public class EquipmentInfoResponse implements Serializable {
    public boolean success;
    public String message;

    public EquipmentInfoResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
