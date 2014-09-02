package au.org.garvan.phenomics.cr.testsuites.main;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import au.org.garvan.phenomics.cr.testsuites.factory.TestSuiteDefinition;
import au.org.garvan.phenomics.cr.testsuites.factory.TestSuiteFactory;

public class DefinitionLoader {

	private TestSuiteFactory tsFactory;
	private TestSuiteDefinition tsDefinition;
	
	public DefinitionLoader(String defFile, TestSuiteFactory tsFactory) {
		this.tsFactory = tsFactory;
		tsDefinition = new TestSuiteDefinition();
		
		loadDefinitionFile(defFile);
	}

	private void loadDefinitionFile(String defFile) {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(defFile));
			Map<Integer, Map<String, String>> propValues = new HashMap<Integer, Map<String, String>>();
			List<Integer> indexes = new ArrayList<Integer>();
			
			for (Object key : properties.keySet()) {
				String propName = (String) key;
				
				int index = propName.indexOf("[");
				int index2 = propName.indexOf("]");
				
				int idx = Integer.parseInt(propName.substring(index + 1, index2));
				String actualProperty = propName.substring(index2 + 2);
			
				if (!indexes.contains(idx)) {
					indexes.add(idx);
				}
				Map<String, String> map = propValues.containsKey(idx) ? propValues.get(idx) : new HashMap<String, String>();
				map.put(actualProperty, properties.getProperty(propName));
				propValues.put(idx, map);
				
			}
			
			Collections.sort(indexes);
			for (int idx : indexes) {
				addTestCase(propValues.get(idx));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addTestCase(Map<String, String> rawProperties) {
		String id = null;
		Properties simpleProperties = new Properties();
		Map<String, Properties> compositeProperties = new HashMap<String, Properties>();
		
		for (String rawP : rawProperties.keySet()) {
			String rawPValue = rawProperties.get(rawP);
			
			if (rawP.startsWith("id")) {
				id = rawPValue;
			}
			if (rawP.startsWith("property")) {
				String pName = parsePName(rawP);
				if (pName != null) {
					simpleProperties.put(pName, rawPValue);
				}
			}
			if (rawP.startsWith("set")) {
				String setId = parsePName(rawP);
				if (setId != null) {
					Properties propMap = compositeProperties.containsKey(setId) ? compositeProperties.get(setId) : new Properties();
					String setProp = parsePPName(rawP);
					if (setProp != null) {
						propMap.put(setProp, rawPValue);
						compositeProperties.put(setId, propMap);
					}
				}
			}
		}
		
		if (simpleProperties.isEmpty()) {
			tsDefinition.addCompositeTestCase(tsFactory.getAllTestCases().get(id), compositeProperties);
		} else {
			tsDefinition.addSimpleTestCase(tsFactory.getAllTestCases().get(id), simpleProperties);
		}
	}

	private String parsePPName(String rawP) {
		int index = rawP.lastIndexOf("[");
		int index2 = rawP.lastIndexOf("]");
		if (index != -1 && index2 != -1) {
			return rawP.substring(index + 1, index2);
		}		
		return null;
	}

	private String parsePName(String rawP) {
		int index = rawP.indexOf("[");
		int index2 = rawP.indexOf("]");
		if (index != -1 && index2 != -1) {
			return rawP.substring(index + 1, index2);
		}		
		return null;
	}

	public TestSuiteDefinition getTsDefinition() {
		return tsDefinition;
	}
}
