package au.org.garvan.phenomics.cr.testsuite.serializer;

public class SerializerNotFoundException extends Exception {

	private static final long serialVersionUID = 3277283842887609236L;

	public SerializerNotFoundException(String message) {
		super("Serializer not found: " + message);
	}

}
