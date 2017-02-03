package faultlocalization.coverage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Scanner;

import com.github.javaparser.ASTHelper;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.ModifierVisitorAdapter;

public class Instrumentalizator extends ModifierVisitorAdapter<Object> {

	private File fileToInstrument;
	private Map<Integer, Integer> mutGenLimitPerLine;
	
	public Instrumentalizator(File f) {
		this.fileToInstrument = f;
		this.mutGenLimitPerLine = null;
	}
	
	public Instrumentalizator(File f, Map<Integer, Integer> mutGenLimitPerLine) {
		this(f);
		this.mutGenLimitPerLine = mutGenLimitPerLine;
	}
	
	public void instrument(File output) throws ParseException, IOException {
		CompilationUnit cu = JavaParser.parse(this.fileToInstrument);
		
		if (this.mutGenLimitPerLine == null) {
			cu.accept(this, null);
		}
		
		String cuAsString = cu.toString();
		String result = "";
		
		if (this.mutGenLimitPerLine != null) {
			Scanner scanner = new Scanner(cuAsString);
			int i = 1;
			while (scanner.hasNextLine()) {
			  String line = scanner.nextLine();
			  if (this.mutGenLimitPerLine.containsKey(i) && this.mutGenLimitPerLine.get(i) > 0) {
				  line += " //mutGenLimit " + this.mutGenLimitPerLine.get(i);
			  }
			  result += line + "\n";
			  i++;
			}
			scanner.close();
		} else {
			result = cuAsString;
		}
		//Files.write(output.toPath(), cu.toString().getBytes());
		Files.write(output.toPath(), result.getBytes());
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
	
	/*
	 * This method creates and returns the wraper statement object to be added to the Block list of Statements,
	 * first, it marks the actual line as executable in the hash map containing that information, then, it adds
	 * a new method call to markExecuted with the file path and the line as arguments, then it returns that Statement.
	 */
	private Statement makeReturnCoverageTrackingCall(int line, Expression expression) {
		//CoverageTracker.markExecutable(file, line); No need for this, I don't want to know if a line could be executed.
		NameExpr coverageTracker = ASTHelper.createNameExpr("return faultlocalization.coverage.CoverageInformationHolder.getInstance().getCoverageInformation(\""+ this.fileToInstrument.getPath().toString()+"\")");
		MethodCallExpr call = new MethodCallExpr(coverageTracker, "mark");
	    ASTHelper.addArgument(call, new IntegerLiteralExpr(String.valueOf(line)));
	    ASTHelper.addArgument(call, expression);
	    return new ExpressionStmt(call);
	}
		
	
	@Override 
	public Node visit(BlockStmt node, Object arg){
		
		/*
		 * Create a new Block Statement to replace the visited one, this modified block
		 * contains a call to the coverage tracker.
		 */
		BlockStmt n = new BlockStmt();
		
		if (node.getParentNode() instanceof Statement) {
			if (	node.getParentNode() instanceof WhileStmt
					||
					node.getParentNode() instanceof SwitchStmt
					||
					(	
							node.getParentNode() instanceof IfStmt
							&&
							(((IfStmt)node.getParentNode()).getThenStmt() == node)
					)
					||
					node.getParentNode() instanceof ForeachStmt
					||
					node.getParentNode() instanceof ForStmt
					||
					node.getParentNode() instanceof CatchClause
					||
					node.getParentNode() instanceof SwitchEntryStmt
				) {
				n.addStatement(makeCoverageTrackingCall(node.getParentNode().getBegin().line));
			}
		}
		
		/*
		 * Add the new statement to the created Block in the first line of the visited block.
		 * Then, add all the statements that belong to the actual node to the block statement
		 * that will replace it.
		 */
		for (Statement st : node.getStmts()){
			if (st instanceof ReturnStmt){
				Statement returnSt = makeReturnCoverageTrackingCall(st.getBegin().line, ((ReturnStmt)st).getExpr());
				n.addStatement(returnSt);
			}else{
				n.addStatement(st);
			}
			if (	!(st instanceof ReturnStmt)
					&&
					!(st instanceof ThrowStmt)
					&& !(	st instanceof WhileStmt
							||
							st instanceof SwitchStmt
							||
							st instanceof IfStmt
							||
							st instanceof ForeachStmt
							||
							st instanceof ForStmt
							||
							st instanceof SwitchEntryStmt
						)
					&& (	(
								st instanceof ExpressionStmt
								&&	(
										((ExpressionStmt)st).getExpression() instanceof VariableDeclarationExpr
										&&
										((VariableDeclarationExpr) ((ExpressionStmt)st).getExpression()).getData() != null
									)
									||
									!(((ExpressionStmt)st).getExpression() instanceof VariableDeclarationExpr)
							)
						)
			) {
				n.addStatement(makeCoverageTrackingCall(st.getBegin().line));
			}
			
		}
		
		/*
		 * Continue visiting the rest of the AST.
		 */
		super.visit(node, arg);
		return n;
	}
	
	
}
