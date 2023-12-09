package core.alignment;

import org.eclipse.rdf4j.spring.support.RDF4JTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Rules: Augmentation.
 * Rules: Alignment (upper onto)
 * Rules: Matching (merge)
 */
@Service
public class AlignmentService {

	private RDF4JTemplate rdf4jTemplate;
	
	public AlignmentService(@Autowired RDF4JTemplate rdf4jTemplate) {
		this.rdf4jTemplate = rdf4jTemplate;
	}
	
}
