import java.nio.file.*;
import java.nio.ByteBuffer;
import java.io.IOException;

public class HelperClass {
    public static final Path FILE_PATH = Paths.get(".SERVER");

    /*
     * This method synchronize the begin of the clients. It can be used to asure
     * that clients send messages only after the server socket is opened.
     * 
     * Use: <Code> DatagramSocket socket = new DatagramSocket(9999);
     * ValiparInitializer.notifyClients();
     */
    public static void notifyClients() throws IOException {
        Files.createFile(FILE_PATH);
    }

    /*
     * This method force the wait of the client for the server socket.
     */
    public static void waitServer() throws IOException {
        while (!Files.exists(FILE_PATH));
    }

    public static void clean() throws IOException {
        Files.deleteIfExists(FILE_PATH);
    }
    
    public static ByteBuffer getBuffer(byte[] sendMessage) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(128);
        buffer.put(sendMessage);
        buffer.flip();
        return buffer;
    }
}