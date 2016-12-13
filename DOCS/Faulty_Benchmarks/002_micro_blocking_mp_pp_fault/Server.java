 /**
 * Concurrent Benchmarks
 * 
 * Title:  blocking_mp_pp - with fault
 * 
 * Description:  This benchmarks has the objective of verify if 
 *               testing tools for concurrent programs are able 
 *               to consider block sends and block receives.  
 *
 * Paradigm:     Message Passing
 * 
 * Fault:       "Setting different message length in send/receive" 
 *               Lines with faults: 54, 60, 72 - Server Process
 *               
 * Year:         2015
 * Company:      ICMC/USP - São Carlos
 *               University of São Paulo (USP)
 *               Institute of Mathematics and Computer Science (ICMC)
 *               
 * @authors      Raphael Negrisoli Batista, 
 *               George Gabriel Mendes Dourado
 * @version      1.0
 */
 
/* TEST 1
 * java Server 
 * java Client 1 
 * java Client 2
 * 
 * EXPECTED OUTPUT:
 * Hello from p1 message 1
 * Hello from p1 message 2
 * Hello from p2 message 1
 * Hello from p2 message 2
 *
 * WRONG OUTPUT:
 * Hello from
 * Hello
 * Hello from p2 message 1
 * Hello from p2 
 */

import java.net.*;

public class Server {
    public static final int port = 55555;
    public static final int messageAmount = 2;

    public static void main(String[] args) throws Exception {
        DatagramSocket server = new DatagramSocket(port);

        HelperClass.makeFile();

        byte[] msg = new byte[10]; // Fault - setting different message length in send/receive
        DatagramPacket packet = new DatagramPacket(msg, msg.length);
        server.receive(packet);

        System.out.println(new String(packet.getData()));

        msg = new byte[5]; // Fault - setting different message length in send/receive
        packet = new DatagramPacket(msg, msg.length);
        server.receive(packet);

        System.out.println(new String(packet.getData()));

        msg = new byte[128];
        packet = new DatagramPacket(msg, msg.length);
        server.receive(packet);

        System.out.println(new String(packet.getData()));

        msg = new byte[14]; // Fault - setting different message length in send/receive
        packet = new DatagramPacket(msg, msg.length);
        server.receive(packet);

        System.out.println(new String(packet.getData()));
        
        HelperClass.deleteFile();
    }
}