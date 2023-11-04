package fcalib.api.utils;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import fcalib.api.fca.*;
import fcalib.lib.fca.*;
import fcalib.lib.utils.KnowledgeGraphAccess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
//TODO JavaDoc

/**
 * Interface, which can be used to easily create a Context
 * from a Knowledge Graph or from File. When using a method from
 * this interface to query Knowledge Graphs, only subjects of the queries are
 * stored as objects in the Context.
 * @author Leon Geis
 */
public interface ContextHelper {

    /**
     * Creates (or "fills") a Context object with data from Wikidata.
     * The first parameter is a Context Object, which can be of any class
     * implementing the Context interface. The second parameter is the URI
     * of a property class. The third one specifies the number of objects
     * that are stored in the Context object.
     * @param context Context Object.
     * @param propertyClass String of the URI of the property Class.
     * @param number Number of Objects.
     * @return Context Object with subjects of the query as objects of the Context
     * and subproperties of the property Class as Attributes.
     */
    static Context<String,String> createContextFromWikidata(Context<String,String> context, String propertyClass, int number){
        //Clear the List of Attributes and the List of Objects of the Context
        context.getContextObjects().clear();
        context.getContextAttributes().clear();
        //Create new Object of type KnowledgeGraphAccess and
        //set the URL of the SPARQL Endpoint
        KnowledgeGraphAccess wa = new KnowledgeGraphAccess("https://query.wikidata.org/sparql");
        //Establish a connection to the SPARQL Endpoint of Wikidata
        wa.establishConnection();

        //Query Wikidata to receive a List of Properties of the given property Class

        //Perform instance of Query to receive all associated properties
        TupleQueryResult classResult = wa.selectQuery(SPARQLQueryGenerator.generateSelectWikidataInstanceOfQuery(propertyClass));
        //Create List of Bindings associated with the variables (?s)
        List<String> bindingNames = classResult.getBindingNames();
        //Create corresponding prefix
        String prefix = "http://www.wikidata.org/prop/direct/";

        //Iterate over the result and add the properties to the context
        for(BindingSet bindingSet : classResult){
            String uri = bindingSet.getValue(bindingNames.get(0)).stringValue();
            context.createAttribute(prefix+ uri.substring(uri.lastIndexOf('/')+1),FCAAttribute.class);
        }
        //Close Result object
        classResult.close();
        //Now Perform SELECT Query and get the Objects according to the Attributes of the Context
        //Note the use of the propertyURI List, this is due to the result of a query, e.g., for the property
        //P25 is: http://www.wikidata.org/entity/P25, but to use them as a predicate we need
        //http://www.wikidata.org/prop/direct/P25.
        TupleQueryResult result = wa.selectQuery(SPARQLQueryGenerator.generateSelectUnionQuery(context.getContextAttributes().stream().map(Attribute::getAttributeID).collect(Collectors.toList()), number));

        //Create List of Bindings associated with the variables
        bindingNames = result.getBindingNames();

        //Add Objects to fca.FCAFormalContext (name of objects are: Q...)
        //This Format is important for later querying
        //Note: Only subjects of the query are saved as objects of the context.
        //Hence the line b.getValue(bn.get(0)).toString(); also saving objects is possible
        //through exchanging the 0 with 2. Also saving both is possible!
        for (BindingSet bindingSet : result){
            String uri = bindingSet.getValue(bindingNames.get(0)).stringValue();
            //Get Identifier of the Subject
            //String identifier = kl.substring(kl.lastIndexOf("/")+1);
            //Checking if Object is already in Object List of the context
            //If not add it to the context.
            if(!context.containsObject(uri)){
                context.createObject(uri, FCAObject.class);
            }
        }
        result.close();
        //Check for each Object, if it has an Attribute of the Context
        for(ObjectAPI<String,String> o : context.getContextObjects()){
            result = wa.selectQuery(SPARQLQueryGenerator.generateSelectPropertyCheckQuery(o.getObjectID(),context.getContextAttributes().stream().map(Attribute::getAttributeID).collect(Collectors.toList())));
            //Get the binding Names
            bindingNames = result.getBindingNames();
            //Create List which contains each binding
            List<String> identifiers = new ArrayList<>();
            //Iterate over results and add Attribute if it is contained in the results
            for(BindingSet bindings : result){
                //Get uri
                String uri = bindings.getValue(bindingNames.get(0)).stringValue();
                //Get value
                //String value = uri.substring(uri.lastIndexOf("/")+1);
                if(!identifiers.contains(uri)){
                    identifiers.add(uri);
                    //Add corresponding Incidence
                    o.addAttribute(uri);
                    context.getAttribute(uri).addObject(o.getObjectID());
                }
            }
            result.close();
        }
        //Close connection
        wa.getConnection().close();
        //Return Context Object
        return context;
    }

    //TODO Verify
    static Context<String,String> createContextFromKnowledgeGraph(Context<String,String> context, String sparqlEndpoint ,List<String> properties, int number){
        //Clear the List of Attribute and Objects
        context.getContextObjects().clear();
        context.getContextAttributes().clear();

        //Create new KnowledgeGraphAccess Object and set URI to the DBPedia SPARQL-Endpoint
        KnowledgeGraphAccess wikidataAccess = new KnowledgeGraphAccess(sparqlEndpoint);

        //Establish a connection
        wikidataAccess.establishConnection();

        //Create a query based on the given property list
        String query = SPARQLQueryGenerator.generateSelectUnionQuery(properties,number);

        //Execute query
        TupleQueryResult result = wikidataAccess.selectQuery(query);

        //Get Binding Names
        List<String> bindingNames = result.getBindingNames();

        //Iterate over results
        for(BindingSet bindingSet : result){
            //Create object and store it in the context
            context.createObject(bindingSet.getValue(bindingNames.get(0)).stringValue(), FCAObject.class);
        }
        result.close();

        //Now for each Object perform query and check, which of the given attributes it has
        for(ObjectAPI<String,String> objectAPI : context.getContextObjects()){

            //Generate query
            query = SPARQLQueryGenerator.generateSelectPropertyCheckQuery(objectAPI.getObjectID(),properties);

            //Perform query
            result = wikidataAccess.selectQuery(query);

            //Get binding names
            bindingNames = result.getBindingNames();

            //Iterate over results
            for(BindingSet bindingSet : result){
                //Create new Attribute
                Attribute<String,String> attribute = new FCAAttribute<>(bindingSet.getValue(bindingNames.get(0)).stringValue());

                //Add incidence
                attribute.addObject(objectAPI.getObjectID());
                objectAPI.addAttribute(attribute.getAttributeID());

                //Add attribute to context
                context.addAttribute(attribute);
            }
        }

        return context;

    }

    //TODO Enable Generic Types from File
    /**
     * Creates a Context from File. The Format has to be the same
     * as the Output Format of OutputPrinter Interface. This
     * could look like the following.
     *   a b c
     * 1 X - X
     * 2 - - X
     * 3 X - -
     * Note that the amount of whitespaces in the first line is
     * irrelevant.
     * @param FilePath Path to the .txt File
     * @return Context Object build from the given File
     * @throws IOException - When file can't be found.
     */
    static Context<String,String> createContextFromFile(String FilePath) throws IOException {
        //Create Reader Object
        BufferedReader reader = new BufferedReader(new FileReader(FilePath));
        //Read in first line attributes
        String firstLine = reader.readLine();
        //Create Context
        Context<String,String> context = new FCAFormalContext<String, String>() {};
        String[] attributes = firstLine.trim().split("\\s+");
        //Create for each Attribute in the table an Attribute in the Context
        for (int i = 0; i < attributes.length; i++) {
            Attribute<String,String> atr = new FCAAttribute<>(attributes[i]);
            context.addAttribute(atr);
        }
        //Go through each remaining line and add first the object
        //with its ID and afterwards all corresponding incidences
        String line = reader.readLine();
        while(line!=null){
            String[] objInc = line.split("\\s+");
            //Create Object
            ObjectAPI<String,String> object = new FCAObject<>(objInc[0]);
            for (int i = 1; i < objInc.length; i++) {
                if(objInc[i].equals("X")){
                    //Add Incidence
                    object.addAttribute(context.getContextAttributes().get(i-1).getAttributeID());
                    context.getContextAttributes().get(i-1).addObject(object.getObjectID());
                }
            }
            context.addObject(object);
            //Read next line
            line = reader.readLine();
        }
        //Close Reader
        reader.close();
        //Return Context
        return context;
    }

    static Context<String,String> createContextFromJSONFile(String filepath){

        JSONParser parser = new JSONParser();

        Context<String,String> jsonContext = new FCAFormalContext<String, String>() {};

        try{
            Object object = parser.parse(new FileReader(filepath));

            JSONObject jsonObject = (JSONObject) object;

            System.out.println(jsonObject);

            System.out.println(jsonObject.get("head"));

            System.out.println(jsonObject.get("results"));

            JSONObject bindings = (JSONObject) jsonObject.get("results");

            JSONArray results = (JSONArray) bindings.get("bindings");
            System.out.println(results);

            Iterator<JSONObject> iterator = results.iterator();
            while(iterator.hasNext()){

                JSONObject currentTriple = iterator.next();
                System.out.println(currentTriple);
                System.out.println("\n\n\n");

                System.out.println(currentTriple.get("s"));
                JSONObject subject = (JSONObject) currentTriple.get("s");
                JSONObject predicate = (JSONObject) currentTriple.get("p");
                System.out.println(subject.get("value"));


                FCAObject<String,String> fcaObject = new FCAObject<>(subject.get("value").toString());
                FCAAttribute<String,String> fcaAttribute = new FCAAttribute<>(predicate.get("value").toString());

                fcaObject.addAttribute(predicate.get("value").toString());
                jsonContext.addObject(fcaObject);


            }



        }catch (Exception e){
            e.printStackTrace();
        }


        //Return Context object
        return jsonContext;

    }

    static MultiValuedContext<String,String,String> createContextFromTTLFile(String filePath) throws IOException {
        //Get all Triples from the File
        List<String> triples = TTLParser.getAllTriples(filePath);

        //Create mvContext in which all triples are stored
        MultiValuedContext<String,String,String> mvContext = new FCAMultiValuedFormalContext<>(){};

        //Go through each Triple
        for(String triple : triples){

            //Remove all Commas and Semicolons
            triple = triple.replaceAll(",","");
            triple = triple.replaceAll(";","");

            //Split the triple String at each whitespace
            String[] splittedTriple = triple.split("\\s+");

            //Create new MultiValuedObject
            //Set the ID of the new Object to the first Element of the splitted String (Subject)
            MultiValuedObject<String,String,String> mvObject = new FCAMultiValuedObject<>(splittedTriple[0]);

            //Add the second element in the splitted String to the object (predicate)
            MultiValuedAttribute<String,String,String> mvAttribute = new FCAMultiValuedAttribute<>(splittedTriple[1]);

            //Get all RDF Objects of the triple in a List and add them to the Value List of the Object
            List<String> values = Arrays.asList(splittedTriple);

            //Get all Objects
            values = values.subList(2,values.size()-1);

            //Now add the attribute with the value list to the object
            mvObject.addAttribute(mvAttribute, values);

            //Add the Object to the Multi-Valued Context
            mvContext.addMultiValuedObject(mvObject);
            //mvContext.addMultiValuedAttribute(mvAttribute);

        }
        //Return Multi-Valued Context
        return mvContext;
    }

    //TODO
    static Context<String,String> createContextFromRDFFile(String filepath) throws IOException{
        return null;
    }


}
