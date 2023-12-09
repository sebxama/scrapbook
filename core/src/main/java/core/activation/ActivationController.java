package core.activation;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.aggregation.AggregationService;
import core.alignment.AlignmentService;
import reactor.core.publisher.Mono;

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

}
