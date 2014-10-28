package au.org.garvan.phenomics.cr.testsuite.common.api.analytics.impl;

import java.util.HashMap;
import java.util.Map;

import au.org.garvan.phenomics.cr.testsuite.common.api.analytics.ITestSuiteAnalyticsResults;

public class TestSuiteAnalyticsResults implements ITestSuiteAnalyticsResults {

	private Map<String, Number> overallResults;
	private Map<String, Map<String, Number>> testCaseBasedResults;
	
	public TestSuiteAnalyticsResults() {
		overallResults = new HashMap<String, Number>();
		testCaseBasedResults = new HashMap<String, Map<String, Number>>();
	}
	
	public void addOverallResult(String name, Number value) {
		overallResults.put(name, value);
	}
	
	public void addTestCaseBasedResult(String name, Map<String, Number> results) {
		testCaseBasedResults.put(name, results);
	}
	
	@Override
	public Map<String, Number> getOverallResults() {
		return overallResults;
	}

	@Override
	public Map<String, Map<String, Number>> getTestCaseBasedResults() {
		return testCaseBasedResults;
	}

}
