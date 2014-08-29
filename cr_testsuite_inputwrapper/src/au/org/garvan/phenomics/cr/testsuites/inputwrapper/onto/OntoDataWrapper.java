package au.org.garvan.phenomics.cr.testsuites.inputwrapper.onto;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuites.common.api.input.IEntityIterator;
import au.org.garvan.phenomics.cr.testsuites.common.api.input.IInputDataWrapper;
import au.org.garvan.phenomics.cr.testsuites.common.api.input.exception.DataSourceLoadingException;
import au.org.garvan.phenomics.cr.testsuites.common.api.input.exception.DataSourcePropertiesLoadingException;
import au.org.garvan.phenomics.cr.testsuites.common.api.input.exception.InvalidMetadataException;
import au.org.garvan.phenomics.cr.testsuites.common.api.input.impl.DataSourceMetadata;
import au.org.garvan.phenomics.cr.testsuites.inputwrapper.util.MetadataReader;
import au.org.garvan.phenomics.cr.testsuites.log.TestSuitesLogger;

public class OntoDataWrapper implements IInputDataWrapper {

	@SuppressWarnings("unchecked")
	private static TestSuitesLogger<OntoDataWrapper> logger = (TestSuitesLogger<OntoDataWrapper>) TestSuitesLogger
			.getLogger(OntoDataWrapper.class);

	private String mainPath;
	
	public OntoDataWrapper() {
	}
	
	@Override
	public IEntityIterator loadData(String dataPropertiesFile) throws DataSourcePropertiesLoadingException, InvalidMetadataException, DataSourceLoadingException {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(dataPropertiesFile));
			DataSourceMetadata metadata = new MetadataReader(properties, mainPath).getMetadata();
			return loadData(metadata);
		} catch (IOException e) {
			logger.fatal(e.getMessage());
			throw new DataSourcePropertiesLoadingException(dataPropertiesFile);
		}
	}

	@Override
	public IEntityIterator loadData(DataSourceMetadata metadata) throws DataSourceLoadingException {
		return new OntologyHandler(metadata).getEntityIterator();
	}
	
	@Override
	public void setMainPath(String mainPath) {
		this.mainPath = mainPath;
	}
}
