package instrumentator.coverage;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class Watchman extends TestWatcher {
	@Override
	protected void failed(Throwable e, Description d){
        instrumentator.coverage.CoverageTracker.markTestResult(false);
	}
	
	@Override
	protected void succeeded(Description d){
		instrumentator.coverage.CoverageTracker.markTestResult(true);
	}
}
