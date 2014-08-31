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
			Map<Integer, Map<String, String>> propValues = new HashMap<Integer, Map<String,String>>();
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
				
				if (actualProperty.equalsIgnoreCase("property")) {
					index = actualProperty.indexOf("[");
					index2 = actualProperty.indexOf("]");
					
					String pName = actualProperty.substring(index + 1, index2).trim();
					Map<String, String> map = propValues.containsKey(idx) ? propValues.get(idx) : new HashMap<String, String>();
					map.put(pName, properties.getProperty(propName));
					propValues.put(idx, map);
				} else {
					Map<String, String> map = propValues.containsKey(idx) ? propValues.get(idx) : new HashMap<String, String>();
					map.put(actualProperty, properties.getProperty(propName));
					propValues.put(idx, map);
				}
			}
			
			Collections.sort(indexes);
			for (int idx : indexes) {
				addTestCase(propValues.get(idx));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addTestCase(Map<String, String> map) {
		String id = map.get("id");
		Properties properties = new Properties();
		for (String key : map.keySet()) {
			if (!key.equalsIgnoreCase("id")) {
				int index = key.indexOf("[");
				int index2 = key.indexOf("]");
				if (index != -1 && index2 != -1) {
					properties.put(key.substring(index + 1, index2).trim(), map.get(key));
				}
			}
		}
		
		tsDefinition.addTestCase(tsFactory.getAllTestCases().get(id), properties);
	}

	public TestSuiteDefinition getTsDefinition() {
		return tsDefinition;
	}
}
