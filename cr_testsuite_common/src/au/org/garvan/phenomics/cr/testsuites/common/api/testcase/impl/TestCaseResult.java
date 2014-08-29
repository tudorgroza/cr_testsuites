package au.org.garvan.phenomics.cr.testsuites.common.api.testcase.impl;

import au.org.garvan.phenomics.cr.testsuites.common.api.testcase.ITestCaseResult;

public class TestCaseResult implements ITestCaseResult {

	private String textualGrouding;
	private String originalLabel;
	private String uri;
	
	public TestCaseResult() {
	}
	
	public TestCaseResult(String textualGrouding, String originalLabel,
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

	public void setUri(String uri) {
		this.uri = uri;
	}
}
