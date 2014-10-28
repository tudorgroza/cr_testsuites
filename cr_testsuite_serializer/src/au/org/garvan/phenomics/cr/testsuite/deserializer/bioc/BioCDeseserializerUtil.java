package au.org.garvan.phenomics.cr.testsuite.deserializer.bioc;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import nu.xom.Node;

public class BioCDeseserializerUtil {

	public static List<Element> getElement(String elementName, Element parent) {
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

	public static Element getSingleElement(String elementName, Element parent) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			Node node = parent.getChild(i);
			if (node instanceof Element) {
				Element elem = (Element) node;
				if (elem.getQualifiedName().equalsIgnoreCase(elementName)) {
					return elem;
				}
			}
		}
		
		return null;
	}

}
