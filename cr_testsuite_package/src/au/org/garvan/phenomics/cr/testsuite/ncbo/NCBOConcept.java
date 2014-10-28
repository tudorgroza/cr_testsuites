package au.org.garvan.phenomics.cr.testsuite.ncbo;

import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.Offset;


public class NCBOConcept {

	private String text;
	private String source;
	private String conceptURI;
	private long start;
	private long end;

	private NCBOOffset offset;
	
	public NCBOConcept() {
		offset = new NCBOOffset();
	}

	public String getConceptURI() {
		return conceptURI;
	}

	public void setConceptURI(String conceptURI) {
		this.conceptURI = conceptURI;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
		this.offset.setStart(start);
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
		this.offset.setEnd(end);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public NCBOOffset getOffset() {
		return offset;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		NCBOConcept o = (NCBOConcept) obj;
		return o.getStart() == start && o.getEnd() == end && o.getConceptURI().equalsIgnoreCase(conceptURI)
				&& o.getText().equalsIgnoreCase(text) && o.getSource().equalsIgnoreCase(source);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) start;
		result = prime * result + (int) end;
		result = prime * result + conceptURI.hashCode();
		result = prime * result + text.hashCode();
		result = prime * result + source.hashCode();
		return result;
	}
	
	@Override
	public String toString() {
		return "<" + start + "::" + end + ">:{{" + text + "}}[[" + source + "::" + conceptURI + "]]";
	}
}
