package au.org.garvan.phenomics.cr.testsuite.serializer;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.ITestSuiteDeserializer;
import au.org.garvan.phenomics.cr.testsuite.common.api.serializer.ITestSuiteSerializer;
import au.org.garvan.phenomics.cr.testsuite.deserializer.DeserializerNotFoundException;
import au.org.garvan.phenomics.cr.testsuite.deserializer.DeserializerResourceLoader;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;

public class TestSuiteSerializerFactory {

	@SuppressWarnings("unchecked")
	private static TestSuiteLogger<TestSuiteSerializerFactory> logger = (TestSuiteLogger<TestSuiteSerializerFactory>) TestSuiteLogger
			.getLogger(TestSuiteSerializerFactory.class);

	private String propertiesFile;
	private Properties properties;
	
	private Map<String, ITestSuiteSerializer> serializers;
	private Map<String, ITestSuiteDeserializer> deserializers;
	
	public TestSuiteSerializerFactory(String propertiesFile) {
		serializers = new HashMap<String, ITestSuiteSerializer>();
		deserializers = new HashMap<String, ITestSuiteDeserializer>();
		
		this.propertiesFile = propertiesFile;
		this.properties = new Properties();
	}
	
	public boolean loadResources() {
		logger.info("Loading resources ...");
		try {
			properties.load(new FileReader(propertiesFile));
			SerializerResourceLoader serializerLoader = new SerializerResourceLoader(properties);
			serializers = serializerLoader.getSerializers();

			DeserializerResourceLoader deserializerLoader = new DeserializerResourceLoader(properties);
			deserializers = deserializerLoader.getDeserializers();

			logger.info("Serializers initialized ...");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public Map<String, ITestSuiteSerializer> getSerializers() {
		return serializers;
	}
	
	public Map<String, ITestSuiteDeserializer> getDeserializers() {
		return deserializers;
	}

	public ITestSuiteSerializer getSerializer(String name) throws SerializerNotFoundException {
		if (serializers.containsKey(name)) {
			return serializers.get(name);
		}
		
		throw new SerializerNotFoundException(name);
	}
	
	public ITestSuiteDeserializer getDeserializer(String name) throws DeserializerNotFoundException {
		if (deserializers.containsKey(name)) {
			return deserializers.get(name);
		}
		
		throw new DeserializerNotFoundException(name);
	}
}
