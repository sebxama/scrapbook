package com.cognescent.core.controllers.aggregation;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognescent.core.services.aggregation.AggregationService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/core-service/api")
public class AggregationController {

	@Autowired
	AggregationService aggregationService;
	
	@PostMapping("/loadRDFData")
	public Mono<String> loadRDFData(String data, RDFFormat format) {
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
		aggregationService.loadRepositoryStatements(query, rules);
		aggregationService.performAggregation();
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
		aggregationService.performAggregation();
		return Mono.just("OK");
	}
	
}
