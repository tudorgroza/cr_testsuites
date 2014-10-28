package au.org.garvan.phenomics.cr.testsuite.ncbo;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class NCBOParser {

	public static final String ANNOTATION = "annotation";
	public static final String ANNOTATEDCLASS = "annotatedClass";
	public static final String ID = "id";
	public static final String ANNOTATIONSCOLLECTION = "annotationsCollection";
	public static final String ANNOTATIONS = "annotations";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String TEXT = "text";

	private List<NCBOConcept> concepts;
	
	public NCBOParser(String content) {
		concepts = new ArrayList<NCBOConcept>();
		parse(content);
	}

	private void parse(String content) {
		try {
			Builder parser = new Builder();
			Document doc = parser.build(new StringReader(content));
			Element root = doc.getRootElement();

			List<Element> elements = getElement(ANNOTATION, root);
			for (Element element : elements) {
				parseElement(element);
			}
		} catch (ValidityException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseElement(Element element) {
		List<Element> elements = getElement(ANNOTATEDCLASS, element);
		Element annotClassElement = elements.get(0);
		
		List<Element> uriElements = getElement(ID, annotClassElement);
		String uri = uriElements.get(0).getValue();
		
		String shortURI = shortURI(uri);
		String source = getSource(uri);

		if (source != null) {
			List<Element> collection = getElement(ANNOTATIONSCOLLECTION, element);
			List<Element> actualAnnotationElements = getElement(ANNOTATIONS, collection.get(0));
	
			for (Element actualAnnotationElement : actualAnnotationElements) {
				NCBOConcept concept = parseAnnotationElement(actualAnnotationElement);
				
				concept.setSource(source);
				concept.setConceptURI(shortURI);
				if (!concepts.contains(concept)) {
					concepts.add(concept);
				}
			}
		}
	}

	private String getSource(String uri) {
		String namespace;
		String shortURI;
		if (uri.contains("#")) {
			int index = uri.lastIndexOf("#");
			namespace = uri.substring(0, index);
			shortURI = uri.substring(index + 1);
		} else {
			int index = uri.lastIndexOf("/");
			namespace = uri.substring(0, index);
			shortURI = uri.substring(index + 1);
		}

		if (namespace.equalsIgnoreCase("http://purl.org/obo/owl/GO")) {
		}
		
		if (namespace.equalsIgnoreCase("http://purl.obolibrary.org/obo")) {
			if (shortURI.startsWith("GO_")) {
				return "GO";
			}
		}
		
		return null;
	}

	private String shortURI(String uri) {
		if (uri.contains("#")) {
			int index = uri.lastIndexOf("#");
			return uri.substring(index + 1);
		} else {
			int index = uri.lastIndexOf("/");
			return uri.substring(index + 1);
		}
	}

	private NCBOConcept parseAnnotationElement(Element actualAnnotationElement) {
		List<Element> fromElement = getElement(FROM, actualAnnotationElement);
		List<Element> toElement = getElement(TO, actualAnnotationElement);
		List<Element> textElement = getElement(TEXT, actualAnnotationElement);
		
		NCBOConcept concept = new NCBOConcept();
		concept.setStart(Long.parseLong(fromElement.get(0).getValue()));
		concept.setEnd(Long.parseLong(toElement.get(0).getValue()) + 1);
		concept.setText(textElement.get(0).getValue());
		
		return concept;
	}

	private List<Element> getElement(String elementName, Element parent) {
		List<Element> list = new ArrayList<Element>();
		for (int i = 0; i < parent.getChildCount(); i++) {
			Node node = parent.getChild(i);
			if (node instanceof Element) {
				Element elem = (Element) node;
				if (elem.getQualifiedName().equalsIgnoreCase(elementName)) {
					list.add(elem);
				}
			}
		}
		
		return list;
	}

	public List<NCBOConcept> getConcepts() {
		return concepts;
	}
}
