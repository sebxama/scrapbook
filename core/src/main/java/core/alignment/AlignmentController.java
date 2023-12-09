package core.alignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.aggregation.AggregationService;
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

}
