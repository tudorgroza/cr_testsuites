package au.org.garvan.phenomics.cr.testsuites.factory;

import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ICompositeTestCase;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ISimpleTestCase;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestCase;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestCaseIterator;

public class TestCaseIterator implements ITestCaseIterator {

	private Map<Integer, ISimpleTestCase> simpleTestCases;
	private Map<Integer, ICompositeTestCase> compositeTestCases;

	private Map<ITestCase, Properties> testCaseProperties;
	
	private int current;
	private int total;
	
	public TestCaseIterator(Map<Integer, ISimpleTestCase> simpleTestCases,
			Map<Integer, ICompositeTestCase> compositeTestCases,
			Map<ITestCase, Properties> testCaseProperties) {
		this.simpleTestCases = simpleTestCases;
		this.compositeTestCases = compositeTestCases;

		this.testCaseProperties = testCaseProperties;

		current = 1;
		total = simpleTestCases.size() + compositeTestCases.size();
	}

	@Override
	public boolean hasNext() {
		return current <= total;
	}

	@Override
	public ITestCase next() {
		ITestCase testCase = null;
		if (simpleTestCases.containsKey(current)) {
			testCase = simpleTestCases.get(current);
		} else {
			testCase = compositeTestCases.get(current);
		}
		current++;
		return testCase;
	}

	@Override
	public Properties getProperties(ITestCase testCase) {
		return testCaseProperties.get(testCase);
	}
	
	@Override
	public void remove() {
		//Not implemented
	}

}
