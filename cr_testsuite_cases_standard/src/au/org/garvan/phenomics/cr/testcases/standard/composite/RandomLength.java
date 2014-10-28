package au.org.garvan.phenomics.cr.testcases.standard.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.IEntityProfile;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ICompositeTestCase;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestCaseConcept;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.impl.TestCaseConcept;
import au.org.garvan.phenomics.cr.testsuite.common.util.TestCasesStandardProperties;

public class RandomLength implements ICompositeTestCase {

	private final static String ID = "Length";
	
	private final static int DEFAULT_NO_ENTRIES = 5;
	private final static String DEFAULT_ALL = "all";

	private Map<String, Properties> defaultProperties;
	
	private Map<String, List<ITestCaseConcept>> testCaseResult;
	private Map<String, Map<String, List<ITestCaseConcept>>> testCases;
	
	public RandomLength(Properties properties) {
		defaultProperties = new LinkedHashMap<String, Properties>();
		createDefaultProperties();
		
		testCaseResult = new HashMap<String, List<ITestCaseConcept>>();
		testCases = new HashMap<String, Map<String, List<ITestCaseConcept>>>();
	}

	private void createDefaultProperties() {
		Properties properties = new Properties();
		properties.put(TestCasesStandardProperties.NO_ENTRIES, DEFAULT_NO_ENTRIES);
		properties.put(TestCasesStandardProperties.LENGTH, DEFAULT_ALL);
		defaultProperties.put(DEFAULT_ALL, properties);
	}

	@Override
	public void addEntity(IEntityProfile profile) {
		for (String label : profile.getLabels()) {
			String[] tokens = label.split(" ");
			String length = Integer.toString(tokens.length);

			Map<String, List<ITestCaseConcept>> map = testCases.containsKey(length) ? testCases.get(length) : new HashMap<String, List<ITestCaseConcept>>();
			List<ITestCaseConcept> list = map.containsKey(profile.getUri()) ? map.get(profile.getUri()) : new ArrayList<ITestCaseConcept>();
			
			TestCaseConcept result = new TestCaseConcept();
			result.setOriginalLabel(label);
			result.setTextualGrouding(label);
			result.setUri(profile.getUri());
			list.add(result);
			map.put(profile.getUri(), list);
			testCases.put(length, map);
		}
	}

	@Override
	public void runTestCases(Map<String, Properties> properties) {
		for (String subTestCase : properties.keySet()) {
			if (subTestCase.equalsIgnoreCase(DEFAULT_ALL)) {
				runAll();
			} else {
				Properties prop = properties.get(subTestCase);
				runSubTestCase(subTestCase, prop);
			}
		}
	}

	private void runAll() {
		List<String> list = new ArrayList<String>();
		for (String len : testCases.keySet()) {
			list.add(len);
		}
		Collections.sort(list);
		
		for (String len : list) {
			Map<String, List<ITestCaseConcept>> map = testCases.get(len);

			List<ITestCaseConcept> testCaseList = new ArrayList<ITestCaseConcept>();
			for (String uri : map.keySet()) {
				List<ITestCaseConcept> tcList = map.get(uri);
				testCaseList.add(tcList.get(0));
			}
			testCaseResult.put(ID + "-" + len, testCaseList);
		}
	}

	private void runSubTestCase(String subTestCase, Properties prop) {
		String length = prop.getProperty(TestCasesStandardProperties.LENGTH);
		int noEntries = prop.containsKey(TestCasesStandardProperties.NO_ENTRIES) ? Integer.parseInt(prop.getProperty(TestCasesStandardProperties.NO_ENTRIES)) : DEFAULT_NO_ENTRIES;
		
		if (length == null) {
			return;
		}
		if (!testCases.containsKey(length.toLowerCase())) {
			return;
		}

		Map<String, List<ITestCaseConcept>> map = testCases.get(length.toLowerCase());
		
		if (noEntries < map.size()) {
			List<Integer> randomNumbers = generateRandom(noEntries, map.size());
			List<ITestCaseConcept> list = new ArrayList<ITestCaseConcept>();

			Iterator<String> uris = map.keySet().iterator();
			int count = 0;
			int left = randomNumbers.size();
			while (uris.hasNext()) {
				if (left == 0) {
					break;
				}
				String uri = uris.next();
				int current = randomNumbers.get(randomNumbers.size() - left);
				if (count == current) {
					List<ITestCaseConcept> testCaseList = map.get(uri);
					list.add(testCaseList.get(0));
					left--;
				}
				
				count++;
			}
			
			testCaseResult.put(ID + "-" + subTestCase, list);
		} else {
			List<ITestCaseConcept> list = new ArrayList<ITestCaseConcept>();
			for (String uri : map.keySet()) {
				List<ITestCaseConcept> testCaseList = map.get(uri);
				list.add(testCaseList.get(0));
			}
			testCaseResult.put(ID + "-" + subTestCase, list);
		}
	}

	private List<Integer> generateRandom(int noEntries, int n) {
		List<Integer> list = new ArrayList<Integer>();
		Random random = new Random(n);
		for (int i = 0; i < noEntries; i++) {
			int nextRandom = random.nextInt(n);
			boolean exists = list.contains(nextRandom);
			while (exists) {
				nextRandom = random.nextInt(n);
				exists = list.contains(nextRandom);
			}
			list.add(nextRandom);
		}
		Collections.sort(list);
		return list;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getName() {
		return "Length - Random";
	}

	@Override
	public List<String> getAcceptedProperties() {
		List<String> list = new ArrayList<String>();
		list.add(TestCasesStandardProperties.LENGTH);
		list.add(TestCasesStandardProperties.NO_ENTRIES);
		return list;
	}

	@Override
	public void reset() {
		testCases = new HashMap<String, Map<String, List<ITestCaseConcept>>>();
		testCaseResult = new HashMap<String, List<ITestCaseConcept>>();
	}

	@Override
	public Map<String, List<ITestCaseConcept>> retrieveTestCases() {
		return testCaseResult;
	}

}
