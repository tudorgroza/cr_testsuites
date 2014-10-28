package au.org.garvan.phenomics.cr.testcases.standard.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.IEntityProfile;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ISimpleTestCase;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestCaseConcept;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.impl.TestCaseConcept;
import au.org.garvan.phenomics.cr.testsuite.common.util.TestCasesStandardProperties;

public class RandomContainsPunctuation implements ISimpleTestCase {

	private final static int DEFAULT_NO_ENTRIES = 5;
	
	private Properties defaultProperties;
	
	private List<ITestCaseConcept> testCaseResult;
	private List<ITestCaseConcept> testCases;

	public RandomContainsPunctuation(Properties properties) {
		defaultProperties = new Properties();
		defaultProperties.put(TestCasesStandardProperties.NO_ENTRIES, DEFAULT_NO_ENTRIES);

		testCaseResult = new ArrayList<ITestCaseConcept>();
		testCases = new ArrayList<ITestCaseConcept>();
	}

	@Override
	public void addEntity(IEntityProfile profile) {
		boolean found = false;
		
		for (String label : profile.getLabels()) {
			String[] tokens = label.split(" ");
			for (String token : tokens) {
				if (!StringUtils.isAlphanumeric(token)) {
					TestCaseConcept result = new TestCaseConcept();
					result.setOriginalLabel(label);
					result.setTextualGrouding(label);
					result.setUri(profile.getUri());
					testCases.add(result);
					found = true;
					
					break;
				}
			}
			
			if (found) {
				break;
			}
		}
	}

	@Override
	public void runTestCases(Properties properties) {
		int noEntries = DEFAULT_NO_ENTRIES;
		if (properties.containsKey(TestCasesStandardProperties.NO_ENTRIES)) {
			noEntries = Integer.parseInt(properties.getProperty(TestCasesStandardProperties.NO_ENTRIES));
			if (noEntries < 1) {
				noEntries = DEFAULT_NO_ENTRIES;
			}
		}
		
		Random random = new Random(testCases.size());
		for (int i = 0; i < noEntries; i++) {
			int nextRandom = random.nextInt(testCases.size());
			ITestCaseConcept testCase = testCases.get(nextRandom);
			testCaseResult.add(testCase);
		}
	}

	@Override
	public String getId() {
		return "Contains-Punctuation";
	}

	@Override
	public String getName() {
		return "Contains punctuation - Random";
	}

	@Override
	public List<String> getAcceptedProperties() {
		List<String> list = new ArrayList<String>();
		list.add(TestCasesStandardProperties.NO_ENTRIES);
		return list;
	}

	@Override
	public void reset() {
		testCases = new ArrayList<ITestCaseConcept>();
		testCaseResult = new ArrayList<ITestCaseConcept>();
	}

	@Override
	public List<ITestCaseConcept> retrieveTestCases() {
		return testCaseResult;
	}
}
