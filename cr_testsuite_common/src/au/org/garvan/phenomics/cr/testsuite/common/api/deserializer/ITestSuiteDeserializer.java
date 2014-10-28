package au.org.garvan.phenomics.cr.testsuite.common.api.deserializer;

import java.util.Map;
import java.util.Properties;

public interface ITestSuiteDeserializer {

	public String getName();

	public Properties getAcceptedProperties();

	public Map<String, Map<String, TestCaseSerializedEntry>> deserializeTestSuite(Properties deserializerProperties);

	public Map<String, Map<String, SystemSerializedEntry>> deserializeSystemAnnotations(Properties deserializerProperties);

}
