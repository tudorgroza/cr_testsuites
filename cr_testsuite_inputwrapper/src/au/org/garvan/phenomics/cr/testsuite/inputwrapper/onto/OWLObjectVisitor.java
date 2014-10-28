package au.org.garvan.phenomics.cr.testsuite.inputwrapper.onto;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationValueVisitorEx;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;

public class OWLObjectVisitor implements OWLAnnotationValueVisitorEx<Object> {

	private String value;
	
	public OWLObjectVisitor() {
	}

	@Override
	public Object visit(IRI iri) {
		return iri;
	}

	@Override
	public Object visit(OWLAnonymousIndividual individual) {
		return individual;
	}

	@Override
	public Object visit(OWLLiteral literal) {
		value = literal.getLiteral();
		return literal;
	}

	public String getValue() {
		return value;
	}
}
