package com.cognescent.core.dao;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.spring.dao.SimpleRDF4JCRUDDao;
import org.eclipse.rdf4j.spring.support.RDF4JTemplate;
import org.springframework.stereotype.Component;

@Component // again, make it a component (see above) 
public class SimpleArtistDao /* extends SimpleRDF4JCRUDDao<Artist, IRI> */ {

/*
	
	public SimpleArtistDao(RDF4JTemplate rdf4JTemplate) {
		super(rdf4JTemplate);
	}
	
	@Override
	protected void populateIdBindings(MutableBindings bindingsBuilder, IRI iri) {
		bindingsBuilder.add(ARTIST_ID, iri);
	}
	
	@Override
	protected void populateBindingsForUpdate(MutableBindings bindingsBuilder, Artist artist) {
		bindingsBuilder
				.add(ARTIST_FIRST_NAME, artist.getFirstName())
				.add(ARTIST_LAST_NAME, artist.getLastName());
	}
	
	@Override
	protected Artist mapSolution(BindingSet querySolution) {
		Artist artist = new Artist();
		artist.setId(QueryResultUtils.getIRI(querySolution, ARTIST_ID));
		artist.setFirstName(QueryResultUtils.getString(querySolution, ARTIST_FIRST_NAME));
		artist.setLastName(QueryResultUtils.getString(querySolution, ARTIST_LAST_NAME));
		return artist;
	}
	
	@Override
	protected String getReadQuery() {
		return "prefix foaf: <http://xmlns.com/foaf/0.1/> "
				+ "prefix ex: <http://example.org/> "
				+ "SELECT ?artist_id ?artist_firstName ?artist_lastName where {"
				+ "?artist_id a ex:Artist; "
				+ "    foaf:firstName ?artist_firstName; "
				+ "    foaf:surname ?artist_lastName ."
				+ " } ";
	}

	@Override
	protected NamedSparqlSupplier getInsertSparql(Artist artist) {
		return NamedSparqlSupplier.of("insert", () -> Queries.INSERT(ARTIST_ID.isA(iri(EX.Artist))
				.andHas(iri(FOAF.FIRST_NAME), ARTIST_FIRST_NAME)
				.andHas(iri(FOAF.SURNAME), ARTIST_LAST_NAME))
				.getQueryString());
	}
	
	@Override
	protected IRI getInputId(Artist artist) {
		if (artist.getId() == null) {
			return getRdf4JTemplate().getNewUUID();
		}
		return artist.getId();
	}

//	It is not uncommon for an application to read a relation present in the repository data into a Map. For example, we might want to group painting ids by artist id. The RelationMapBuilder provides the necesary functionality for such cases:
//
//	RelationMapBuilder b = new RelationMapBuilder(getRDF4JTemplate(), EX.creatorOf);
//	Map<IRI, Set<IRI>> paintingsByArtists = b.buildOneToMany();

//	The RDF4JCRUDDao is essentially the same as the SimpleRDF4JCRUDDao, with the one difference that it has three type parameters, ENTITY, INPUT, and ID. The class thus allows different classes for input and output: creation and updates use INPUT, e.g. save(INPUT), reading methods use ENTITY, e.g. ENTITY getById(ID).

*/
	
}
