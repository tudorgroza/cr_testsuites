package au.org.garvan.phenomics.cr.testsuite.deserializer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.ITestSuiteDeserializer;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;
import au.org.garvan.phenomics.cr.testsuite.serializer.util.SerializerUtil;

public class DeserializerResourceLoader {

	@SuppressWarnings("unchecked")
	private static TestSuiteLogger<DeserializerResourceLoader> logger = (TestSuiteLogger<DeserializerResourceLoader>) TestSuiteLogger
			.getLogger(DeserializerResourceLoader.class);

	private Map<String, ITestSuiteDeserializer> deserializers;
	
	public DeserializerResourceLoader(Properties properties) {
		deserializers = new HashMap<String, ITestSuiteDeserializer>();
		loadDeserializers(properties);
	}

	private void loadDeserializers(Properties properties) {
		Map<Integer, Map<String, String>> propValues = new HashMap<Integer, Map<String,String>>();
		
		for (Object key : properties.keySet()) {
			String propName = (String) key;
			if (!propName.startsWith(SerializerUtil.P_DESERIALIZER)) {
				continue;
			}
			
			int index = propName.indexOf("[");
			int index2 = propName.indexOf("]");
			
			int idx = Integer.parseInt(propName.substring(index + 1, index2));
			String actualProperty = propName.substring(index2 + 2);
			
			Map<String, String> map = propValues.containsKey(idx) ? propValues.get(idx) : new HashMap<String, String>();
			map.put(actualProperty, properties.getProperty(propName));
			propValues.put(idx, map);
		}
		
		for (int idx : propValues.keySet()) {
			instatiateSerializer(propValues.get(idx));
		}
	}

	@SuppressWarnings("unchecked")
	private void instatiateSerializer(Map<String, String> map) {
		if (!map.containsKey(SerializerUtil.P_CLASS) &&
				!map.containsKey(SerializerUtil.P_NAME)) {
			return;
		}

		logger.info("Instantiating serializer (" + map.get(SerializerUtil.P_NAME) + "): " + map.get(SerializerUtil.P_CLASS));
		Properties properties = retrieveProperties(map);

		try {
			Class<ITestSuiteDeserializer> deserializerCls = (Class<ITestSuiteDeserializer>) Class
					.forName(map.get(SerializerUtil.P_CLASS));
			ITestSuiteDeserializer deserializer = null;
			deserializer = deserializerCls.getConstructor(Properties.class).newInstance(properties);
			deserializers.put(deserializer.getName(), deserializer);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			
			logger.error("Unable to instantiate serializer: " + map.get(SerializerUtil.P_NAME) + " ...");
			logger.error(e.getMessage());
		}
		
		logger.info("Serializer: " + map.get(SerializerUtil.P_NAME) + " successfully instatiated ...");

	}

	private Properties retrieveProperties(Map<String, String> map) {
		Properties properties = new Properties();
		
		for (String key : map.keySet()) {
			String value = map.get(key);
			if (key.startsWith(SerializerUtil.P_PROPERTY)) {
				int index = key.indexOf("[");
				String tmp = key.substring(index + 1).trim();
				index = tmp.indexOf("]");
				String actualProperty = tmp.substring(0, index).trim();
				properties.put(actualProperty, value);
			}
		}
		
		return properties;
	}

	public Map<String, ITestSuiteDeserializer> getDeserializers() {
		return deserializers;
	}
}
