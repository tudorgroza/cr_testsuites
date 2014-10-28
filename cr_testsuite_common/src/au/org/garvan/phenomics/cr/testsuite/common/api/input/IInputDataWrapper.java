package au.org.garvan.phenomics.cr.testsuite.common.api.input;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.exception.DataSourceLoadingException;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.exception.DataSourcePropertiesLoadingException;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.exception.InvalidMetadataException;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.impl.DataSourceMetadata;


public interface IInputDataWrapper {

	public void setMainPath(String mainPath);
	
	public IEntityIterator loadData(String dataPropertiesFile) throws DataSourcePropertiesLoadingException, InvalidMetadataException, DataSourceLoadingException;

	public IEntityIterator loadData(DataSourceMetadata metadata) throws DataSourceLoadingException;

}
