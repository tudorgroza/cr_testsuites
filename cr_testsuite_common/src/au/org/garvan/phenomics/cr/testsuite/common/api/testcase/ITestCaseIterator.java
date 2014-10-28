package au.org.garvan.phenomics.cr.testsuite.common.api.testcase;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public interface ITestCaseIterator extends Iterator<ITestCase>{

	public Properties getPropertiesForSimpleTestCase(ISimpleTestCase testCase);

	public Map<String, Properties> getPropertiesForCompositeTestCase(ICompositeTestCase testCase);

}
