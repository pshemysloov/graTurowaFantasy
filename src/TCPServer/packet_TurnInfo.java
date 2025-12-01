package TCPServer;

import java.io.Serializable;

class packet_TurnInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String nickname;
    private final int a;
    private final int b;
    private final int c;

    public packet_TurnInfo(String nickname, int a, int b, int c) {
        this.nickname = nickname;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public String getNickname() { return nickname; }
    public int getA() { return a; }
    public int getB() { return b; }
    public int getC() { return c; }

    @Override
    public String toString() {
        return "TCPServer.packet_TurnInfo{" +
                "nickname='" + nickname + '\'' +
                ", a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}