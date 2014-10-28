package au.org.garvan.phenomics.cr.testsuite.deserializer;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.ITestSuiteDeserializer;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;

public class TestSuiteDeserializerFactory {

	@SuppressWarnings("unchecked")
	private static TestSuiteLogger<TestSuiteDeserializerFactory> logger = (TestSuiteLogger<TestSuiteDeserializerFactory>) TestSuiteLogger
			.getLogger(TestSuiteDeserializerFactory.class);

	private String propertiesFile;
	private Properties properties;
	
	private Map<String, ITestSuiteDeserializer> deserializers;
	
	public TestSuiteDeserializerFactory(String propertiesFile) {
		deserializers = new HashMap<String, ITestSuiteDeserializer>();

		this.propertiesFile = propertiesFile;
		this.properties = new Properties();
	}
	
	public boolean loadResources() {
		logger.info("Loading resources ...");
		try {
			properties.load(new FileReader(propertiesFile));
			DeserializerResourceLoader deserializerLoader = new DeserializerResourceLoader(properties);
			deserializers = deserializerLoader.getDeserializers();

			logger.info("Deserializers initialized ...");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public Map<String, ITestSuiteDeserializer> getDeserializers() {
		return deserializers;
	}
	
	public ITestSuiteDeserializer getDeserializer(String name) throws DeserializerNotFoundException {
		if (deserializers.containsKey(name)) {
			return deserializers.get(name);
		}
		
		throw new DeserializerNotFoundException(name);
	}
}
