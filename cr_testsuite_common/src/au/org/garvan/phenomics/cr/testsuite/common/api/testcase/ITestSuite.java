package au.org.garvan.phenomics.cr.testsuite.common.api.testcase;

import java.util.List;
import java.util.Map;

public interface ITestSuite {

	public Map<String, List<ITestCaseConcept>> getTestCases();

}
