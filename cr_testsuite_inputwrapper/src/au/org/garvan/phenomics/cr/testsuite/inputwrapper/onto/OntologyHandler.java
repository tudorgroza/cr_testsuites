package au.org.garvan.phenomics.cr.testsuite.inputwrapper.onto;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import au.org.garvan.phenomics.cr.testsuite.common.api.input.IEntityIterator;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.exception.DataSourceLoadingException;
import au.org.garvan.phenomics.cr.testsuite.common.api.input.impl.DataSourceMetadata;
import au.org.garvan.phenomics.cr.testsuite.common.util.CommonUtil;
import au.org.garvan.phenomics.cr.testsuite.inputwrapper.spec.SpecDefinition;
import au.org.garvan.phenomics.cr.testsuite.inputwrapper.spec.SpecReader;
import au.org.garvan.phenomics.cr.testsuite.log.TestSuiteLogger;

public class OntologyHandler {

	@SuppressWarnings("unchecked")
	private static TestSuiteLogger<OntoDataWrapper> logger = (TestSuiteLogger<OntoDataWrapper>) TestSuiteLogger
			.getLogger(OntoDataWrapper.class);

	private OWLOntologyManager manager;
	private OWLOntology ontology;
	private SpecDefinition specDefinition;

	private DataSourceMetadata metadata;
	private String errorMessage;
	private boolean ready;
	
	public OntologyHandler(DataSourceMetadata metadata) {
		this.metadata = metadata;
		this.manager = OWLManager.createOWLOntologyManager();
		this.errorMessage = null;

		loadOntology();
	}
	
	private void loadOntology() {
		IRI iri = IRI.create(new File(metadata.getLocation()));
		
		try {
			double sTime = System.currentTimeMillis();
			ontology = manager.loadOntologyFromOntologyDocument(iri);
			double eTime = System.currentTimeMillis();
			double totalTime = (eTime - sTime) / 1000;
			String formatTime = CommonUtil.formatTime(totalTime);
			logger.info("Ontology loaded [" + formatTime + "] ...");

			specDefinition = new SpecReader(metadata.getSpecFile()).getSpecDefinition();
			if (specDefinition == null) {
				errorMessage = "Unable to process the spec definition file: " + metadata.getSpecFile();
				ready = false;
			}
			
			if (specDefinition.getConceptTypes().isEmpty()) {
				errorMessage = "The spec file needs to specify at least one type of concept to be processed (e.g., CLASS or INSTANCE).";
				ready = false;
			}

			ready = true;
		} catch (OWLOntologyCreationException e) {
			errorMessage = "Ontology parsing error: " + e.getMessage();
			ready = false;
		}
	}

	public IEntityIterator getEntityIterator() throws DataSourceLoadingException {
		if (!ready) {
			throw new DataSourceLoadingException(errorMessage);
		}
		return new EntityIterator(ontology, specDefinition);
	}
}
