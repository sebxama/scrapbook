package fcalib.api.utils;

import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;

/**
 * Interface describing the access to a Knowledge Graph (KG),
 * via the SPARQL-Endpoint.
 * Note: The implementation of this interface makes
 * extensive use of the RDF4J library to connect
 * to the SPARQL-Endpoint of a KG. Hence, a lot
 * of the return types being Objects from that library.
 * For more information on the RDF4J library,
 * see <a href="https://rdf4j.org/documentation/">https://rdf4j.org/documentation/</a>
 * @author Leon Geis
 */
public interface SPARQLEndpointAccess {

    /**
     * Set URL of SPARQL Endpoint.
     * @param sparqlEndpoint String of the URL Endpoint.
     */
    void setSparqlEndpoint(String sparqlEndpoint);

    /**
     * Get currently used SPARQL Endpoint URL.
     * @return URL as a String.
     */
    String getSparqlEndpoint();

    void closeConnection();

    /**
     * Get RepositoryConnection object of
     * the current connection.
     * @return RepositoryConnection object.
     */
    RepositoryConnection getConnection();

    /**
     * Sets SPARQL Repository and starts connection.
     */
    void establishConnection();

    /**
     * Performs a CONSTRUCT Query on the Knowledge Graph.
     *
     * @param query SPARQL Query as String Object.
     * @return If no Exception occurred: Object of type {@link org.eclipse.rdf4j.query.GraphQueryResult}, otherwise <code>null</code>
     */
    GraphQueryResult constructQuery(String query);

    /**
     * Performs a SELECT Query on the Knowledge Graph.
     *
     * @param query SPARQL Query as String Object.
     * @return If no Exception occurred: Object of type {@link org.eclipse.rdf4j.query.TupleQueryResult}, otherwise <code>null</code>
     */
    TupleQueryResult selectQuery(String query);

    /**
     * Performs an ASK Query on the Knowledge Graph.
     *
     * @param query SPARQL Query as String Object.
     * @return If no Exception occured: Either true or false, otherwise <code>null</code>
     */
    Boolean booleanQuery(String query);

}
