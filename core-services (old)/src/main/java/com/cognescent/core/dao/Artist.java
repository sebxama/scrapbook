package com.cognescent.core.dao;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;

public class Artist {
    // recommended pattern: use a public Variable constant for each of the entities fields 
    // for use in queries and result processing. 
	public static final Variable ARTIST_ID = SparqlBuilder.var("artist_id"); 
	public static final Variable ARTIST_FIRST_NAME = SparqlBuilder.var("artist_firstName");
	public static final Variable ARTIST_LAST_NAME = SparqlBuilder.var("artist_lastName");
	private IRI id;
	private String firstName;
	private String lastName;
    // getter, setter, constructor, ...
    // be sure to implement equals() and hashCode() for proper behaviour of collections!
}
