package au.org.garvan.phenomics.cr.testsuite.inputwrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.IEntityProfile;

public class EntityProfile implements IEntityProfile {

	private String uri;
	private String type;

	private List<String> labels;
	private List<String> synonyms;

	public EntityProfile() {
		labels = Collections.synchronizedList(new ArrayList<String>());
		synonyms = Collections.synchronizedList(new ArrayList<String>());
	}

	public EntityProfile(String uri, String type) {
		this.uri = uri;
		this.type = type;

		labels = Collections.synchronizedList(new ArrayList<String>());
		synonyms = Collections.synchronizedList(new ArrayList<String>());
	}

	@Override
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void addLabel(String newLabel) {
		labels.add(newLabel);
	}

	public void addSynonym(String newSynonym) {
		synonyms.add(newSynonym);
	}

	@Override
	public List<String> getLabels() {
		return labels;
	}

	@Override
	public List<String> getSynonyms() {
		return synonyms;
	}
	
	@Override
	public String toString() {
		return uri + " (" + type + "): " + labels;
	}
}
