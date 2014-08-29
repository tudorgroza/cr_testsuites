package au.org.garvan.phenomics.cr.testsuites.common.api.input.exception;

public class DataSourceLoadingException extends Exception {

	private static final long serialVersionUID = -918200826779414280L;

	public DataSourceLoadingException(String message) {
		super("Unable to load data source: " + message);
	}

}
