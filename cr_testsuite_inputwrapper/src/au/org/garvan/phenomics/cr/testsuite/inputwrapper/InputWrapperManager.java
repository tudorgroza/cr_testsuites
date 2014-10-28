package au.org.garvan.phenomics.cr.testsuite.inputwrapper;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.IInputDataWrapper;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.exception.NonExistingDataWrapperException;
import au.org.garvan.phenomics.cr.testsuite.inputwrapper.util.InputWrapperLoader;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;

public class InputWrapperManager {

	@SuppressWarnings("unchecked")
	private static TestSuiteLogger<InputWrapperManager> logger = (TestSuiteLogger<InputWrapperManager>) TestSuiteLogger
			.getLogger(InputWrapperManager.class);

	private Map<String, IInputDataWrapper> dataWrappers;
	private Properties properties;
	
	private boolean ready;
	
	public InputWrapperManager(String propertiesFile, String mainPath) {
		readProperties(propertiesFile);
		
		dataWrappers = new HashMap<String, IInputDataWrapper>();
		dataWrappers = new InputWrapperLoader(properties, mainPath).getDataWrappers();
		
		ready = !dataWrappers.isEmpty();
	}

	private void readProperties(String propertiesFile) {
		properties = new Properties();
		try {
			properties.load(new FileReader(propertiesFile));
		} catch (IOException e) {
			logger.fatal("Unable to read Input Wrapper Manager properties: " + e.getMessage());
		}
	}
	
	public IInputDataWrapper getDataWrapper(String dataWrapper) throws NonExistingDataWrapperException {
		if (dataWrappers.containsKey(dataWrapper)) {
			return dataWrappers.get(dataWrapper);
		}
		
		throw new NonExistingDataWrapperException(dataWrapper);
	}
	
	public boolean isReady() {
		return ready;
	}
}