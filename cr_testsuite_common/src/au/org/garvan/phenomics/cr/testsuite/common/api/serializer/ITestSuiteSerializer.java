package au.org.garvan.phenomics.cr.testsuite.common.api.serializer;

import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestSuite;

public interface ITestSuiteSerializer {

	public String getName();

	public Properties getAcceptedProperties();

	public void serialize(ITestSuite testSuite, Properties serializerProperties);
}
