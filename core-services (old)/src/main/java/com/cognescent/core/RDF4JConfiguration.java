package com.cognescent.core;

import org.eclipse.rdf4j.spring.RDF4JConfig;
import org.eclipse.rdf4j.spring.support.RDF4JTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cognescent.core.services.CognescentService;

@Configuration
@Import(RDF4JConfig.class)
public class RDF4JConfiguration {

	@Bean
	public CognescentService getCognescentService(@Autowired RDF4JTemplate template) {
		return new CognescentService(template);
	}
	
}
