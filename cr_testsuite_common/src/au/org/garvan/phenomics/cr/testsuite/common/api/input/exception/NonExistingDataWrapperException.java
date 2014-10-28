package au.org.garvan.phenomics.cr.testsuite.common.api.input.exception;

public class NonExistingDataWrapperException extends Exception {

	private static final long serialVersionUID = -6356597053333914808L;

	public NonExistingDataWrapperException(String message) {
		super("Requested data wrapper: <" + message + "> not found!");
	}

}
