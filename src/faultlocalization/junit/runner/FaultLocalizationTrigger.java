package faultlocalization.junit.runner;

import faultlocalization.coverage.CoverageInformation;

public class FaultLocalizationTrigger implements JunitTrigger<Void> {

	private CoverageInformation coverageInformation;
	
	public FaultLocalizationTrigger(CoverageInformation ci) {
		this.coverageInformation = ci;
	}
	
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
		this.coverageInformation.applyMarkedLines(false);
		return null;
	}

	@Override
	public Void evaluateIfTestPassed() {
		this.coverageInformation.applyMarkedLines(true);
		return null;
	}

	@Override
	public boolean allowTestToRun() {
		return true;
	}

}
