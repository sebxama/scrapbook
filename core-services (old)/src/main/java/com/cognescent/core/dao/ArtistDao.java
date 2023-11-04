package com.cognescent.core.dao;

import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.spring.dao.RDF4JDao;
import org.eclipse.rdf4j.spring.support.RDF4JTemplate;
import org.springframework.stereotype.Component;

@Component // make the DAO a spring component so it's auto-detected in the classpath scan
public class ArtistDao  /* extends RDF4JDao */ {

/*
	
	public ArtistDao(RDF4JTemplate rdf4JTemplate) {
		super(rdf4JTemplate);
	}

    // recommended: encapsulate the keys for queries in an object
    // so it's easier to find them when you need them
	static abstract class QUERY_KEYS {
		public static final String ARTISTS_WITHOUT_PAINTINGS = "artists-without-paintings";
	}

    // prepare the named queries, assigning each one of the keys
	@Override
	protected NamedSparqlSupplierPreparer prepareNamedSparqlSuppliers(NamedSparqlSupplierPreparer preparer) {
		return preparer
            .forKey(QUERY_KEYS.ARTISTS_WITHOUT_PAINTINGS)
            .supplySparql(Queries.SELECT(
                            ARTIST_ID)
                            .where(
                                ARTIST_ID.isA(iri(EX.Artist))
                                .and(ARTIST_ID.has(iri(EX.creatorOf), Painting.PAINTING_ID).optional())
                                .filter(not(bound(Painting.PAINTING_ID)))).getQueryString()
            );
	}

    // use the named query with getNamedTupleQuery(String)
	public Set<Artist> getArtistsWithoutPaintings(){
		return getNamedTupleQuery(QUERY_KEYS.ARTISTS_WITHOUT_PAINTINGS)
						.evaluateAndConvert()
						.toStream()
						.map(bs -> QueryResultUtils.getIRI(bs, ARTIST_ID))
						.map(iri -> getById(iri))
						.collect(Collectors.toSet());
	}
    
    // ...

*/

}
