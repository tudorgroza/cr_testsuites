package au.org.garvan.phenomics.cr.testsuite.main;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.analytics.TestSuiteAnalyticsFactory;
import au.org.garvan.phenomics.cr.testsuite.common.api.analytics.ITestSuiteAnalyticsResults;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.ITestSuiteDeserializer;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.SystemSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.TestCaseSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.util.CommonUtil;
import au.org.garvan.phenomics.cr.testsuite.deserializer.DeserializerNotFoundException;
import au.org.garvan.phenomics.cr.testsuite.deserializer.TestSuiteDeserializerFactory;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;

public class TestSuiteAnalytics {

	private TestSuiteDeserializerFactory tsDeserializerFactory;
	private TestSuiteAnalyticsFactory tsAnalyticsFactory;
	private String mainPath;

	private Properties properties;

	public TestSuiteAnalytics(String mainProperties) {
		mainPath = "";
		loadProperties(mainProperties);

		tsDeserializerFactory = new TestSuiteDeserializerFactory(
				CommonUtil.checkAndRelocateFolder(mainPath,
						properties.getProperty(TestProperties.P_DESERIALIZER),
						CommonUtil.PATH_PATTERN));
		tsDeserializerFactory.loadResources();
		
		tsAnalyticsFactory = new TestSuiteAnalyticsFactory(
				CommonUtil.checkAndRelocateFolder(mainPath,
						properties.getProperty(TestProperties.P_ANALYTICS),
						CommonUtil.PATH_PATTERN));
		tsAnalyticsFactory.loadResources();

		run();
	}

	private void loadProperties(String mainProperties) {
		properties = new Properties();
		try {
			properties.load(new FileReader(mainProperties));
			if (properties.containsKey(TestProperties.P_LOGLEVEL)) {
				TestSuiteLogger.setUpLogger(properties
						.getProperty(TestProperties.P_LOGLEVEL));
			}
			if (properties.containsKey(TestProperties.P_MAINPATH)) {
				mainPath = properties.getProperty(TestProperties.P_MAINPATH);
				if (!mainPath.endsWith("/")) {
					mainPath += "/";
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void run() {
		try {
			ITestSuiteDeserializer deserializer = tsDeserializerFactory.getDeserializer(properties.getProperty(TestProperties.P_ANALYTICS_TS_DESERIALIZER_NAME));
			Properties deserializerProperties = parseDeserializerProperties(TestProperties.P_ANALYTICS_TS_DESERIALIZER_PROP);
			Map<String, Map<String, TestCaseSerializedEntry>> testSuite = deserializer.deserializeTestSuite(deserializerProperties);

			deserializer = tsDeserializerFactory.getDeserializer(properties.getProperty(TestProperties.P_ANALYTICS_SYS_DESERIALIZER_NAME));
			deserializerProperties = parseDeserializerProperties(TestProperties.P_ANALYTICS_SYS_DESERIALIZER_PROP);
			Map<String, Map<String, SystemSerializedEntry>> systemData = deserializer.deserializeSystemAnnotations(deserializerProperties);

			List<String> analyticsRuns = parseAnalyticsRuns();
			for (String run : analyticsRuns) {
				ITestSuiteAnalyticsResults results = tsAnalyticsFactory.getAnalyticsEntry(run).compute(testSuite, systemData);
				
				System.out.println(" ==== " + run + " ==== ");
				System.out.println(" - Overall: ");
				for (String key : results.getOverallResults().keySet()) {
					System.out.println(" -- " + key + ": " + results.getOverallResults().get(key));
				}

				System.out.println("-----------------------");
				System.out.println(" - Structured: ");
				Map<String, Map<String, Number>> structuredResults = results.getTestCaseBasedResults();
				for (String struct : structuredResults.keySet()) {
					System.out.println(" -- " + struct);
					Map<String, Number> resMap = structuredResults.get(struct);
					for (String key : resMap.keySet()) {
						System.out.println(" --- " + key + ": " + resMap.get(key));
					}
				}
				System.out.println(" =======================");
			}
		} catch (DeserializerNotFoundException e) {
			e.printStackTrace();
		}
	}

	private List<String> parseAnalyticsRuns() {
		Map<Integer, String> runs = new HashMap<Integer, String>();
		List<Integer> runOrder = new ArrayList<Integer>();
		
		for (Object key : properties.keySet()) {
			String propName = (String) key;
			if (propName.startsWith(TestProperties.P_ANALYTICS_TS_RUN)) {
				int index = propName.indexOf("[");
				int index2 = propName.indexOf("]");
				
				String order = propName.substring(index + 1, index2).trim();
				int orderVal = Integer.parseInt(order);
				runs.put(orderVal, properties.getProperty(propName));
				runOrder.add(orderVal);
			}
		}
		

		List<String> list = new ArrayList<String>();
		Collections.sort(runOrder);
		for (int order : runOrder) {
			list.add(runs.get(order));
		}
		
		return list;
	}

	private Properties parseDeserializerProperties(String propHeader) {
		Properties serializerProperties = new Properties();
		
		for (Object key : properties.keySet()) {
			String propName = (String) key;
			if (propName.startsWith(propHeader)) {
				int index = propName.indexOf("[");
				int index2 = propName.indexOf("]");
				
				String actualProperty = propName.substring(index + 1, index2).trim();
				
				serializerProperties.put(actualProperty, CommonUtil.checkAndRelocateFolder(mainPath, properties.getProperty(propName), CommonUtil.PATH_PATTERN));
			}
		}
		return serializerProperties;
	}

	public static void main(String[] args) {
		TestSuiteLogger.setUpLogger("INFO");
		new TestSuiteAnalytics(args[0]);
	}

}
