package au.org.garvan.phenomics.cr.testsuites.inputwrapper;

import junit.framework.TestCase;

import org.junit.Test;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.IEntityIterator;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.IEntityProfile;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.IInputDataWrapper;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.exception.DataSourceLoadingException;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.exception.DataSourcePropertiesLoadingException;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.exception.InvalidMetadataException;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.exception.NonExistingDataWrapperException;
import au.org.garvan.phenomics.cr.testsuite.inputwrapper.InputWrapperManager;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;

public class InputWrapperTest extends TestCase {

	@SuppressWarnings("unchecked")
	private static TestSuiteLogger<InputWrapperTest> logger = (TestSuiteLogger<InputWrapperTest>) TestSuiteLogger
			.getLogger(InputWrapperTest.class);

	@Test
	public void testInputWrapper() {
		TestSuiteLogger.setUpLogger("DEBUG");
		
		String mainPath = "/home/tudor/EXTRA_SPACE/NEW_TestSuites";
		String propFile = mainPath + "/input_wrapper.properties";
		
		logger.info("Initializing Input wrapper manager ...");
		InputWrapperManager iwm = new InputWrapperManager(propFile, mainPath);
		assertTrue(iwm.isReady());

		logger.info("Retrieving Input wrapper ...");
		IInputDataWrapper inputWrapper = null;
		try {
			inputWrapper = iwm.getDataWrapper("OWL Ontology Wrapper");
		} catch (NonExistingDataWrapperException e) {
			e.printStackTrace();
		}
		
		assertNotNull(inputWrapper);

		logger.info("Loading HPO ...");
		String iwPropFile = "/home/tudor/EXTRA_SPACE/NEW_TestSuites/data_in/hpo/hpo.properties";
		IEntityIterator entityInterator = null;
		try {
			entityInterator = inputWrapper.loadData(iwPropFile);
		} catch (DataSourcePropertiesLoadingException
				| InvalidMetadataException | DataSourceLoadingException e) {
			e.printStackTrace();
		}

		assertNotNull(entityInterator);

		while(entityInterator.hasNext()) {
			IEntityProfile entityProfile = entityInterator.next();
			assertNotNull(entityProfile);
			
			logger.debug(entityProfile.getUri() + ": " + entityProfile.getLabels());
		}
	}
	
}
