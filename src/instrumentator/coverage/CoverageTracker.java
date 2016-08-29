package instrumentator.coverage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CoverageTracker {

	//Maps filenames and line numbers to true (Executed) or false (Not executed)
	private static Map<String, Map<Integer, Boolean>> coverage = new HashMap<String, Map<Integer,Boolean>>();

	/**
	 * Creates a file and saves the results of the coverage in it.
	 * Serializes the coverage in some format.
	 */
	public static void writeCoverageToFile() {
		List<String> lines = new LinkedList<String>();
		for (String filename : coverage.keySet()){
			lines.add("FILE: "+filename+"\n");
			for (Map.Entry<Integer, Boolean> line: coverage.get(filename).entrySet()){
				lines.add("BLOCK AT LINE: "+line.getKey()+", EXECUTED: "+line.getValue());
			}
		}
		Path file = Paths.get(System.getProperty("user.dir")+"/report.txt");
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Mark a block as executed.
	 * @param filename the name of the file.
	 * @param line the number of the executed block first line.
	 */
	public static void markExecuted(String filename, int line) {
		//If it's not already in the Map, add it.;
		if (!coverage.containsKey(filename)) {
			coverage.put(filename, new HashMap<Integer,Boolean>());
		}
		//Then set it as executed.
		coverage.get(filename).put(line, true);
	}
	
	public static void markExecutable(String filename, int line) {
		if (!coverage.containsKey(filename)){
			coverage.put(filename, new HashMap<Integer,Boolean>());
		}
		coverage.get(filename).put(line, false);
	}
	/*
	 * We add a Shutdown Hook, that is, a thread that will launch on the JVM shutdown sequence.
	 * In this thread, we tell the JVM to run the coverage writing.
	 * Shutdown Hooks are unstarted threads that are registered with Runtime.addShutdownHook().
	 * Multiple shutdown hooks can create a deadlock or race condition between them, as they are threads that run in parallel.
	 * However, a thread can't be registered multiple times, as the JVM will throw IllegalArgumentException stating that this particular thread is already registered.
	 * The remaining exceptions that this could provoke are IllegalStateException when the JVM is already shutting down and a new hook tries to register or
	 * a Security Exception if the addition of hooks is not allowed by the Runtime Permissions.
	 * In an abrupt shutdown,shutdown hooks will not run.
	 * 
	 * Information about Shutdown Hook:
	 * http://techno-terminal.blogspot.com.ar/2015/08/shutdown-hooks.html
	 * https://docs.oracle.com/javase/7/docs/api/java/lang/Runtime.html
	 * http://hellotojavaworld.blogspot.com.ar/2010/11/runtimeaddshutdownhook.html
	 */
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				writeCoverageToFile();
			}
		});
	}
}
