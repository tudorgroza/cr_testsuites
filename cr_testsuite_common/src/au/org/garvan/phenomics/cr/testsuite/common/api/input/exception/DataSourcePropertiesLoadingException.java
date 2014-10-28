package au.org.garvan.phenomics.cr.testsuite.common.api.input.exception;

public class DataSourcePropertiesLoadingException extends Exception {

	private static final long serialVersionUID = -9162747038049736984L;

	public DataSourcePropertiesLoadingException(String propertiesFile) {
		super("Unable to load data source properties: " + propertiesFile);
	}
}
