package au.org.garvan.phenomics.cr.testsuite.analytics.basic;

import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.analytics.ITestSuiteAnalyticsEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.analytics.ITestSuiteAnalyticsResults;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.SystemSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.TestCaseSerializedEntry;

public class TestSuiteAnalytics_FScore implements ITestSuiteAnalyticsEntry {

	private Properties properties;
	
	public TestSuiteAnalytics_FScore(Properties properties) {
		this.properties = properties;
	}

	@Override
	public ITestSuiteAnalyticsResults compute(
			Map<String, Map<String, TestCaseSerializedEntry>> testSuite,
			Map<String, Map<String, SystemSerializedEntry>> systemAnnotations) {
		return new FScoreComputer(testSuite, systemAnnotations, properties).getAnalytics();
	}
	
	@Override
	public String getName() {
		return "F-Score";
	}
	
}
