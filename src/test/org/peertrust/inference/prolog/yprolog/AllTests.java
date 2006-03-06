package test.org.peertrust.inference.prolog.yprolog;

import junit.framework.TestSuite;

public class AllTests extends TestSuite {
	public AllTests() {
		this("all YProlog related.tests");
	}

	public AllTests(String name) {
		super(name);
		addTestSuite(YPrologEngineTest.class);
	}

	public static void main(String args[]) {
		junit.textui.TestRunner.run(new AllTests());
	}

}

