package TCPServer.Packets;

import java.io.Serializable;

public class LoginInfoResponse implements Serializable {
    public boolean success;
    public String message;
    public String nickname;

    public LoginInfoResponse(boolean success, String message, String nickname) {
        this.success = success;
        this.message = message;
        this.nickname = nickname;
    }

    public LoginInfoResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
