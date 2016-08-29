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

public class Instrumentator {

	public static void main(String[] args) {
		//Create a File object with the path given as argument.
		File file = new File(args[0]);
		//Try to read, parse and save the modified file.
		try {
			//Parse the given file and create a Compilation Unit object that represents it.
			CompilationUnit cu = JavaParser.parse(file);
			//Make the visitor traverse the AST.
			cu.accept(new CoverageVisitor(args[0]), null);
			//Print the modified representation (Delete this)
			System.out.println(cu);
			//Encode in a byte array the modified compilation unit that represents the instrumented program.
			byte[] lines = cu.toString().getBytes();
			//Get the given path passed as argument.
			Path path = Paths.get(args[0]);
			//Overwrite the file with the modified file (Maybe create a new file instead?)
			Files.write(path, lines);
		} catch (ParseException | IOException e) {
			// Catch and report a parsing exception or a file IO exception.
			e.printStackTrace();
		}
	}
	
}
