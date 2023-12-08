package core.alignment;

import java.util.Set;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.aggregation.AggregationService;
import core.model.KindStatementImpl;
import core.model.KindStatements;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/core/alignment")
public class AlignmentController {

	@Autowired
	AggregationService aggregationService;

	@Autowired
	AlignmentService alignmentService;
	
	@GetMapping("/ping")
	public Mono<String> ping() {
		return Mono.just("OK");
	}

	/**
	 * Performs Alignment over Aggregation KindStatement(s).
	 */
	@GetMapping("/performAlignment")
	public Mono<String> performAlignment() {
		String query = null;
		String[] rules = null;
//		System.out.println("performAlignment");
//		aggregationService.loadRepositoryStatements(query, rules);
//		System.out.println("contexts");
//		aggregationService.performContextKindsAggregation();
//		System.out.println("subjects");
//		aggregationService.performSubjectKindsAggregation();
//		System.out.println("properties");
//		aggregationService.performPropertyKindsAggregation();
//		System.out.println("objects");
//		aggregationService.performObjectKindsAggregation();
//		System.out.println("alignment");
//		Set<KindStatement> set = KindStatements.getInstance().getKindStatements(null, null, null, null);
//		alignmentService.performSubjectKindsAlignment(set);
		return Mono.just("OK");
	}

}
