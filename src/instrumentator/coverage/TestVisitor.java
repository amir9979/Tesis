package instrumentator.coverage;

import java.util.EnumSet;


import com.github.javaparser.ASTHelper;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.ModifierVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitor;

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
	public Node visit(ClassOrInterfaceDeclaration cd, Object arg){
		System.out.println("Entre a la clase de nombre "+cd.getName() );
		String init = "new instrumentator.coverage.Watchman()";
		VariableDeclarator variables = new VariableDeclarator(new VariableDeclaratorId("watcher"),new NameExpr(init));
		FieldDeclaration field = new FieldDeclaration(Modifier.PUBLIC.toEnumSet(), new ClassOrInterfaceType("instrumentator.coverage.Watchman"), variables);
		ASTHelper.addMember(cd, field);
		cd.getFieldByName("watcher").addAnnotation("Rule");
		super.visit(cd, arg);
		System.out.println(cd);
		return cd;
	}
	
//	@Override
//	public Node visit(MethodDeclaration md, Object arg){
//		if (md.getAnnotations().contains(new MarkerAnnotationExpr(new NameExpr("After")))){
//			md.getBody().addStatement(makeCoverageTrackingCall(md.getBegin().line));
//		} else {
//			md.addMarkerAnnotation("After");
//			md.getBody().addStatement(makeCoverageTrackingCall(md.getBegin().line));
//		}
//		return md;		
//	}
	
	private FieldDeclaration ruleDeclaration(ClassOrInterfaceDeclaration cd){
		String initString = "Watchman";
		//FieldDeclaration fd = new FieldDeclaration(Modifier.PRIVATE, new ClassOrInterfaceDeclaration("instrumentator.coverage.Watchman"), variable)
return null;
	}
	
	private BodyDeclaration<MethodDeclaration> afterMethod(ClassOrInterfaceDeclaration cd){
		MethodDeclaration method = new MethodDeclaration(Modifier.PUBLIC.toEnumSet(), ASTHelper.VOID_TYPE, "teardown");
        ASTHelper.addMember(cd, method);
        // add after annotation
        method.addAnnotation("After");
        
        // add a body to the method
        BlockStmt block = new BlockStmt();
        method.setBody(block);

        // add a statement do the method body
        ASTHelper.addStmt(block, makeCoverageTrackingCall());
		return method;
	}
	
	private BodyDeclaration<MethodDeclaration> failureMethod(ClassOrInterfaceDeclaration cd){
		MethodDeclaration method = new MethodDeclaration(Modifier.PROTECTED.toEnumSet(), ASTHelper.VOID_TYPE, "failed");
        ASTHelper.addMember(cd, method);

        // add after annotation
        method.addAnnotation("Override");
        
        
        // add a body to the method
        BlockStmt block = new BlockStmt();
        method.setBody(block);

        // add a statement do the method body
        ASTHelper.addStmt(block, makeCoverageTrackingCall());
		return method;
	}	
	
	private Statement makeCoverageTrackingCall() {
		//CoverageTracker.markExecutable(file, line);
		NameExpr coverageTracker = ASTHelper.createNameExpr("instrumentator.coverage.CoverageTracker");
		MethodCallExpr call = new MethodCallExpr(coverageTracker, "markTestResult");
//	    ASTHelper.addArgument(call, new StringLiteralExpr(file));
	    ASTHelper.addArgument(call, new BooleanLiteralExpr(true));
	    return new ExpressionStmt(call);
	}
}
