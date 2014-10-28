package au.org.garvan.phenomics.cr.testsuite.common.api.deserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.impl.TestCaseConcept;

public class SystemSerializedEntry {

	private String id;
	private Map<Offset, List<TestCaseConcept>> testCaseConcepts;
	
	public SystemSerializedEntry() {
		testCaseConcepts = new HashMap<Offset, List<TestCaseConcept>>();
	}
	
	public SystemSerializedEntry(String id) {
		this.id = id;
		testCaseConcepts = new HashMap<Offset, List<TestCaseConcept>>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addTestCaseConcept(Offset offset, TestCaseConcept testCaseConcept) {
		List<TestCaseConcept> list = testCaseConcepts.containsKey(offset) ? testCaseConcepts.get(offset) : new ArrayList<TestCaseConcept>();
		if (!list.contains(testCaseConcept)) {
			list.add(testCaseConcept);
		}
		testCaseConcepts.put(offset, list);
	}
	
	public Map<Offset, List<TestCaseConcept>> getTestCaseConcepts() {
		return testCaseConcepts;
	}
}
