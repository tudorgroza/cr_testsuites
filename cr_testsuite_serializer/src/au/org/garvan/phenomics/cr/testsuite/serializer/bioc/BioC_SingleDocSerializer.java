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
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestCaseConcept;
import au.org.garvan.phenomics.cr.testsuite.common.api.testcase.ITestSuite;
import au.org.garvan.phenomics.cr.testsuite.serializer.util.BioC;

public class BioC_SingleDocSerializer {

	private ITestSuite testSuite;
	private String outputFolder;
	private String filePrefix;
	
	public BioC_SingleDocSerializer(ITestSuite testSuite, String outputFolder,
			String filePrefix) {
		this.testSuite = testSuite;
		this.outputFolder = outputFolder;
		this.filePrefix = filePrefix;
		
		run();
	}

	private void run() {
		Element textRoot = new Element(BioC.COLLECTION);
		addMetadata(textRoot, "cr_testsuite_text.key");

		Element annotRoot = new Element(BioC.COLLECTION);
		addMetadata(annotRoot, "cr_testsuite_annot.key");

		Map<String, List<ITestCaseConcept>> testCases = testSuite.getTestCases();
		for (String type : testCases.keySet()) {
			List<ITestCaseConcept> testCaseList = testCases.get(type);
			Element documentElement = createTextDocument(type, testCaseList);
			textRoot.appendChild(documentElement);
			
			documentElement = createAnnotDocument(type, testCaseList);
			annotRoot.appendChild(documentElement);
		}

		Document textDocument = new Document(textRoot);
		writeToDisk(textDocument, "text");

		Document annotDocument = new Document(annotRoot);
		writeToDisk(annotDocument, "annot");
	}
	
	private Element createTextDocument(String type, List<ITestCaseConcept> testCaseList) {
		Element documentElement = new Element(BioC.DOCUMENT);

		Element idElement = new Element(BioC.ID);
		idElement.appendChild(type);
		documentElement.appendChild(idElement);
		
		int lastOffset = -1;
		for (ITestCaseConcept testCase : testCaseList) {
			Element passageElement = new Element(BioC.PASSAGE);
			Element infonElement = new Element(BioC.INFON);
			Attribute key = new Attribute(BioC.A_KEY, BioC.A_V_TYPE);
			infonElement.addAttribute(key);
			infonElement.appendChild("label");
			passageElement.appendChild(infonElement);
			
			Element offsetElement = new Element(BioC.OFFSET);
			int offset = lastOffset == -1 ? 0 : lastOffset;
			offsetElement.appendChild(Integer.toString(offset));
			passageElement.appendChild(offsetElement);
			
			Element textElement = new Element(BioC.TEXT);
			textElement.appendChild(testCase.getTextualGrouding());
			lastOffset = offset + testCase.getTextualGrouding().length() + 1;
			passageElement.appendChild(textElement);

			documentElement.appendChild(passageElement);
		}
		
		return documentElement;
	}

	private Element createAnnotDocument(String type, List<ITestCaseConcept> testCaseList) {
		Element documentElement = new Element(BioC.DOCUMENT);

		Element idElement = new Element(BioC.ID);
		idElement.appendChild(type);
		documentElement.appendChild(idElement);
		
		int lastOffset = -1;
		int count = 1;
		for (ITestCaseConcept testCase : testCaseList) {
			Element passageElement = new Element(BioC.PASSAGE);
			Element infonElement = new Element(BioC.INFON);
			Attribute a_key = new Attribute(BioC.A_KEY, BioC.A_V_TYPE);
			infonElement.addAttribute(a_key);
			infonElement.appendChild("label");
			passageElement.appendChild(infonElement);
			
			Element offsetElement = new Element(BioC.OFFSET);
			int offset = lastOffset == -1 ? 0 : lastOffset;
			offsetElement.appendChild(Integer.toString(offset));
			passageElement.appendChild(offsetElement);

			Element annotElement = new Element(BioC.ANNOTATION);
			Attribute a_id = new Attribute(BioC.A_ID, type + "_" + Integer.toString(count));
			annotElement.addAttribute(a_id);
			
			Element annotInfonElement = new Element(BioC.INFON);
			Attribute infon_key = new Attribute(BioC.A_KEY, BioC.A_V_TERM);
			annotInfonElement.addAttribute(infon_key);
			annotInfonElement.appendChild(testCase.getUri());
			annotElement.appendChild(annotInfonElement);
			
			Element locationElement = new Element(BioC.INFON);
			Attribute a_offset = new Attribute(BioC.A_OFFSET, Integer.toString(offset));
			locationElement.addAttribute(a_offset);
			Attribute a_length = new Attribute(BioC.A_LENGTH, Integer.toString(testCase.getTextualGrouding().length()));
			locationElement.addAttribute(a_length);
			annotElement.appendChild(locationElement);
			
			Element textElement = new Element(BioC.TEXT);
			textElement.appendChild(testCase.getTextualGrouding());
			lastOffset = offset + testCase.getTextualGrouding().length() + 1;
			annotElement.appendChild(textElement);
			
			passageElement.appendChild(annotElement);
			documentElement.appendChild(passageElement);
			count++;
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
