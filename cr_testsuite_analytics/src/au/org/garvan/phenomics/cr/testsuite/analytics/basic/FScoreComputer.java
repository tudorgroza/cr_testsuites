package au.org.garvan.phenomics.cr.testsuite.analytics.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.analytics.util.TestSuiteAnalyticsUtil;
import au.org.garvan.phenomics.cr.testsuite.common.api.analytics.ITestSuiteAnalyticsResults;
import au.org.garvan.phenomics.cr.testsuite.common.api.analytics.impl.TestSuiteAnalyticsResults;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.Offset;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.SystemSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.TestCaseSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.impl.TestCaseConcept;

public class FScoreComputer {

	private TestSuiteAnalyticsResults analyticsResults;
	private Map<String, Map<String, TestCaseSerializedEntry>> testSuite;

	private Map<String, Integer> typeTotal;
	private Map<String, Integer> typeFound;
	private Map<String, Integer> typeCorrect;
	
	private int total;
	private int found;
	private int correct;
	
	private boolean longestMatch;
	
	public FScoreComputer(
			Map<String, Map<String, TestCaseSerializedEntry>> testSuite,
			Map<String, Map<String, SystemSerializedEntry>> systemAnnotations,
			Properties properties) {
		this.longestMatch = properties.containsKey(TestSuiteAnalyticsUtil.LONGEST_MATCH) ? 
				Boolean.parseBoolean(properties.getProperty(TestSuiteAnalyticsUtil.LONGEST_MATCH)) : false;
		this.testSuite = testSuite;
		analyticsResults = new TestSuiteAnalyticsResults();
		
		typeTotal = new HashMap<String, Integer>();
		typeFound = new HashMap<String, Integer>();
		typeCorrect = new HashMap<String, Integer>();
		
		initTypes();
		
		total = 0;
		found = 0;
		correct = 0;

		for (String type : testSuite.keySet()) {
			Map<String, TestCaseSerializedEntry> testCases = testSuite.get(type);
			Map<String, SystemSerializedEntry> sysEntries = systemAnnotations.get(type);
			
			total += testCases.size();
			
			compare(type, testCases, sysEntries);
		}
		
		addResultsToAnalytics();
		computeAndAddTypesToAnalytics();
	}

	private void compare(String type, Map<String, TestCaseSerializedEntry> testCases,
			Map<String, SystemSerializedEntry> sysEntries) {
		int localFound = 0;
		Map<String, Map<Offset, TestCaseConcept>> longestConcepts = new HashMap<String, Map<Offset,TestCaseConcept>>();
		
		for (String annotId : sysEntries.keySet()) {
			Map<Offset, List<TestCaseConcept>> map = sysEntries.get(annotId).getTestCaseConcepts();
			
			for (Offset offset : map.keySet()) {
				if (longestMatch) {
					TestCaseConcept tcc = longestMatch(map.get(offset));
					Map<Offset, TestCaseConcept> longMap = longestConcepts.containsKey(annotId) ? longestConcepts.get(annotId) : new HashMap<Offset, TestCaseConcept>();
					longMap.put(offset, tcc);
					longestConcepts.put(annotId, longMap);
					found++;
					localFound++;
				} else {
					found += map.get(offset).size();
					localFound += map.get(offset).size();
				}
			}
		}
		typeFound.put(type, localFound);
		
		int localCorrect = 0;
		for (String annotId : testCases.keySet()) {
			if (sysEntries.containsKey(annotId)) {
				TestCaseSerializedEntry tcEntry = testCases.get(annotId);
				
				if (longestMatch) {
					Map<Offset, TestCaseConcept> longMap = longestConcepts.get(annotId);
					for (Offset o : longMap.keySet()) {
						String uri = "http://purl.obolibrary.org/obo/" + longMap.get(o).getUri();
						if (uri.equalsIgnoreCase(tcEntry.getTestCaseConcept().getUri())) {
							localCorrect++;
							correct++;
							break;
						}						
					}					
				} else {
					SystemSerializedEntry sEntry = sysEntries.get(annotId);

					for (Offset o : sEntry.getTestCaseConcepts().keySet()) {
						List<TestCaseConcept> list = sEntry.getTestCaseConcepts().get(o);
						
						boolean found = false;
						for (TestCaseConcept concept : list) {
							String uri = "http://purl.obolibrary.org/obo/" + concept.getUri();
							if (uri.equalsIgnoreCase(tcEntry.getTestCaseConcept().getUri())) {
								localCorrect++;
								correct++;

								found = true;
								break;
							}
						}
						
						if (found) {
							break;
						}
					}
				}
			}
		}
		
		typeCorrect.put(type, localCorrect);
	}

	private TestCaseConcept longestMatch(List<TestCaseConcept> list) {
		TestCaseConcept longest = list.get(0);

		if (list.size() > 1) {
			for (int i = 1; i < list.size(); i++) {
				TestCaseConcept concept = list.get(i);
				if (concept.getTextualGrouding().length() > longest.getTextualGrouding().length()) {
					longest = concept;
				}
			}
		}
		
		return longest;
	}
	
	private void initTypes() {
		for (String type : testSuite.keySet()) {
			typeTotal.put(type, testSuite.get(type).size());
			typeFound.put(type, 0);
			typeCorrect.put(type, 0);
		}
		
	}

	private void addResultsToAnalytics() {
		double precision = 0.0;
		double recall = (double) correct / total;
		double f1 = 0.0;
		
		if (found != 0) {
			precision = (double) correct / found;
		}
		
		if (precision != 0 && recall != 0) {
			f1 = (2 * precision * recall) / (precision + recall);
		}
		
		analyticsResults.addOverallResult("Precision", precision);
		analyticsResults.addOverallResult("Recall", recall);
		analyticsResults.addOverallResult("F-Score", f1);
	}

	private void computeAndAddTypesToAnalytics() {
		for (String type : testSuite.keySet()) {
			int typeT = typeTotal.get(type);
			int typeF = typeFound.get(type);
			int typeC = typeCorrect.get(type);
			
			Map<String, Number> values = new HashMap<String, Number>();
			double precision = 0.0;
			double recall = (double) typeC / typeT;
			double f1 = 0.0;
			
			if (typeF != 0) {
				precision = (double) typeC / typeF;
			}
			
			if (precision != 0 && recall != 0) {
				f1 = (2 * precision * recall) / (precision + recall);
			}
			
			values.put("Precision", precision);
			values.put("Recall", recall);
			values.put("F-Score", f1);
			
			analyticsResults.addTestCaseBasedResult(type, values);
		}
	}

	public ITestSuiteAnalyticsResults getAnalytics() {
		return analyticsResults;
	}

}
