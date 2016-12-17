package faultlocalization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.InitializationError;

import faultlocalization.coverage.CoverageInformationHolder;
import faultlocalization.coverage.Instrumentalizator;
import faultlocalization.coverage.SpectrumBasedFormula;
import faultlocalization.coverage.SpectrumBasedFormula.Formulas;
import faultlocalization.junit.runner.FaultLocalizationTrigger;
import faultlocalization.junit.runner.JunitTestRunner;
import faultlocalization.junit.runner.TestRunnerException;
import faultlocalization.loader.Reloader;

/**
 * Fault localization application.
 * 
 * This application takes a java file and a set of junit tests and
 * will run several spectrum-based fault localization techniques such as
 * <p>
 * <li>TARANTULA</li>
 * <li>OCHIAI</li>
 * <li>OP2</li>
 * <li>BARINEL</li>
 * <li>DSTAR</li>
 * <p>
 * @author stein
 * @version 0.1
 */
public class Main {
	
	public static final String version = "0.1";

	public static void main(String[] args) {
		
		args = new String[]{"-p", "/home/stein/Projects/FaultLocalization/FaultLocalization/faulty_code/",
							"-c", "introclass.median.introclass_3cf6d33a_007",
							"-t", "/home/stein/Projects/FaultLocalization/FaultLocalization/tests/",
							"-j", "median_tests.Median_Tests",
							"-o", "/home/stein/Desktop/FL/",
							"-f", "tarantula", "OCHIAI", "OP2", "BARINEL", "DSTAR"};
		
		Options options = new Options();
		Option path = new Option("p", "path", true, "qualified path to faulty code e.g.: src/ or /Users/ppargento/Documents/workspace/pepe/src/");
		path.setRequired(true);
		path.setType(String.class);
		path.setArgs(1);
		
		Option lib = new Option("l", "lib", true, "classpaths needed");
		lib.setRequired(false);
		lib.setType(String.class);
		lib.setArgs(Option.UNLIMITED_VALUES);
		
		Option className = new Option("c", "class-name", true, "qualified class name of the class to evaluate e.g.: main.util.Pair");
		className.setRequired(true);
		className.setType(String.class);
		className.setArgs(1);
		
		Option testsFolder = new Option("t", "tests-folder", true, "qualified path to the junit tests to use e.g.: test/ or /Users/ppargento/Documents/workspace/pepe/test/");
		testsFolder.setRequired(true);
		testsFolder.setType(String.class);
		testsFolder.setArgs(1);
		
		Option tests = new Option("j", "junit-tests", true, "jUnit tests to use");
		tests.setRequired(true);
		tests.setType(String.class);
		tests.setArgs(Option.UNLIMITED_VALUES);
		
		Option techniques = new Option("f", "techniques", true, "spectrum-based fault localization techniques to be used");
		techniques.setRequired(false);
		techniques.setType(String.class);
		techniques.setArgs(Option.UNLIMITED_VALUES);
		
		Option output = new Option("o", "output", true, "The folder in which to save the instrumented faulty class file");
		output.setRequired(true);
		output.setType(String.class);
		output.setArgs(1);
		
//		Option stopAtFirstProblem = new Option("p", "panic", true, "Defines if the application should stop at the first problem or it will try to continue as long as it can");
//		stopAtFirstProblem.setRequired(false);
//		stopAtFirstProblem.setArgs(1);
//		stopAtFirstProblem.setType(Boolean.class);
		
		Option help = new Option("h", "help", false, "print commands");
		help.setRequired(false);
		
		Options miscOptions = new Options();
		miscOptions.addOption(help);
		
		options.addOption(path);
		options.addOption(lib);
		options.addOption(className);
		options.addOption(testsFolder);
		options.addOption(tests);
		options.addOption(techniques);
		options.addOption(output);
//		options.addOption(stopAtFirstProblem);
		options.addOption(help);
		
		CommandLineParser parser = new DefaultParser();
		
//		try {
//			CommandLine cmd = parser.parse(miscOptions, args);
//			if (cmd.hasOption("h")) {
//				HelpFormatter formatter = new HelpFormatter();
//				formatter.printHelp("FaultLocalization", options );
//				return;
//			}
//		} catch (ParseException e) {
//		}
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			
			//boolean quitAtFirstIssue = Boolean.parseBoolean(cmd.getOptionValue(stopAtFirstProblem.getOpt(), "true"));
			
			File fcodeFolder = Paths.get(cmd.getOptionValue(path.getOpt())).toFile();
			
			if (!fcodeFolder.exists() || !fcodeFolder.isDirectory()) {
				System.err.println("Faulty code folder doesn't exist or is not a folder : " + fcodeFolder.toString());
				return;
			}
			
			String faultyClassName = cmd.getOptionValue(className.getOpt());
			
			String fclassNameAsPath = faultyClassName.replaceAll("\\.", File.separator);
			
			File fclassFile = fcodeFolder.toPath().resolve(fclassNameAsPath + ".java").toFile();
			
			if (!fclassFile.exists() || !fclassFile.isFile()) {
				System.err.println("Faulty class doesn't exist or ir not a file : " + fclassFile.toString());
				return;
			}
			
			File jtestsFolder = Paths.get(cmd.getOptionValue(testsFolder.getOpt())).toFile();
			
			if (!jtestsFolder.exists() || !jtestsFolder.isDirectory()) {
				System.err.println("jUnit tests folder doesn't exist or is not a folder : " + jtestsFolder.toString());
				return;
			}
			
			String[] jutests = cmd.getOptionValues(tests.getOpt());
			List<File> junitTests = new LinkedList<>();
			
			for (String jutest : jutests) {
				String jtestAsPath = jutest.replaceAll("\\.", File.separator);
				File jtestFile = jtestsFolder.toPath().resolve(jtestAsPath + ".class").toFile();
				if (!jtestFile.exists() || !jtestFile.isFile()) {
					System.err.println("jUnit test doesn't exist or ir not a file : " + jtestFile.toString());
					return;
				}
				junitTests.add(jtestFile);
			}
			
			List<Formulas> sbFormulas = new LinkedList<>();
					
			if (cmd.hasOption(techniques.getOpt())) {
			
				String[] sbTechniques = cmd.getOptionValues(techniques.getOpt());
			
				for (String sbt : sbTechniques) {
					boolean found = false;
					for (Formulas sbf : SpectrumBasedFormula.Formulas.values()) {
						if (sbt.compareToIgnoreCase(sbf.getName()) == 0) {
							found = true;
							sbFormulas.add(sbf);
							break;
						}
					}
					if (!found) {
						System.err.println("Technique " + sbt + " is not valid");
						return;
					}
				}
				
			}
			
			if (sbFormulas.isEmpty()) {
				for (Formulas sbf : SpectrumBasedFormula.Formulas.values()) {
					sbFormulas.add(sbf);
				}
			}
			
			String outputValue = cmd.getOptionValue(output.getOpt());
			File outputFolder = Paths.get(cmd.getOptionValue(output.getOpt())).toFile();
			
			if (outputFolder.isFile()) {
				System.err.println("Output must be a folder : " + outputValue);
				return;
			}
			
			if (!outputFolder.exists()) outputFolder.mkdirs();
			
			
			Set<String> librariesPaths = new TreeSet<>();
			if (cmd.hasOption(lib.getOpt())) {
				String[] libs = cmd.getOptionValues(lib.getOpt());
				for (String l : libs) {
					if (Paths.get(l).toFile().exists()) {
						librariesPaths.add(l);
					} else {
						System.err.println("Library path doesn't exist : " + l);
						return;
					}
				}
			}
			
			List<String> classpath = new LinkedList<>();
			classpath.add("bin/");
			classpath.add(fcodeFolder.getPath().toString() + File.separator);
			classpath.add(jtestsFolder.getPath().toString() + File.separator);
			classpath.addAll(librariesPaths);
			
			System.out.println("Fault Localization - version " + version);
			System.out.println("Running Fault Localization with the following arguments");
			System.out.println("--------------------------------------------------------");
			System.out.println("Faulty code source folder  : " + fcodeFolder.getPath().toString());
			System.out.println("Faulty code classname      : " + faultyClassName);
			System.out.println("jUnit test binary folder   : " + jtestsFolder.getPath().toString());
			System.out.println("jUnit tests : ");
			for (File jt : junitTests) System.out.println("        " + jt.getPath().toString());
			System.out.println("Spectrum-Based formulas : ");
			for (Formulas f : sbFormulas) System.out.println("        " + f.getName());
			System.out.println("Output folder              : " + outputFolder.getPath().toString());
			System.out.println("Classpath : ");
			for (String c : classpath) System.out.println("        " + c);
			
			
			//TODO: run fault localization here!
			//Steps for fault localization
			//-----------------------------------
			//1 : Instrument faulty class file
			//2 : Compile instrumented faulty class
			//3 : Run tests
			//4 : Calculate rankings
			//5 : Output results
			
			Reloader reloader = new Reloader(classpath, Thread.currentThread().getContextClassLoader());
			reloader.setSpecificClassPath(faultyClassName, outputFolder.getPath().toString() + File.separator);
			reloader.markClassAsReloadable(faultyClassName);
			reloader.markEveryClassInFolderAsReloadable(jtestsFolder.getPath().toString() + File.separator);
			
			//Instrument faulty class file
			
			File outputFile = outputFolder.toPath().resolve(fclassNameAsPath+".java").toFile();
			if (outputFile.exists()) outputFile.delete();
			outputFile.getParentFile().mkdirs();
			outputFile.createNewFile();
			
			Instrumentalizator instrumentalizator = new Instrumentalizator(fclassFile);
			instrumentalizator.instrument(outputFile);
			
			//Compile instrumented faulty class
			
			File fileToCompile = outputFile;
			if (!fileToCompile.exists() || !fileToCompile.isFile() || !fileToCompile.getName().endsWith(".java")) {
				System.err.println("Invalid file to compile : " + fileToCompile.getPath());
				return;
			}
			File[] files = new File[]{fileToCompile};
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
			boolean compileResult = compiler.getTask(null, fileManager, null, Arrays.asList(new String[] {"-classpath", pathsAsString(classpath)}), null, compilationUnit).call();
			
			if (!compileResult) {
				System.err.println("Failed to compile " + fileToCompile.getPath().toString());
				return;
			}
			
			//Run tests
			
			CoverageInformationHolder.getInstance().instantiateCoverageInformation(fclassFile.getPath().toString());
			FaultLocalizationTrigger trigger = new FaultLocalizationTrigger(CoverageInformationHolder.getInstance().getCoverageInformation(fclassFile.getPath().toString()));
			for (String test : jutests) {
				Class<?> testToRun;
				try {
					reloader = reloader.getLastChild();
					testToRun = reloader.rloadClass(test, true);
					
					JunitTestRunner testRunner = new JunitTestRunner(testToRun, trigger);
					Result result = testRunner.run();
					killStillRunningJUnitTestcaseThreads();
					
//					for (Failure f : result.getFailures()) {
//						System.out.println(f.toString());
//						Throwable t = f.getException();
//						if (t != null) t.printStackTrace();
//					}
					
					
				} catch (TestRunnerException | InitializationError | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			//Calculate rankings and output results
			
			for (Formulas f : sbFormulas) {
				SpectrumBasedFormula sbf = new SpectrumBasedFormula(f);
				System.out.println(sbf.toString());
				System.out.println("Statements ranking :");
				for (Entry<Integer, Float> rank : sbf.rankStatements(CoverageInformationHolder.getInstance().getCoverageInformation(fclassFile.getPath().toString())).entrySet()) {
					System.out.println("s : " + rank.getKey() + " r : " + rank.getValue());
				}
				System.out.println("================================");
			}
			
			
		} catch (ParseException e) {
			System.err.println("Incorrect options.  Reason: " + e.getMessage() );
			return;
		} catch (com.github.javaparser.ParseException e) {
			System.err.println("Error while instrumenting (parsing/modifying AST)");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error while instrumenting (when writing instrumented file)");
			e.printStackTrace();
		}
		
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
	public static void killStillRunningJUnitTestcaseThreads() {
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

}
