package faultlocalization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.runners.model.InitializationError;

import com.github.javaparser.ParseException;

import faultlocalization.coverage.CoverageInformation;
import faultlocalization.coverage.CoverageInformationHolder;
import faultlocalization.coverage.SpectrumBasedFormula;
import faultlocalization.coverage.SpectrumBasedFormula.Formulas;
import faultlocalization.coverage.Instrumentalizator;
import faultlocalization.junit.runner.FaultLocalizationTrigger;
import faultlocalization.junit.runner.JunitTestRunner;
import faultlocalization.junit.runner.TestRunnerException;
import faultlocalization.loader.Reloader;

public class Api {
	
	private static CoverageInformation ci;

	public static Map<String, Map<Integer, Float>> rankStatements (
															String className,
															File fcodeFolder,
															File jtestsFolder,
															File outputFolder,
															String[] junitTests,
															List<Formulas> formulas,
															Collection<String> libraries) throws ParseException, IOException {
		List<String> classpath = new LinkedList<>();
		classpath.add("bin/");
		classpath.add(fcodeFolder.getPath().toString() + File.separator);
		classpath.add(jtestsFolder.getPath().toString() + File.separator);
		classpath.addAll(libraries);
		
		Reloader reloader = new Reloader(classpath, Thread.currentThread().getContextClassLoader());
		reloader.setSpecificClassPath(className, outputFolder.getPath().toString() + File.separator);
		reloader.markClassAsReloadable(className);
		reloader.markEveryClassInFolderAsReloadable(jtestsFolder.getPath().toString() + File.separator);
		
		//Instrument faulty class file
		
		String fclassNameAsPath = className.replaceAll("\\.", File.separator);
		File fclassFile = fcodeFolder.toPath().resolve(fclassNameAsPath + ".java").toFile();
		
		File outputFile = outputFolder.toPath().resolve(fclassNameAsPath+".java").toFile();
		if (outputFile.exists()) outputFile.delete();
		outputFile.getParentFile().mkdirs();
		outputFile.createNewFile();
		
		Instrumentalizator instrumentalizator = new Instrumentalizator(fclassFile);
		instrumentalizator.instrument(outputFile);
		
		//Compile instrumented faulty class
		
		File fileToCompile = outputFile;
		if (!fileToCompile.exists() || !fileToCompile.isFile() || !fileToCompile.getName().endsWith(".java")) {
			throw new IllegalArgumentException("Invalid file to compile : " + fileToCompile.getPath());
		}
		File[] files = new File[]{fileToCompile};
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
		boolean compileResult = compiler.getTask(null, fileManager, null, Arrays.asList(new String[] {"-classpath", pathsAsString(classpath)}), null, compilationUnit).call();
		
		if (!compileResult) {
			System.err.println("Failed to compile " + fileToCompile.getPath().toString());
			throw new IllegalStateException("Failed to compile " + fileToCompile.getPath().toString());
		}
		
		//Run tests
		
		CoverageInformationHolder.getInstance().instantiateCoverageInformation(fclassFile.getPath().toString());
		FaultLocalizationTrigger trigger = new FaultLocalizationTrigger(CoverageInformationHolder.getInstance().getCoverageInformation(fclassFile.getPath().toString()));
		for (String test : junitTests) {
			Class<?> testToRun;
			try {
				reloader = reloader.getLastChild();
				testToRun = reloader.rloadClass(test, true);
				
				JunitTestRunner testRunner = new JunitTestRunner(testToRun, trigger);
				testRunner.run();
				killStillRunningJUnitTestcaseThreads();
				
				
			} catch (TestRunnerException | InitializationError | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		Map<String, Map<Integer, Float>> results = new TreeMap<>();
		
		
		ci = CoverageInformationHolder.getInstance().getCoverageInformation(fclassFile.getPath().toString());
		
		for (Formulas f : formulas) {
			SpectrumBasedFormula sbf = new SpectrumBasedFormula(f);
			results.put(f.getName(), sbf.rankStatements(ci));
		}
		File rankingOutputFile = outputFolder.toPath().resolve(fclassNameAsPath+"-ranking.txt").toFile();
		if (rankingOutputFile.exists()) rankingOutputFile.delete();
		rankingOutputFile.getParentFile().mkdirs();
		rankingOutputFile.createNewFile();
		writeRankingToFile(results, rankingOutputFile);
		
		return results;
	}
	
	public static File generateMutGenLimitVersion(	String className,
													File fcodeFolder,
													File outputFolder,
													int line) throws IOException, ParseException {
		return generateMutGenLimitVersion(className, fcodeFolder, outputFolder, line, 5);
	}
	
	private static void writeRankingToFile(Map<String, Map<Integer,Float>> ranking, File outputFolder) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("RANKING START \n____________________________ \n");
		for (String formula : ranking.keySet()){
			sb.append("FORMULA: "+formula +"\n");
			sb.append("========================\n");
			Map<Integer, Float> rank = ranking.get(formula);
			for (Entry<Integer, Float>  r : rank.entrySet()){
				sb.append("LINE: "+r.getKey()+" --- RANK: "+ r.getValue()+"  EXECUTED: "+ getCoverageInformation().getExecutedTimes(r.getKey())+" TIMES\n");
			}
			sb.append("========================\n");
		}
		Files.write(outputFolder.toPath(), sb.toString().getBytes());
	}
	
	public static File generateMutGenLimitVersion( String className,
												   File fcodeFolder,
												   File outputFolder,
												   int line,
												   int mgl) throws IOException, ParseException {
		String classNameAsPath = className.replaceAll("\\.", File.separator);
		File classFile = fcodeFolder.toPath().resolve(classNameAsPath + ".java").toFile();
		File outputMGLFolder = outputFolder.toPath().resolve(className+"-"+line).toFile();
		File outputFile = outputMGLFolder.toPath().resolve(classNameAsPath+".java").toFile();
		if (outputFile.exists()) outputFile.delete();
		outputFile.getParentFile().mkdirs();
		outputFile.createNewFile();
		
		Map<Integer, Integer> mglPerLine = new TreeMap<>();
		mglPerLine.put(line, mgl);
		Instrumentalizator instrumentalizator = new Instrumentalizator(classFile, mglPerLine);
		instrumentalizator.instrument(outputFile);
		return outputFile;
	}
	
	public static File generateMutGenLimitMultiVersion( String className,
												   File fcodeFolder,
												   File outputFolder,
												   Map<Integer, Integer> mutGenLimitsPerLine) throws IOException, ParseException {
		String classNameAsPath = className.replaceAll("\\.", File.separator);
		String fileName = className;
		for (Entry<Integer, Integer> l : mutGenLimitsPerLine.entrySet()) {
			fileName += "_" + l.getKey() + "-" + l.getValue();
		}
		File classFile = fcodeFolder.toPath().resolve(classNameAsPath + ".java").toFile();	
		File outputMGLFolder = outputFolder.toPath().resolve(fileName).toFile();
		File outputFile = outputMGLFolder.toPath().resolve(classNameAsPath+".java").toFile();
		if (outputFile.exists()) outputFile.delete();
		outputFile.getParentFile().mkdirs();
		outputFile.createNewFile();
		
		Instrumentalizator instrumentalizator = new Instrumentalizator(classFile, mutGenLimitsPerLine);
		instrumentalizator.instrument(outputFile);
		return outputFile;
	}
	
	private static String pathsAsString(List<String> paths) {
		String res = "";
		Iterator<String> pit = paths.iterator();
		while (pit.hasNext()) {
			res += pit.next();
			if (pit.hasNext()) {
				res += File.pathSeparator;
			}
		}
		return res;
	}
	
	@SuppressWarnings("deprecation")
	private static void killStillRunningJUnitTestcaseThreads() {
	    Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
	    for (Thread thread : threadSet) {
	        if (!(thread.isDaemon())) {
	            final StackTraceElement[] threadStackTrace = thread.getStackTrace();
	            if (threadStackTrace.length > 1) {
	                StackTraceElement firstMethodInvocation = threadStackTrace[threadStackTrace.length - 1];
	                if (firstMethodInvocation.getClassName().startsWith("org.junit")) {
	                    // HACK: must use deprecated method
	                    thread.stop();
	                }
	            }
	        }
	    }
	}

	public static CoverageInformation getCoverageInformation() {
		return ci;
	}
	
}
