import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ClientUdp {
    public static void main(String[] args) throws Exception {
        int processId = Integer.parseInt(args[0]);

        DatagramChannel channel = DatagramChannel.open();

        InetSocketAddress addr = new InetSocketAddress("localhost", 9999);
        channel.configureBlocking(false);

        HelperClass.waitServer();

        String newData = "Hello from Client " + processId;

        ByteBuffer buf = HelperClass.getBuffer(newData.getBytes());

        int sendResult = 0;
        do {
            sendResult = channel.send(buf, addr);
        } while (sendResult == 0);

        int reads = 0;
        SocketAddress srcAddr;

        ByteBuffer receiveBuffer = ByteBuffer.allocate(128);
        while (reads < 1) {
            srcAddr = channel.receive(receiveBuffer);
            receiveBuffer.flip();
            byte[] message = new byte[receiveBuffer.remaining()];
            receiveBuffer.get(message, 0, message.length);
            receiveBuffer.clear();
			System.out.println("Client - " + new String(message));
            reads++;
        }
    }
}