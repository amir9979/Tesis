package faultlocalization.junit.runner;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized;
import org.junit.runners.ParentRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;

//TODO: comment
public class TriggerSupportRunnerBuilder extends RunnerBuilder {

	private JunitTrigger<?> trigger;
	
	public TriggerSupportRunnerBuilder(JunitTrigger<?> trigger) {
		this.trigger = trigger;
	}
	
	@Override
	public Runner runnerForClass(Class<?> arg0) throws Throwable {
		return retrieveTestRunner(arg0, this.trigger);
	}
	
	private ParentRunner<?> retrieveTestRunner(Class<?> testToRun, JunitTrigger<?> trigger) throws Throwable {
		RunWith runWithAnnotation = testToRun.getAnnotation(RunWith.class);
		if (runWithAnnotation != null) {
			if (runWithAnnotation.value().equals(Parameterized.class)) {
				return new TriggerSupportParameterized(testToRun, trigger);
			} else if (runWithAnnotation.value().equals(Suite.class)) {
				return new Suite(testToRun, this);
			}
		} else if (TestCase.class.isAssignableFrom(testToRun)) {
			return new TriggerSupportBlockJUnit4ClassRunner(testToRun, trigger);
		} else if (TestSuite.class.isAssignableFrom(testToRun)) {
			return new TriggerSupportBlockJUnit4ClassRunner(testToRun, trigger);
		} else if (testToRun.getAnnotation(Test.class) != null) {
			return new TriggerSupportBlockJUnit4ClassRunner(testToRun, trigger);
		} else {
			throw new IllegalArgumentException("Class : " + testToRun.toString() + " is not a valid junit test");
		}
		return new BlockJUnit4ClassRunner(testToRun);
	}

}
