package au.org.garvan.phenomics.cr.testsuites.common.api.testcase;

import java.util.Iterator;
import java.util.Properties;

public interface ITestCaseIterator extends Iterator<ITestCase>{

	public Properties getProperties(ITestCase testCase);

}
