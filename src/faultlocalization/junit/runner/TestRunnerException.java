package faultlocalization.junit.runner;

public class TestRunnerException extends Exception {

	public TestRunnerException(String string, Exception e) {
		super(string, e);
	}

	public TestRunnerException(String string, Throwable e) {
		super(string, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4416792597305116693L;

}
