package instrumentator.coverage;

import com.github.javaparser.ASTHelper;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitorAdapter;

public class CoverageVisitor extends ModifierVisitorAdapter<Object>{
	
	private String file;
	
	public CoverageVisitor(String file) {
		this.file = file;
	}
	

	
	
	@Override 
	public Node visit(BlockStmt node, Object arg){
		BlockStmt n = new BlockStmt();
		n.addStatement(makeCoverageTrackingCall(file, node.getBegin().line));
		for (Statement st : node.getStmts()){
			n.addStatement(st);
		}
		super.visit(node, arg);
		return n;
	}

	private Statement makeCoverageTrackingCall(String file, int line) {
		CoverageTracker.markExecutable(file, line);
		NameExpr coverageTracker = ASTHelper.createNameExpr("instrumentator.coverage.CoverageTracker");
		MethodCallExpr call = new MethodCallExpr(coverageTracker, "markExecuted");
	    ASTHelper.addArgument(call, new StringLiteralExpr(file));
	    ASTHelper.addArgument(call, new IntegerLiteralExpr(String.valueOf(line)));
	    return new ExpressionStmt(call);
	}
	
//	public void ParseAndSave(){ 
//		try {
//			CompilationUnit cu = JavaParser.parse(new StringReader(this.file));
//
//		    //System.out.println(helloClass.getMembers())
//		    
//		    for (TypeDeclaration t : cu.getTypes()){
//		    	for (BodyDeclaration bd : t.getMembers()){
//				    for (BlockStmt b : ASTHelper.getNodesByType(bd, BlockStmt.class)) {
//				    	
//				    	//Package of method (Scope)
//				    	NameExpr pack = ASTHelper.createNameExpr("instrumentator.coverage.CoverageTracker");
//				    	//Method to be called
//				    	MethodCallExpr meth = new MethodCallExpr(pack,"markExecuted");
//				    	//Add as arguments the class that executes the block and the initial position of the block
//				    	ASTHelper.addArgument(meth, new StringLiteralExpr(t.getName()+".class"));
//				    	ASTHelper.addArgument(meth, new IntegerLiteralExpr(String.valueOf((b.getBegin().line))));
//				    	//Add the statement to mark the executed line (See how to add it on the beggining of the block
//				    	ASTHelper.addStmt(b, meth);
//				    }
//		    	}
//		    }
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
