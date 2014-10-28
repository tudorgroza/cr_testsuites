package au.org.garvan.phenomics.cr.testsuite.factory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestCaseConcept;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestSuite;

public class TestSuite implements ITestSuite {

	private Map<String, List<ITestCaseConcept>> testCases;

	public TestSuite() {
		testCases = new LinkedHashMap<String, List<ITestCaseConcept>>();
	}

	public void addResult(String testCaseId, List<ITestCaseConcept> testCaseList) {
		testCases.put(testCaseId, testCaseList);
	}

	public void addResult(String testCaseId, Map<String, List<ITestCaseConcept>> map) {
		for (String subTestCase : map.keySet()) {
			testCases.put(subTestCase, map.get(subTestCase));
		}
	}

	@Override
	public Map<String, List<ITestCaseConcept>> getTestCases() {
		return testCases;
	}
}
