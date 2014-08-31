package au.org.garvan.phenomics.cr.testsuites.factory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestCaseResult;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestSuite;

public class TestSuite implements ITestSuite {

	private Map<String, List<ITestCaseResult>> testCases;

	public TestSuite() {
		testCases = new LinkedHashMap<String, List<ITestCaseResult>>();
	}

	public void addResult(String testCaseId, List<ITestCaseResult> testCaseList) {
		testCases.put(testCaseId, testCaseList);
	}

	public void addResult(String testCaseId, Map<String, List<ITestCaseResult>> map) {
		for (String subTestCase : map.keySet()) {
			testCases.put(subTestCase, map.get(subTestCase));
		}
	}

	@Override
	public Map<String, List<ITestCaseResult>> getTestCases() {
		return testCases;
	}
}
