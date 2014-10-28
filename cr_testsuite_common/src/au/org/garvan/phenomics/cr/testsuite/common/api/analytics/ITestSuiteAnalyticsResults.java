package au.org.garvan.phenomics.cr.testsuite.common.api.analytics;

import java.util.Map;

public interface ITestSuiteAnalyticsResults {

	public Map<String, Number> getOverallResults();
	
	public Map<String, Map<String, Number>> getTestCaseBasedResults();

}
