package au.org.garvan.phenomics.cr.testsuite.ncbo;

public class NCBOOffset {

	private long start;
	private long end;

	public NCBOOffset() {
	}

	public NCBOOffset(long start, long end) {
		this.start = start;
		this.end = end;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * start * end);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		NCBOOffset offset = (NCBOOffset) obj;
		return start == offset.getStart() && end == offset.getEnd();
	}
	
	@Override
	public String toString() {
		return start + ":" + end;
	}
}
