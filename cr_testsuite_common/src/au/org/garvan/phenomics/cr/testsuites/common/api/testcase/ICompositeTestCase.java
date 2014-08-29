package au.org.garvan.phenomics.cr.testsuites.common.api.testcase;

import java.util.Map;

public interface ICompositeTestCase extends ITestCase {

	public Map<String, ITestCaseResult> retrieveTestCases();
	
}
