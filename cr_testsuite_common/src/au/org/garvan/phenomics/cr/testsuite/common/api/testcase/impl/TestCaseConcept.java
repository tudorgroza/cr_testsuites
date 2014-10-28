package au.org.garvan.phenomics.cr.testsuite.common.api.testcase.impl;

import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestCaseConcept;

public class TestCaseConcept implements ITestCaseConcept {

	private String textualGrouding;
	private String originalLabel;
	private String uri;
	
	public TestCaseConcept() {
		this.textualGrouding = "";
		this.originalLabel = "";
		this.uri = "";
	}
	
	public TestCaseConcept(String textualGrouding, String originalLabel,
			String uri) {
		this.textualGrouding = textualGrouding;
		this.originalLabel = originalLabel;
		this.uri = uri;
	}

	@Override
	public String getTextualGrouding() {
		return textualGrouding;
	}

	public void setTextualGrouding(String textualGrouding) {
		this.textualGrouding = textualGrouding;
	}

	@Override
	public String getOriginalLabel() {
		return originalLabel;
	}

	public void setOriginalLabel(String originalLabel) {
		this.originalLabel = originalLabel;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public String toString() {
		return textualGrouding + " (" + uri +")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		TestCaseConcept tcr = (TestCaseConcept) obj;
		return tcr.getOriginalLabel().equalsIgnoreCase(originalLabel) &&
				tcr.getTextualGrouding().equalsIgnoreCase(textualGrouding) &&
				tcr.getUri().equalsIgnoreCase(uri);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + originalLabel.hashCode();
		result = prime * result + textualGrouding.hashCode();
		result = prime * result + uri.hashCode();
		return result;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
