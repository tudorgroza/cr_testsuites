package au.org.garvan.phenomics.cr.testsuite.deserializer;

public class DeserializerNotFoundException extends Exception {

	private static final long serialVersionUID = 8259736705510318946L;

	public DeserializerNotFoundException(String message) {
		super("Deserializer not found: " + message);
	}

}
