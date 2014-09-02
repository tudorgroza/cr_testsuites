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
	
	private Map<ISimpleTestCase, Properties> simpleTestCaseProperties;
	private Map<ICompositeTestCase, Map<String, Properties>> compositeTestCaseProperties;
	private int curentCount;

	public TestSuiteDefinition() {
		curentCount = 0;
		simpleTestCases = new LinkedHashMap<Integer, ISimpleTestCase>();
		compositeTestCases = new LinkedHashMap<Integer, ICompositeTestCase>();
		simpleTestCaseProperties = new LinkedHashMap<ISimpleTestCase, Properties>();
		compositeTestCaseProperties = new LinkedHashMap<ICompositeTestCase, Map<String,Properties>>();
	}

	public void addSimpleTestCase(ITestCase testCase, Properties properties) {
		curentCount++;
		simpleTestCases.put(curentCount, (ISimpleTestCase) testCase);
		simpleTestCaseProperties.put((ISimpleTestCase) testCase, properties);
	}
	
	public void addCompositeTestCase(ITestCase testCase, Map<String, Properties> propertyMap) {
		curentCount++;
		compositeTestCases.put(curentCount, (ICompositeTestCase) testCase);
		compositeTestCaseProperties.put((ICompositeTestCase) testCase, propertyMap);
	}

	public ITestCaseIterator iterator() {
		return new TestCaseIterator(simpleTestCases, compositeTestCases,
				simpleTestCaseProperties, compositeTestCaseProperties);
	}
}
