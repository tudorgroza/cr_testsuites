package au.org.garvan.phenomics.cr.testsuite.ncbo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.SystemSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.TestCaseSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.impl.TestCaseConcept;
import au.org.garvan.phenomics.cr.testsuite.deserializer.bioc.BioC_MultiDocTestSuiteDeserializer;
import au.org.garvan.phenomics.cr.testsuite.serializer.bioc.BioC_MultiDocSystemSerializer;

public class NCBOAnnotate {

	private Map<String, Map<String, TestCaseSerializedEntry>> tsEntries;
	private NCBOAnnotator annotator;

	private Map<String, Map<String, SystemSerializedEntry>> systemEntries;

	public NCBOAnnotate() {
		BioC_MultiDocTestSuiteDeserializer deserializer = new BioC_MultiDocTestSuiteDeserializer("/home/tudor/EXTRA_SPACE/NEW_TestSuite/ts", "go");
		tsEntries = deserializer.deserialize();
		
		systemEntries = new HashMap<String, Map<String,SystemSerializedEntry>>();
		
		annotator = new NCBOAnnotator(new String[] { NCBOAnnotator.ONTO_HP }, null, 0);

		for (String cat : tsEntries.keySet()) {
			System.out.println(" - Category: " + cat);
			Map<String, TestCaseSerializedEntry> catEntries = tsEntries.get(cat);
			Map<String, SystemSerializedEntry> sysEntries = annotate(catEntries);
			
			systemEntries.put(cat, sysEntries);
		}
		
		System.out.println(" - Serializing results ...");
		new BioC_MultiDocSystemSerializer(systemEntries, "/home/tudor/EXTRA_SPACE/NEW_TestSuite/annotations/go", "go");
	}

	private Map<String, SystemSerializedEntry> annotate(Map<String, TestCaseSerializedEntry> catEntries) {
		Map<String, SystemSerializedEntry> sysEntries = new HashMap<String, SystemSerializedEntry>();

		for (String id : catEntries.keySet()) {
			TestCaseSerializedEntry entry = catEntries.get(id);
			String annotResult = annotator.analyzeText(entry.getTestCaseConcept().getTextualGrouding());
			SystemSerializedEntry systemEntry = new SystemSerializedEntry(id);

			NCBOParser parser = new NCBOParser(annotResult);
			List<NCBOConcept> concepts = parser.getConcepts();
			for (NCBOConcept concept : concepts) {
				TestCaseConcept tcr = new TestCaseConcept();
				
				tcr.setOriginalLabel(concept.getText());
				tcr.setTextualGrouding(concept.getText());
				tcr.setUri(concept.getConceptURI());

				systemEntry.addTestCaseConcept(entry.getOffset(), tcr);
			}
			
			sysEntries.put(id, systemEntry);
		}
		
		return sysEntries;
	}
	
	public static void main(String[] args) {
		new NCBOAnnotate();
	}
}
