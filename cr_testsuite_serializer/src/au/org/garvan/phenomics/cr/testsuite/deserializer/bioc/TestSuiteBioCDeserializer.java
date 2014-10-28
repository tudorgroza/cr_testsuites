package au.org.garvan.phenomics.cr.testsuite.deserializer.bioc;

import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.ITestSuiteDeserializer;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.SystemSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.TestCaseSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.util.TestSuiteSerializerStandardProperties;

public class TestSuiteBioCDeserializer implements ITestSuiteDeserializer {

	private Properties acceptedProperties;

	public TestSuiteBioCDeserializer(Properties properties) {
		acceptedProperties = new Properties();
		acceptedProperties.put(TestSuiteSerializerStandardProperties.OUTPUT_FOLDER, "");
		acceptedProperties.put(TestSuiteSerializerStandardProperties.FILE_PREFIX, "");
		acceptedProperties.put(TestSuiteSerializerStandardProperties.INDIVIDUAL_DOCUMENTS, Boolean.toString(false));
	}
	
	@Override
	public String getName() {
		return "BioC Deserializer";
	}

	@Override
	public Properties getAcceptedProperties() {
		return acceptedProperties;
	}

	@Override
	public Map<String, Map<String, TestCaseSerializedEntry>> deserializeTestSuite(Properties deserializerProperties) {
		boolean individualDocs = Boolean.parseBoolean(deserializerProperties.getProperty(TestSuiteSerializerStandardProperties.INDIVIDUAL_DOCUMENTS));
		if (individualDocs) {
			return new BioC_MultiDocTestSuiteDeserializer(deserializerProperties.getProperty(TestSuiteSerializerStandardProperties.INPUT_FOLDER), 
					deserializerProperties.getProperty(TestSuiteSerializerStandardProperties.FILE_PREFIX)).deserialize();
		} else {
			return new BioC_SingleDocTestSuiteDeserializer(deserializerProperties.getProperty(TestSuiteSerializerStandardProperties.INPUT_FILE)).deserialize();
		}
	}

	@Override
	public Map<String, Map<String, SystemSerializedEntry>> deserializeSystemAnnotations(
			Properties deserializerProperties) {
		boolean individualDocs = Boolean.parseBoolean(deserializerProperties.getProperty(TestSuiteSerializerStandardProperties.INDIVIDUAL_DOCUMENTS));
		if (individualDocs) {
			return new BioC_MultiDocSystemDeserializer(deserializerProperties.getProperty(TestSuiteSerializerStandardProperties.INPUT_FOLDER), 
					deserializerProperties.getProperty(TestSuiteSerializerStandardProperties.FILE_PREFIX)).deserialize();
		} else {
			return new BioC_SingleDocSystemDeserializer(deserializerProperties.getProperty(TestSuiteSerializerStandardProperties.INPUT_FILE)).deserialize();
		}
	}

}
