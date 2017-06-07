package faultlocalization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.github.javaparser.ParseException;

//import org.apache.commons.cli.CommandLine;
//import org.apache.commons.cli.CommandLineParser;
//import org.apache.commons.cli.DefaultParser;
//import org.apache.commons.cli.Option;
//import org.apache.commons.cli.Options;
//import org.apache.commons.cli.ParseException;

import faultlocalization.coverage.CoverageInformation;
import faultlocalization.coverage.SpectrumBasedFormula;
import faultlocalization.coverage.SpectrumBasedFormula.Formulas;

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
 * @version 0.5
 */
public class Main {
	
	public static final String version = "0.5";
	public static int rankedStatements = 3;
	public static int mutGenLimitMin = 1;
	public static int mutGenLimitMax = 5;
	public static boolean generateGradientVersion = false;
	public static int gradientStep = 2;

	public static void main(String[] args) {
		
//		args = new String[]{"-p", "/Users/gaston/Documents/EclipseWorkspace/FaultLocalization/faulty_code/",
//							/*1*/ //"-c", "introclass.median.introclass_0cdfa335_003",
//							/*2*/ //"-c", "introclass.median.introclass_0cea42f9_003",
//							/*3*/ //"-c", "introclass.median.introclass_15cb07a7_003",
//							/*4*/ //"-c", "introclass.median.introclass_1b31fa5c_000",
//							/*5*/ //"-c", "introclass.median.introclass_1bf73a9c_000",
//							/*5*/ //"-c", "introclass.median.introclass_1bf73a9c_000",
//							/*6*/ //"-c", "introclass.median.introclass_1bf73a9c_003",
//							/*7*/ //"-c", "introclass.median.introclass_1c2bb3a4_000",
//							/*8*/ //"-c", "introclass.median.introclass_2c155667_000",
//							/*9*/ //"-c", "introclass.median.introclass_30074a0e_000",
//							/*10*/ //"-c", "introclass.median.introclass_317aa705_000",
//							/*11*/ //"-c", "introclass.median.introclass_317aa705_002",
//							/*12*/ //"-c", "introclass.median.introclass_317aa705_003",
//							/*13*/ //"-c", "introclass.median.introclass_36d8008b_000",
//							/*14*/ //"-c", "introclass.median.introclass_3b2376ab_003",
//							/*15*/ //"-c", "introclass.median.introclass_3b2376ab_006",
//							/*16*/ //"-c", "introclass.median.introclass_3cf6d33a_007",
//							/*17*/ //"-c", "introclass.median.introclass_48b82975_000",
//							/*18*/ //"-c", "introclass.median.introclass_68eb0bb0_000",
//							/*19*/ //"-c", "introclass.median.introclass_6aaeaf2f_000",
//							/*20*/ //"-c", "introclass.median.introclass_6e464f2b_003",
//							/*21*/ //"-c", "introclass.median.introclass_89b1a701_003",
//							/*22*/ //"-c", "introclass.median.introclass_89b1a701_007",
//							/*23*/ //"-c", "introclass.median.introclass_89b1a701_010",
//							/*24*/ //"-c", "introclass.median.introclass_9013bd3b_000",
//							/*25*/ //"-c", "introclass.median.introclass_90834803_003",
//							/*26*/ //"-c", "introclass.median.introclass_90834803_010",
//							/*27*/ //"-c", "introclass.median.introclass_90834803_015",
//							/*28*/ //"-c", "introclass.median.introclass_90a14c1a_000",
//							/*29*/ //"-c", "introclass.median.introclass_93f87bf2_010",
//							/*30*/ //"-c", "introclass.median.introclass_93f87bf2_012",
//							/*31*/ //"-c", "introclass.median.introclass_93f87bf2_015",
//							/*32*/ //"-c", "introclass.median.introclass_95362737_000",
//							/*33*/ //"-c", "introclass.median.introclass_95362737_003",
//							/*34*/ //"-c", "introclass.median.introclass_9c9308d4_003",
//							/*35*/ //"-c", "introclass.median.introclass_9c9308d4_007",
//							/*36*/ //"-c", "introclass.median.introclass_9c9308d4_012",
//							/*37*/ //"-c", "introclass.median.introclass_aaceaf4a_003",
//							/*38*/ //"-c", "introclass.median.introclass_af81ffd4_004",
//							/*39*/ //"-c", "introclass.median.introclass_af81ffd4_007",
//							/*40*/ //"-c", "introclass.median.introclass_b6fd408d_000",
//							/*41*/ //"-c", "introclass.median.introclass_b6fd408d_001",
//							/*42*/ //"-c", "introclass.median.introclass_c716ee61_000",
//							/*43*/ //"-c", "introclass.median.introclass_c716ee61_001",
//							/*44*/ //"-c", "introclass.median.introclass_c716ee61_002",
//							/*45*/ //"-c", "introclass.median.introclass_cd2d9b5b_010",
//							/*46*/ //"-c", "introclass.median.introclass_d009aa71_000",
//							/*47*/ //"-c", "introclass.median.introclass_d120480a_000",
//							/*48*/ //"-c", "introclass.median.introclass_d2b889e1_000",
//							/*49*/ //"-c", "introclass.median.introclass_d43d3207_000",
//							/*50*/ //"-c", "introclass.median.introclass_d4aae191_000",
//							/*51*/ //"-c", "introclass.median.introclass_e9c6206d_000",
//							/*52*/ //"-c", "introclass.median.introclass_e9c6206d_001",
//							/*53*/ //"-c", "introclass.median.introclass_fcf701e8_000",
//							/*54*/ //"-c", "introclass.median.introclass_fcf701e8_002",
//							/*55*/ //"-c", "introclass.median.introclass_fcf701e8_003",
//							/*56*/ //"-c", "introclass.median.introclass_fe9d5fb9_000",
//							/*57*/ "-c", "introclass.median.introclass_fe9d5fb9_002",
//							
//							
//							"-t", "/Users/gaston/Documents/EclipseWorkspace/FaultLocalization/tests/",
//							/*1*/ //"-j", "median_tests.introclass_0cdfa335_003_Tests",
//							/*2*/ //"-j", "median_tests.introclass_0cea42f9_003_Tests",
//							/*3*/ //"-j", "median_tests.introclass_15cb07a7_003_Tests",
//							/*4*/ //"-j", "median_tests.introclass_1b31fa5c_000_Tests",
//							/*5*/ //"-j", "median_tests.introclass_1bf73a9c_000_Tests",
//							/*6*/ //"-j", "median_tests.introclass_1bf73a9c_003_Tests",
//							/*7*/ //"-j", "median_tests.introclass_1c2bb3a4_000_Tests",
//							/*8*/ //"-j", "median_tests.introclass_2c155667_000_Tests",
//							/*9*/ //"-j", "median_tests.introclass_30074a0e_000_Tests",
//							/*10*/ //"-j", "median_tests.introclass_317aa705_000_Tests",
//							/*11*/ //"-j", "median_tests.introclass_317aa705_002_Tests",
//							/*12*/ //"-j", "median_tests.introclass_317aa705_003_Tests",
//							/*13*/ //"-j", "median_tests.introclass_36d8008b_000_Tests",
//							/*14*/ //"-j", "median_tests.introclass_3b2376ab_003_Tests",
//							/*15*/ //"-j", "median_tests.introclass_3b2376ab_006_Tests",
//							/*16*/ //"-j", "median_tests.introclass_3cf6d33a_007_Tests",
//							/*17*/ //"-j", "median_tests.introclass_48b82975_000_Tests",
//							/*18*/ //"-j", "median_tests.introclass_68eb0bb0_000_Tests",
//							/*19*/ //"-j", "median_tests.introclass_6aaeaf2f_000_Tests",
//							/*20*/ //"-j", "median_tests.introclass_6e464f2b_003_Tests",
//							/*21*/ //"-j", "median_tests.introclass_89b1a701_003_Tests",
//							/*22*/ //"-j", "median_tests.introclass_89b1a701_007_Tests",
//							/*23*/ //"-j", "median_tests.introclass_89b1a701_010_Tests",
//							/*24*/ //"-j", "median_tests.introclass_9013bd3b_000_Tests",
//							/*25*/ //"-j", "median_tests.introclass_90834803_003_Tests",
//							/*26*/ //"-j", "median_tests.introclass_90834803_010_Tests",
//							/*27*/ //"-j", "median_tests.introclass_90834803_015_Tests",
//							/*28*/ //"-j", "median_tests.introclass_90a14c1a_000_Tests",
//							/*29*/ //"-j", "median_tests.introclass_93f87bf2_010_Tests",
//							/*30*/ //"-j", "median_tests.introclass_93f87bf2_012_Tests",
//							/*31*/ //"-j", "median_tests.introclass_93f87bf2_015_Tests",
//							/*32*/ //"-j", "median_tests.introclass_95362737_000_Tests",
//							/*33*/ //"-j", "median_tests.introclass_95362737_003_Tests",
//							/*34*/ //"-j", "median_tests.introclass_9c9308d4_003_Tests",
//							/*35*/ //"-j", "median_tests.introclass_9c9308d4_007_Tests",
//							/*36*/ //"-j", "median_tests.introclass_9c9308d4_012_Tests",
//							/*37*/ //"-j", "median_tests.introclass_aaceaf4a_003_Tests",
//							/*38*/ //"-j", "median_tests.introclass_af81ffd4_004_Tests",
//							/*39*/ //"-j", "median_tests.introclass_af81ffd4_007_Tests",
//							/*40*/ //"-j", "median_tests.introclass_b6fd408d_000_Tests",
//							/*41*/ //"-j", "median_tests.introclass_b6fd408d_001_Tests",
//							/*42*/ //"-j", "median_tests.introclass_c716ee61_000_Tests",
//							/*43*/ //"-j", "median_tests.introclass_c716ee61_001_Tests",
//							/*44*/ //"-j", "median_tests.introclass_c716ee61_002_Tests",
//							/*45*/ //"-j", "median_tests.introclass_cd2d9b5b_010_Tests",
//							/*46*/ //"-j", "median_tests.introclass_d009aa71_000_Tests",
//							/*47*/ //"-j", "median_tests.introclass_d120480a_000_Tests",
//							/*48*/ //"-j", "median_tests.introclass_d2b889e1_000_Tests",
//							/*49*/ //"-j", "median_tests.introclass_d43d3207_000_Tests",
//							/*50*/ //"-j", "median_tests.introclass_d4aae191_000_Tests",
//							/*51*/ //"-j", "median_tests.introclass_e9c6206d_000_Tests",
//							/*52*/ //"-j", "median_tests.introclass_e9c6206d_001_Tests",
//							/*53*/ //"-j", "median_tests.introclass_fcf701e8_000_Tests",
//							/*54*/ //"-j", "median_tests.introclass_fcf701e8_002_Tests",
//							/*55*/ //"-j", "median_tests.introclass_fcf701e8_003_Tests",
//							/*56*/ //"-j", "median_tests.introclass_fe9d5fb9_000_Tests",
//							/*57*/ "-j", "median_tests.introclass_fe9d5fb9_002_Tests",
//							
//							
//							
//							//14"-j", "median_tests.introclass_3b2376ab_003_Tests",
//							
//							
//							"-o", "/Users/gaston/Desktop/FL/",
//							"-f", "tarantula", "OCHIAI", "OP2", "BARINEL", "DSTAR",
//							"-n", "3",
//							"-m", "1", "5",
//							"-g", "2"};
//		
//		Options options = new Options();
//		Option path = new Option("p", "path", true, "qualified path to faulty code e.g.: src/ or /Users/ppargento/Documents/workspace/pepe/src/");
//		path.setRequired(true);
//		path.setType(String.class);
//		path.setArgs(1);
//		
//		Option lib = new Option("l", "lib", true, "classpaths needed");
//		lib.setRequired(false);
//		lib.setType(String.class);
//		lib.setArgs(Option.UNLIMITED_VALUES);
//		
//		Option className = new Option("c", "class-name", true, "qualified class name of the class to evaluate e.g.: main.util.Pair");
//		className.setRequired(true);
//		className.setType(String.class);
//		className.setArgs(1);
//		
//		Option testsFolder = new Option("t", "tests-folder", true, "qualified path to the junit tests to use e.g.: test/ or /Users/ppargento/Documents/workspace/pepe/test/");
//		testsFolder.setRequired(true);
//		testsFolder.setType(String.class);
//		testsFolder.setArgs(1);
//		
//		Option tests = new Option("j", "junit-tests", true, "jUnit tests to use");
//		tests.setRequired(true);
//		tests.setType(String.class);
//		tests.setArgs(Option.UNLIMITED_VALUES);
//		
//		Option techniques = new Option("f", "techniques", true, "spectrum-based fault localization techniques to be used");
//		techniques.setRequired(false);
//		techniques.setType(String.class);
//		techniques.setArgs(Option.UNLIMITED_VALUES);
//		
//		Option output = new Option("o", "output", true, "The folder in which to save the instrumented faulty class file");
//		output.setRequired(true);
//		output.setType(String.class);
//		output.setArgs(1);
//		
//		Option rankLimit = new Option("n", "rankLimit", true, "How many ranked statements will be taken into account when generating the mutGenLimit versions");
//		rankLimit.setRequired(false);
//		rankLimit.setType(Integer.class);
//		rankLimit.setArgs(1);
//		
//		Option mglLimit = new Option("m", "mutGenLimitRange", true, "Max value for mutGenLimit comments");
//		mglLimit.setRequired(false);
//		mglLimit.setType(Integer.class);
//		mglLimit.setArgs(2);
//		
//		Option gradientVersion = new Option("g", "gradientVersion", true, "Generate gradient mutGenLimit version");
//		gradientVersion.setRequired(false);
//		gradientVersion.setType(Integer.class);
//		gradientVersion.setArgs(1);
		
		
//		Option stopAtFirstProblem = new Option("p", "panic", true, "Defines if the application should stop at the first problem or it will try to continue as long as it can");
//		stopAtFirstProblem.setRequired(false);
//		stopAtFirstProblem.setArgs(1);
//		stopAtFirstProblem.setType(Boolean.class);
		
//		Option help = new Option("h", "help", false, "print commands");
//		help.setRequired(false);
//		
//		Options miscOptions = new Options();
//		miscOptions.addOption(help);
//		
//		options.addOption(path);
//		options.addOption(lib);
//		options.addOption(className);
//		options.addOption(testsFolder);
//		options.addOption(tests);
//		options.addOption(techniques);
//		options.addOption(output);
//		options.addOption(rankLimit);
//		options.addOption(mglLimit);
//		options.addOption(gradientVersion);
////		options.addOption(stopAtFirstProblem);
//		options.addOption(help);
//		
//		CommandLineParser parser = new DefaultParser();
//		
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
			Properties defaultProperties = new Properties();
			String defaultFile = "default.props";
			//Load default properties
			FileInputStream in = new FileInputStream(defaultFile);
			defaultProperties.load(in);
			in.close();
			
			//Create application properties with defaults.
			Properties applicationProperties = new Properties(defaultProperties);
			
			//If a custom properties file is passed, try to load the new ones.
			if (args.length == 0){
				System.out.println("No properties file passed, using defaults");
			} else {
				args[0] = "appProperties.props";
				System.out.println("Properties file passed: "+args[0]+" attempting to load custom properties.");
				String customPropertiesFile = args[0];
				in = new FileInputStream(customPropertiesFile);
				/*
				 * Load the custom properties over default ones. Any new, non existing value is added
				 * on the set is added to the Properties object, any existing value is overwritten
				 * if present in the new file.
				 */
				applicationProperties.load(in);
				in.close();
			}
				//Load faulty code folder
			File fCodeFolder = Paths.get(applicationProperties.getProperty("faultyClassLocation")).toFile();
			//If the folder doesn't exist throw error
			if (!fCodeFolder.exists() || !fCodeFolder.isDirectory()){
				System.err.println("Faulty code folder doesn't exist or is not a directory: "+fCodeFolder.toString());
				return;
			}
			//Load faulty class name
			String faultyClassName = applicationProperties.getProperty("faultyClass");
			//Create path from class name 
			String fClassNameAsPath = faultyClassName.replaceAll("\\.", File.separator);
			//Load faulty class
			File fClassFile = fCodeFolder.toPath().resolve(fClassNameAsPath + ".java").toFile();
			//If the class doesn't exist throw error
			if (!fClassFile.exists() || !fClassFile.isFile()){
				System.err.println("Faulty class doesn't exist or is not a file: "+fClassFile.toString());
			}
			//Load tests file folder, if it doesn't exist throw error
			File jTestsFolder = Paths.get(applicationProperties.getProperty("testsFolder")).toFile();
			if (!jTestsFolder.exists() || !jTestsFolder.isDirectory()){
				System.err.println("jUnit tests folder doesn't exist or is not a directory: "+ jTestsFolder.toString());
				return;
			}
			//Get the juTests from the properties file, each test should be separated with a comma
			String[] juTests = applicationProperties.getProperty("tests").split(",");
			List<File> jUnitTests = new LinkedList<>();
			//Check if all the jUnit tests exists and add them to the jUnitTests list
			for (String juTest : juTests){
				String jTestAsPath = juTest.replaceAll("\\.",File.separator);
				File jTestFile = jTestsFolder.toPath().resolve(jTestAsPath+".class").toFile();
				if (!jTestFile.exists() || !jTestFile.isFile()){
					System.err.println("jUnit test doesn't exists or is not a file: "+jTestFile.toString());
					return;
				}
				jUnitTests.add(jTestFile);
			}
			//Get the formulas to be used from the properties file, each formula should be separated with a comma.
			List<Formulas> sbFormulas = new LinkedList<>();
			String[] sbTechniques = applicationProperties.getProperty("techniques", "").split(", ");
			for (String sbt : sbTechniques){
				boolean found = false;
				for (Formulas sbf : SpectrumBasedFormula.Formulas.values()){
					if (sbt.compareToIgnoreCase(sbf.getName())== 0){
						found = true;
						sbFormulas.add(sbf);
						break;
					}
				}
				if (!found){
					System.err.println("Technique "+ sbt + " is not valid. Ignoring it.");
				}
			}
			if (sbFormulas.isEmpty()){
				for (Formulas sbf: SpectrumBasedFormula.Formulas.values()){
					sbFormulas.add(sbf);
				}
			}
			String outputValue = applicationProperties.getProperty("output");
			File outputFolder = Paths.get(outputValue).toFile();
			if (!outputFolder.isDirectory()){
				System.err.println("Output must be an existant folder : "+outputValue);
				return;
			}
			outputFolder.mkdirs();
			Set<String> librariesPaths = new TreeSet<>();
			String libsString = applicationProperties.getProperty("libs", "");
			String[] libs = new String[0];
			if (libsString.length() > 0){
				libs = libsString.split(", ");
			}
			for (String l : libs){
				if (Paths.get(l).toFile().exists()){
					librariesPaths.add(l);
				} else {
					System.err.println("Library path doesn't exist : "+l);
					return;
				}
			}
			List<String> classpath = new LinkedList<>();
			classpath.add("/bin");
			classpath.add(fCodeFolder.getPath().toString() + File.separator);
			classpath.add(jTestsFolder.getPath().toString() + File.separator);
			classpath.addAll(librariesPaths);
			
			Integer rl = Integer.parseInt(applicationProperties.getProperty("rankLimit"));
			if (rl>0){
				Main.rankedStatements = rl;
			} else {
				System.err.println("Max ranked statements to consider must be a positive value "+rl);
				return;
			}
			
//				Integer mgl = Integer.parseInt("mglLimit");
//				if (mgl > 0){
//					Main.mutGenLimitMax = mgl;
//				} else {
//					System.err.println("Max mutGenLimit value must be a positive value " + mgl);
//					return;
//				}
			
			String[] mglRangeValues = applicationProperties.getProperty("mglLimit", "").split(", ");
			Integer[] mglRange = new Integer[] {Integer.parseInt(mglRangeValues[0]), Integer.parseInt(mglRangeValues[1])};
			
			if (mglRange[0] > mglRange[1]){
				System.err.println("Min mutGenLimit is greater than Max mutGenLimit ("+ mglRange[0] + "..."+ mglRange[1]+"");
				return;
			}
			if (mglRange[0] > 0){
				Main.mutGenLimitMin = mglRange[0];
			} else {
				System.err.println("Min mutGenLimit value must be a positive value : "+mglRangeValues[0]);
				return;
			}
			if (mglRange[1] > 0 ){
				Main.mutGenLimitMax = mglRange[1];
			} else {
				System.err.println("Max mutGenLimit value must be a positive value: "+ mglRangeValues[1]);
				return;
			}
			Integer gstep = Integer.parseInt(applicationProperties.getProperty("gradientVersion"));
			if (gstep > 0) {
				Main.generateGradientVersion = true;
				Main.gradientStep = gstep;
			} else {
				System.err.println("Gradient version step must be a positive value "+gstep);
				return;
			}
			System.out.println("Fault Localization - version " + version);
			System.out.println("Running Fault Localization with the following arguments");
			System.out.println("--------------------------------------------------------");
			System.out.println("Faulty code source folder         : " + fCodeFolder.getPath().toString());
			System.out.println("Faulty code classname             : " + faultyClassName);
			System.out.println("jUnit test binary folder          : " + jTestsFolder.getPath().toString());
			System.out.println("jUnit tests                       : ");
			for (File jt : jUnitTests) System.out.println("        " + jt.getPath().toString());
			System.out.println("Spectrum-Based formulas : ");
			for (Formulas f : sbFormulas) System.out.println("        " + f.getName());
			System.out.println("Output folder                     : " + outputFolder.getPath().toString());
			System.out.println("Classpath : ");
			for (String c : classpath) System.out.println("        " + c);
//			System.out.println("Max ranked statements to consider : " + Main.rankedStatements);
//			System.out.println("Min mutGenLimit value to use      : " + Main.mutGenLimitMax);
//			System.out.println("Max mutGenLimit value to use      : " + Main.mutGenLimitMax);
//			System.out.println("Generate gradient version         : " + Main.generateGradientVersion);
//			if (Main.generateGradientVersion)
//				System.out.println("Gradient version step             : " + Main.gradientStep);
//			
			//Calculate rankings and output results
			Map<String, Map<Integer, Float>> rankings = Api.rankStatements(faultyClassName, fCodeFolder, jTestsFolder, outputFolder, juTests, sbFormulas, librariesPaths);
			CoverageInformation ci = Api.getCoverageInformation();
			
			for (Formulas f : sbFormulas) {
				Map<Integer, Float> ranking = rankings.get(f.getName());
				System.out.println("================================");
				System.out.println(f.getName());
				System.out.println("Statements ranking :");
				for (Entry<Integer, Float> rank : ranking.entrySet()) {
					System.out.println("s : " + rank.getKey() + " r : " + rank.getValue() + " executed times : " + Api.getCoverageInformation().getExecutedTimes(rank.getKey()) );
				}
				System.out.println("================================");
				System.out.println();
			}
			
			//Generate mutGenLimit versions
			
//			Set<Integer> linesToMark = new TreeSet<>();
//			for (Formulas f : sbFormulas) {
//				Map<Integer, Float> ranking = rankings.get(f.getName());
//				for (Entry<Integer, Float> r : ranking.entrySet()) {
//					if (r.getValue() > 0 && !linesToMark.contains(r.getKey())) {
//						linesToMark.add(r.getKey());
//					}
//				}
//			}
//			
//			System.out.println("lines to mark: "+linesToMark.size());
//			
//			if (!linesToMark.isEmpty()) {
//				System.out.println("lines to mark is empty!!!!");
//				List<Integer> orderedLines = new LinkedList<>(linesToMark);
//				
//				orderedLines = orderedLines.subList(0, Math.min(orderedLines.size(), Main.rankedStatements));
//				
//				Map<Integer, List<Map<Integer, Integer>>> mglVersionsPerLine = new TreeMap<>();
//				for (Integer line : orderedLines) {
//					mglVersionsPerLine.put(line, generateMGLPerLine(line, Main.mutGenLimitMin, Main.mutGenLimitMax, null));
//				}
//				
//				List<Map<Integer, Integer>> mutGenLimitVersions = new LinkedList<>();
//				
//				List<Map<Integer, Integer>> lastMGLVersions = new LinkedList<>();
//				for (int l = 0 ; l < orderedLines.size(); l++) {
//					int line = orderedLines.get(l);
//					List<Map<Integer, Integer>> currentLineMGLVersions = mglVersionsPerLine.get(line);
//					if (!lastMGLVersions.isEmpty()) {
//						List<Map<Integer, Integer>> newLastMGLVersions = new LinkedList<>();
//						for (Map<Integer, Integer> lastMGLVersion : lastMGLVersions) {
//							List<Map<Integer, Integer>> newMultiLineVersions = generateMGLPerLine(line, Main.mutGenLimitMin, Main.mutGenLimitMax, lastMGLVersion);
//							newLastMGLVersions.add(newMultiLineVersions.get(newMultiLineVersions.size() - 1));
//							mutGenLimitVersions.addAll(newMultiLineVersions);
//						}
//						lastMGLVersions.addAll(newLastMGLVersions);
//					}
//					mutGenLimitVersions.addAll(currentLineMGLVersions);
//					lastMGLVersions.add(currentLineMGLVersions.get(currentLineMGLVersions.size() - 1));
//				}
//				
//				for (Map<Integer, Integer> mglv : mutGenLimitVersions) {
//					System.out.println("===============================");
//					for (Entry<Integer, Integer> lineMGL : mglv.entrySet()) {
//						System.out.println("Line : " + lineMGL.getKey() + "      |     mutGenLimit : " + lineMGL.getValue());
//					}
//				}
//				
//				int idx = 0;
//				for (Map<Integer, Integer> mglv : mutGenLimitVersions) {
//					File out = outputFolder.toPath().resolve("MutGenLimitVersions").resolve(String.valueOf(idx)).toFile();
//					File markedFile = Api.generateMutGenLimitMultiVersion(faultyClassName, fCodeFolder, out, mglv);
//					System.out.println("Marked version saved to : " + markedFile.getPath());
//					idx++;
//				}
//				
//			}
		} catch (IOException e){
			System.err.println("Error while instrumenting (When writing instrumented file)");
			System.err.println("IO EXCEPTION");
			e.printStackTrace();
		} catch (ParseException e) {
			System.err.println("Incorrect Options. Reason: "+e.getMessage());
			e.printStackTrace();
		}
//	} catch (ParseException e) {
//		System.err.println("Incorrect options.  Reason: " + e.getMessage() );
//		return;
//	} catch (com.github.javaparser.ParseException e) {
//		System.err.println("Error while instrumenting (parsing/modifying AST)");
//		e.printStackTrace();
//	}
	}
	
//	private static List<Map<Integer, Integer>> generateMGLPerLine(int line, int min, int max, Map<Integer, Integer> lastMGLVersion) {
//		List<Map<Integer,Integer>> result = new LinkedList<>();
//		for (int v = min; v <= max; v++) {
//			Map<Integer, Integer> lineMgl = new TreeMap<>();
//			lineMgl.put(line, v);
//			if (lastMGLVersion != null) {
//				lineMgl.putAll(lastMGLVersion);
//			}
//			result.add(lineMgl);
//		}
//		return result;
//	}
	

}
