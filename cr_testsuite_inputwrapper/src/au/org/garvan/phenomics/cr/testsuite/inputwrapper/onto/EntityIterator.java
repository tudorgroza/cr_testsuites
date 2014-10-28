package au.org.garvan.phenomics.cr.testsuite.inputwrapper.onto;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.IEntityIterator;
import au.org.garvan.phenomics.cr.testsuite.inputwrapper.EntityProfile;
import au.org.garvan.phenomics.cr.testsuite.inputwrapper.spec.SpecDefinition;

public class EntityIterator implements IEntityIterator {

	private OWLOntology ontology;
	private SpecDefinition specDefinition;

	private Iterator<OWLClass> classIterator;
	private Iterator<OWLNamedIndividual> instanceIterator;
	private OWLObjectVisitor objectVisitor;
	
	public EntityIterator(OWLOntology ontology, SpecDefinition specDefinition) {
		this.ontology = ontology;
		this.specDefinition = specDefinition;
		this.objectVisitor = new OWLObjectVisitor();
		
		if (specDefinition.getConceptTypes().contains(SpecDefinition.CONCEPT_CLASS)) {
			this.classIterator = ontology.getClassesInSignature().iterator();
		}
		if (specDefinition.getConceptTypes().contains(SpecDefinition.CONCEPT_INSTANCE)) {
			this.instanceIterator = ontology.getIndividualsInSignature().iterator();
		}
	}
	
	@Override
	public boolean hasNext() {
		if ((classIterator == null) && (instanceIterator == null)) {
			return false;
		}
		
		if (classIterator != null) {
			if (!classIterator.hasNext()) {
				classIterator = null;
				
				if (instanceIterator == null) {
					return false;
				} else {
					if (!instanceIterator.hasNext()) {
						instanceIterator = null;
						return false;
					} else {
						return true;
					}
				}
			} else {
				return true;
			}
		} else {
			if (instanceIterator == null) {
				return false;
			} else {
				if (!instanceIterator.hasNext()) {
					instanceIterator = null;
					return false;
				} else {
					return true;
				}
			}
		}
	}

	@Override
	public EntityProfile next() {
		if (classIterator != null) {
			OWLClass cls = classIterator.next();
			return createEntityProfile(cls.getAnnotationAssertionAxioms(ontology), cls.getIRI(), SpecDefinition.CONCEPT_CLASS);
		} else {
			if (instanceIterator != null) {
				OWLNamedIndividual instance = instanceIterator.next();
				return createEntityProfile(instance.getAnnotationAssertionAxioms(ontology), instance.getIRI(), SpecDefinition.CONCEPT_INSTANCE);
			}
		}
		
		// SHOULD NEVER HAPPEN !!!
		return null;
	}

	private EntityProfile createEntityProfile(Set<OWLAnnotationAssertionAxiom> annotations, IRI iri, String conceptType) {
		EntityProfile entityProfile = new EntityProfile(iri.toString(), conceptType);
		
		for (OWLAnnotationAssertionAxiom annotationAxiom : annotations) {
			OWLAnnotationProperty annotationProperty = annotationAxiom.getProperty();
			OWLAnnotationValue annotationValue = annotationAxiom.getValue();
			annotationValue.accept(objectVisitor);
			String propertyURI = annotationProperty.getIRI().toString();
			String value = objectVisitor.getValue();

			if (specDefinition.hasLabelProperty(propertyURI)) {
				if (isValid(annotationAxiom, specDefinition.getLabelRestrictions(propertyURI))) {
					entityProfile.addLabel(value);
				}
			}
			if (specDefinition.hasSynonymProperty(propertyURI)) {
				if (isValid(annotationAxiom, specDefinition.getSynonymRestrictions(propertyURI))) {
					entityProfile.addSynonym(value);
				}
			}
		}
		
		return entityProfile;
	}

	private boolean isValid(OWLAnnotationAssertionAxiom annotationAxiom, Map<String, List<String>> restrictions) {
		if (restrictions.isEmpty()) {
			return true;
		}
		
		Set<OWLAnnotation> annotations = annotationAxiom.getAnnotations();
		for (OWLAnnotation annotation : annotations) {
			annotation.getValue().accept(objectVisitor);
			String annotPropertyURI = annotation.getProperty().getIRI().toString();
			String annotValue = objectVisitor.getValue();

			if (restrictions.containsKey(annotPropertyURI)) {
				List<String> list = restrictions.get(annotPropertyURI);
				if (list.contains(annotValue)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void remove() {
	}
}
