package au.org.garvan.phenomics.cr.testsuites.common.api.testcase;

import java.util.List;

import au.org.garvan.phenomics.cr.testsuites.common.api.input.IEntityProfile;

public interface ITestCase {

	public String getId();

	public String getName();
	
	public List<String> getAcceptedProperties();

	public void addEntity(IEntityProfile profile);

	public void reset();

}
