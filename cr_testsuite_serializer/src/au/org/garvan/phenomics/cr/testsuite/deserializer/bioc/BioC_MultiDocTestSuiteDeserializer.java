package au.org.garvan.phenomics.cr.testsuite.deserializer.bioc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.Offset;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.TestCaseSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.impl.TestCaseConcept;
import au.org.garvan.phenomics.cr.testsuite.serializer.util.BioC;

public class BioC_MultiDocTestSuiteDeserializer {

	private Map<String, Map<String, TestCaseSerializedEntry>> tsEntries;
	
	public BioC_MultiDocTestSuiteDeserializer(String inputFolder, String filePrefix) {
		tsEntries = new HashMap<String, Map<String,TestCaseSerializedEntry>>();
		
		File[] files = new File(inputFolder).listFiles();
		for (File file : files) {
			if (file.getName().startsWith(filePrefix) && isAnnotationFile(file)) {
				readTSEntry(file);
			}
		}
	}

	private void readTSEntry(File file) {
		Builder parser = new Builder();
		try {
			Document doc = parser.build(file);
			Element documentElement = BioCDeseserializerUtil.getSingleElement(BioC.DOCUMENT, doc.getRootElement());
			
			Element id = BioCDeseserializerUtil.getSingleElement(BioC.ID, documentElement);
			Map<String, TestCaseSerializedEntry> testCases = new HashMap<String, TestCaseSerializedEntry>();
			
			List<Element> passages = BioCDeseserializerUtil.getElement(BioC.PASSAGE, documentElement);
			for (Element passage : passages) {
				parsePassage(passage, testCases);
			}
			
			tsEntries.put(id.getValue(), testCases);
		} catch (ValidityException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parsePassage(Element passage, Map<String, TestCaseSerializedEntry> testCases) {
		Element annotElement = BioCDeseserializerUtil.getSingleElement(BioC.ANNOTATION, passage);
		String annotationId = annotElement.getAttributeValue(BioC.A_ID);
		
		TestCaseConcept tcResult = new TestCaseConcept();
		Offset offset = new Offset();
		
		for (int i = 0; i < annotElement.getChildCount(); i++) {
			Node node = annotElement.getChild(i);
			if (node instanceof Element) {
				Element elem = (Element) node;
				if (elem.getQualifiedName().equalsIgnoreCase(BioC.INFON)) {
					String key = elem.getAttributeValue(BioC.A_KEY);
					if (key == null) {
						int start = Integer.parseInt(elem.getAttributeValue(BioC.A_OFFSET));
						int length = Integer.parseInt(elem.getAttributeValue(BioC.A_LENGTH));
						
						offset.setStart(start);
						offset.setEnd(start + length);
					} else {
						tcResult.setUri(elem.getValue());
					}
				}
				if (elem.getQualifiedName().equalsIgnoreCase(BioC.TEXT)) {
					tcResult.setOriginalLabel(elem.getValue());
					tcResult.setTextualGrouding(elem.getValue());
				}
			}
		}

		testCases.put(annotationId, new TestCaseSerializedEntry(annotationId, offset, tcResult));
	}

	private boolean isAnnotationFile(File file) {
		String fileName = file.getName();
		int index = fileName.lastIndexOf(".");
		if (index != -1) {
			fileName = fileName.substring(0, index);
			return fileName.endsWith("_annot");
		}
		return false;
	}

	public Map<String, Map<String, TestCaseSerializedEntry>> deserialize() {
		return tsEntries;
	}

}
