package instrumentator.coverage;

import java.util.HashMap;
import java.util.Map;

public class CoverageTracker {

	//Maps filenames and line numbers to true (Executed) or false (Not executed)
	private static Map<String, Map<Integer, Boolean>> coverage = new HashMap<String, Map<Integer,Boolean>>();

	/**
	 * Creates a file and saves the results of the coverage in it.
	 */
	public static void writeCoverageToFile() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Mark a line as executed.
	 * @param filename
	 * @param line
	 */
	public static void markExecuted(String filename, int line) {
		if (!coverage.containsKey(filename)) {
			coverage.put(filename, new HashMap<Integer,Boolean>());
		}
		coverage.get(filename).put(line, true);
	}
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				writeCoverageToFile();
			}
		});
	}
}
