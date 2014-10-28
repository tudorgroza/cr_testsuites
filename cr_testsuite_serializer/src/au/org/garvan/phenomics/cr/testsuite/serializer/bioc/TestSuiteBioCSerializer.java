package au.org.garvan.phenomics.cr.testsuite.serializer.bioc;

import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.serializer.ITestSuiteSerializer;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestSuite;
import au.org.garvan.phenomics.cr.testsuite.common.util.TestSuiteSerializerStandardProperties;

public class TestSuiteBioCSerializer implements ITestSuiteSerializer {

	private Properties acceptedProperties;

	public TestSuiteBioCSerializer(Properties properties) {
		acceptedProperties = new Properties();
		acceptedProperties.put(TestSuiteSerializerStandardProperties.OUTPUT_FILE, "");
		acceptedProperties.put(TestSuiteSerializerStandardProperties.OUTPUT_FOLDER, "");
		acceptedProperties.put(TestSuiteSerializerStandardProperties.FILE_PREFIX, "");
		acceptedProperties.put(TestSuiteSerializerStandardProperties.INDIVIDUAL_DOCUMENTS, Boolean.toString(false));
	}

	@Override
	public String getName() {
		return "BioC Serializer";
	}

	@Override
	public Properties getAcceptedProperties() {
		return acceptedProperties;
	}

	@Override
	public void serialize(ITestSuite testSuite, Properties serializerProperties) {
		boolean individualDocs = Boolean.parseBoolean(serializerProperties.getProperty(TestSuiteSerializerStandardProperties.INDIVIDUAL_DOCUMENTS));
		if (individualDocs) {
			new BioC_MultiDocSerializer(testSuite,
					serializerProperties.getProperty(TestSuiteSerializerStandardProperties.OUTPUT_FOLDER), 
					serializerProperties.getProperty(TestSuiteSerializerStandardProperties.FILE_PREFIX));
		} else {
			new BioC_SingleDocSerializer(testSuite,
					serializerProperties.getProperty(TestSuiteSerializerStandardProperties.OUTPUT_FOLDER), 
					serializerProperties.getProperty(TestSuiteSerializerStandardProperties.FILE_PREFIX));
		}
	}
}
