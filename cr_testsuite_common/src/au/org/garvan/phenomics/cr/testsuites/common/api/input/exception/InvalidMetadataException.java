package au.org.garvan.phenomics.cr.testsuites.common.api.input.exception;

public class InvalidMetadataException extends Exception {

	private static final long serialVersionUID = 3948746664864496036L;

	public InvalidMetadataException(String message) {
		super("Invalid metadata: " + message);
	}
}
