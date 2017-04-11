package instrumentator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;



import org.junit.runner.JUnitCore;
import org.junit.runners.JUnit4;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

import instrumentator.coverage.CoverageVisitor;
import instrumentator.coverage.TestVisitor;
import jdk.nashorn.internal.runtime.options.Options;

public class Instrumentator {

	//private static ArrayList<File> filesToInstrumentate = new ArrayList<>();
	public static void main(String[] args) {
		/*
		 * Attempt to load the configuration file.
		 * if it is null then load a default one.
		 */
		try {
			Properties defaultProperties = new Properties();
			String defaultFile = "default.props";
			//Load default properties
			FileInputStream in = new FileInputStream(defaultFile);
			defaultProperties.load(in);
			in.close();
			
			//Create application properties with defaults.
			Properties applicationProperties = new Properties(defaultProperties);
			
			//Load application properties from disk.
			String customPropertiesFile = args[0];
			in = new FileInputStream(customPropertiesFile);
			/*
			 * Load the custom properties over default ones, any new, non existing value on the set is added to the
			 * applicationProperties object, any existing value is overwritten if present in the new file 
			 */
			applicationProperties.load(in);
			in.close();

			/*
			 * Get the paths from the properties and stores them in two String objects
			 */
			String filePath = applicationProperties.getProperty("faultyClassLocation");
			String testPath = applicationProperties.getProperty("testsLocation");
			/*
			 * Get all the files inside that path.
			 */
			ArrayList<File> filesToInstrumentate = (ArrayList<File>) Files.walk(Paths.get(filePath)).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
			ArrayList<File> testFilesToInstrumentate = (ArrayList<File>) Files.walk(Paths.get(testPath)).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
			/*
			 * Get the output folder
			 */
			String classesOutputPath = applicationProperties.getProperty("classesOutputLocation");
			String testsOutputPath = applicationProperties.getProperty("testsOutputLocation");
			/*
			 * Create 2 Java File Object using the path given as argument, one of those files
			 * is the Java source code to instrument, and the other one is the JUnit test suite
			 * that will be used to detect suspicious lines.
			 */
			//File file = new File(filePath);
			//File testFile = new File(testPath);
			/*
			 * We try to parse the given file using JavaParser, this creates a compilation unit that is
			 * an AST representation of the parsed file.
			 */
			/*
			 * Check if the user gives a particular formula, if not, use the default value.
			 */
			String formula = applicationProperties.getProperty("formula");
			for (File file : filesToInstrumentate){
				CompilationUnit cu = JavaParser.parse(file);
				/*
				 * Create the visitors that will traverse the AST and modify the files.
				 */
				cu.accept(new CoverageVisitor(file.getAbsolutePath()), null);
				
				/*
				 * Encode in a bite array the modified compilation units that represents both the instrumented 
				 * Java Source code and the modified Test Suite.
				 */
				byte[] lines = cu.toString().getBytes();
				/*
				 * Create the new names for the instrumented files.
				 */
				String filename = filePath.split("/")[filePath.split("/").length-1];//.replaceAll(".java", "_instrumented.java");
				
				/*
				 * Create the new packages
				 */
				String filePack = filePath.replace("./faulty_code/", "").replace(filename, "");
				//String testFilePack = testPath.replace("./tests/", "").replace(testFilename, "");
				//System.out.println(testFilePack);
				/*
				 * Make the new path for the instrumented files and save it in that location
				 */
				Path instClassFilePath = Paths.get(classesOutputPath+file.getName());
				
				/*
				 * Create the directories if they doesn't exist
				 */
				Files.createDirectories(instClassFilePath.getParent());
				
				/*
				 * See if files exists, if not create them
				 */
				if (!Files.exists(instClassFilePath)){
					Files.createFile(instClassFilePath);
				}
				/*
				 * Write the byte array representing the modified files in the corresponding path.
				 */
				Files.write(instClassFilePath, lines);
				
			}
			for (File testFile : testFilesToInstrumentate){
				System.out.println(testFile);
				CompilationUnit tcu = JavaParser.parse(testFile);
				System.out.println(tcu);
				tcu.accept(new TestVisitor(testFile.getAbsolutePath(),filePath, formula), null);
				byte[] testFileLines = tcu.toString().getBytes();
				Path instTestPath = Paths.get(testsOutputPath+testFile.getName());
				Files.createDirectories(instTestPath.getParent());
				if (!Files.exists(instTestPath)){
					Files.createFile(instTestPath);
				}
				Files.write(instTestPath, testFileLines);
			}
		} catch (IOException | ParseException  e) {
			e.printStackTrace();
		}
//		/*
//		 * Get the output directories. If null then set the output to a default destination.
//		 */
//		String default_output = "";
//		String output = args.length > 2 ? args[2] : default_output;
		
		
	//	try {
			
			
			
			//System.out.println(filePath.replace("./faulty_code", ""));
			
			
			/*
			 * Compile the new instrumented class.
			 */
			
			//Retrieve the system java compiler
//			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//			StandardJavaFileManager sjfm = compiler.getStandardFileManager(null, null, null);
//			
//			File jf = new File(instFilePath.toString());
//			
//			Iterable fileObjects = sjfm.getJavaFileObjects(jf);
//			
//			String[] options = new String[]{"-d", "./instrumented/instrumented/"+filePack+"bin"};
//			
//			boolean r = compiler.getTask(null, null, null, Arrays.asList(options), null, fileObjects).call();
//			
//			if (r){
//				System.out.println("Intrumented program succesfully compiled at "+"./instrumented/instrumented/"+filePack+"bin/"+filename);
//				System.out.println(instFilePath.toFile().toURI().toURL());
//				URL url = new URL("instrumented/instrumented/"+filePack+"bin/");
//				File a = new File(url.toURI());
//				System.out.println(a.exists());
//				URL[] urls = new URL[]{ new URL("file://./instrumented/instrumented/"+filePack+"bin/")};
//				URLClassLoader ucl = new URLClassLoader(urls);
////				System.out.println(cu.getPackage().getPackageName());
//				Class<?> clazz = ucl.loadClass(cu.getPackage().getPackageName()+"."+filename.replace(".java",""));
//				ucl.close();
//			}
			//Attempt to compile the instrumented file.
//			int result = compiler.run(null, null, null, instFilePath.toString());
//			System.out.println(result);
//			String classPath = instFilePath.toFile().toURI().toString().replace("./", "");
//			classPath = classPath.replace(filename, "");
//			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {new URL(classPath)});
//			System.out.println(classLoader.getURLs()[0].toString());
//			Class<?> cl = classLoader.loadClass(filename);
//			Object cln = cl.newInstance();
//			System.out.println(cln);
//			Runtime.getRuntime().exec("javac -cp *:. "+ instFilePath).waitFor();
//			Runtime.getRuntime().exec("javac -cp *:. "+ instTestPath).waitFor();
//			JUnitCore junit = new JUnitCore();
	//	} catch (){//| ClassNotFoundException | InstantiationException | IllegalAccessException  e) {
			// Catch and report a parsing exception or a file IO exceptions.
//		}
	}
	
	
}
