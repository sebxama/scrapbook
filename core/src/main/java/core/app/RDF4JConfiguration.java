package core.app;

import org.eclipse.rdf4j.spring.RDF4JConfig;
import org.eclipse.rdf4j.spring.support.RDF4JTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import core.aggregation.AggregationService;

@Configuration
@Import(RDF4JConfig.class)
public class RDF4JConfiguration {

	@Bean
	public AggregationService getAggregationService(@Autowired RDF4JTemplate template) {
		return new AggregationService(template);
	}
	
}
