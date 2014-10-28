package au.org.garvan.phenomics.cr.testsuite.factory;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.IEntityIterator;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ICompositeTestCase;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ISimpleTestCase;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestCase;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;

public class TestSuiteFactory {

	@SuppressWarnings("unchecked")
	private static TestSuiteLogger<TestSuiteFactory> logger = (TestSuiteLogger<TestSuiteFactory>) TestSuiteLogger
			.getLogger(TestSuiteFactory.class);

	private Properties properties;
	private String propertiesFile;
	
	private Map<String, ITestCase> testCases;
	
	public TestSuiteFactory(String propertiesFile) {
		this.propertiesFile = propertiesFile;
		properties = new Properties();
		testCases = new HashMap<String, ITestCase>();
	}
	
	public boolean loadResources() {
		logger.info("Loading resources ...");
		try {
			properties.load(new FileReader(propertiesFile));
			TestCasesInitializer initializer = new TestCasesInitializer(properties);
			testCases = initializer.getTestCases();
			
			logger.info("Test cases initialized ...");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Map<String, ITestCase> getAllTestCases() {
		return testCases;
	}
	
	public Map<String, ISimpleTestCase> getSimpleTestCases() {
		Map<String, ISimpleTestCase> map = new LinkedHashMap<String, ISimpleTestCase>();
		
		for (String testCaseId : testCases.keySet()) {
			if (testCases.get(testCaseId) instanceof ISimpleTestCase) {
				map.put(testCaseId, (ISimpleTestCase) testCases.get(testCaseId));
			}
		}
		
		return map;
	}

	public Map<String, ICompositeTestCase> getCompositeTestCases() {
		Map<String, ICompositeTestCase> map = new LinkedHashMap<String, ICompositeTestCase>();

		for (String testCaseId : testCases.keySet()) {
			if (testCases.get(testCaseId) instanceof ICompositeTestCase) {
				map.put(testCaseId, (ICompositeTestCase) testCases.get(testCaseId));
			}
		}

		return map;
	}

	public TestSuite generateTestSuite(TestSuiteDefinition tsDefinition, IEntityIterator entityIterator) {
		return new TestSuiteGenerator(tsDefinition, entityIterator).getTestSuite();
	}
	
	public static void main(String[] args) {
		TestSuiteFactory tsf = new TestSuiteFactory("/home/tudor/EXTRA_SPACE/NEW_TestSuites/ts_factory.properties");
		tsf.loadResources();
	}
}
