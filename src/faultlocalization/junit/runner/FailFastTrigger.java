package faultlocalization.junit.runner;

public class FailFastTrigger implements JunitTrigger<Void> {

	private boolean runTest = true;
	
	@Override
	public Void evaluateBeforeTest() {
		return null;
	}

	@Override
	public Void evaluateAfterTest() {
		return null;
	}

	@Override
	public Void evaluateIfTestFailed() {
		this.runTest = false;
		return null;
	}

	@Override
	public Void evaluateIfTestPassed() {
		return null;
	}

	@Override
	public boolean allowTestToRun() {
		return this.runTest;
	}


}
