package au.org.garvan.phenomics.cr.testsuites.main;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuites.common.api.input.IEntityIterator;
import au.org.garvan.phenomics.cr.testsuites.common.api.input.IInputDataWrapper;
import au.org.garvan.phenomics.cr.testsuites.common.api.input.exception.DataSourceLoadingException;
import au.org.garvan.phenomics.cr.testsuites.common.api.input.exception.DataSourcePropertiesLoadingException;
import au.org.garvan.phenomics.cr.testsuites.common.api.input.exception.InvalidMetadataException;
import au.org.garvan.phenomics.cr.testsuites.common.api.input.exception.NonExistingDataWrapperException;
import au.org.garvan.phenomics.cr.testsuites.common.api.serializer.ITestSuiteSerializer;
import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestSuite;
import au.org.garvan.phenomics.cr.testsuites.common.util.CommonUtil;
import au.org.garvan.phenomics.cr.testsuites.factory.TestSuiteDefinition;
import au.org.garvan.phenomics.cr.testsuites.factory.TestSuiteFactory;
import au.org.garvan.phenomics.cr.testsuites.inputwrapper.InputWrapperManager;
import au.org.garvan.phenomics.cr.testsuites.log.TestSuitesLogger;
import au.org.garvan.phenomics.cr.testsuites.serializer.SerializerNotFoundException;
import au.org.garvan.phenomics.cr.testsuites.serializer.TestSuiteSerializerFactory;

public class TestSuitesTest {

	private TestSuiteFactory tsFactory;
	private TestSuiteSerializerFactory tsSerializerFactory;
	private InputWrapperManager iwManager;
	private String mainPath;
	
	private Properties properties;
	
	public TestSuitesTest(String mainProperties) {
		mainPath = "";
		loadProperties(mainProperties);
		
		iwManager = new InputWrapperManager(CommonUtil.checkAndRelocateFolder(mainPath, properties.getProperty(TestProperties.P_IW), CommonUtil.PATH_PATTERN), mainPath);
		tsFactory = new TestSuiteFactory(CommonUtil.checkAndRelocateFolder(mainPath, properties.getProperty(TestProperties.P_FACTORY), CommonUtil.PATH_PATTERN));
		tsFactory.loadResources();
		
		tsSerializerFactory = new TestSuiteSerializerFactory(CommonUtil.checkAndRelocateFolder(mainPath, properties.getProperty(TestProperties.P_SERIALIZER), CommonUtil.PATH_PATTERN));
		tsSerializerFactory.loadResources();
		
		run();
	}
	
	private void loadProperties(String mainProperties) {
		properties = new Properties();
		try {
			properties.load(new FileReader(mainProperties));
			if (properties.containsKey(TestProperties.P_LOGLEVEL)) {
				TestSuitesLogger.setUpLogger(properties.getProperty(TestProperties.P_LOGLEVEL));
			}
			if (properties.containsKey(TestProperties.P_MAINPATH)) {
				mainPath = properties.getProperty(TestProperties.P_MAINPATH);
				if (!mainPath.endsWith("/")) {
					mainPath += "/";
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void run() {
		try {
			IInputDataWrapper dataWrapper = iwManager.getDataWrapper(properties.getProperty(TestProperties.P_IW_DATAWRAPPER));
			
			String ontoLocation = CommonUtil.checkAndRelocateFolder(mainPath, properties.getProperty(TestProperties.P_IW_SOURCE), CommonUtil.PATH_PATTERN);
			System.out.println(ontoLocation);
			
			IEntityIterator entityIterator = dataWrapper.loadData(ontoLocation);
			
			String defLocation = CommonUtil.checkAndRelocateFolder(mainPath, properties.getProperty(TestProperties.P_FACTORY_DEF), CommonUtil.PATH_PATTERN);
			TestSuiteDefinition tsDefinition = new DefinitionLoader(defLocation, tsFactory).getTsDefinition();
			
			
			ITestSuite testSuite = tsFactory.generateTestSuite(tsDefinition, entityIterator);
			ITestSuiteSerializer serializer = tsSerializerFactory.getSerializer(properties.getProperty(TestProperties.P_SERIALIZER_NAME));
			Properties serializerProperties = parseSerializerProperties();
			serializer.serialize(testSuite, serializerProperties);
		} catch (NonExistingDataWrapperException | DataSourcePropertiesLoadingException | InvalidMetadataException | DataSourceLoadingException | SerializerNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Properties parseSerializerProperties() {
		Properties serializerProperties = new Properties();
		
		for (Object key : properties.keySet()) {
			String propName = (String) key;
			if (propName.startsWith("serializer.property")) {
				int index = propName.indexOf("[");
				int index2 = propName.indexOf("]");
				
				String actualProperty = propName.substring(index + 1, index2).trim();
				
				serializerProperties.put(actualProperty, CommonUtil.checkAndRelocateFolder(mainPath, properties.getProperty(propName), CommonUtil.PATH_PATTERN));
			}
		}
		return serializerProperties;
	}

	public static void main(String[] args) {
		TestSuitesLogger.setUpLogger("INFO");
		new TestSuitesTest(args[0]);
	}
}
