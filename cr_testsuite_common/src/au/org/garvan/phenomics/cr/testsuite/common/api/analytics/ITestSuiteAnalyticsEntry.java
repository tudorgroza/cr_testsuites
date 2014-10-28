package au.org.garvan.phenomics.cr.testsuite.common.api.analytics;

import java.util.Map;

import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.SystemSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.TestCaseSerializedEntry;

public interface ITestSuiteAnalyticsEntry {

	public String getName();
	
	public ITestSuiteAnalyticsResults compute(Map<String, Map<String, TestCaseSerializedEntry>> testSuite, Map<String, Map<String, SystemSerializedEntry>> systemAnnotations);

}
