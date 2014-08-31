package au.org.garvan.phenomics.cr.testsuites.factory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ICompositeTestCase;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ISimpleTestCase;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestCase;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestCaseIterator;

public class TestSuiteDefinition {

	private Map<Integer, ISimpleTestCase> simpleTestCases;
	private Map<Integer, ICompositeTestCase> compositeTestCases;
	private Map<ITestCase, Properties> testCaseProperties;
	private int curentCount;

	public TestSuiteDefinition() {
		curentCount = 0;
		simpleTestCases = new LinkedHashMap<Integer, ISimpleTestCase>();
		compositeTestCases = new LinkedHashMap<Integer, ICompositeTestCase>();
		testCaseProperties = new LinkedHashMap<ITestCase, Properties>();
	}
	
	public void addTestCase(ITestCase testCase, Properties properties) {
		if (testCase instanceof ISimpleTestCase) {
			curentCount++;
			simpleTestCases.put(curentCount, (ISimpleTestCase) testCase);
		}
		if (testCase instanceof ICompositeTestCase) {
			curentCount++;
			compositeTestCases.put(curentCount, (ICompositeTestCase) testCase);
		}

		testCaseProperties.put(testCase, properties);
	}

	public ITestCaseIterator iterator() {
		return new TestCaseIterator(simpleTestCases, compositeTestCases,
				testCaseProperties);
	}
}
