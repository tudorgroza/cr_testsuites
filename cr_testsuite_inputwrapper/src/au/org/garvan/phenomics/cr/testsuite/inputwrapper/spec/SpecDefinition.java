package au.org.garvan.phenomics.cr.testsuite.inputwrapper.spec;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SpecDefinition {

	public static final String CONCEPT_CLASS = "CLASS";

	public static final String CONCEPT_INSTANCE = "INSTANCE";
	
	private List<String> conceptTypes;
	
	private Map<String, Map<String, List<String>>> labelProperties;
	private Map<String, Map<String, List<String>>> synonymProperties;

	private Map<String, List<String>> uriPatterns;

	private Map<String, Map<String, String>> negatedPatterns;
	private Map<String, Map<String, String>> negatedFullPatterns;
	
	private List<String> subClassPatterns;

	public SpecDefinition() {
		labelProperties = new LinkedHashMap<String, Map<String, List<String>>>();
		synonymProperties = new LinkedHashMap<String, Map<String, List<String>>>();
		uriPatterns = new LinkedHashMap<String, List<String>>();
		conceptTypes = new ArrayList<String>();
		
		negatedPatterns = new LinkedHashMap<String, Map<String,String>>();
		negatedFullPatterns = new LinkedHashMap<String, Map<String,String>>();
		
		subClassPatterns = new ArrayList<String>();
	}

	public List<String> getConceptTypes() {
		return conceptTypes;
	}

	public void setConceptTypes(List<String> conceptTypes) {
		this.conceptTypes = conceptTypes;
	}

	public Map<String, Map<String, List<String>>> getLabelProperties() {
		return labelProperties;
	}

	public Map<String, Map<String, List<String>>> getSynonymProperties() {
		return synonymProperties;
	}

	public Map<String, List<String>> getLabelRestrictions(String labelPropertyURI) {
		return labelProperties.get(labelPropertyURI);
	}

	public Map<String, List<String>> getSynonymRestrictions(String synonymPropertyURI) {
		return synonymProperties.get(synonymPropertyURI);
	}

	public boolean hasLabelProperty(String labelPropertyURI) {
		return labelProperties.containsKey(labelPropertyURI);
	}

	public boolean hasSynonymProperty(String synonymPropertyURI) {
		return synonymProperties.containsKey(synonymPropertyURI);
	}

	public void addURIPattern(String nameSpace, List<String> patterns) {
		uriPatterns.put(nameSpace, patterns);
	}
	
	public boolean acceptsAll() {
		return uriPatterns.isEmpty();
	}
	
	public Map<String, List<String>> getUriPatterns() {
		return uriPatterns;
	}

	public void setUriPatterns(Map<String, List<String>> uriPatterns) {
		this.uriPatterns = uriPatterns;
	}

	public void processURIPatterns() {
		for (String nameSpace : uriPatterns.keySet()) {
			if (nameSpace.equalsIgnoreCase("^")) {
				subClassPatterns.addAll(uriPatterns.get(nameSpace));
			} else {
				Map<String, String> negated = new LinkedHashMap<String, String>();
				Map<String, String> negatedFull = new LinkedHashMap<String, String>();
				
				List<String> list = uriPatterns.get(nameSpace);
				
				for (String s : list) {
					int index = s.indexOf("*");
					if (s.startsWith("~")) {
						if (index == -1) {
							negatedFull.put(s.substring(1), "");
						} else {
							negated.put(s.substring(1, index), "");
						}
					}
				}
				
				negatedPatterns.put(nameSpace, negated);
				negatedFullPatterns.put(nameSpace, negatedFull);
			}
		}
	}
	
	public boolean isValidURI(String namespace, String id) {
		boolean result = false;
		if (uriPatterns.containsKey(namespace)) {
			return !negatedFullPatterns.get(namespace).containsKey(id) && !startsWith(id, negatedPatterns.get(namespace));
		}
		return result;
	}
	
	private boolean startsWith(String id, Map<String, String> map) {
		boolean result = false;
		
		for (String s : map.keySet()) {
			if (id.startsWith(s)) {
				result = true;
				break;
			}
		}
		
		return result;
	}

	public Map<String, Map<String, String>> getNegatedPatterns() {
		return negatedPatterns;
	}

	public Map<String, Map<String, String>> getNegatedFullPatterns() {
		return negatedFullPatterns;
	}

	public List<String> getSubClassPatterns() {
		return subClassPatterns;
	}
}
