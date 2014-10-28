package au.org.garvan.phenomics.cr.testsuite.ncbo;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class NCBOAnnotator {

	public static final String ONTO_HP = "1125";

	private static final String DEFAULT_NCBOURL = "http://data.bioontology.org/annotator";

	private HttpClient httpClient;
	private String ontologiesToKeep;

	public NCBOAnnotator(String[] ontologies, String proxyHost, int proxyPort) {
		httpClient = new HttpClient();
		if (proxyHost != null) {
			httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
		}
		httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,
				"Annotator Client Example - Annotator");

		ontologiesToKeep = "";
		for (String onto : ontologies) {
			ontologiesToKeep += onto + ",";
		}
		if (!ontologiesToKeep.trim().equalsIgnoreCase("")) {
			ontologiesToKeep = ontologiesToKeep.substring(0,
				ontologiesToKeep.length() - 1);
		}
	}

	public String analyzeText(String text) {
		try {
			PostMethod method = new PostMethod(DEFAULT_NCBOURL);

//			method.addParameter("ontologies", "http://data.bioontology.org/ontologies/HP");
			method.addParameter("text", text);
			method.addParameter("format", "xml");
			method.addParameter("apikey",
					"24e68634-54e0-11e0-9d7b-005056aa3316");

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != -1) {
				try {
					String contents = method.getResponseBodyAsString();
					method.releaseConnection();
					return contents;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static void main(String[] args) {
		String test = "T-helper 1 cell differentiation";
		NCBOAnnotator annotator = new NCBOAnnotator(new String[] { ONTO_HP },
				null, 0);
		String annotResult = annotator.analyzeText(test);
		System.out.println(annotResult);
	}

}
