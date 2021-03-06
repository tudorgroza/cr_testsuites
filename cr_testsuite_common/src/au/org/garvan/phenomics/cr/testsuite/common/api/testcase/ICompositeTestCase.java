package au.org.garvan.phenomics.cr.testsuite.common.api.testcase;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface ICompositeTestCase extends ITestCase {

	public void runTestCases(Map<String, Properties> properties);

	public Map<String, List<ITestCaseConcept>> retrieveTestCases();
	
}
