package au.org.garvan.phenomics.cr.testsuites.factory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestCaseResult;

public class TestSuite {

	private Map<String, List<ITestCaseResult>> testCases;

	public TestSuite() {
		testCases = new LinkedHashMap<String, List<ITestCaseResult>>();
	}

	public void addResult(List<ITestCaseResult> testCaseList) {
		for (ITestCaseResult testCaseResult : testCaseList) {
			List<ITestCaseResult> list = testCases.containsKey(testCaseResult
					.getTextualGrouding()) ? testCases.get(testCaseResult
					.getTextualGrouding()) : new ArrayList<ITestCaseResult>();
			list.add(testCaseResult);
			testCases.put(testCaseResult.getTextualGrouding(), list);
		}
	}

	public void addResult(Map<String, ITestCaseResult> map) {
		for (ITestCaseResult testCaseResult : map.values()) {
			List<ITestCaseResult> list = testCases.containsKey(testCaseResult
					.getTextualGrouding()) ? testCases.get(testCaseResult
					.getTextualGrouding()) : new ArrayList<ITestCaseResult>();
			list.add(testCaseResult);
			testCases.put(testCaseResult.getTextualGrouding(), list);
		}
	}

	public Map<String, List<ITestCaseResult>> getTestCases() {
		return testCases;
	}
}
