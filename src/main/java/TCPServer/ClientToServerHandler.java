package TCPServer;

import Core.Player;
import Core.SkillRegister;
import TCPServer.Packets.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientToServerHandler {
    private static final String HOST = Global.HOST;
    private static final int PORT = Global.PORT;

    public RegisterResponse sendRegisterInfo(RegisterInfo info) throws IOException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            // wyślij pakiet
            oos.writeObject(info);
            oos.flush();

            // Odbierz odpowiedź od serwera
            Object response = ois.readObject();
            if (response instanceof RegisterResponse) {
                return (RegisterResponse) response;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("Problem z wyslaniem pakietu");
            e.printStackTrace();
            return null;
        }
    }

    public LoginInfoResponse sendLoginInfo(LoginInfo info) throws IOException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {


            // wyślij pakiet
            oos.writeObject(info);
            oos.flush();

            // Odbierz odpowiedź od serwera
            Object response = ois.readObject();
            if (response instanceof LoginInfoResponse) {
                return (LoginInfoResponse) response;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("Problem z wysłaniem pakietu loginInfo");
            e.printStackTrace();
            return null;
        }
    }

    public void sendLogoutInfo(String nickname) throws IOException {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {


            oos.writeObject(new LogoutInfo(nickname));
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public EquipmentInfoResponse sendEquipmentInfo(Player player) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            // "strength;accuracy;intelligence;willpower;constitution;skill1;skill2;skill3;skill4;level;experience"
            StringBuilder sb = new StringBuilder();

            sb
            .append(player.attributes.strength).append(";")
            .append(player.attributes.accuracy).append(";")
            .append(player.attributes.intelligence).append(";")
            .append(player.attributes.willpower).append(";")
            .append(player.attributes.constitution).append(";")
            .append(SkillRegister.getIdBySkill(player.skills[0])).append(";")
            .append(SkillRegister.getIdBySkill(player.skills[1])).append(";")
            .append(SkillRegister.getIdBySkill(player.skills[2])).append(";")
            .append(SkillRegister.getIdBySkill(player.skills[3])).append(";")
            .append(player.level).append(";")
            .append(player.experience).append(";")
            .append(player.attributePoints);

            String playerData = sb.toString();

            oos.writeObject(new EquipmentInfo(playerData, player.name));
            oos.flush();

            Object response = ois.readObject();
            if (response instanceof EquipmentInfoResponse) {
                return (EquipmentInfoResponse) response;
            } else {
                return null;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
