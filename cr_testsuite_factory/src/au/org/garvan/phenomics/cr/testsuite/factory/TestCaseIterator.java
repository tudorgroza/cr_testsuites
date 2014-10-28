package au.org.garvan.phenomics.cr.testsuite.factory;

import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ICompositeTestCase;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ISimpleTestCase;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestCase;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestCaseIterator;

public class TestCaseIterator implements ITestCaseIterator {

	private Map<Integer, ISimpleTestCase> simpleTestCases;
	private Map<Integer, ICompositeTestCase> compositeTestCases;

	private Map<ISimpleTestCase, Properties> simpleTestCaseProperties;
	private Map<ICompositeTestCase, Map<String, Properties>> compositeTestCaseProperties;
	
	private int current;
	private int total;
	
	public TestCaseIterator(Map<Integer, ISimpleTestCase> simpleTestCases,
			Map<Integer, ICompositeTestCase> compositeTestCases,
			Map<ISimpleTestCase, Properties> simpleTestCaseProperties,
			Map<ICompositeTestCase, Map<String, Properties>> compositeTestCaseProperties) {
		this.simpleTestCases = simpleTestCases;
		this.compositeTestCases = compositeTestCases;

		this.simpleTestCaseProperties = simpleTestCaseProperties;
		this.compositeTestCaseProperties = compositeTestCaseProperties;

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
	public Properties getPropertiesForSimpleTestCase(ISimpleTestCase testCase) {
		return simpleTestCaseProperties.get(testCase);
	}

	@Override
	public Map<String, Properties> getPropertiesForCompositeTestCase(ICompositeTestCase testCase) {
		return compositeTestCaseProperties.get(testCase);
	}

	@Override
	public void remove() {
		//Not implemented
	}

}
