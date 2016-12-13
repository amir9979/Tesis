/**
 * Concurrent Benchmarks
 * 
 * Title:  Parallel GCD  - with faults    
 * 
 * Description:  Parallel GCD is a program that calculates the
 *               Greatest Common Divisor (GCD) among three numbers
 *               using three slaves processes.
 *
 * Paradigm:     Message Passing
 * 
 * Fault:       "Fault in the loop that precedes a send primitive"
 *               Line with fault: 67 - GcdSlave Process
 *               
 * Year:         2015
 * Company:      ICMC/USP - São Carlos
 *               University of São Paulo (USP)
 *               Institute of Mathematics and Computer Science (ICMC)
 *               
 * @author       George Gabriel Mendes Dourado
 * @version      1.0
 */
 
/* TEST 1
 * java GcdSlave 1
 * java GcdSlave 2
 * java GcdSlave 3
 * java GcdMaster 8 12 7
 * 
 * OUTPUT:
 * Result = 1
 */

/* TEST 2 - always reveal the fault - "Fault in the loop that precedes a send primitive"
 * java GcdSlave 1
 * java GcdSlave 2
 * java GcdSlave 3
 * java GcdMaster 9 18 6
 * 
 * EXPECTED OUTPUT:
 * Result = 3
 * 
 * But, because of the fault, the test 2 does not show output
 */

/* TEST 3 - can or no reveal the second fault (it depends of the synchronization order)
 * java GcdSlave 1
 * java GcdSlave 2
 * java GcdSlave 3
 * java GcdMaster 9 9 3
 * 
 * EXPECTED OUTPUT:
 * Result = 3
 */

import java.net.*;
import java.io.*;

public class GcdMaster {
    public static void main(String[] args) throws IOException {
    	final int ZERO = 0;
        final int processId = 0;
        int firstValue = 0;  // X
        int secondValue = 0; // Y
        int thirdValue = 0;  // Z
        int receivedValueOfProcess1 = 0; // GCD result received of the slave process
        int receivedValueOfProcess2 = 0; // GCD result received of the slave process
        int receivedValueOfProcess3 = 0; // GCD result received of the slave process
        String data;

        if (args != null && args.length == 3) {
            firstValue = Integer.parseInt(args[0]);
            secondValue = Integer.parseInt(args[1]);
            thirdValue = Integer.parseInt(args[2]);
        } else {
            System.out.println("Wrong number of arguments.");
        }
     
        // create socket
        DatagramSocket socket = new DatagramSocket();
        int port = socket.getLocalPort();
        InetAddress addressIP = InetAddress.getLocalHost();
        String ip = addressIP.getHostAddress();

        // make address file with IP:PORT
        String address = HelperClass.makeAddress(ip, port);
        HelperClass.makeAddressFile(processId, address);

        // wait slaves
        HelperClass.waitSlaves();

	    // get remote IP and PORT of the slave 1
        String hostIP = HelperClass.readRemoteIP(1);
        InetAddress remoteIPSlave1 = InetAddress.getByName(hostIP);
        int remotePortSlave1 = HelperClass.readRemotePort(1);

        // get remote IP and PORT of the slave 2
        hostIP = HelperClass.readRemoteIP(2);
        InetAddress remoteIPSlave2 = InetAddress.getByName(hostIP);
        int remotePortSlave2 = HelperClass.readRemotePort(2);

        // get remote IP and PORT of the slave 3
        hostIP = HelperClass.readRemoteIP(3);
        InetAddress remoteIPSlave3 = InetAddress.getByName(hostIP);
        int remotePortSlave3 = HelperClass.readRemotePort(3);

        // send x and y to the slave 1
        String values = HelperClass.makeMessage(firstValue, secondValue);
        sendValueToSlave(socket, values, remoteIPSlave1, remotePortSlave1);

        // send y and z to the slave 2
        values = HelperClass.makeMessage(secondValue, thirdValue);
        sendValueToSlave(socket, values, remoteIPSlave2, remotePortSlave2);

        byte[] receiveBuffer;
        
        // receiving the results of the GCD returned by the slaves 1 and 2
        for (int i = 1; i <= 2; i++) {
            receiveBuffer = new byte[255];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);
            data = new String(receivePacket.getData());
            if (i == 1) {
                receivedValueOfProcess1 = (Integer.parseInt(data.trim()));
            } else {
                receivedValueOfProcess2 = (Integer.parseInt(data.trim()));
            }
        }

        /* verification of the returned results. If slave 1 or slave 2 
           returned the result one, then finalize the slave process 3
           sending two data with value ZERO to him */
        if (receivedValueOfProcess1 == 1 || receivedValueOfProcess2 == 1) {
            values = HelperClass.makeMessage(ZERO, ZERO);
            sendValueToSlave(socket, values, remoteIPSlave3, remotePortSlave3);
            System.out.println("Result = 1");
            
        } else { // otherwise, send to slave 3 the results returned by the slaves 1 and 2 
            values = HelperClass.makeMessage(receivedValueOfProcess1, receivedValueOfProcess2);
            sendValueToSlave(socket, values, remoteIPSlave3, remotePortSlave3);
            
            receiveBuffer = new byte[255];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            // receiving the result of the GCD returned by the slave process 3
            socket.receive(receivePacket);
            data = new String(receivePacket.getData());
            receivedValueOfProcess3 = (Integer.parseInt(data.trim()));
            System.out.println("Result = " + receivedValueOfProcess3);
        }

	    // delete address file
        new File("parallel_gcd0.txt").delete();
    }

    // send method
    private static void sendValueToSlave(DatagramSocket socket, String values,
    		InetAddress remoteIP, int remotePort) throws IOException {
        byte[] sendBuffer = new byte[255];
        sendBuffer = values.toString().getBytes();
        DatagramPacket datagram = new DatagramPacket(sendBuffer, sendBuffer.length, remoteIP, remotePort);
        socket.send(datagram);
    }
}