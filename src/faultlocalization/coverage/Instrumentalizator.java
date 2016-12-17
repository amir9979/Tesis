package faultlocalization.coverage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.github.javaparser.ASTHelper;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitorAdapter;

public class Instrumentalizator extends ModifierVisitorAdapter<Object> {

	private File fileToInstrument;
	
	
	public Instrumentalizator(File f) {
		this.fileToInstrument = f;
	}
	
	public void instrument(File output) throws ParseException, IOException {
		CompilationUnit cu = JavaParser.parse(this.fileToInstrument);
		cu.accept(this, null);
		Files.write(output.toPath(), cu.toString().getBytes());
	}
	
	/*
	 * This method creates and returns the Statement object to be added to the Block list of Statements,
	 * first, it marks the actual line as executable in the hash map containing that information, then, it adds
	 * a new method call to markExecuted with the file path and the line as arguments, then it returns that Statement.
	 */
	private Statement makeCoverageTrackingCall(int line) {
		//CoverageTracker.markExecutable(file, line); No need for this, I don't want to know if a line could be executed.
		NameExpr coverageTracker = ASTHelper.createNameExpr("faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation(\""+ this.fileToInstrument.getPath().toString()+"\")");
		MethodCallExpr call = new MethodCallExpr(coverageTracker, "mark");
	    ASTHelper.addArgument(call, new IntegerLiteralExpr(String.valueOf(line)));
	    return new ExpressionStmt(call);
	}
	
	
	@Override 
	public Node visit(BlockStmt node, Object arg){
		/*
		 * Create a new Block Statement to replace the visited one, this modified block
		 * contains a call to the coverage tracker.
		 */
		BlockStmt n = new BlockStmt();
		/*
		 * Add the new statement to the created Block in the first line of the visited block.
		 * Then, add all the statements that belong to the actual node to the block statement
		 * that will replace it.
		 */
		n.addStatement(makeCoverageTrackingCall(node.getBegin().line));
		for (Statement st : node.getStmts()){
			n.addStatement(st);
		}
		/*
		 * Continue visiting the rest of the AST.
		 */
		super.visit(node, arg);
		return n;
	}
	
	
}
