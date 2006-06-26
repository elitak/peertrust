package unittest;

import unittest.model.ConfigFileTest;
import unittest.model.PSResourceImplTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class UniTest {
	public static Test suite() {
        final TestSuite suite = new TestSuite();
        //suite.addTest(XXX.suite());
        suite.addTestSuite(PSResourceImplTest.class);
        suite.addTestSuite(ConfigFileTest.class);
        return suite;
    }

}
