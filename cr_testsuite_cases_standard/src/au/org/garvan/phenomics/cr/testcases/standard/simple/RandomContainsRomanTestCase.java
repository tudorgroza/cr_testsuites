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

public class RandomContainsRomanTestCase implements ISimpleTestCase {

	private final static int DEFAULT_NO_ENTRIES = 5;
	
	private Properties defaultProperties;
	
	private List<ITestCaseConcept> testCaseResult;
	private List<ITestCaseConcept> testCases;

	public RandomContainsRomanTestCase(Properties properties) {
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
				if (StringUtils.isAllUpperCase(token)) {
					try {
						toArabic(token);
						TestCaseConcept result = new TestCaseConcept();
						result.setOriginalLabel(label);
						result.setTextualGrouding(label);
						result.setUri(profile.getUri());
						testCases.add(result);
						found = true;
						
						break;
						
					} catch (Exception e) {
						// Not good
					}
					
				}
			}
			
			if (found) {
				break;
			}
		}
	}

	private int toArabic(String number) throws Exception {
        if (number.equalsIgnoreCase("")) return 0;
        if (number.startsWith("M")) return 1000 + toArabic(number.substring(1));
        if (number.startsWith("CM")) return 900 + toArabic(number.substring(2));
        if (number.startsWith("D")) return 500 + toArabic(number.substring(1));
        if (number.startsWith("CD")) return 400 + toArabic(number.substring(2));
        if (number.startsWith("C")) return 100 + toArabic(number.substring(1));
        if (number.startsWith("XC")) return 90 + toArabic(number.substring(2));
        if (number.startsWith("L")) return 50 + toArabic(number.substring(1));
        if (number.startsWith("XL")) return 40 + toArabic(number.substring(2));
        if (number.startsWith("X")) return 10 + toArabic(number.substring(1));
        if (number.startsWith("IX")) return 9 + toArabic(number.substring(2));
        if (number.startsWith("V")) return 5 + toArabic(number.substring(1));
        if (number.startsWith("IV")) return 4 + toArabic(number.substring(2));
        if (number.startsWith("I")) return 1 + toArabic(number.substring(1));
        throw new Exception("Something's wrong");
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
		return "Contains-Roman";
	}

	@Override
	public String getName() {
		return "Contains numberals (Roman) - Random";
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
