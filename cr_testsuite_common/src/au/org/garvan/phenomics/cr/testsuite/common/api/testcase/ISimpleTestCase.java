package au.org.garvan.phenomics.cr.testsuite.common.api.testcase;

import java.util.List;
import java.util.Properties;

public interface ISimpleTestCase extends ITestCase {

	public void runTestCases(Properties properties);

	public List<ITestCaseConcept> retrieveTestCases();

}
