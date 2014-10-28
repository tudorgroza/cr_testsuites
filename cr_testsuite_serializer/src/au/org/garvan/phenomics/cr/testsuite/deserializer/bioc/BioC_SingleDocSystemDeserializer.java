package au.org.garvan.phenomics.cr.testsuite.deserializer.bioc;

import java.util.HashMap;
import java.util.Map;

import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.SystemSerializedEntry;

public class BioC_SingleDocSystemDeserializer {

	private Map<String, Map<String, SystemSerializedEntry>> systemAnnotations;

	public BioC_SingleDocSystemDeserializer(String inputFile) {
		systemAnnotations = new HashMap<String, Map<String,SystemSerializedEntry>>();
	}

	public Map<String, Map<String, SystemSerializedEntry>> deserialize() {
		return systemAnnotations;
	}

}
