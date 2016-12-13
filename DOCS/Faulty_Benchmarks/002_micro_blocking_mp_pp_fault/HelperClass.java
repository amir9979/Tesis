import java.io.*;
import java.nio.file.*;

public class HelperClass {
    public static void waitServer() {
        while (!Files.exists(Paths.get("server")));
    }

    public static void makeFile() throws IOException {
        Path fp = Paths.get("server");
        Files.createFile(fp);
    }

    public static void deleteFile() throws IOException {
        new File("server").delete();
    }
}