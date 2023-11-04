package fcalib.lib.utils;

/**
 * This is a simple and generic Builder for
 * creating SPARQL Queries.
 * Note: The queries, which can be created by using this class
 * are very restricted and only cover a minimal amount of
 * possibilities. One can easily write own
 * queries and simply save them as a String.
 * @author Leon Geis
 */
public class SPARQLQueryBuilder{

    /**
     * The query which will be build.
     */
    private String query;

    /**
     * Default Constructor of the class.
     */
    public SPARQLQueryBuilder(){
        //Initialize query
        this.query="";
    }

    /**
     * Returns the query as a String.
     * This method is always called at the end of
     * the method invocations.
     * @return String
     */
    public String build(){
        return this.query;
    }

    /**
     * Specifies a Prefix and the corresponding URI.
     * @param prefix Prefix of the URI.
     * @param URL String of the URL.
     * @return String
     */
    public SPARQLQueryBuilder prefix(String prefix, String URL){
        this.query += "PREFIX "+prefix+": <"+URL+">\n";
        return this;
    }

    /**
     * Adds the SELECT statement to the query.
     * @return String
     */
    public SPARQLQueryBuilder select(){
        this.query += "SELECT ";
        return this;
    }

    /**
     * Adds the FROM keyword to the query.
     * @return String
     */
    public SPARQLQueryBuilder from(){
        this.query += "FROM ";
        return this;
    }

    /**
     * Adds the ASK statement to the query.
     * @return String
     */
    public SPARQLQueryBuilder ask(){
        this.query += "ASK ";
        return this;
    }

    /**
     * Adds the CONSTRUCT statement to the query.
     * @return String
     */
    public SPARQLQueryBuilder construct(){
        this.query += "CONSTRUCT ";
        return this;
    }

    /**
     * Adds the DISTINCT keyword to the query.
     * @return String
     */
    public SPARQLQueryBuilder distinct(){
        this.query += " DISTINCT ";
        return this;
    }

    /**
     * Adds a variable to the query.
     * @param var String of the variable (e.g. ?item)
     * @return String
     */
    public SPARQLQueryBuilder variable(String var){
        this.query += var;
        return this;
    }

    /**
     * Adds the WHERE keyword to the query.
     * @return String
     */
    public SPARQLQueryBuilder where(){
        this.query += " WHERE { ";
        return this;
    }

    /**
     * Adds the given String as a Subject to the query.
     * The subject is always part of a RDF triple with
     * (subject,predicate,object).
     * @param subject URI of the subject.
     * @return String
     */
    public SPARQLQueryBuilder subject(String subject){
        this.query += subject+" ";
        return this;
    }

    /**
     * Adds the given String as a Predicate to the query.
     * The Predicate is always part of a RDF triple with
     * (subject,predicate,object).
     * @param predicate URI of the predicate.
     * @return String
     */
    public SPARQLQueryBuilder predicate(String predicate){
        this.query += predicate+" ";
        return this;
    }

    /**
     * Adds the given String as an Object to the query.
     * The object is always part of a RDF triple with
     * (subject,predicate,object).
     * @param object URI of the object.
     * @return String
     */
    public SPARQLQueryBuilder object(String object){
        this.query += object+". ";
        return this;
    }

    /**
     * Adds a closing bracket to the query, which is
     * necessary when ending a the query itself.
     * @return String
     */
    public SPARQLQueryBuilder end(){
        this.query += "}";
        return this;
    }

    /**
     * Adds the given query as a subquery to the query.
     * @param query Subquery as a String.
     * @return String.
     */
    public SPARQLQueryBuilder subquery(String query){
        this.query += "{ "+query+" }";
        return this;
    }

    /**
     * Adds a BIND statement to the query.
     * @param expression String of the expression.
     * @param variable Name of the Variable.
     * @return String
     */
    public SPARQLQueryBuilder bind(String expression, String variable){
        this.query += "BIND ("+expression+" AS "+variable+")";
        return this;
    }

    /**
     * Adds a FILTER statement to the query.
     * @param expression Expression, which is used inside
     *                   the FILTER statement.
     * @return String
     */
    public SPARQLQueryBuilder filter(String expression){
        this.query += "FILTER("+expression+")";
        return this;
    }

    /**
     * Adds the Wikidata Label Service to the query.
     * The language can be specified via parameter.
     * Note: Only works when using entities from Wikidata.
     * @param language Language Token (en for english)
     * @return String
     */
    public SPARQLQueryBuilder wikidataServiceLabel(String language){
        this.query += "SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],"+language+"\". }";
        return this;
    }

    /**
     * Adds a GROUP BY statement to the query.
     * @param variable Variable which will be grouped after.
     * @return String
     */
    public SPARQLQueryBuilder groupBy(String variable){
        this.query += " GROUP BY "+variable;
        return this;
    }

    /**
     * Adds a HAVING statement to the query.
     * @param expression Expression, which is used inside the
     *                   HAVING statement.
     * @return String
     */
    public SPARQLQueryBuilder having(String expression){
        this.query += "HAVING "+expression;
        return this;
    }

    /**
     * Adds a Limit to the query. The limit
     * specifies how many individuals are queried.
     * @param limit Amount of queried individuals.
     * @return String
     */
    public SPARQLQueryBuilder limit(int limit){
        this.query += "LIMIT "+limit;
        return this;
    }

}



