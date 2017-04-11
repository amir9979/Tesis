package instrumentator.coverage;

import com.github.javaparser.ASTHelper;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.ModifierVisitorAdapter;

public class TestVisitor extends ModifierVisitorAdapter<Object>{

	private String testFile;
	private String packageFile;
	private String formula;
	
	public TestVisitor(String testFile, String formula){
		this.setTestFile(testFile);
		this.formula = formula;
	}
	
//	public TestVisitor(String testFile, String packageOfFileToTest){
//		this.setTestFile(testFile);
//		this.packageFile = packageOfFileToTest;
//	}
//	
	public TestVisitor(String testFile, String packageOfFileToTest, String formula){
		this.setTestFile(testFile);
		this.packageFile = packageOfFileToTest;
		this.formula = formula;
	}

	public TestVisitor(String formula){
		this.formula = formula;
	}
	public String getTestFile() {
		return testFile;
	}

	public void setTestFile(String testFile) {
		this.testFile = testFile;
	}
	
	@Override 
	public Node visit(PackageDeclaration pd, Object arg){
		pd.setName(new NameExpr("instrumented."+pd.getPackageName()));
		super.visit(pd, arg);
		return pd;
	}
	
//	@Override
//	public Node visit(ImportDeclaration id, Object arg){
//		if (id.getName().toString().contains(packageFile)){
//			id.setName(new NameExpr("instrumented."+id.getName()));
//		}
//		super.visit(id, arg);
//		return id;
//	}
	
	@Override
	public Node visit(ClassOrInterfaceDeclaration cd, Object arg){
		addRuleDeclaration(cd);
		cd.tryAddImportToParentCompilationUnit(org.junit.Rule.class);
		addBeforeMethod(cd);
		cd.tryAddImportToParentCompilationUnit(org.junit.Before.class);
		addAfterClassMethod(cd);
		cd.tryAddImportToParentCompilationUnit(org.junit.AfterClass.class);
//		replaceConstructors(cd);
//		cd.setName(cd.getName()+"_instrumented");
		super.visit(cd, arg);
		//System.out.println(cd);
		return cd;
	}
	
	
//	private void replaceConstructors(ClassOrInterfaceDeclaration cd){
//		System.out.println(cd.getName());
//		System.out.println(cd.getMembers());
//		System.out.println(cd.getMethodsByName(cd.getName()));
//	}
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
	
	private void addRuleDeclaration(ClassOrInterfaceDeclaration cd){
		String init = "new instrumentator.coverage.Watchman()";
		VariableDeclarator variables = new VariableDeclarator(new VariableDeclaratorId("watcher"),new NameExpr(init));
		FieldDeclaration fd = new FieldDeclaration(Modifier.PUBLIC.toEnumSet(), new ClassOrInterfaceType("instrumentator.coverage.Watchman"), variables);
		ASTHelper.addMember(cd, fd);
		cd.getFieldByName("watcher").addAnnotation("Rule");
	}
	
	private BodyDeclaration<MethodDeclaration> addAfterClassMethod(ClassOrInterfaceDeclaration cd){
		MethodDeclaration method = new MethodDeclaration(Modifier.PUBLIC.toEnumSet(), ASTHelper.VOID_TYPE, "set_tracker_data");
        method.addModifier(Modifier.STATIC);
		ASTHelper.addMember(cd, method);
        // add after annotation
        method.addAnnotation("AfterClass");
        
        // add a body to the method
        BlockStmt block = new BlockStmt();
        method.setBody(block);

        // add a statement do the method body
        ASTHelper.addStmt(block, setTrackerValues());
		return method;
	}
	
	
	private BodyDeclaration<MethodDeclaration> addBeforeMethod(ClassOrInterfaceDeclaration cd){
		MethodDeclaration method = new MethodDeclaration(Modifier.PUBLIC.toEnumSet(), ASTHelper.VOID_TYPE, "clear_coverage");
        ASTHelper.addMember(cd, method);
        // add after annotation
        method.addAnnotation("Before");
        
        // add a body to the method
        BlockStmt block = new BlockStmt();
        method.setBody(block);

        // add a statement do the method body
        ASTHelper.addStmt(block, makeCoverageClearCall());
		return method;
	}
//	
//	private BodyDeclaration<MethodDeclaration> failureMethod(ClassOrInterfaceDeclaration cd){
//		MethodDeclaration method = new MethodDeclaration(Modifier.PROTECTED.toEnumSet(), ASTHelper.VOID_TYPE, "failed");
//        ASTHelper.addMember(cd, method);
//
//        // add after annotation
//        method.addAnnotation("Override");
//        
//        
//        // add a body to the method
//        BlockStmt block = new BlockStmt();
//        method.setBody(block);
//
//        // add a statement do the method body
//        ASTHelper.addStmt(block, makeCoverageTrackingCall());
//		return method;
//	}	
	
	private Statement makeCoverageClearCall(){
		NameExpr coverageTracker = ASTHelper.createNameExpr("instrumentator.coverage.CoverageTracker");
		MethodCallExpr call = new MethodCallExpr(coverageTracker,"clearCoverageMap");
		return new ExpressionStmt(call);
	}
	
	private Statement setTrackerValues(){
		NameExpr coverageTracker = ASTHelper.createNameExpr("instrumentator.coverage.CoverageTracker");
		MethodCallExpr call = new MethodCallExpr(coverageTracker,"setFormula");
		ASTHelper.addArgument(call, new IntegerLiteralExpr(String.valueOf(this.formula)));
		return new ExpressionStmt(call);
	}
	
//	private Statement makeCoverageTrackingCall() {
//		//CoverageTracker.markExecutable(file, line);
//		NameExpr coverageTracker = ASTHelper.createNameExpr("instrumentator.coverage.CoverageTracker");
//		MethodCallExpr call = new MethodCallExpr(coverageTracker, "markTestResult");
////	    ASTHelper.addArgument(call, new StringLiteralExpr(file));
//	    ASTHelper.addArgument(call, new BooleanLiteralExpr(true));
//	    return new ExpressionStmt(call);
//	}
}
