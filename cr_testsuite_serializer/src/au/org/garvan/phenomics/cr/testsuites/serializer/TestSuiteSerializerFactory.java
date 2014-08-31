package au.org.garvan.phenomics.cr.testsuites.serializer;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuites.common.api.serializer.ITestSuiteSerializer;
import au.org.garvan.phenomics.cr.testsuites.log.TestSuitesLogger;

public class TestSuiteSerializerFactory {

	@SuppressWarnings("unchecked")
	private static TestSuitesLogger<TestSuiteSerializerFactory> logger = (TestSuitesLogger<TestSuiteSerializerFactory>) TestSuitesLogger
			.getLogger(TestSuiteSerializerFactory.class);

	private String propertiesFile;
	private Properties properties;
	private Map<String, ITestSuiteSerializer> serializers;
	
	public TestSuiteSerializerFactory(String propertiesFile) {
		serializers = new HashMap<String, ITestSuiteSerializer>();
		this.propertiesFile = propertiesFile;
		this.properties = new Properties();
	}
	
	public boolean loadResources() {
		logger.info("Loading resources ...");
		try {
			properties.load(new FileReader(propertiesFile));
			SerializerResourceLoader initializer = new SerializerResourceLoader(properties);
			serializers = initializer.getSerializers();
			
			logger.info("Test cases initialized ...");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public Map<String, ITestSuiteSerializer> getSerializers() {
		return serializers;
	}
	
	public ITestSuiteSerializer getSerializer(String name) throws SerializerNotFoundException {
		if (serializers.containsKey(name)) {
			return serializers.get(name);
		}
		
		throw new SerializerNotFoundException(name);
	}
}
