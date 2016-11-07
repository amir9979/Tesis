package instrumentator.coverage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import instrumentator.utils.Pair;
/*
 * 
 * This class contains the maps that track both the coverage and final stats of the tool
 */
public class CoverageTracker {

	//Maps line numbers to true (Executed) or false (Not executed)
	private static Map<Integer, Boolean> coverage = new HashMap<Integer,Boolean>();

	
	//Maps lines numbers to amount of executions on passing and failing tests
	private static Map<Integer, Pair<Integer, Integer>> stats = new HashMap<Integer, Pair<Integer, Integer>>();
	
	/**
	 * Attempt to use Lcov records as used in: http://ismail.badawi.io/blog/2013/05/03/writing-a-code-coverage-tool/
	 */
	private static void writeCoverageToFile(){
		String report = generateReport();
		String reportPath = System.getProperty("report.path", "instrumented/report.txt");
		FileWriter writer = null;
		try {
			writer = new FileWriter(reportPath);
			writer.write(report);
		} catch (IOException io){
			throw new RuntimeException(io);
		} finally {
			try{
				writer.close();
			} catch (IOException e){
				throw new RuntimeException(e);
			}
		}
		System.out.println(reportPath);
	}
	
	private static String generateReport(){
		StringBuilder sb = new StringBuilder();
		sb.append(generateLcov());
		return sb.toString();
	}
	
	private static String generateLcov(){
		StringBuilder sb = new StringBuilder();
		for (Integer line : stats.keySet()){
			sb.append("LINE: "+line.toString()+" - "+stats.get(line).toString()+"\n");
		}
		return sb.toString();
	}

	/**
	 * Creates a file and saves the results of the coverage in it.
	 * Serializes the coverage in some format.
	 */
//	public static void writeCoverageToFile() {
////		List<String> lines = new LinkedList<String>();
////		for (String filename : coverage.keySet()){
////			lines.add("FILE: "+filename+"\n");
////			for (Map.Entry<Integer, Boolean> line: coverage.get(filename).entrySet()){
////				lines.add("BLOCK AT LINE: "+line.getKey()+", EXECUTED: "+line.getValue());
////			}
////		}
////		Path file = Paths.get(System.getProperty("user.dir")+"/instrumented/report.txt");
////		try {
////			coverage = new HashMap<String, Map<Integer, Boolean>>();
////			Files.write(file, lines, Charset.forName("UTF-8"));
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////	}

	/**
	 * Mark a block as executed on a passing test o failing one
	 */
	
	
	/**
	 * Mark a block as executed.
	 * @param line the number of the executed block first line.
	 */
	public static void markExecuted(int line) {
		//Mark the block as executed.
		coverage.put(line, true);
	}
	
	
	/**
	 * Add count according to the value passed to the method. This method should be called on teardown in the test suite
	 */
	public static void markTestResult(boolean result){
		//Search all the executed lines
		for (Integer line : coverage.keySet()){
			//If it doesn't exist on the stats map, add it.
			if (!stats.containsKey(line)){
				stats.put(line, new Pair<Integer, Integer>(0,0));
			}
			//Add the count afterwards, on the first one if it's true, on the second if it's false.
			Pair<Integer,Integer> lineCount = stats.get(line);
			if (result){
				lineCount.setLeft(lineCount.getLeft()+1);
			} else {
				lineCount.setRight(lineCount.getRight()+1);
			}
			stats.put(line, lineCount);
		}
	}
	
	/**
	 * Call this on every setUp for the test so a new coverage map will generate.
	 */
	public static void clearCoverageMap(){
		coverage.clear();
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
				System.out.println("Coverage to be written: "+coverage.toString());
				writeCoverageToFile();
			}
		});
	}
}
