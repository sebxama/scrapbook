package core.aggregation;

import java.util.Map;
import java.util.Set;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.model.Statement;
import core.model.Subject;
import core.model.SubjectKind;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/core/aggregation")
public class AggregationController {

	@Autowired
	AggregationService aggregationService;

	@GetMapping("/ping")
	public Mono<String> ping() {
		return Mono.just("OK");
	}
	
	@PostMapping("/loadRepository")
	public Mono<String> loadRepository(String data, RDFFormat format) {
		aggregationService.loadRDFData(data, format);
		return Mono.just("OK");
	}

	/**
	 * Performs Aggregation over RDF4J Repository Server configured in application.properties.
	 * Use RDF4J Workbench to create / load data into Repository.
	 */
	@GetMapping("/performAggregation")
	public Mono<String> performAggregation() {
		String query = null;
		String[] rules = null;
		System.out.println("performAggregation");
		aggregationService.loadRepositoryStatements(query, rules);
		aggregationService.performContextKindsAggregation();
		aggregationService.performSubjectKindsAggregation();
		aggregationService.performPropertyKindsAggregation();
		aggregationService.performModelObjectKindsAggregation();
		return Mono.just("OK");
	}

	/**
	 * Performs Aggregation over RDF4J Repository Server configured in application.properties.
	 * Use RDF4J Workbench to create / load data into Repository.
	 * @param query: SPARQL query to filter Repository.
	 * @param rules: String array of SPARQL Rules to materialize from Repository.
	 */
	@GetMapping("/performAggregationForQueryAndRules")
	public Mono<String> performAggregationForQueryAndRules(@RequestParam("query") String query, @RequestParam("rules") String[] rules) {
		// FIXME:
		query = null;
		rules = null;
		aggregationService.loadRepositoryStatements(query, rules);
		aggregationService.performSubjectKindsAggregation();
		return Mono.just("OK");
	}
	
}
