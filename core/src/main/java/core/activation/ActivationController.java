package core.activation;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.aggregation.AggregationService;
import core.alignment.AlignmentService;
import core.model.KindStatement;
import reactor.core.publisher.Mono;

// Infer state / flow: contexts / interactions.
@RestController
@RequestMapping("/core/activation")
public class ActivationController {

	@Autowired
	AggregationService aggregationService;

	@Autowired
	AlignmentService alignmentService;

	@Autowired
	ActivationService activationService;
	
	@GetMapping("/ping")
	public Mono<String> ping() {
		return Mono.just("OK");
	}

	@GetMapping("/performActivation")
	public Mono<String> performActivation() {
		String query = null;
		String[] rules = null;
		System.out.println("performActivation");
		aggregationService.loadRepositoryStatements(query, rules);
		aggregationService.performContextKindsAggregation();
		aggregationService.performSubjectKindsAggregation();
		aggregationService.performPropertyKindsAggregation();
		aggregationService.performObjectKindsAggregation();
		Set<KindStatement> set = KindStatements.getInstance().getKindStatements();
		alignmentService.performSubjectKindsAlignment(set);
		activationService.performSubjectKindsActivation(set);
		return Mono.just("OK");
	}
	
}
