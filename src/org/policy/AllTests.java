package org.policy;

import test.org.policy.action.ActionRequestTest;
import test.org.policy.action.ActionResultTest;
import test.org.policy.action.JDBCWrapperTest;
import test.org.policy.action.LDAPWrapperTest;
import test.org.policy.action.ParserTest;
import test.org.policy.action.QueryGUITest;
import test.org.policy.action.RegExpWrapperTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for test.org.policy.action");
		//$JUnit-BEGIN$
		suite.addTestSuite(ParserTest.class);
		suite.addTestSuite(RegExpWrapperTest.class);
		suite.addTestSuite(JDBCWrapperTest.class);
		suite.addTestSuite(LDAPWrapperTest.class);
		suite.addTestSuite(ActionResultTest.class);
		suite.addTestSuite(ActionRequestTest.class);
		suite.addTestSuite(QueryGUITest.class);
		//$JUnit-END$
		return suite;
	}

}
