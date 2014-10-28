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
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.SystemSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.impl.TestCaseConcept;
import au.org.garvan.phenomics.cr.testsuite.serializer.util.BioC;

public class BioC_MultiDocSystemDeserializer {

	private Map<String, Map<String, SystemSerializedEntry>> systemAnnotations;

	public BioC_MultiDocSystemDeserializer(String inputFolder, String filePrefix) {
		systemAnnotations = new HashMap<String, Map<String,SystemSerializedEntry>>();

		File[] files = new File(inputFolder).listFiles();
		for (File file : files) {
			if (file.getName().startsWith(filePrefix) && isAnnotationFile(file)) {
				readSystemEntry(file);
			}
		}
	}

	private void readSystemEntry(File file) {
		Builder parser = new Builder();
		try {
			Document doc = parser.build(file);
			Element documentElement = BioCDeseserializerUtil.getSingleElement(BioC.DOCUMENT, doc.getRootElement());
			
			Element id = BioCDeseserializerUtil.getSingleElement(BioC.ID, documentElement);
			Map<String, SystemSerializedEntry> sysEntries = new HashMap<String, SystemSerializedEntry>();
			
			List<Element> passages = BioCDeseserializerUtil.getElement(BioC.PASSAGE, documentElement);
			for (Element passage : passages) {
				parsePassage(passage, sysEntries);
			}
			
			systemAnnotations.put(id.getValue(), sysEntries);
		} catch (ValidityException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parsePassage(Element passage,
			Map<String, SystemSerializedEntry> sysEntries) {
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
		
		SystemSerializedEntry sysEntry = sysEntries.containsKey(annotationId) ? sysEntries.get(annotationId) : new SystemSerializedEntry(annotationId);
		sysEntry.addTestCaseConcept(offset, tcResult);
		sysEntries.put(annotationId, sysEntry);
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

	public Map<String, Map<String, SystemSerializedEntry>> deserialize() {
		return systemAnnotations;
	}

}
