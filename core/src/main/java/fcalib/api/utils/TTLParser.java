package fcalib.api.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fcalib.lib.utils.Pair;

/**
 * Utility interface with static methods
 * to extract all triples of a .ttl file.
 * @author Leon Geis
 */
public interface TTLParser {

    /**
     * Get all Namespaces (or prefixes) of
     * a .ttl file.
     * Note: Only returns those, which start with
     * either "@prefix" or "PREFIX".
     * @param filePath Filepath of the .ttl file.
     * @return A List of Pairs(L,R) with L=abbreviation of
     *         the namespace, and R=URI of the namespace.
     * @throws IOException When file cannot be found.
     */
    static List<Pair<String,String>> getNamespaces(String filePath) throws IOException {

        //Create Reader Object
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        //Create List to be returned at the end
        List<Pair<String,String>> namespaces = new ArrayList<>();

        //Read lines as long as they start with @prefix or PREFIX
        //The case of @base and BASE is discarded here
        while(true){
            String currentLine = reader.readLine();
            if(currentLine.contains("@prefix") || currentLine.contains("PREFIX")) {
                String[] parts = currentLine.split("\\s+");
                Pair<String,String> p = new Pair<>(parts[1].replace(":",""),parts[2].substring(1, parts[2].length()-1));
                namespaces.add(p);
            }else{
                break;
            }
        }
        reader.close();
        return namespaces;
    }

    /**
     * Get all RDF-Triples of a .ttl file.
     * Each Triple ends with a "." and the
     * whole statement is captured. Thus, also
     * including ";" and "," statements in the same string.
     * Note: Necessary for this method to work is a newline
     * between each statement.
     * @param filePath Filepath of the .ttl file.
     * @return A List of Strings, with each String being a
     *         whole statement.
     * @throws IOException When the file cannot be found.
     */
    static List<String> getAllTriples(String filePath) throws IOException{

        //Create Reader Object
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        //Create List to be returned at the end
        List<String> triples = new ArrayList<>();

        String currentLine;

        //Skip Namespaces
        do {
            currentLine = reader.readLine();
        } while (currentLine.contains("@prefix") || currentLine.contains("PREFIX"));

        while(currentLine!=null){

            StringBuilder triple = new StringBuilder();

            if(currentLine.startsWith("<")){
                triple.append(currentLine);
                currentLine = reader.readLine();
                while(currentLine!=null && !currentLine.isBlank()){
                    triple.append(currentLine);
                    currentLine = reader.readLine();
                }
                triples.add(triple.toString());
            }

            currentLine = reader.readLine();

        }
        reader.close();
        return triples;
    }

    /**
     * Get the subject of a RDF-Triple.
     * Works with a String from the method
     * getAllTriples().
     * Note: To work with other methods, there must be
     * a whitespace between each part of the statement.
     * @param rdfTriple String of the RDF-Triple.
     * @return String of the Subject including "<" and ">".
     */
    static String getSubject(String rdfTriple){
        String[] split = rdfTriple.split("\\s+");
        return split[0];
    }
    /**
     * Get the predicate of a RDF-Triple.
     * Works with a String from the method
     * getAllTriples().
     * Note: To work with other methods, there must be
     * a whitespace between each part of the statement.
     * @param rdfTriple String of the RDF-Triple.
     * @return String of the Predicate including "<" and ">"
     *         and ":".
     */
    static String getPredicate(String rdfTriple){
        String[] split = rdfTriple.split("\\s+");
        return split[1];
    }
    /**
     * Get the object of a RDF-Triple.
     * Works with a String from the method
     * getAllTriples().
     * Note: To work with other methods, there must be
     * a whitespace between each part of the statement.
     * Also only the first object is returned until "," or ";".
     * @param rdfTriple String of the RDF-Triple.
     * @return String of the Object including "<" and ">".
     */
    static String getObject(String rdfTriple){
        String[] split = rdfTriple.split("\\s+");
        return split[2];
    }

}
