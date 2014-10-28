package au.org.garvan.phenomics.cr.testsuite.common.api.testcase;

import java.util.List;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.IEntityProfile;

public interface ITestCase {

	public String getId();

	public String getName();
	
	public List<String> getAcceptedProperties();

	public void addEntity(IEntityProfile profile);

	public void reset();

}
