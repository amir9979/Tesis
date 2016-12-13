/**
 * Concurrent Benchmarks
 * 
 * Title:  Parallel GCD Two Slaves - with fault    
 * 
 * Description:  Parallel GCD is a program that calculates the
 *               Greatest Common Divisor (GCD) among three numbers
 *               using two slaves processes.          
 *
 * Paradigm:     Message Passing
 * 
 * Fault:       "Observability fault"
 *               Line with fault: 126 - GcdMaster Process
 *               
 * Year:         2014
 * Company:      ICMC/USP - São Carlos
 *               University of São Paulo (USP)
 *               Institute of Mathematics and Computer Science (ICMC)
 *               
 * @author       George Gabriel Mendes Dourado
 * @version      1.0
 */

/* TEST 1 - no reveal the fault
 * java GcdSlave 1 
 * java GcdSlave 2 
 * java GcdMaster 1 3 6
 * 
 * OUTPUT:
 * Result = 1
 */

/* TEST 2 - can or no reveal the fault in second iteration, depending 
 * of the order of arrival of the responses from Slaves (synchronization order)
 *
 * java GcdSlave 1 
 * java GcdSlave 2 
 * java GcdMaster 2 4 8
 * 
 * OUTPUT:
 * Result = 2
 */

import java.net.*;
import java.io.*;
import java.nio.file.*;

public class GcdMaster {
    public static void main(String[] args) throws IOException {
	final int ZERO = 0;
	final int processId = 0;
	final int amountOfProcess = 3;
	int iter = 2; // total number of iterations
	int result = -1; // stores the final GCD result
	int sent = 0;
	int firstValue = 0; // X
	int secondValue = 0; // Y
	int thirdValue = 0; // Z
	String values = "";

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

	Path fp = Paths.get("master");
	Files.createFile(fp);

	// get remote IP and PORT of the slave 1
	String hostIP = HelperClass.readRemoteIP(1);
	InetAddress remoteIPSlave1 = InetAddress.getByName(hostIP);
	int remotePortSlave1 = HelperClass.readRemotePort(1);

	// get remote IP and PORT of the slave 2
	hostIP = HelperClass.readRemoteIP(2);
	InetAddress remoteIPSlave2 = InetAddress.getByName(hostIP);
	int remotePortSlave2 = HelperClass.readRemotePort(2);

	byte[] receiveBuffer;

	while (result == -1) {
	    while (sent < iter) {
            if (sent == 0) {
	            values = HelperClass.makeMessage(firstValue, secondValue);
	            sendValueToSlave(socket, values, remoteIPSlave1, remotePortSlave1);
		    } else {
		    	values = HelperClass.makeMessage(secondValue, thirdValue);
		    	sendValueToSlave(socket, values, remoteIPSlave2, remotePortSlave2);
			}
			sent++;
	    }

	    while (sent > 0) {
            receiveBuffer = new byte[255];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);
            String data = new String(receivePacket.getData());

            if (sent == 2) {
                firstValue = (Integer.parseInt(data.trim()));
	        } else {
		    	secondValue = (Integer.parseInt(data.trim()));
            }
	        sent--;
        }

	    if (iter == 2 && (firstValue == 1 || secondValue == 1)) {
	        result = 1;
	    } else if (iter == 1) {
	        result = firstValue; // Fault - used (result = firstValue) instead of (result = secondValue)
	    }
	    iter--;
    }   
    // these messages will finish the slaves processes
    values = HelperClass.makeMessage(ZERO, ZERO);
    sendValueToSlave(socket, values, remoteIPSlave1, remotePortSlave1);
    sendValueToSlave(socket, values, remoteIPSlave2, remotePortSlave2);

    HelperClass.closeFiles(amountOfProcess);
    Files.deleteIfExists(fp);
    System.out.println("Result = " + result);
}// end of Main

    // send method
    private static void sendValueToSlave(DatagramSocket socket, String values, InetAddress remoteIP, int remotePort)
	        throws IOException {
	byte[] sendBuffer = new byte[255];
	sendBuffer = values.toString().getBytes();
	DatagramPacket datagram = new DatagramPacket(sendBuffer, sendBuffer.length, remoteIP, remotePort);
	socket.send(datagram);
    }
}
