package au.org.garvan.phenomics.cr.testsuite.common.api.input;

import java.util.List;

public interface IEntityProfile {

	public String getUri();

	public String getType();

	public List<String> getLabels();

	public List<String> getSynonyms();
}
