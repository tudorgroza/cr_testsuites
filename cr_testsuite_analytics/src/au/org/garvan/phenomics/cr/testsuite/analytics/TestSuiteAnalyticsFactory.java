package au.org.garvan.phenomics.cr.testsuite.analytics;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.analytics.ITestSuiteAnalyticsEntry;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;

public class TestSuiteAnalyticsFactory {

	@SuppressWarnings("unchecked")
	private static TestSuiteLogger<TestSuiteAnalyticsFactory> logger = (TestSuiteLogger<TestSuiteAnalyticsFactory>) TestSuiteLogger
			.getLogger(TestSuiteAnalyticsFactory.class);

	private Properties properties;
	private String propertiesFile;

	private Map<String, ITestSuiteAnalyticsEntry> analyticsEntries;
	
	public TestSuiteAnalyticsFactory(String propertiesFile) {
		this.propertiesFile = propertiesFile;
		properties = new Properties();
		analyticsEntries = new HashMap<String, ITestSuiteAnalyticsEntry>();
	}
	
	public boolean loadResources() {
		logger.info("Loading resources ...");
		try {
			properties.load(new FileReader(propertiesFile));
			TestSuiteAnalyticsInitializer initializer = new TestSuiteAnalyticsInitializer(properties);
			analyticsEntries = initializer.getEntries();
			
			logger.info("Test cases initialized ...");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public ITestSuiteAnalyticsEntry getAnalyticsEntry(String name) {
		return analyticsEntries.get(name);
	}
}
