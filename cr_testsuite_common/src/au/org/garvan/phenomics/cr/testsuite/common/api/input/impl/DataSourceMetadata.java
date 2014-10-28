package au.org.garvan.phenomics.cr.testsuite.common.api.input.impl;

public class DataSourceMetadata {

	private String title;
	private String acronym;
	private String description;
	private String url;
	private String location;
	private String specFile;
	private String releaseDate;

	public DataSourceMetadata() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSpecFile() {
		return specFile;
	}

	public void setSpecFile(String specFile) {
		this.specFile = specFile;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	@Override
	public String toString() {
		return title + " (" + acronym + "): " + location + " (" + specFile + ")";
	}
}
