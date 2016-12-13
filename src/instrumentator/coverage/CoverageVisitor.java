package instrumentator.coverage;

import com.github.javaparser.ASTHelper;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitorAdapter;

public class CoverageVisitor extends ModifierVisitorAdapter<Object>{
	
	private String file;
	
	public CoverageVisitor(String file) {
		this.setFile(file);
	}
	
	private String getFile() {
		return file;
	}
	
//	@Override
//	public Node visit(ClassOrInterfaceDeclaration cd, Object arg){
//		cd.setName(cd.getName()+"_instrumented");
//		super.visit(cd, arg);	
//		return cd;
//	}
	
	@Override 
	public Node visit(PackageDeclaration pd, Object arg){
		pd.setName(new NameExpr("instrumented."+pd.getPackageName()));
		super.visit(pd, arg);
		return pd;
	}
	
	/*
	 * This method creates and returns the Statement object to be added to the Block list of Statements,
	 * first, it marks the actual line as executable in the hash map containing that information, then, it adds
	 * a new method call to markExecuted with the file path and the line as arguments, then it returns that Statement.
	 */
	private Statement makeCoverageTrackingCall(int line) {
		//CoverageTracker.markExecutable(file, line); No need for this, I don't want to know if a line could be executed.
		NameExpr coverageTracker = ASTHelper.createNameExpr("instrumentator.coverage.CoverageTracker");
		MethodCallExpr call = new MethodCallExpr(coverageTracker, "markExecuted");
	    ASTHelper.addArgument(call, new IntegerLiteralExpr(String.valueOf(line)));
	    return new ExpressionStmt(call);
	}

	private void setFile(String file) {
		this.file = file;
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
