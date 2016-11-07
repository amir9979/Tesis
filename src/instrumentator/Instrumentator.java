package instrumentator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
			tcu.accept(new TestVisitor(testPath), null);
			/*
			 * Encode in a bite array the modified compilation units that represents both the instrumented 
			 * Java Source code and the modified Test Suite.
			 */
			byte[] lines = cu.toString().getBytes();
			byte[] testFileLines = tcu.toString().getBytes();
			/*
			 * Create the new names for the instrumented files.
			 */
			String filename = filePath.split("/")[filePath.split("/").length-1];//.replaceAll(".java", "_instrumented.java");
			String testFilename = testPath.split("/")[testPath.split("/").length-1];//.replaceAll(".java","_instrumented.java");
			/*
			 * Make the new path for the instrumented files and save it in that location
			 */
			Path instFilePath = Paths.get("./instrumented/"+filename);
			Path instTestPath = Paths.get("./instrumented/"+testFilename);
			/*
			 * Write the byte array representing the modified files in the corresponding path.
			 */
			Files.write(instFilePath, lines);
			Files.write(instTestPath, testFileLines);
		} catch (ParseException | IOException e) {
			// Catch and report a parsing exception or a file IO exceptions.
			e.printStackTrace();
		}
	}
	
}
