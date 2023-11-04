package fcalib.api.utils;

import java.util.List;

/**
 * Interface describing a SPARQL Query Generator for Wikidata.
 * This interface can be used to generate a SPARQL Query.
 * The resulting Query will always be represented as a String.
 * This interface only generates specific queries. For a more
 * generic query generation, use the SPARQLQueryBuilder in lib.utils.
 * Note: One can also write own Queries and save them
 * as a String. For more information on how to write own
 * SPARQL Queries, see <a href="https://www.w3.org/TR/rdf-sparql-query/">https://www.w3.org/TR/rdf-sparql-query/</a>
 *
 * Note: One can use arbitrary URIs of Properties/Entities. One can also
 * use prefixes that are not contained in the 'prefixes' String by simply extending it.
 * @author Leon Geis
 */
public interface SPARQLQueryGenerator {

    /**
     * The prefixes, which are used in all queries.
     */
    String prefixes =
            "PREFIX sc: <http://purl.org/science/owl/sciencecommons/>\n"+
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
            "PREFIX schema: <http://schema.org/>\n"+
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"+
            "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n"+
            "PREFIX wd: <http://www.wikidata.org/entity/>\n"+
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"+
            "PREFIX dbp: <http://dbpedia.org/property/>\n"+
            "PREFIX dbr: <http://dbpedia.org/resource/>\n"+
            "PREFIX dbo: <http://dbpedia.org/ontology/>\n"+
            "PREFIX yago: <http://yago-knowledge.org/resource/>\n";

    /**
     * Generates a SELECT Query with the Keyword UNION. This can be used to
     * query subjects (or objects), which share at least on of the properties in the
     * properties List.
     * Note: When passing a negative limit, the limit parameter will not be used.
     * @param properties List of properties.
     * @param limit Determines the amount of subjects (or objects), that are returned by the query.
     * @return The Query as a String.
     */
    static String generateSelectUnionQuery(List<String> properties, int limit){
        //Create String to be returned at the end
        String query = "";
        //First add the prefixes
        query += prefixes;
        //Add the Head of the query
        query += "SELECT DISTINCT * WHERE {";
        //Iterate over each property and add it to the query
        int i=1;
        for(String prop : properties){
            if(i == properties.size()){
                query += "{ ?s <"+prop+"> ?o.}} ";
            }else {
                query += "{ ?s <"+prop+"> ?o.} UNION ";
            }
            i++;
        }
        //At the end check if limit is specified
        if(limit>0){
            query += "LIMIT "+limit;
        }
        //Return generated query
        return query;
    }

    /**
     * Generates a ASK Query. This can be used to ask the repository, if the given
     * entity has the specified property.
     * @param entity Entity to be checked.
     * @param property Property used for the check.
     * @return The Query as a String.
     */
    static String generateAskQuery(String entity, String property){
        return prefixes+"ASK WHERE {<"+entity+"> <"+property+"> ?o.}";
    }

    /**
     * Generates a SELECT Query with the instance of Property of Wikidata (wdt:P31).
     * This can be used to e.g., retrieve all properties of a Property Class.
     * @param propertyClass The Property Class.
     * @return The Query as a String.
     * Note: Works only on the Wikidata KG.
     */
    static String generateSelectWikidataInstanceOfQuery(String propertyClass){
        return prefixes+"select * where {?s wdt:P31 <" + propertyClass + ">.}";
    }

    /**
     * Generates a SELECT Query one can use to query, which of the properties
     * out of the List, the entity has.
     * @param entity Entity to be checked.
     * @param properties List of properties to be used.
     * @return The Query as a String.
     */
    static String generateSelectPropertyCheckQuery(String entity, List<String> properties){
        //Create String to be returned
        String query = prefixes+"";
        //Add first part of the query
        query += "select * where{ <";
        //Add entity
        query += entity+"> ?p ?o. ";
        //Add BIND constraint
        query += "BIND ((";
        //For each property add the corresponding constraint
        for(String prop : properties){
            //If the current String is the last element add additional end of query
            if(properties.indexOf(prop)==properties.size()-1){
                query += "?p=<"+prop+">) AS ?result) FILTER (?result=true)}";
            }else {
                query += "?p=<" + prop + "> || ";
            }
        }
        return query;
    }

    /**
     * Generates a SELECT Query one can use to get all equivalent Properties, by using
     * the owl:equivalentProperty Keyword.
     * @param properties List of properties.
     * @return The Query as a String.
     */
    static String generateSelectPropertyCheckQuery(List<String> properties){
        //Create String to be returned
        String query = prefixes+"";
        //Add first part of the query
        query += "select * where{ ?s owl:equivalentProperty ?o.";
        //Add BIND constraint
        query += "BIND ((";
        //For each property add the corresponding constraint
        for(String prop : properties){
            //If the current String is the last element add additional end of query
            if(properties.indexOf(prop)==properties.size()-1){
                query += "?o=<"+prop+">) AS ?result) FILTER (?result=true)}";
            }else {
                query += "?o=<" + prop + "> || ";
            }
        }
        return query;
    }

    /**
     * Generates a SELECT Query, which can be used to query if the repository has
     * any subject, which is the same as the specified entity, by using the
     * owl:sameAs Keyword.
     * @param entity Entity to be used.
     * @return The Query as a String.
     */
    static String generateSelectSameAsQuery(String entity){
        return "SELECT * WHERE { ?s owl:sameAs <"+entity+">.}";
    }

    /**
     * Generates a SELECT Query, which can be used to query if the repository has
     * any property , which is the same as the specified entity, by using the
     * owl:equivalentProperty Keyword.
     * @param property Property to be used.
     * @return The Query as a String.
     */
    static String generateSelectEquivalentPropertyQuery(String property){
        return prefixes+"SELECT * WHERE {?s owl:equivalentProperty <"+property+"> .}";
    }

    /**
     * Generates a SELECT Query, by using the Wikidata Property (wdt:P1628). One can
     * use this query to get all properties, which are equivalent to the specified property.
     * Note: Works only on the Wikidata KG.
     * @param property Property to be used.
     * @return The Query as a String.
     */
    static String generateSelectWikidataEquivalentPropertyQuery(String property){
        //wdt:P1628 is equivalent to owl:equivalentProperty
        return "SELECT * WHERE { <"+property+"> wdt:P1628 ?o.}";
    }

}
