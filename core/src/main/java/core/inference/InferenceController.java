package core.inference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.aggregation.AggregationService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/core/inference")
public class InferenceController {

	@Autowired
	AggregationService aggregationService;

	@Autowired
	InferenceService inferenceService;
	
	@GetMapping("/ping")
	public Mono<String> ping() {
		return Mono.just("OK");
	}

	@GetMapping("/performInference")
	public Mono<String> performInference() {
		inferenceService.performInference();
		return Mono.just("OK");
	}	
	
}
