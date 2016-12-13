/**
 * Concurrent Benchmarks
 * 
 * Title:  non_blocking_mp
 * 
 * Description:  This benchmarks has the objective of verify if testing tools 
 * 		         for concurrent programs are able to consider non-blocking send 
 * 		         and non-blocking receive, and also consider communications 
 * 		         events (non-blocking send and receive inside of repetition structure).  
 *
 * Paradigm:     Message Passing
 *
 * Fault:       "Exclusion of Commands"
 *               Line with fault: among 31 and 31 in the ClientUdp process
 *               
 * Year:         2014
 * Company:      ICMC/USP - São Carlos
 *               University of São Paulo (USP)
 *               Institute of Mathematics and Computer Science (ICMC)
 *               
 * @authors      Raphael Negrisoli Batista
 * 		         George Gabriel Mendes Dourado
 * @version      1.0
 */

/* TEST 1
 * java ServerUdp
 * java ClientUdp 1
 * java ClientUdp 2
 * 
 * OUTPUT:
 * Server - Hello from Client 1
 * Server - Hello from Client 2
 * Client - Hello from Server
 * Client - Hello from Server
 */

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ServerUdp {
    public static final int PORT = 9999;
    
    public static void main(String[] args) throws Exception {
        int messages = 2;

        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(9999));
        channel.configureBlocking(false);

        HelperClass.notifyClients();

        ByteBuffer readBuffer = ByteBuffer.allocate(128);

        int reads = 0;
        SocketAddress srcAddr = null;
        while (reads < messages) {
            do {
                srcAddr = channel.receive(readBuffer);
                if (srcAddr == null) {
                    Thread.sleep((long) 5);
                } else {
                    readBuffer.flip();  
                    byte[] message = new byte[readBuffer.remaining()];
                    readBuffer.get(message, 0, message.length);
                    readBuffer.clear(); 

                    System.out.println("Server - " + new String(message));
                    reads++;
                    int sendResult = 0;

                    byte[] sendMessage = "Hello from Server".getBytes();

                    ByteBuffer buffer = HelperClass.getBuffer(sendMessage);

                    do {
                        sendResult = channel.send(buffer, srcAddr);
                    } while (sendResult < 0);
                }
            } while (srcAddr == null);
        }
        HelperClass.clean();
    }
}