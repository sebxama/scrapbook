package core.inference;

import java.util.Set;

import org.eclipse.rdf4j.spring.support.RDF4JTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.model.Kind;
import core.model.Resource;
import core.model.Statement;
import core.model.Statements;
import core.model.Subject;
import reactor.core.publisher.Flux;

/**
 * Rules: Augmentation.
 * Rules: Alignment (upper onto)
 * Rules: Matching (merge)
 */
@Service
public class InferenceService {

	private RDF4JTemplate rdf4jTemplate;
	
	public InferenceService(@Autowired RDF4JTemplate rdf4jTemplate) {
		this.rdf4jTemplate = rdf4jTemplate;
	}
	
	public void performInference() {
		
		Set<Statement> stats = Statements.getInstance().getStatements();

		// TODO: Getters as Functions. Augment getters inferences with Rule's Grammar
		//       Contexts / Concepts (i.e.: Kinds transforms / order / relationships).
		
		StatementSubjectRule subject = new StatementSubjectRule();
		ResourceOccurrenceKindRule kind = new ResourceOccurrenceKindRule();
		KindResourceRule resource = new KindResourceRule();
		
		System.out.println("INFERENCE:");
		
		Flux<Statement> flux = Flux.fromIterable(stats);
		flux
		.map(subject)
		.map(kind)
		.map(resource)
		.map(Resource::getIRI)
		.subscribe(System.out::println);
		
	}
	
}
