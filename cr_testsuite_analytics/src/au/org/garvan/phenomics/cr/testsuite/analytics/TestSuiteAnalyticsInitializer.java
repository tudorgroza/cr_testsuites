package au.org.garvan.phenomics.cr.testsuite.analytics;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.analytics.util.TSAnalyticsProperties;
import au.org.garvan.phenomics.cr.testsuite.common.api.analytics.ITestSuiteAnalyticsEntry;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;

public class TestSuiteAnalyticsInitializer {

	@SuppressWarnings("unchecked")
	private static TestSuiteLogger<TestSuiteAnalyticsInitializer> logger = (TestSuiteLogger<TestSuiteAnalyticsInitializer>) TestSuiteLogger
			.getLogger(TestSuiteAnalyticsInitializer.class);

	private Map<String, ITestSuiteAnalyticsEntry> analyticsEntries;

	public TestSuiteAnalyticsInitializer(Properties properties) {
		this.analyticsEntries = new HashMap<String, ITestSuiteAnalyticsEntry>();

		loadAnalyticsEntries(properties);
	}

	private void loadAnalyticsEntries(Properties properties) {
		Map<Integer, Map<String, String>> propValues = new HashMap<Integer, Map<String,String>>();
		
		for (Object key : properties.keySet()) {
			String propName = (String) key;
			
			int index = propName.indexOf("[");
			int index2 = propName.indexOf("]");
			
			int idx = Integer.parseInt(propName.substring(index + 1, index2));
			String actualProperty = propName.substring(index2 + 2);
			
			Map<String, String> map = propValues.containsKey(idx) ? propValues.get(idx) : new HashMap<String, String>();
			map.put(actualProperty, properties.getProperty(propName));
			propValues.put(idx, map);
		}
		
		for (int idx : propValues.keySet()) {
			instatiateTestCase(propValues.get(idx));
		}
	}

	private void instatiateTestCase(Map<String, String> map) {
		if (!map.containsKey(TSAnalyticsProperties.P_TITLE) && 
				!map.containsKey(TSAnalyticsProperties.P_CLASS)) {
			return;
		}

		logger.info("Instantiating analytics: " + map.get(TSAnalyticsProperties.P_TITLE) + " ...");
		Properties properties = retrieveProperties(map);
		init(map, properties);
	}

	@SuppressWarnings("unchecked")
	private void init(Map<String, String> map, Properties properties) {
		try {
			Class<ITestSuiteAnalyticsEntry> analyticsEntryCls = (Class<ITestSuiteAnalyticsEntry>) Class
					.forName(map.get(TSAnalyticsProperties.P_CLASS));
			ITestSuiteAnalyticsEntry analyticsEntry = null;
			analyticsEntry = analyticsEntryCls.getConstructor(Properties.class).newInstance(properties);
			analyticsEntries.put(analyticsEntry.getName(), analyticsEntry);

			logger.info("Test analytics: " + analyticsEntry.getName() + " successfully instatiated ...");
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			
			logger.error("Unable to instantiate analytics: " + map.get(TSAnalyticsProperties.P_TITLE) + " ...");
			logger.error(e.getMessage());
		}
	}

	private Properties retrieveProperties(Map<String, String> map) {
		Properties properties = new Properties();
		
		for (String key : map.keySet()) {
			String value = map.get(key);
			if (key.startsWith(TSAnalyticsProperties.P_PROPERTY)) {
				int index = key.indexOf("[");
				String tmp = key.substring(index + 1).trim();
				index = tmp.indexOf("]");
				String actualProperty = tmp.substring(0, index).trim();
				properties.put(actualProperty, value);
			}
		}
		
		return properties;
	}

	public Map<String, ITestSuiteAnalyticsEntry> getEntries() {
		return analyticsEntries;
	}

}
