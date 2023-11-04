package fcalib.api.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import fcalib.api.fca.*;
import fcalib.lib.utils.Pair;

/**
 * Interface describing Output functionalities,
 * which can either be printing to the Console or
 * saving the results in a file.
 * Note: The file path cannot be changed and
 * all files created by these methods are
 * created there.
 * @author Leon Geis
 */
public interface OutputPrinter {

    /**
     * Print the given Context to the Console.
     * @param context the given context
     */
    static <T extends Context<O,A>,O,A> void printCrosstableToConsole(T context){
        //Headline of the Output
        System.out.println("Crosstable of the context with ID: "+context.getContextID());
        System.out.println("The Crosstable of the current context: X:Object has Attribute; -:Object does not have Attribute");
        //Initial space for formatting purposes
        //Using the Stream API to receive the longest ID (as a String) of an Object
        int length = Collections.max(context.getContextObjects(), Comparator.comparing(object -> object.getObjectID().toString().length())).
                getObjectID().toString().length()+1;
        for (int i = 0; i < length; i++) {
            System.out.print(" ");
        }
        //Print each Attribute name
        for(Attribute<O,A> a : context.getContextAttributes()){
            System.out.print(a.getAttributeID()+" ");
        }
        //Newline
        System.out.println();
        //Go through each Object and print the corresponding Attributes
        for(ObjectAPI<O,A> o : context.getContextObjects()){
            //Get the length of the name (formatting purposes)
            int nameLength = o.getObjectID().toString().length();
            //Print name of Object
            System.out.print(o.getObjectID());
            //Printing whitespaces for formatting
            while(nameLength<length){
                System.out.print(" ");
                nameLength++;
            }
            //Go through each Attribute and check if Object has Attribute
            //If it does Print X else -
            for(Attribute<O,A> a : context.getContextAttributes()){
                if(o.getDualEntities().contains(a.getAttributeID())){
                    System.out.print("X ");
                }else {
                    System.out.print("- ");
                }
                //Printing whitespaces for formatting of the table
                for (int i = 0; i < a.getAttributeID().toString().length()-1; i++) {
                    System.out.print(" ");
                }
            }
            //Newline for each Object
            System.out.println();
        }
    }

    static <T extends MultiValuedContext<O,A,V>, O,A,V> void printMultiValuedTableToConsole(T context){
        //Headline of the Output
        System.out.println("Crosstable of the multi-valued context with ID: "+context.getContextID());
        System.out.println("The Table of the current context: ");
        //Initial space for formatting purposes
        //Using the Stream API to receive the longest ID (as a String) of an Object
        int length = Collections.max(context.getContextObjects(), Comparator.comparing(object -> object.getObjectID().toString().length())).
                getObjectID().toString().length()+1;
        for (int i = 0; i < length; i++) {
            System.out.print(" ");
        }
        //Print each Attribute name
        //Get longest List of Values
        List<Integer> lengthValues = new ArrayList<>();
        for(MultiValuedObject<O,A,V> o : context.getContextObjects()){
            for(Pair<MultiValuedAttribute<O,A,V>, List<V>> pair : o.getDualEntities()){
                 lengthValues.add(pair.getRight().toString().length());
            }
        }

        int i=0;
        for(MultiValuedAttribute<O,A,V> a : context.getContextAttributes()){
            System.out.print(a.getAttributeID()+ String.format("%1$"+lengthValues.get(i)+"s", ""));
            ++i;
        }
        //Newline
        System.out.println();
        //Go through each Object and print the corresponding Attributes
        for(MultiValuedObject<O,A,V> o : context.getContextObjects()){
            //Get the length of the name (formatting purposes)
            int nameLength = o.getObjectID().toString().length();
            //Print name of Object
            System.out.print(o.getObjectID());
            //Printing whitespaces for formatting
            while(nameLength<length){
                System.out.print(" ");
                nameLength++;
            }
            //Go through each Attribute and check if Object has Attribute
            //If it does Print X else -
            i=0;
            for(MultiValuedAttribute<O,A,V> a : context.getContextAttributes()){
                if(o.containsAttribute(a)){
                    int currentValueLength = o.getAttribute(a).getRight().size();
                    int newlength = lengthValues.get(i)-currentValueLength;
                    System.out.print(o.getAttribute(a).getRight()+String.format("%1$"+newlength+"s", ""));
                }else {
                    System.out.print("-"+ String.format("%1$"+lengthValues.get(i)+"s", ""));
                }
                //Printing whitespaces for formatting of the table
                for (int j = 0; j < a.getAttributeID().toString().length()-1; j++) {
                    System.out.print(" ");
                }
            }
            //Newline for each Object
            System.out.println();
        }
    }

    /**
     * Write the given Context to a file.
     * @param name name of file
     */
    static <T extends Context<O,A>,O,A> void writeCrosstableToFile(T context, String filepath, String name){
        //Append file name to path
        String fileName = filepath+name;
        //Print to Console
        System.out.println("Writing to File: "+fileName);
        try (Writer fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))){
            //Headline of the Output
            fileWriter.write("Crosstable of the context with ID: "+context.getContextID()+"\n");
            fileWriter.write("The Crosstable of the current context: X:Object has Attribute; -:Object does not have Attribute\n");
            //Initial space for formatting purposes
            //Using the Stream API to receive the longest ID (as a String) of an Object
            int length = Collections.max(context.getContextObjects(),Comparator.comparing(object -> object.getObjectID().toString().length())).getObjectID().toString().length()+1;
            for (int i = 0; i < length; i++) {
                fileWriter.write(" ");
            }
            //Print each Attribute name
            for(Attribute<O,A> a : context.getContextAttributes()){
                fileWriter.write(a.getAttributeID()+" ");
            }
            //Newline
            fileWriter.write("\n");
            //Go through each Object and print the corresponding Attributes
            for(ObjectAPI<O,A> o : context.getContextObjects()){
                //Get the length of the name (formatting purposes)
                int nameLength = o.getObjectID().toString().length();
                //Print name of Object
                fileWriter.write(o.getObjectID().toString());
                //Printing whitespaces for formatting
                while(nameLength<length){
                    fileWriter.write(" ");
                    nameLength++;
                }
                //Go through each Attribute and check if Object has Attribute
                //If it does Print X else -
                for(Attribute<O,A> a : context.getContextAttributes()){
                    if(o.getDualEntities().contains(a.getAttributeID())){
                        fileWriter.write("X ");
                    }else {
                        fileWriter.write("- ");
                    }
                    //Printing whitespaces for formatting of the table
                    for (int i = 0; i < a.getAttributeID().toString().length()-1; i++) {
                        fileWriter.write(" ");
                    }
                }
                //Newline for each Object
                fileWriter.write("\n");
            }
            //Print Message to Console
            System.out.println("Writing Successful!");
            //Catch Exception
        }catch (Exception e){
            //Print Error to Console
            System.out.println("Writing not successful!");
            e.printStackTrace();
        }
    }

    /**
     * Print all Concepts of the given Context
     * to the Console.
     * @param context the given Context
     */
    static <T extends Context<O,A>,O,A> void printConceptsToConsole(T context){
        System.out.println("Concepts of the context with ID: "+context.getContextID());
        System.out.println("Concepts (A,B) with A⊆G and B⊆M. G is the set of all Objects and M the set of all Attributes.");
        List<List<Attribute<O,A>>> closures = Computation.computeAllClosures(context);
        List<Concept<O,A>> concepts = Computation.computeAllConcepts(closures,context);
        for(Concept<O,A> concept : concepts){
            concept.printConcept();
        }
    }

    /**
     * Write all Concepts of the given Context to
     * a file.
     * @param context the given context
     * @param name name of file
     */
    static <T extends Context<O,A>,O,A> void writeConceptsToFile(T context, String filepath, String name){
        //Append the file name
        String fileName = filepath+name;
        //Print to Console
        System.out.println("Writing to File: "+fileName);
        //Create Writer Object
        try(Writer fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))){
            //Write Headline of the file
            fileWriter.write("Concepts of the context with ID: "+context.getContextID()+"\n");
            fileWriter.write("Concepts (A,B) with A⊆G and B⊆M. G is the set of all Objects and M the set of all Attributes.\n");
            //Get the List of all Concepts
            List<Concept<O,A>> concepts = Computation.computeAllConcepts(Computation.computeAllClosures(context),context);
            //Go through each concept and display the extent and the intent
            for(Concept<O,A> concept : concepts){
                fileWriter.write("CONCEPT:"+concept.getExtent().stream().map(ObjectAPI::getObjectID).collect(Collectors.toList())+";");
                fileWriter.write(concept.getIntent().stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"\n");
            }
            //Display Success Message
            System.out.println("Writing Successful!");
            //Catch Exception and Display Message
        }catch (Exception e){
            System.out.println("Writing not Successful!");
            e.printStackTrace();
        }
    }

    /**
     * Displays all Implications of the Stem Base of the Context
     * on the console.
     * @param context The Context from which the Stem Base will be computed
     */
    static <T extends Context<O,A>,O,A> void printStemBaseToConsole(T context){
        System.out.println("Stem Base of the context with ID: "+context.getContextID());
        System.out.println("Implications with the premise on the left and the conclusion on the right.");
        for(Implication<O,A> impl : Computation.computeStemBase(context)){
            System.out.print(impl.toString()+" "+Computation.computeImplicationSupport(impl,context)+"\n");
        }
    }
    /**
     * Writes all Implications of the Stem Base of the given Context with the corresponding
     * support to the specific file.
     * @param context The Context from the Stem Base will be computed.
     * @param name The Name of the File ("example.txt")
     */
    static <T extends Context<O,A>,O,A> void writeStemBaseToFile(T context, String filepath, String name){
        //Append the file name
        String fileName = filepath+name;
        //Print to Console
        System.out.println("Writing to File: "+fileName);
        //Create Writer Object
        try(Writer fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))){
            //Write Headline of the file
            fileWriter.write("Stem Base of the context with ID: "+context.getContextID()+"\n");
            fileWriter.write("Implications with the premise on the left and the conclusion on the right.\n");
            //Go through each Implication and display the premise and the conclusion
            for(Implication<O,A> impl : Computation.computeStemBase(context)){
                fileWriter.write(impl.toString()+" "+Computation.computeImplicationSupport(impl,context)+"\n");
            }
            //Display Success Message
            System.out.println("Writing Successful!");
            //Catch Exception and Display Message
        }catch (Exception e){
            System.out.println("Writing not Successful!");
            e.printStackTrace();
        }
    }

}
