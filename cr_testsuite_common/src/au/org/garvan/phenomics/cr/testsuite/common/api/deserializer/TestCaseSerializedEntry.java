package au.org.garvan.phenomics.cr.testsuite.common.api.deserializer;

import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.impl.TestCaseConcept;

public class TestCaseSerializedEntry {

	private String id;
	private Offset offset;
	private TestCaseConcept testCaseConcept;
	
	public TestCaseSerializedEntry() {
		
	}
	
	public TestCaseSerializedEntry(String id, Offset offset, TestCaseConcept testCaseConcept) {
		this.id = id;
		this.offset = offset;
		this.testCaseConcept = testCaseConcept;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Offset getOffset() {
		return offset;
	}

	public void setOffset(Offset offset) {
		this.offset = offset;
	}

	public TestCaseConcept getTestCaseConcept() {
		return testCaseConcept;
	}

	public void setTestCaseConcept(TestCaseConcept testCaseConcept) {
		this.testCaseConcept = testCaseConcept;
	}

	@Override
	public String toString() {
		return id + " <" + offset.toString() + "> = " + testCaseConcept.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		TestCaseSerializedEntry o = (TestCaseSerializedEntry) obj;
		return o.getId().equalsIgnoreCase(id) && 
				o.getTestCaseConcept().equals(testCaseConcept) && 
				o.getOffset().equals(offset);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id.hashCode();
		result = prime * result + testCaseConcept.hashCode();
		result = prime * result + offset.hashCode();
		return result;
	}
	
}
