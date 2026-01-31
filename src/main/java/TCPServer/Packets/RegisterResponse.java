package TCPServer.Packets;

import Core.Author;

import java.io.Serializable;

@Author(name = "Przemysław Błaszczyk")
public class RegisterResponse implements Serializable {
    public boolean success;
    public String message;

    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
