package instrumentator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.junit.runner.JUnitCore;
import org.junit.runners.JUnit4;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

import instrumentator.coverage.CoverageVisitor;
import instrumentator.coverage.TestVisitor;

public class Instrumentator {

	public static void main(String[] args) {
		/*
		 * Get the paths and store them in two String
		 */
		String filePath = args[0];
		String testPath = args[1];
		/*
		 * Create 2 Java File Object using the path given as argument, one of those files
		 * is the Java source code to instrument, and the other one is the JUnit test suite
		 * that will be used to detect suspicious lines.
		 */
		File file = new File(filePath);
		File testFile = new File(testPath);
		/*
		 * We try to parse the given file using JavaParser, this creates a compilation unit that is
		 * an AST representation of the parsed file.
		 */
		try {
			/*
			 * Parse both files and assign that to a Compilation Unit object.
			 */
			CompilationUnit cu = JavaParser.parse(file);
			CompilationUnit tcu = JavaParser.parse(testFile);
			/*
			 * Create the visitors that will traverse the AST and modify the files.
			 */
			cu.accept(new CoverageVisitor(filePath), null);
			if (args.length == 3){
				int formula = Integer.parseInt(args[2]);
				tcu.accept(new TestVisitor(testPath, filePath.split("/")[filePath.split("/").length-2],formula), null);
			} else {
				tcu.accept(new TestVisitor(testPath, filePath.split("/")[filePath.split("/").length-2]), null);
			}
			
			/*
			 * Encode in a bite array the modified compilation units that represents both the instrumented 
			 * Java Source code and the modified Test Suite.
			 */
			byte[] lines = cu.toString().getBytes();
			byte[] testFileLines = tcu.toString().getBytes();
			System.out.println(filePath.replace("./faulty_code", ""));
			/*
			 * Create the new names for the instrumented files.
			 */
			String filename = filePath.split("/")[filePath.split("/").length-1];//.replaceAll(".java", "_instrumented.java");
			String testFilename = testPath.split("/")[testPath.split("/").length-1];//.replaceAll(".java","_instrumented.java");
			/*
			 * Create the new packages
			 */
			String filePack = filePath.replace("./faulty_code/", "").replace(filename, "");
			String testFilePack = testPath.replace("./tests/", "").replace(testFilename, "");
			System.out.println(filePack);
			System.out.println(testFilePack);
			/*
			 * Make the new path for the instrumented files and save it in that location
			 */
			Path instFilePath = Paths.get("./instrumented/instrumented/"+filePack+"/"+filename);
			Path instTestPath = Paths.get("./instrumented/instrumented/"+testFilePack+"/"+testFilename);
			/*
			 * Create the directories if they doesn't exist
			 */
			Files.createDirectories(instFilePath.getParent());
			Files.createDirectories(instTestPath.getParent());
			/*
			 * See if files exists, if not create them
			 */
			if (!Files.exists(instFilePath)){
				Files.createFile(instFilePath);
			}
			if (!Files.exists(instTestPath)){
				Files.createFile(instTestPath);
			}
			/*
			 * Write the byte array representing the modified files in the corresponding path.
			 */
			Files.write(instFilePath, lines);
			Files.write(instTestPath, testFileLines);
			/*
			 * Compile the new
			 */
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			
			compiler.run(null, null, null, instFilePath.toString());
			String classPath = instFilePath.toFile().toURI().toString().replace("./", "");
			classPath = classPath.replace(filename, "");
			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {new URL(classPath)});
			System.out.println(classLoader.getURLs()[0].toString());
			Class<?> cl = classLoader.loadClass(filename);
			Object cln = cl.newInstance();
			System.out.println(cln);
//			Runtime.getRuntime().exec("javac -cp *:. "+ instFilePath).waitFor();
//			Runtime.getRuntime().exec("javac -cp *:. "+ instTestPath).waitFor();
			JUnitCore junit = new JUnitCore();
		} catch (ParseException | IOException | ClassNotFoundException | InstantiationException | IllegalAccessException  e) {
			// Catch and report a parsing exception or a file IO exceptions.
			e.printStackTrace();
		}
	}
	
}
