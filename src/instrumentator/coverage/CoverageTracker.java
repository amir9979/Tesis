package instrumentator.coverage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import instrumentator.utils.Pair;
/*
 * 
 * This class contains the maps that track both the coverage and final stats of the tool
 */
public class CoverageTracker {

	//Maps line numbers to true (Executed) or false (Not executed)
	private static Map<Integer, Boolean> coverage = new HashMap<Integer,Boolean>();

	private enum Formulas {
		TARANTULA,
		OCHIAI,
		OP2,
		BARINEL,
		DSTAR,
		ALL
	}
	
	private static int formula;
	private static int exp = 2;
	
	//Maps lines numbers to amount of executions on passing and failing tests
	private static Map<Integer, Pair<Integer, Integer>> stats = new HashMap<Integer, Pair<Integer, Integer>>();
	
	//Total amount of failed tests
	private static int failed = 0;
	
	//Total amount of passed tests
	private static int passed = 0;
	
	/**
	 * Deprecated:
	 * Attempt to use Lcov records as used in: http://ismail.badawi.io/blog/2013/05/03/writing-a-code-coverage-tool/
	 */
	
	/**
	 * Reads the Path where the report should be written, generates the report and saves it.
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
	}
	
	private static void writeRankingToFile(){
		String report = generateRankingReport(formula);
		String reportPath = System.getProperty("report.path", "instrumented/ranking.txt");
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
	}
	
	private static String generateRankingReport(int formula){
		StringBuilder sb = new StringBuilder();
		sb.append("RANKING START\n");
		switch (formula){
			case 1:
				sb.append("USING FORMULA: "+Formulas.TARANTULA+"\n");
				break;
			case 2:
				sb.append("USING FORMULA: "+Formulas.OCHIAI+"\n");
				break;
			case 3:
				sb.append("USING FORMULA: "+Formulas.OP2+"\n");
				break;
			case 4:
				sb.append("USING FORMULA: "+Formulas.BARINEL+"\n");
				break;
			case 5:
				sb.append("USING FORMULA: "+Formulas.DSTAR+"\n");
				break;
			default: 
				sb.append("USING "+Formulas.ALL+" FORMULAS \n");
				break;
		}
		sb.append("|====================================| \n");
		sb.append(generateRanking(formula));
		sb.append("RANKING END; \n");
		return sb.toString();
	}
	
	private static String generateRanking(int formula){
		StringBuilder sb = new StringBuilder();
		for (Integer line : stats.keySet()){
			int lineFails = stats.get(line).getRight();
			int lineSuccess = stats.get(line).getLeft();
			sb.append("|-LINE: "+line.toString()+"\n|---RANK:");
			switch(formula){
				case 1:
					sb.append("\t"+tarantulaRank(lineFails, lineSuccess, failed, passed)+"\n");
					break;
				case 2:
					sb.append("\t"+ochiaiRank(lineFails, lineSuccess, failed)+"\n");
					break;
				case 3:
					sb.append("\t"+op2Rank(lineFails, lineSuccess, passed)+"\n");
					break;
				case 4:
					sb.append("\t"+barinelRank(lineFails, lineSuccess)+"\n");
					break;
				case 5:
					sb.append("\t"+dStarRank(lineFails, lineSuccess, failed, exp)+"\n");
					break;
				default:
					sb.append("\n"+combinedRanks(lineFails, lineSuccess, failed, passed, exp));
					break;
			}
			sb.append("|====================================| \n");
		}
		return sb.toString();
	}
	/**
	 * Functions that creates a string with the result of each formula.
	 */
	
	/**
	 * Method that generates a String with the value of Tarantula ranking
	 * @param lineFails participation of a line in failing tests
	 * @param lineSuccess participation of a line in successful tests
	 * @param totalFailed amount of failing tests
	 * @param totalPassed amount of successful tests
	 * @return String with the value of Tarantula ranking
	 */
	private static String tarantulaRank(int lineFails, int lineSuccess, int totalFailed, int totalPassed){
		StringBuilder sb = new StringBuilder();
		float result = (lineFails/(float)totalFailed)/((lineFails/(float)totalFailed)+(lineSuccess/(float)totalPassed));
		return sb.append(result).toString();
	}
	
	/**
	 * Method that generates a String with the value of Ochiai ranking
	 * @param lineFails participation of a line in failing tests
	 * @param lineSuccess participation of a line in successful tests	
	 * @param totalFailed amount of failing tests
	 * @return String with the value of Ochiai ranking
	 */
	private static String ochiaiRank(int lineFails, int lineSuccess, int totalFailed){
		StringBuilder sb = new StringBuilder();
		float result = lineFails / (float) Math.sqrt(totalFailed * (lineFails + lineSuccess));
		return sb.append(result).toString();
	}
	
	/**
	 * Method that generates a String with the value of OP2 ranking
	 * @param lineFails participation of a line in failing tests
	 * @param lineSuccess participation of a line in successful tests
	 * @param totalPassed amount of successful tests
	 * @return String with the value of Op2 ranking
	 */
	private static String op2Rank(int lineFails, int lineSuccess, int totalPassed){
		StringBuilder sb = new StringBuilder();
		float result = lineFails - (lineSuccess / (float)(totalPassed+1));
		return sb.append(result).toString();
	}
	
	/**
	 * Method that generates a String with the value of Barinel ranking
	 * @param lineFails participation of a line in failing tests
	 * @param lineSuccess participation of a line in successful tests
	 * @return String with the value of Barinel ranking
	 */
	private static String barinelRank(int lineFails, int lineSuccess){
		StringBuilder sb = new StringBuilder();
		float result = 1 - (lineSuccess/(float)(lineSuccess+lineFails));
		return sb.append(result).toString();
	}
	
	private static String dStarRank(int lineFails, int lineSuccess, int totalFailed, int exp){
		StringBuilder sb = new StringBuilder();
		float result = (float) (Math.pow(lineFails, exp) / (float)(lineSuccess+(totalFailed - lineFails))); 
		return sb.append(result).toString();
	}
	
	private static String combinedRanks(int lineFails, int lineSuccess, int totalFailed, int totalPassed, int exp){
		StringBuilder sb = new StringBuilder();
		sb.append("|-----TARANTULA: "+tarantulaRank(lineFails, lineSuccess, totalFailed, totalPassed)+"\n");
		sb.append("|-----OCHIAI: "+ochiaiRank(lineFails, lineSuccess, totalFailed)+"\n");
		sb.append("|-----OP2: "+op2Rank(lineFails, lineSuccess, totalPassed)+"\n");
		sb.append("|-----BARINEL: "+barinelRank(lineFails, lineSuccess)+"\n");
		sb.append("|-----DSTAR: "+dStarRank(lineFails, lineSuccess, totalFailed, exp)+"\n");
		return sb.toString();
	}
	/*
	 * Generates a simple report containing the stats of the lines, the total amount of failed tests
	 * and the total amount of passed tests
	 */
	private static String generateReport(){
		StringBuilder sb = new StringBuilder();
		sb.append(generateTestAmount());
		sb.append(generateStats());
		return sb.toString();
	}
	
	private static String generateStats(){
		StringBuilder sb = new StringBuilder();
		for (Integer line : stats.keySet()){
			sb.append("LINE: "+line.toString()+" - "+stats.get(line).toString()+"\n");
		}
		return sb.toString();
	}
	
	private static String generateTestAmount(){
		return "PASSED TESTS: "+passed+"\n"+"FAILED TESTS: "+failed+"\n";
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
		//Increase the failed and passed counters
		if (result) passed++;
		else failed++;
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
//				System.out.println("Coverage: "+coverage.toString());
//				System.out.println("Stats: "+stats.toString());
				writeCoverageToFile();
				writeRankingToFile();
			}
		});
	}
	public static int getFormula() {
		return formula;
	}

	public static void setFormula(int formula) {
		CoverageTracker.formula = formula;
	}
}
