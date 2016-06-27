package instrumentator;

import instrumentator.coverage.CoverageTracker;

public class Hello {
	public static void main(String[] args) {
		System.out.println("hello, world");
		CoverageTracker.markExecuted("/path",5);
		
		CoverageTracker.writeCoverageToFile();
	}
}
