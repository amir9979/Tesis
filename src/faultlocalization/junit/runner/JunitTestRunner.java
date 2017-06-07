package faultlocalization.junit.runner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.Parameterized;
import org.junit.runners.ParentRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

public class JunitTestRunner {
	private JunitTrigger<?> trigger;
	private Class<?> testToRun;
	private Runner testRunner;
	private JUnitCore core = new JUnitCore();
	
	
	public JunitTestRunner(Class<?> testToRun, JunitTrigger<?> trigger) throws IllegalArgumentException, TestRunnerException {
		this.trigger = trigger;
		this.testToRun = testToRun;
		try {
			this.testRunner = retrieveTestRunner(testToRun);
		} catch (Throwable e) {
			throw new TestRunnerException(this.getClass().getName()+"#MuJavaJunitTestRunner("+testToRun.getName()+")", e);
		}
	}
	
	public Result run() throws InitializationError {
		return this.testRunner!=null?this.core.run(this.testRunner):this.core.run(this.testToRun);
	}
	
	private ParentRunner<?> retrieveTestRunner(Class<?> testToRun) throws Throwable {
		RunWith runWithAnnotation = testToRun.getAnnotation(RunWith.class);
		if (runWithAnnotation != null) {
			if (runWithAnnotation.value().equals(Parameterized.class)) {
				return this.trigger!=null? new TriggerSupportParameterized(testToRun, this.trigger):null;
			}  else if (runWithAnnotation.value().equals(Suite.class)) {
				return new Suite(testToRun, new TriggerSupportRunnerBuilder(this.trigger));
			}
		} else if (TestCase.class.isAssignableFrom(testToRun)) {
			this.testRunner = null; //TODO: for the moment will be using this runner
		} else if (TestSuite.class.isAssignableFrom(testToRun)) {
			this.testRunner = null; //TODO: for the moment will be using this runner
		} else if (hasTestMethod(testToRun)) {
			return this.trigger != null? new TriggerSupportBlockJUnit4ClassRunner(testToRun, this.trigger):null;
		} else {
			throw new IllegalArgumentException("Class : " + testToRun.toString() + " is not a valid junit test");
		}
		return null;
	}
	
	private boolean hasTestMethod(Class<?> testToRun) {
		Method[] methods = testToRun.getDeclaredMethods();
		for (Method m : methods) {
			if (Modifier.isPublic(m.getModifiers())) {
				if (m.getAnnotation(Test.class) != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	
}
