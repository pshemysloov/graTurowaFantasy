package TCPServer.Packets;

import java.io.Serializable;

public class LoginInfoResponse implements Serializable {
    public boolean success;
    public String message;

    public LoginInfoResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
