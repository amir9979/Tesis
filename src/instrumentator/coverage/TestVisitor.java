package instrumentator.coverage;

import com.github.javaparser.ASTHelper;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitorAdapter;

public class TestVisitor extends ModifierVisitorAdapter<Object>{

	private String testFile;
	
	public TestVisitor(String testFile){
		this.setTestFile(testFile);
	}

	public String getTestFile() {
		return testFile;
	}

	public void setTestFile(String testFile) {
		this.testFile = testFile;
	}
	
	@Override
	public Node visit(MethodDeclaration md, Object arg){
		if (md.getAnnotations().contains(new MarkerAnnotationExpr(new NameExpr("After"))))	
			md.getBody().addStatement(makeCoverageTrackingCall(getTestFile(), md.getBegin().line));
		return md;		
	}
	
	private Statement makeCoverageTrackingCall(String file, int line) {
		CoverageTracker.markExecutable(file, line);
		NameExpr coverageTracker = ASTHelper.createNameExpr("instrumentator.coverage.CoverageTracker");
		MethodCallExpr call = new MethodCallExpr(coverageTracker, "markExecuted");
	    ASTHelper.addArgument(call, new StringLiteralExpr(file));
	    ASTHelper.addArgument(call, new IntegerLiteralExpr(String.valueOf(line)));
	    return new ExpressionStmt(call);
	}
}
