package au.org.garvan.phenomics.cr.testsuites.serializer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuites.common.api.serializer.ITestSuiteSerializer;
import au.org.garvan.phenomics.cr.testsuites.log.TestSuitesLogger;
import au.org.garvan.phenomics.cr.testsuites.serializer.util.SerializerUtil;

public class SerializerResourceLoader {

	@SuppressWarnings("unchecked")
	private static TestSuitesLogger<SerializerResourceLoader> logger = (TestSuitesLogger<SerializerResourceLoader>) TestSuitesLogger
			.getLogger(SerializerResourceLoader.class);

	private Map<String, ITestSuiteSerializer> serializers;
	
	public SerializerResourceLoader(Properties properties) {
		serializers = new HashMap<String, ITestSuiteSerializer>();
		loadSerializers(properties);
	}

	private void loadSerializers(Properties properties) {
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
			Class<ITestSuiteSerializer> serializerCls = (Class<ITestSuiteSerializer>) Class
					.forName(map.get(SerializerUtil.P_CLASS));
			ITestSuiteSerializer serializer = null;
			serializer = serializerCls.getConstructor(Properties.class).newInstance(properties);
			serializers.put(serializer.getName(), serializer);
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

	public Map<String, ITestSuiteSerializer> getSerializers() {
		return serializers;
	}
}
