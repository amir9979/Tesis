package faultlocalization.junit.runner;

public interface JunitTrigger<R> {
	
	public R evaluateBeforeTest();
	
	public R evaluateAfterTest();
	
	public R evaluateIfTestFailed();
	
	public R evaluateIfTestPassed();
	
	public boolean allowTestToRun();
	
}
