package com.cognescent.core.services;

import org.eclipse.rdf4j.spring.support.RDF4JTemplate;

/*
 * Parent Services helper methods.
 * Streams / Embedding (FCA: Formal Concept Analysis).
 */
public class CognescentService {	
	
	private RDF4JTemplate template;
	
	public CognescentService(RDF4JTemplate template) {
		this.template = template;
	}
	
	public RDF4JTemplate getTemplate() {
		return this.template;
	}
	
}
