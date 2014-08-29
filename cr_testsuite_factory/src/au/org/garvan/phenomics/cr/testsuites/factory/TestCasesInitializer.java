package au.org.garvan.phenomics.cr.testsuites.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ICompositeTestCase;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ISimpleTestCase;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestCase;
import au.org.garvan.phenomics.cr.testsuites.log.TestSuitesLogger;
import au.org.garvan.phenomics.cr.testsuites.util.TSFactoryProperties;

public class TestCasesInitializer {

	@SuppressWarnings("unchecked")
	private static TestSuitesLogger<TestCasesInitializer> logger = (TestSuitesLogger<TestCasesInitializer>) TestSuitesLogger
			.getLogger(TestCasesInitializer.class);

	private Map<String, ITestCase> testCases;

	public TestCasesInitializer(Properties properties) {
		this.testCases = new HashMap<String, ITestCase>();

		loadTestCases(properties);
	}

	private void loadTestCases(Properties properties) {
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
		if (!map.containsKey(TSFactoryProperties.P_TYPE) && 
				!map.containsKey(TSFactoryProperties.P_CLASS) &&
				!map.containsKey(TSFactoryProperties.P_ID)) {
			return;
		}

		String type = map.get(TSFactoryProperties.P_TYPE);
		logger.info("Instantiating test case: " + map.get(TSFactoryProperties.P_ID) + " (type: " + map.get(TSFactoryProperties.P_TYPE) + "): " + map.get(TSFactoryProperties.P_CLASS));
		Properties properties = retrieveProperties(map);

		if (type.equalsIgnoreCase(TSFactoryProperties.V_SIMPLE)) {
			initSimple(map, properties);
		}

		if (type.equalsIgnoreCase(TSFactoryProperties.V_COMPOSITE)) {
			initComposite(map, properties);
		}
	}

	@SuppressWarnings("unchecked")
	private void initComposite(Map<String, String> map, Properties properties) {
		try {
			Class<ICompositeTestCase> candidateTestCaseCls = (Class<ICompositeTestCase>) Class
					.forName(map.get(TSFactoryProperties.P_CLASS));
			ICompositeTestCase compositeTestCase = null;
			compositeTestCase = candidateTestCaseCls.getConstructor(Properties.class).newInstance(properties);
			testCases.put(compositeTestCase.getId(), compositeTestCase);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			
			logger.error("Unable to instantiate test case: " + map.get(TSFactoryProperties.P_ID) + " ...");
			logger.error(e.getMessage());
		}
		
		logger.info("Test cases: " + map.get(TSFactoryProperties.P_ID) + " successfully instatiated ...");
	}

	@SuppressWarnings("unchecked")
	private void initSimple(Map<String, String> map, Properties properties) {
		try {
			Class<ISimpleTestCase> candidateTestCaseCls = (Class<ISimpleTestCase>) Class
					.forName(map.get(TSFactoryProperties.P_CLASS));
			ISimpleTestCase simpleTestCase = null;
			simpleTestCase = candidateTestCaseCls.getConstructor(Properties.class).newInstance(properties);
			testCases.put(simpleTestCase.getId(), simpleTestCase);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			
			logger.error("Unable to instantiate test case: " + map.get(TSFactoryProperties.P_ID) + " ...");
			logger.error(e.getMessage());
		}
		
		logger.info("Test cases: " + map.get(TSFactoryProperties.P_ID) + " successfully instatiated ...");
	}

	private Properties retrieveProperties(Map<String, String> map) {
		Properties properties = new Properties();
		
		for (String key : map.keySet()) {
			String value = map.get(key);
			if (key.startsWith(TSFactoryProperties.P_PROPERTY)) {
				int index = key.indexOf("[");
				String tmp = key.substring(index + 1).trim();
				index = tmp.indexOf("]");
				String actualProperty = tmp.substring(0, index).trim();
				properties.put(actualProperty, value);
			}
		}
		
		return properties;
	}

	public Map<String, ITestCase> getTestCases() {
		return testCases;
	}
}
