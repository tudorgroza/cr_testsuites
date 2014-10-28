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

public class RandomContainsStopWord implements ICompositeTestCase {

	private final static String ID = "Contains-STOP";
	private final static int DEFAULT_NO_ENTRIES = 5;

	private Map<String, Properties> defaultProperties;
	
	private Map<String, List<ITestCaseConcept>> testCaseResult;
	private Map<String, Map<String, List<ITestCaseConcept>>> testCases;
	
	public RandomContainsStopWord(Properties properties) {
		defaultProperties = new LinkedHashMap<String, Properties>();
		createDefaultProperties();
		
		testCaseResult = new HashMap<String, List<ITestCaseConcept>>();
		testCases = new HashMap<String, Map<String, List<ITestCaseConcept>>>();
	}

	private void createDefaultProperties() {
		Properties properties = new Properties();
		properties.put(TestCasesStandardProperties.NO_ENTRIES, DEFAULT_NO_ENTRIES);
		properties.put(TestCasesStandardProperties.STOP_WORD, "of");
		defaultProperties.put("of", properties);

		properties = new Properties();
		properties.put(TestCasesStandardProperties.NO_ENTRIES, DEFAULT_NO_ENTRIES);
		properties.put(TestCasesStandardProperties.STOP_WORD, "to");
		defaultProperties.put("to", properties);

		properties = new Properties();
		properties.put(TestCasesStandardProperties.NO_ENTRIES, DEFAULT_NO_ENTRIES);
		properties.put(TestCasesStandardProperties.STOP_WORD, "by");
		defaultProperties.put("by", properties);

		properties = new Properties();
		properties.put(TestCasesStandardProperties.NO_ENTRIES, DEFAULT_NO_ENTRIES);
		properties.put(TestCasesStandardProperties.STOP_WORD, "from");
		defaultProperties.put("from", properties);
	}

	@Override
	public String getId() {
		return "Contains-STOP";
	}

	@Override
	public String getName() {
		return "Contains STOP words - Random";
	}

	@Override
	public List<String> getAcceptedProperties() {
		List<String> list = new ArrayList<String>();
		list.add(TestCasesStandardProperties.STOP_WORD);
		list.add(TestCasesStandardProperties.NO_ENTRIES);
		return list;
	}

	@Override
	public void addEntity(IEntityProfile profile) {
		for (String label : profile.getLabels()) {
			String[] tokens = label.split(" ");
			for (String token : tokens) {
				token = token.trim().toLowerCase();
				if (!token.equalsIgnoreCase("")) {
					Map<String, List<ITestCaseConcept>> map = testCases.containsKey(token) ? testCases.get(token) : new HashMap<String, List<ITestCaseConcept>>();
					List<ITestCaseConcept> list = map.containsKey(profile.getUri()) ? map.get(profile.getUri()) : new ArrayList<ITestCaseConcept>();
					
					TestCaseConcept result = new TestCaseConcept();
					result.setOriginalLabel(label);
					result.setTextualGrouding(label);
					result.setUri(profile.getUri());
					list.add(result);
					
					map.put(profile.getUri(), list);
					testCases.put(token, map);
				}
			}
		}
	}

	@Override
	public void runTestCases(Map<String, Properties> properties) {
		for (String subTestCase : properties.keySet()) {
			Properties prop = properties.get(subTestCase);
			runSubTestCase(subTestCase, prop);
		}
	}

	private void runSubTestCase(String subTestCase, Properties prop) {
		String stopWord = prop.getProperty(TestCasesStandardProperties.STOP_WORD);
		int noEntries = prop.containsKey(TestCasesStandardProperties.NO_ENTRIES) ? Integer.parseInt(prop.getProperty(TestCasesStandardProperties.NO_ENTRIES)) : DEFAULT_NO_ENTRIES;
		
		if (stopWord == null) {
			return;
		}
		if (!testCases.containsKey(stopWord.toLowerCase())) {
			return;
		}

		Map<String, List<ITestCaseConcept>> map = testCases.get(stopWord.toLowerCase());
		
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
	public void reset() {
		testCases = new HashMap<String, Map<String, List<ITestCaseConcept>>>();
		testCaseResult = new HashMap<String, List<ITestCaseConcept>>();
	}

	@Override
	public Map<String, List<ITestCaseConcept>> retrieveTestCases() {
		return testCaseResult;
	}

}
