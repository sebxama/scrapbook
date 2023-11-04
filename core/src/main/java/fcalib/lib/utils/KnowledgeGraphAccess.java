package fcalib.lib.utils;

import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

import fcalib.api.utils.SPARQLEndpointAccess;
/**
 * Class describing the Access to a Knowledge Graph. Each
 * object has an own SPARQL-Endpoint URI String, which is used
 * to establish a connection and perform queries on.
 * @author Leon Geis
 */
public class KnowledgeGraphAccess implements SPARQLEndpointAccess {

    /**
     * URL for the used SPARQL Endpoint.
     */
    private String sparqlEndpoint;
    /**
     * Repository Object of Wikidata.
     */
    private Repository repository;
    /**
     * Connection to the Wikidata Repository.
     */
    private RepositoryConnection repositoryConnection;

    /**
     * Constructor of the Class setting the SPARQL Endpoint.
     * @param sparqlEndpoint URL of the SPARQL Endpoint as
     *                       a String.
     */
    public KnowledgeGraphAccess(String sparqlEndpoint){
        //Set Up Basic Logging to Console
        org.apache.log4j.BasicConfigurator.configure();
        //Set the Endpoint URL
        this.sparqlEndpoint=sparqlEndpoint;
    }

    /**
     * Set URL of SPARQL Endpoint.
     * @param sparqlEndpoint String of the URL Endpoint.
     */
    public void setSparqlEndpoint(String sparqlEndpoint){
        this.sparqlEndpoint=sparqlEndpoint;
    }

    /**
     * Get currently used SPARQL Endpoint URL.
     * @return URL as a String.
     */
    public String getSparqlEndpoint(){
        return this.sparqlEndpoint;
    }

    /**
     * Get RepositoryConnection object of
     * the current connection.
     * @return RepositoryConnection object.
     */
    public RepositoryConnection getConnection(){
        return this.repositoryConnection;
    }

    /**
     * Sets SPARQL Repository and starts connection.
     */
    public void establishConnection(){
        //Create Repository Object with SPARQL Endpoint
        this.repository = new SPARQLRepository(sparqlEndpoint);

        //Create Connection Object to the specified Repository
        this.repositoryConnection = repository.getConnection();
    }

    /**
     * Close the Connection to the Repository.
     */
    public void closeConnection(){
        this.repositoryConnection.close();
    }

    /**
     * Performs a CONSTRUCT Query on the Knowledge Graph.
     *
     * @param query SPARQL Query as String Object.
     * @return If no Exception occurred: Object of type {@link org.eclipse.rdf4j.query.GraphQueryResult}, otherwise <code>null</code>
     */
    public GraphQueryResult constructQuery(String query){
        try{
            //Return result of the specified SPARQL Query
            return this.repositoryConnection.prepareGraphQuery(query).evaluate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Performs a SELECT Query on the Knowledge Graph.
     *
     * @param query SPARQL Query as String Object.
     * @return If no Exception occurred: Object of type {@link org.eclipse.rdf4j.query.TupleQueryResult}, otherwise <code>null</code>
     */
    public TupleQueryResult selectQuery(String query){
        try{
            //Return result of the specified SPARQL Query
            return this.repositoryConnection.prepareTupleQuery(query).evaluate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Performs an ASK Query on the Knowledge Graph.
     *
     * @param query SPARQL Query as String Object.
     * @return If no Exception occured: Either true or false, otherwise <code>null</code>
     */
    public Boolean booleanQuery(String query){
        try{
            return this.repositoryConnection.prepareBooleanQuery(query).evaluate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
