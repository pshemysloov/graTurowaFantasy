package TCPServer.Packets;

import Core.Author;

import java.io.Serializable;

@Author(name = "Przemysław Błaszczyk")
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
