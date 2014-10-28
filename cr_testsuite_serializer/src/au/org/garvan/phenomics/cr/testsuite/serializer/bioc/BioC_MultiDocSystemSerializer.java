package au.org.garvan.phenomics.cr.testsuite.serializer.bioc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.Offset;
import au.org.garvan.phenomics.cr.testsuite.common.api.deserializer.SystemSerializedEntry;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.impl.TestCaseConcept;
import au.org.garvan.phenomics.cr.testsuite.serializer.util.BioC;

public class BioC_MultiDocSystemSerializer {

	Map<String, Map<String, SystemSerializedEntry>> systemEntries;
	private String outputFolder;
	private String filePrefix;
	
	public BioC_MultiDocSystemSerializer(Map<String, Map<String, SystemSerializedEntry>> systemEntries, String outputFolder,
			String filePrefix) {
		this.systemEntries = systemEntries;
		this.outputFolder = outputFolder;
		this.filePrefix = filePrefix;
		
		run();
	}

	private void run() {
		for (String type : systemEntries.keySet()) {
			Map<String, SystemSerializedEntry> sysEntry = systemEntries.get(type);
			generate(type, sysEntry);
		}
	}
	
	private void generate(String type, Map<String, SystemSerializedEntry> sysEntry) {
		Element annotRoot = new Element(BioC.COLLECTION);
		addMetadata(annotRoot, "cr_testsuite_annot.key");

		Element documentElement = createAnnotDocument(type, sysEntry);
		annotRoot.appendChild(documentElement);
		
		Document annotDocument = new Document(annotRoot);
		writeToDisk(annotDocument, type + "_annot");
	}

	private Element createAnnotDocument(String type, Map<String, SystemSerializedEntry> sysEntry) {
		Element documentElement = new Element(BioC.DOCUMENT);

		Element idElement = new Element(BioC.ID);
		idElement.appendChild(type);
		documentElement.appendChild(idElement);

		for (String annot_id : sysEntry.keySet()) {
			SystemSerializedEntry entry = sysEntry.get(annot_id);
			for (Offset offset : entry.getTestCaseConcepts().keySet()) {
				List<TestCaseConcept> list = entry.getTestCaseConcepts().get(offset);				
				
				for (TestCaseConcept concept : list) {
					Element passageElement = new Element(BioC.PASSAGE);
					Element infonElement = new Element(BioC.INFON);
					Attribute a_key = new Attribute(BioC.A_KEY, BioC.A_V_TYPE);
					infonElement.addAttribute(a_key);
					infonElement.appendChild("annotation");
					passageElement.appendChild(infonElement);
					
					Element offsetElement = new Element(BioC.OFFSET);
					offsetElement.appendChild(Integer.toString(offset.getStart()));
					passageElement.appendChild(offsetElement);

					Element annotElement = new Element(BioC.ANNOTATION);
					Attribute a_id = new Attribute(BioC.A_ID, annot_id);
					annotElement.addAttribute(a_id);
					
					Element annotInfonElement = new Element(BioC.INFON);
					Attribute infon_key = new Attribute(BioC.A_KEY, BioC.A_V_TERM);
					annotInfonElement.addAttribute(infon_key);
					annotInfonElement.appendChild(concept.getUri());
					annotElement.appendChild(annotInfonElement);
					
					Element locationElement = new Element(BioC.INFON);
					Attribute a_offset = new Attribute(BioC.A_OFFSET, Integer.toString(offset.getStart()));
					locationElement.addAttribute(a_offset);
					Attribute a_length = new Attribute(BioC.A_LENGTH, Integer.toString(offset.getEnd() - offset.getStart()));
					locationElement.addAttribute(a_length);
					annotElement.appendChild(locationElement);
					
					Element textElement = new Element(BioC.TEXT);
					textElement.appendChild(concept.getTextualGrouding());
					annotElement.appendChild(textElement);
					
					passageElement.appendChild(annotElement);
					documentElement.appendChild(passageElement);
				}
			}
		}
		
		return documentElement;
	}
	private void addMetadata(Element root, String keyFile) {
		Element source = new Element(BioC.SOURCE);
		source.appendChild("CR Test Suite creator");
		root.appendChild(source);
		
		Element date = new Element(BioC.DATE);
		Date now = new Date();
		String format = new SimpleDateFormat("yyyyMMdd").format(now);
		date.appendChild(format);
		root.appendChild(date);
		
		Element key = new Element(BioC.KEY);
		key.appendChild(keyFile);
		root.appendChild(key);
	}

	private void writeToDisk(Document document, String type) {
		String outFolder = outputFolder.endsWith("/") ? outputFolder : outputFolder + "/";
		try {
			Serializer serializer = new Serializer(new FileOutputStream(
					outFolder + filePrefix + "_" + type + ".xml"), "UTF-8");
			serializer.setIndent(4);
			serializer.setMaxLength(400);
			serializer.write(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
