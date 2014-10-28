package au.org.garvan.phenomics.cr.testsuite.common.api.deserializer;

public class Offset {

	public int start;
	public int end;

	public Offset() {
	}
	
	public Offset(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		Offset o = (Offset) obj;
		return o.getStart() == start && o.getEnd() == end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + start;
		result = prime * result + end;
		return result;
	}
	
	@Override
	public String toString() {
		return start + "::" + end;
	}
}
