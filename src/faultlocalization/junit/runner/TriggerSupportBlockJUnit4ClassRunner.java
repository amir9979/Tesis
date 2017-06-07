package faultlocalization.junit.runner;

import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class TriggerSupportBlockJUnit4ClassRunner extends BlockJUnit4ClassRunner {
	
	private JunitTrigger<?> trigger;

	public TriggerSupportBlockJUnit4ClassRunner(Class<?> klass, JunitTrigger<?> trigger) throws InitializationError {
		super(klass);
		this.trigger = trigger;
	}
	
	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		EachTestNotifier eachNotifier= makeNotifier(method, notifier);
		if (method.getAnnotation(Ignore.class) != null) {
			eachNotifier.fireTestIgnored();
			return;
		}
		eachNotifier.fireTestStarted();
		try {
			this.trigger.evaluateBeforeTest();
			if (this.trigger.allowTestToRun()) methodBlock(method).evaluate();
			this.trigger.evaluateIfTestPassed();
		} catch (AssumptionViolatedException e) {
			eachNotifier.addFailedAssumption(e);
			this.trigger.evaluateIfTestFailed();
		} catch (Throwable e) {
			eachNotifier.addFailure(e);
			this.trigger.evaluateIfTestFailed();
		} finally {
			eachNotifier.fireTestFinished();
			this.trigger.evaluateAfterTest();
		}
	}
	
	private EachTestNotifier makeNotifier(FrameworkMethod method, RunNotifier notifier) {
		Description description= describeChild(method);
		return new EachTestNotifier(notifier, description);
	}

}
