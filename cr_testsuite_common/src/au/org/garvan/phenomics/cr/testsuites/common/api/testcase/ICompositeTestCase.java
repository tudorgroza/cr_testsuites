package au.org.garvan.phenomics.cr.testsuites.common.api.testcase;

import java.util.List;
import java.util.Map;

public interface ICompositeTestCase extends ITestCase {

	public Map<String, List<ITestCaseResult>> retrieveTestCases();
	
}
