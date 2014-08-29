package au.org.garvan.phenomics.cr.testsuites.inputwrapper.spec;

import au.org.garvan.phenomics.cr.testsuites.common.util.CommonUtil;

import com.google.gson.Gson;

public class SpecReader {

	private SpecDefinition specDefinition;
	
	public SpecReader(String specFile) {
		unmarshallSpecDefinition(specFile);
	}

	private void unmarshallSpecDefinition(String specFile) {
		Gson gson = new Gson();
		String jsonContent = CommonUtil.readFile(specFile);
		specDefinition = gson.fromJson(jsonContent, SpecDefinition.class);
		specDefinition.processURIPatterns();
	}

	public SpecDefinition getSpecDefinition() {
		return specDefinition;
	}
}
