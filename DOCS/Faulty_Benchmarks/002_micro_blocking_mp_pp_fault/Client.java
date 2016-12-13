import java.net.*;

public class Client {
    public static void main(String[] args) throws Exception {
        int processId = Integer.parseInt(args[0]);

        DatagramSocket socket = new DatagramSocket();

        InetSocketAddress srvAddr = new InetSocketAddress(InetAddress.getLocalHost(), Server.port);

        String msgDefault = "Hello from p" + processId + " message ";

        HelperClass.waitServer();

        byte[] msg = (msgDefault + "1").getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, srvAddr);
        socket.send(packet);

        msg = (msgDefault + "2").getBytes();
        packet = new DatagramPacket(msg, msg.length, srvAddr);
        socket.send(packet);
    }
}