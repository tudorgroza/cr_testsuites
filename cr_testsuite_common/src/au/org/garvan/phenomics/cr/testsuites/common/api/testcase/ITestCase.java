package au.org.garvan.phenomics.cr.testsuites.common.api.testcase;

import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuites.common.api.input.IEntityProfile;

public interface ITestCase {

	public String getId();

	public String getName();
	
	public Properties getAcceptedProperties();

	public void addEntity(IEntityProfile profile);

	public void runTestCases(Properties properties);

	public void reset();

}
