package au.org.garvan.phenomics.cr.testsuite.factory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.IEntityIterator;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.IEntityProfile;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ICompositeTestCase;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ISimpleTestCase;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestCase;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestCaseIterator;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestCaseConcept;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;

public class TestSuiteGenerator {

	@SuppressWarnings("unchecked")
	private static TestSuiteLogger<TestSuiteGenerator> logger = (TestSuiteLogger<TestSuiteGenerator>) TestSuiteLogger
			.getLogger(TestSuiteGenerator.class);

	private TestSuiteDefinition tsDefition;
	private IEntityIterator entityIterator;
	private TestSuite testSuite;
	
	public TestSuiteGenerator(TestSuiteDefinition tsDefition,
			IEntityIterator entityIterator) {
		this.tsDefition = tsDefition;
		this.entityIterator = entityIterator;
		
		testSuite = new TestSuite();

		logger.info("New test suite generator instatiated ...");
		logger.info("Test cases received:");

		ITestCaseIterator tcIterator = tsDefition.iterator();
		while (tcIterator.hasNext()) {
			ITestCase testCase = tcIterator.next();
			testCase.reset();
			logger.info(testCase.getName() + " (" + testCase.getId() + ")");
		}
		
		generate();
	}

	private void generate() {
		logger.info("Generating test cases ...");
		logger.info("Iterating over entity profiles ...");
		while(entityIterator.hasNext()) {
			IEntityProfile profile = entityIterator.next();

			ITestCaseIterator tcIterator = tsDefition.iterator();
			while (tcIterator.hasNext()) {
				ITestCase testCase = tcIterator.next();
				testCase.addEntity(profile);
			}
		}
		logger.info("Done iterating over entity profiles ...");
		
		logger.info("Creating test cases ...");
		ITestCaseIterator tcIterator = tsDefition.iterator();
		while (tcIterator.hasNext()) {
			ITestCase testCase = tcIterator.next();
			if (testCase instanceof ICompositeTestCase) {
				ICompositeTestCase compTestCase = (ICompositeTestCase) testCase;
				Map<String, Properties> properties = tcIterator.getPropertiesForCompositeTestCase(compTestCase);
				compTestCase.runTestCases(properties);
				Map<String, List<ITestCaseConcept>> map = compTestCase.retrieveTestCases();
				testSuite.addResult(testCase.getId(), map);
			} else {
				ISimpleTestCase simpleTestCase = (ISimpleTestCase) testCase;
				Properties properties = tcIterator.getPropertiesForSimpleTestCase(simpleTestCase);
				simpleTestCase.runTestCases(properties);
				List<ITestCaseConcept> result = simpleTestCase.retrieveTestCases();
				testSuite.addResult(testCase.getId(), result);
			}
		}		
		logger.info("Done creating test cases ...");
	}

	public TestSuite getTestSuite() {
		return testSuite;
	}

}
