package au.org.garvan.phenomics.cr.testsuites.serializer.standard;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuites.common.api.serializer.ITestSuiteSerializer;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestCaseResult;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestSuite;
import au.org.garvan.phenomics.cr.testsuites.common.util.TestSuiteSerializerStandardProperties;

public class TestSuiteFileSerializer implements ITestSuiteSerializer {

	private Properties acceptedProperties;

	public TestSuiteFileSerializer(Properties properties) {
		acceptedProperties = new Properties();
		acceptedProperties.put(TestSuiteSerializerStandardProperties.OUTPUT_FILE, "");
	}
	
	@Override
	public String getName() {
		return "File serializer";
	}

	@Override
	public void serialize(ITestSuite testSuite, Properties serializerProperties) {
		String outFile = serializerProperties.getProperty(TestSuiteSerializerStandardProperties.OUTPUT_FILE);
		
		try {
			PrintWriter pw = new PrintWriter(outFile);
			
			Map<String, List<ITestCaseResult>> testCases = testSuite.getTestCases();
			for (String type : testCases.keySet()) {
				pw.write("#" + type + "\n\n");
				
				List<ITestCaseResult> testCaseList = testCases.get(type);
				for (ITestCaseResult testCase : testCaseList) {
					pw.write(testCase.getTextualGrouding() + " | " + testCase.getUri() + "\n");
				}
				
				pw.write("\n");
				pw.flush();
			}
			
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Properties getAcceptedProperties() {
		return acceptedProperties;
	}

}
