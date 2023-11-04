package fcalib.api.fca;

import org.apache.commons.collections4.CollectionUtils;

import fcalib.lib.fca.*;
import fcalib.lib.utils.IndexedList;
import fcalib.lib.utils.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Interface with only static methods, which can
 * be used to execute the algorithms of FCA
 * with possibly different classes implementing
 * the corresponding interfaces.
 * @author Leon Geis
 */
public interface Computation {

    /**
     * Compute the Prime of a List of Objects w.r.t.
     * the given context. Only the Objects of the List,
     * which are present in the context will be used.
     * Note: The type of the List can be any class implementing
     * the ObjectAPI interface (or even a subclass extending
     * the already existing FCAObject class).
     * @param objects List of Objects.
     * @param context Context Object used for computing the Prime.
     * @return List of Attributes all of the Objects.
     * have in common.
     */
    @SuppressWarnings("unchecked")
    static <O,A,T extends Context<O,A>> List<Attribute<O,A>> computePrimeOfObjects(List<? extends ObjectAPI<O, A>> objects, T context){
        //If the List is empty return M (all Attributes)
        if(objects.isEmpty()){
            return new ArrayList<>(context.getContextAttributes());
        }else {
            //Create HashSet that contains all IDs of the Attributes of the Objects
            HashSet<A> IDs = new HashSet<>();
            //Set boolean to indicate the first element
            boolean first = true;
            //Iterate over each Attribute and add the Object IDs of this Attribute to the List
            for (ObjectAPI<O, A> object : objects) {
                //Check if the context contains this object
                if(context.containsObject(object.getObjectID())) {
                    if (first) {
                        IDs.addAll((Collection<? extends A>) object.getDualEntities());
                        first = false;
                    } else {
                        IDs.retainAll(object.getDualEntities());
                    }
                }

            }
            //Create List to be returned
            List<Attribute<O,A>> attributes = new ArrayList<>();
            //Get corresponding List of Attributes
            for(A a : IDs){
                attributes.add(context.getAttribute(a));
            }
            return attributes;
        }
    }

    /**
     * Compute the Prime of a List of Attributes w.r.t.
     * the given context. Only the Attributes of the List,
     * which are present in the context will be used.
     * Note: The type of the List can be any class implementing
     * the Attribute interface (or even a subclass extending
     * the already existing FCAAttribute class).
     * @param attributes List of Attributes.
     * @param context Context Object used for computing the Prime.
     * @return List of Objects all of the Attributes
     * have in common.
     */
    @SuppressWarnings("unchecked")
    static <O,A,T extends Context<O,A>> List<ObjectAPI<O,A>> computePrimeOfAttributes(List<? extends Attribute<O, A>> attributes, T context){
        //If the List is empty return G (all Objects)
        if(attributes.isEmpty()){
            return new ArrayList<>(context.getContextObjects());
        }else {
            //Create HashSet that contains all IDs of the Objects of the Attributes
            HashSet<O> IDs = new HashSet<>();
            //Set boolean to indicate the first element
            boolean first = true;
            //Iterate over each Attribute and add the Object IDs of this Attribute to the List
            for (Attribute<O, A> attr : attributes) {
                //Check if the context contains this attribute
                if(context.containsAttribute(attr.getAttributeID())) {
                    if (first) {
                        IDs.addAll((Collection<? extends O>) attr.getDualEntities());
                        first = false;
                    } else {
                        IDs.retainAll(attr.getDualEntities());
                    }
                }
            }
            //Create List to be returned
            List<ObjectAPI<O,A>> objects = new ArrayList<>();
            //Get corresponding List of Objects
            for(O o : IDs){
                objects.add(context.getObject(o));
            }
            return objects;
        }
    }

    /**
     * Computes all Concepts of the Context.
     * All Concepts are of the form (A,A')
     * with A⊆G. G is the set of all Objects.
     * Note: Naive Implementation.
     * @param context Context Object from which the
     *                Concepts will be computed.
     */
    static <O,A,T extends Context<O,A>> List<Concept<O,A>> computeAllConcepts(T context){
        //Create List of all Concepts of this Context, which will be returned at the end.
        List<Concept<O,A>> concepts = new ArrayList<>();

        //First add smallest extent (All Objects and the Attributes they have in common)
        FCAConcept<O,A> first = new FCAConcept<>();

        //Add Concept (G,G') to the List
        first.setExtent(new ArrayList<>(context.getContextObjects()));
        first.setIntent(computePrimeOfObjects(context.getContextObjects(),context));
        concepts.add(first);

        //Iterate over each Attribute
        for(Attribute<O,A> a : context.getContextAttributes()){
            //Calculate the Prime for this Attribute
            //Use getAttributePrime Method; Hence the Parameter
            //Collections.singletonList.
            //Equivalently, one can use here a.getObjects();
            List<? extends ObjectAPI<O,A>> prime = computePrimeOfAttributes(Collections.singletonList(a),context);

            //Calculate the Intersection of the prime and each set of Objects in the list
            //--> A ∩ m'
            //Create Temporary List to save new sets of Objects
            List<Concept<O,A>> temp = new ArrayList<>();
            for(Concept<O,A> con : concepts){
                //Copy prime FCAObject List
                List<ObjectAPI<O,A>> intersection = new ArrayList<>(prime);
                //Perform Intersection using retainAll and the set of Objects of each concept
                intersection.retainAll(con.getExtent());
                //Check if Intersection is contained in List of all current Objects;
                //if not add it to the temporary List
                if(!concepts.stream().map(Concept::getExtent).collect(Collectors.toList()).contains(intersection)){
                    Concept<O,A> concept = new FCAConcept<>();
                    concept.setExtent(intersection);
                    //Check if Extent is empty
                    //If it is: Add Intent as (∅,∅')
                    if(concept.getExtent().isEmpty()){
                        concept.setIntent(context.getContextAttributes());
                    }else{
                        concept.setIntent(computePrimeOfObjects(intersection,context));
                    }
                    //Add Concept to Temporary List
                    temp.add(concept);
                }
            }
            //Only add non-redundant sets
            for(Concept<O,A> c : temp){
                if(!concepts.stream().map(Concept::getExtent).collect(Collectors.toList()).contains(c.getExtent())){
                    concepts.add(c);
                }
            }
        }
        //Return complete List of Concepts
        return concepts;
    }

    /**
     * Computes all Concepts of the Context.
     * Uses a List of Lists of Attributes (closures) to compute
     * the corresponding concepts and returns them.
     * Note: One can use the method computeAllClosures to
     * receive a List of closures of a Context Object.
     * @param closures List of Closures
     * @param context Context Object from which the
     *                Concepts will be computed.
     * @return List of Concepts of the Context.
     */
    static <O,A,T extends Context<O,A>,S extends Attribute<O,A>> List<Concept<O,A>> computeAllConcepts(List<List<S>> closures, T context){
        //Create List of FCAConcepts that will be returned
        List<Concept<O,A>> concepts = new ArrayList<>();
        //Go through List of closures and create a FCAConcept Object
        //for each closure. Also compute the prime and set the Extent accordingly.
        for(List<S> list : closures){
            Concept<O,A> concept = new FCAConcept<>();
            concept.setIntent(list);
            concept.setExtent(computePrimeOfAttributes(list,context));
            concepts.add(concept);
        }
        //Return List of concepts
        return concepts;
    }

    /**
     * Computes all Closures of the Context.
     * Note: One can use the optional parameter and
     * set it to <code>true</code> and all closures
     * will be printed on the console.
     * Note: All Closures are computed by using
     * the implemented FirstClosure and NextClosure
     * algorithms.
     * @param context Context Object from which the
     *                Closures will be computed.
     * @return List of Lists of Attributes, which are
     * the closures of the context object.
     */
    static <O,A,T extends Context<O,A>> List<List<Attribute<O,A>>> computeAllClosures(T context, boolean... print){
        //Setting the Optional Parameter to False
        boolean printBool = print.length >= 1 && print[0];
        //Create List of Lists of Attributes (Closures) to be returned
        List<List<Attribute<O,A>>> closures = new ArrayList<>();
        //Set the lectically first Set
        IndexedList<Attribute<O,A>> A = computeFirstClosure(context);
        if(printBool)System.out.println("CLOSURES:");
        while(A != null){
            //Print (Optional)
            if(printBool)System.out.println(A.getIndexedList().stream().map(Pair::getLeft).map(Attribute::getAttributeID).collect(Collectors.toList()));
            //Add A to the List to be returned
            closures.add(A.getIndexedList().stream().map(Pair::getLeft).collect(Collectors.toList()));
            A = computeNextClosure(A, context);
        }
        //Return the List of all Closures
        return closures;
    }

    /**
     * Computes the first closed set, i.e., ∅'',
     * of the given Context Object.
     * Note: Only Attributes of the Context Object
     * will be used.
     * @param context Context Object from which the
     *                first closure will be computed.
     * @return First closed set as IndexedList
     */
    static <O,A,T extends Context<O,A>> IndexedList<Attribute<O,A>> computeFirstClosure(T context){
        //Create Indexed Attribute List of the Context
        IndexedList<Attribute<O,A>> index = new IndexedList<>(context.getContextAttributes());
        //Create a new IndexedList
        IndexedList<Attribute<O,A>> closure = new IndexedList<>();
        //Go through each Attribute of ∅'' and add the corresponding Pair to the
        //newly created IndexedList
        List<Attribute<O,A>> primeOfObjects = computePrimeOfObjects(context.getContextObjects(),context);
        for(Attribute<O,A> attr : primeOfObjects){
            closure.add(index.getPair(index.getIndex(attr)));
        }
        //return ∅''
        return closure;
    }

    /**
     * Computes the lectically next closed set of the given first parameter
     * w.r.t. the Context Object.
     * Note: Only Attributes of the Context Object will be used.
     * @param next The List from which the next closed set will be computed.
     * @param context Context Object from which the
     *                next closed set will be computed.
     * @return IndexedList of the next closed set.
     */
    @SuppressWarnings("unchecked")
    static <O,A,T extends Context<O,A>,S extends Attribute<O,A>> IndexedList<Attribute<O,A>> computeNextClosure(IndexedList<S> next, T context){
        //Create Indexed Attribute List of the Context
        IndexedList<Attribute<O,A>> indexed = new IndexedList<>(context.getContextAttributes());
        //Go through List of all Attributes in reverse Order
        //Provide listIterator Parameter with size of the List to get a pointer
        //to the end of the list.
        ListIterator<Pair<Attribute<O, A>, Integer>> iterator = indexed.getIndexedList().listIterator(indexed.getIndexedList().size());
        while(iterator.hasPrevious()){
            //Get the previous Pair of Attribute and Index
            Pair<Attribute<O, A>, Integer> previous = iterator.previous();
            //If the Attribute is contained in next remove it
            if(next.contains((S) previous.getLeft())){
                next.remove((S) previous.getLeft());
            }else{
                //Create union of the current List and the previous Attribute
                IndexedList<S> union = new IndexedList<>();
                union.add((Pair<S, Integer>) previous);
                union.addAll(next.getIndexedList());
                //Compute the double Prime of the union and save it
                List<Attribute<O,A>> B = computePrimeOfObjects(computePrimeOfAttributes(union.getObjects(),context),context);
                //Remove all Attributes from B that are in next
                B.removeAll(next.getObjects());
                //Use a flag to verify if Attributes are < than previous (m)
                boolean flag=true;
                //Go through each Attribute left in B and check
                //if it is smaller (lectically) than previous (m)
                //If it is set the flag to false and stop.
                for(Attribute<O,A> a : B){
                    //Use the indexed Attribute List to get the index
                    if(indexed.getIndex(a) < previous.getRight()){
                        flag = false;
                        break;
                    }
                }
                //Now if the flag is still true, which means that there is not element in B,
                //which is smaller than previous (m), create a indexed List and return it.
                if(flag){
                    //Create the List
                    IndexedList<Attribute<O,A>> ret = new IndexedList<>();
                    //Again compute the double prime of the union and add each attribute as a pair
                    //Using the indexed Attribute List to get an index for each Attribute
                    List<Attribute<O,A>> doublePrime = computePrimeOfObjects(computePrimeOfAttributes(union.getObjects(),context),context);
                    for(Attribute<O,A> a : doublePrime ){
                        ret.add(new Pair<>(a,indexed.getIndex(a)));
                    }
                    //Return the IndexedList
                    return ret;
                }
            }
        }
        //If all Attributes are traversed in reverse Order and none of the
        //above statements fits, return null.
        return null;
    }

    /**
     * Computes the Closure of a List of Attribute w.r.t a List of
     * Implications in linear time.
     * Note: The provided List of Attributes will be modified and
     * returned afterwards.
     * @param attributes List of Attributes
     * @param implications List of Implications
     * @return Closure of the List of Attributes w.r.t the
     * List of Implications.
     */
    @SuppressWarnings("unchecked")
    static <O,A,S extends Attribute<O,A>> List<S> computeLinClosure(List<S> attributes, List<? extends Implication<O,A>> implications){

        //Create List of Pairs. Here, the Pair consists on the left side of
        //a single FCAAttribute, and on the right side a List of Implications.
        //Note: The left side is always the Attribute itself
        //      and the right side are Implications which contain this Attribute
        //      in their premise.
        List<Pair<S,List<Implication<O,A>>>> attrList = new ArrayList<>();

        //Create List of Pairs, with Implications on the left and Integers, describing the size of the premise
        //on the right
        List<Pair<Implication<O,A>,Integer>> count = new ArrayList<>();

        //Go through each Implication provided via parameter
        for(Implication<O,A> implication : implications){
            //Save the size of the implication and save the pair in count
            count.add(new Pair<>(implication,implication.getPremise().size()));
            //Check if left side of the implication is empty
            if(implication.getPremise().isEmpty()){
                //Add the conclusion to the set of attributes provided via parameter
                attributes.addAll((Collection<? extends S>) implication.getConclusion());

                //Remove duplicate Elements using Stream API
                attributes = attributes.stream().distinct().collect(Collectors.toList());
            }
            //Go through each Attribute in the Premise of the Implication
            for(Attribute<O,A> attribute : implication.getPremise()){
                //Get a List of all Attributes on the left side
                List<Attribute<O,A>> lhsAttributes = attrList.stream().map(Pair::getLeft).collect(Collectors.toList());
                //If this List contains the variable 'attribute', get the Pair of that variable and
                //add the Implication to the right side
                if(lhsAttributes.contains(attribute)){
                    //Go through each Pair of the attrList and if the left side of a pair,
                    //matches the variable 'attribute', add the implication to the right side.
                    for(Pair<S,List<Implication<O,A>>> p : attrList){
                        if(p.getLeft().equals(attribute)){
                            p.getRight().add(implication);
                            break;
                        }
                    }
                }
                //If not, add the variable 'attribute' with a Singleton List containing
                //the current Implication
                else{
                    List<Implication<O,A>> implList = new ArrayList<>();
                    implList.add(implication);
                    attrList.add(new Pair<>((S)attribute,implList));
                }
            }
        }
        //Create a copy of the attribute List, which is provided via parameter and (possibly) changed
        //during computation above:
        List<Attribute<O,A>> updated = new ArrayList<>(attributes);

        //While the copy (updated) is not empty
        while(!updated.isEmpty()){
            //Get an Attribute out of the List and remove it afterwards from the list
            Iterator<Attribute<O,A>> iterator = updated.iterator();
            Attribute<O,A> m = iterator.next();
            updated.remove(m);
            //Get the List of Implications that contain m in their premise
            List<Implication<O,A>> implicationsOfm = new ArrayList<>();
            for(Pair<S,List<Implication<O,A>>> p : attrList){
                if(p.getLeft().equals(m)){
                    implicationsOfm = p.getRight();
                    break;
                }
            }
            //Now, go through each Implication of m
            for(Implication<O,A> implOfm : implicationsOfm){
                //Get the pair of the implication and decrease the right side by 1
                for(Pair<Implication<O,A>,Integer> countPair : count){
                    if(countPair.getLeft().equals(implOfm)){
                        countPair.setRight(countPair.getRight()-1);
                        //If the right side is now 0
                        if(countPair.getRight()==0){
                            //Add the conclusion of the implication without elements from
                            //the attribute List (provided via parameter) to this list
                            //First create a copy of the conclusion
                            List<S> conclusionCopy = new ArrayList<>((Collection<? extends S>) implOfm.getConclusion());
                            //Remove all Elements that are in the provided Attribute List from the Copy
                            conclusionCopy.removeAll(attributes);
                            //Add this copy now to the attribute List provided via parameter
                            //and the 'updated' List
                            attributes.addAll(conclusionCopy);
                            updated.addAll(conclusionCopy);
                        }
                    }
                }
            }
        }
        //Return the closure of the provided List
        return attributes;
    }

    /**
     * Computes the Stem Base of the Context Object.
     * Note: The Stem Base is computed by using the
     * implement LinClosure algorithm.
     * @param context Context Object from which the
     *                Stem Base will be computed.
     * @return List of all Implications of the Stem Base
     */
    static <O,A,T extends Context<O,A>> List<Implication<O,A>> computeStemBase(T context){
        //Create IndexedList of the Attributes of the Context
        IndexedList<Attribute<O,A>> indexedAttributes = new IndexedList<>(context.getContextAttributes());
        //Create new List and set it to the closure of the empty set
        List<Attribute<O,A>> A = computeFirstClosure(context).getObjects();
        //Create empty List for the Implications
        List<Implication<O,A>> implList = new ArrayList<>();
        //If A is not empty add first Implication to Implication List
        if(!A.isEmpty()){
            implList.add(new FCAImplication<>(new ArrayList<>(),A));
        }
        //Get the Attribute with the highest Index in the Attribute List
        Attribute<O,A> max = indexedAttributes.getMaxElement();
        while (!CollectionUtils.isEqualCollection(A,indexedAttributes.getObjects())){
            //Get all attributes smaller than 'max' and traverse them in reverse order
            List<Attribute<O,A>> smallerAttributes = new ArrayList<>();
            for(Pair<Attribute<O,A>,Integer> indexed : indexedAttributes.getIndexedList()){
                if(indexed.getRight()<=indexedAttributes.getIndex(max)){
                    smallerAttributes.add(indexed.getLeft());
                }
            }
            //Reverse the order
            ListIterator<Attribute<O,A>> iterator = smallerAttributes.listIterator(smallerAttributes.size());
            while(iterator.hasPrevious()){
                Attribute<O,A> atr = iterator.previous();
                if(A.contains(atr)){
                    A.remove(atr);
                }else {
                    //Create Copy of A and add the current Attribute 'atr'
                    List<Attribute<O,A>> attrCopy = new ArrayList<>(A);
                    attrCopy.add(atr);
                    //Compute the Closure of this copy List
                    List<Attribute<O,A>> B = computeLinClosure(attrCopy,implList);
                    //Remove A from B
                    B.removeAll(A);
                    //Go through each remaining Attribute in B and check if no element lectically smaller than
                    //'atr' is contained
                    //Create boolean variable
                    boolean flag = true;
                    for(Attribute<O,A> atrInB : B){
                        if(indexedAttributes.getIndex(atrInB) < indexedAttributes.getIndex(atr)){
                            flag = false;
                            break;
                        }
                    }
                    //Now if the flag is still true:
                    if(flag){
                        //Add back the Elements from A to B and set A to B
                        B.addAll(A);
                        A = B;
                        //Set max Attribute to current Attribute
                        max = atr;
                        //Exit for loop
                        break;
                    }
                }
            }
            //if A is not equal its closure add corresponding Implication
            //Compute ClosureOperator of A
            List<Attribute<O,A>> closureA = computePrimeOfObjects(computePrimeOfAttributes(A,context),context);
            if(!CollectionUtils.isEqualCollection(A,closureA)){
                //Check if the size of the Closure is not equal the size of all Attributes
                //If it is remove all Attribute from the Conclusion that are contained in the premise
                if(closureA.size()!=context.getContextAttributes().size())closureA.removeAll(A);
                //Add the implication to the List
                implList.add(new FCAImplication<>(A,closureA));
            }

            //Remove the closure of A from A itself
            List<Attribute<O,A>> closureWithoutA = new ArrayList<>(closureA);
            closureWithoutA.removeAll(A);
            //Go through each Attribute in closureWithoutA and
            //if no Attribute smaller than 'max' is contained
            //then set A to closureA and max to the largest Attribute
            //in the IndexedList of Attributes
            //Set boolean flag to true
            boolean flag = true;
            for(Attribute<O,A> atr : closureWithoutA){
                if(indexedAttributes.getIndex(atr)<indexedAttributes.getIndex(max)){
                    flag = false;
                    break;
                }
            }
            //If the flag is still true perform steps mentioned above:
            if(flag){
                A = new ArrayList<>(closureA);
                //set max to the largest Attribute in Indexed Attribute List
                max = indexedAttributes.getMaxElement();
            }else{
                //Get List of all Attributes in A that are smaller equal than max
                List<Attribute<O,A>> smaller = new ArrayList<>();
                for(Attribute<O,A> atr : A){
                    if(indexedAttributes.getIndex(atr)<=indexedAttributes.getIndex(max)){
                        smaller.add(atr);
                    }
                }
                //Set A to 'smaller'
                A = smaller;
            }
        }

        return implList;
    }

    /**
     * Computes the support of an Implication w.r.t the
     * given context. The support of an Implication is always
     * between 0 and 1. 0 meaning this Implication holds for
     * no intent of the context and 1 meaning this Implication
     * holds in every intent of the context.
     * Note: If the Implication contains an Attribute, which is
     * not present in the Context and if the List of Objects
     * in the Context is empty, 0 is returned. This means all Attributes
     * of the Implication (premise+conclusion) have to be present in the
     * Context.
     * @param implication Implication from which the support will
     *                    be computed.
     * @param context Context Object from which the Implication
     *                will be checked against.
     * @return The Support of this Implication ranging from
     * 0 to 1 [0,1].
     */
    static <O,A,T extends Context<O,A>, S extends Implication<O,A>> BigDecimal computeImplicationSupport(S implication, T context){
        //First check if the context has Objects,
        //if not return 0
        if(context.getContextObjects().isEmpty())return new BigDecimal(0);
        //Check if the premise and the conclusion of the implication
        //are contained in the Attribute List of the context
        //Create HashSet, which contains both premise and conclusion attributes
        HashSet<Attribute<O,A>> implAttributes = new HashSet<>();
        //Add all Attributes from the implication to the HashSet
        implAttributes.addAll(implication.getPremise());
        implAttributes.addAll(implication.getConclusion());
        //Now check if each Attribute is also contained in the context
        //If an Attribute is not contained return 0
        for(Attribute<O,A> attribute : implAttributes){
            if(!context.containsAttribute(attribute.getAttributeID()))return new BigDecimal(0);
        }
        //Compute the Prime of the Implication Attributes
        List<ObjectAPI<O,A>> implObjectPrime = computePrimeOfAttributes(new ArrayList<>(implAttributes),context);
        //Compute the support for this implication and return it
        //Create BigDecimal objects
        BigDecimal primeSize = new BigDecimal(implObjectPrime.size());
        BigDecimal contextSize = new BigDecimal(context.getContextObjects().size());
        return primeSize.divide(contextSize,5, RoundingMode.UP);
    }


    /**
     * Reduces the given context, i.e., removes all empty rows and columns, as well
     * as all full rows and columns.
     * @param context The context to be reduced.
     * @param <O> Type of the Objects
     * @param <A> Type of the Attributes
     * @param <T> Type of the Context, which needs
     *           to implement the Context interface.
     */
    static <O,A,T extends Context<O,A>> void reduceContext(T context){
        //Remove all "empty" rows
        context.getContextObjects().removeIf(object -> Computation.computePrimeOfObjects(Collections.singletonList(object), context).size() == 0);
        //Remove all "empty" columns
        context.getContextAttributes().removeIf(attribute -> Computation.computePrimeOfAttributes(Collections.singletonList(attribute), context).size() == 0);

        //Get the List of Objects
        List<ObjectAPI<O,A>> contextObjects = new ArrayList<>(context.getContextObjects());

        //Now remove all rows, where an object has every attribute
        for(ObjectAPI<O,A> objectAPI : contextObjects){
            if(Computation.computePrimeOfObjects(Collections.singletonList(objectAPI),context).size() == context.getContextAttributes().size()){
                //First remove this object from every attribute
                for(Attribute<O,A> attribute : context.getContextAttributes()){
                    attribute.getDualEntities().remove(objectAPI.getObjectID());
                }
                //Afterwards remove the objects itself
                context.getContextObjects().remove(objectAPI);
            }
        }

        //Get the List of Attributes
        List<Attribute<O,A>> attributes = new ArrayList<>(context.getContextAttributes());

        //Now remove all columns, where every object has a specific attribute
        for(Attribute<O,A> attribute : attributes){
            if(Computation.computePrimeOfAttributes(Collections.singletonList(attribute),context).size() == context.getContextObjects().size()){
                //First remove this attribute from every object
                for(ObjectAPI<O,A> objectAPI : context.getContextObjects()){
                    objectAPI.getDualEntities().remove(attribute.getAttributeID());
                }
                //Afterwards remove the attribute itself
                context.getContextAttributes().remove(attribute);
            }
        }
    }

    /**
     * Computes the Confidence of an Implication w.r.t. a given Context Object.
     * This method enables association rule mining.
     * @param implication The Implication from which the Confidence should be computed.
     * @param context Context Object used to compute the Prime of the
     *                Attributes of the Implication.
     * @param <O> Type of the Objects
     * @param <A> Type of the Attributes
     * @param <T> Type of the Context
     * @param <S> Type of the Implication
     * @return A double value with the range between 0 and 1. It always returns 1, if
     * the prime of the premise of the Implication is empty.
     */
    static <O,A, T extends Context<O,A>, S extends Implication<O,A>> BigDecimal computeConfidence(S implication, T context){
        //First compute the Prime of the premise of the implication
        //and check if it is empty:
        List<ObjectAPI<O,A>> prime = Computation.computePrimeOfAttributes(implication.getPremise(),context);
        if(prime.isEmpty()){
            //Return 1
            return new BigDecimal(1);
        }else{
            //Compute the confidence
            //Add the Attributes of the premise and the conclusion together
            List<Attribute<O,A>> implicationAttributes = new ArrayList<>(implication.getPremise());
            implicationAttributes.addAll(implication.getConclusion());
            //Compute the prime of the List of all Attributes of the Implication
            //and divide the result with the prime of the premise, return the result
            BigDecimal implSize = new BigDecimal(Computation.computePrimeOfAttributes(implicationAttributes,context).size());
            BigDecimal premiseSize = new BigDecimal(Computation.computePrimeOfAttributes(implication.getPremise(),context).size());
            return implSize.divide(premiseSize,RoundingMode.CEILING);
        }
    }

    //TODO JAVADOC + Enable Generic Attribute Type instead of "String"
    static <O,A,V> Context<O,String> nominalScaleContext(MultiValuedContext<O,A,V> multiValuedContext, boolean unique){

        //Create Single-Valued Context to be returned
        Context<O,String> context = new FCAFormalContext<>(){};

        //Iterate over each Attribute and scale it nominally
        //Add the scaled attribute to the context object
        for(MultiValuedAttribute<O,A,V> attribute : multiValuedContext.getContextAttributes()){
            List<ObjectAPI<O,String>> objects = nominalScaleAttribute(attribute, unique);
            for(ObjectAPI<O,String> object : objects){
                if(context.containsObject(object.getObjectID())){
                    for(Object atr : object.getDualEntities()){
                        context.getObject(object.getObjectID()).addAttribute(atr.toString());
                        if(context.containsAttribute(atr.toString())){
                            //Get the Attribute and add the object to it
                            context.getAttribute(atr.toString()).addObject(object.getObjectID());
                        }else {
                            Attribute<O, String> newAttribute = new FCAAttribute<>(atr.toString());
                            newAttribute.addObject(object.getObjectID());
                            context.addAttribute(newAttribute);
                        }

                    }
                }else {
                    context.addObject(object);
                }
            }
        }
        return context;
    }

    //TODO JAVADOC + Enable Generic Attribute Type instead of "String"

    /**
     *
     * @param attribute
     * @param unique If the Values of the Attribute are unique (are only present
     *               in this Attribute) set it to true, otherwise false.
     * @param <O>
     * @param <A>
     * @param <V>
     * @return
     */
    static <O,A,V> List<ObjectAPI<O,String>> nominalScaleAttribute(MultiValuedAttribute<O,A,V> attribute, boolean unique){

        //Create List to be returned
        //This List contains Pairs of Incidences with an Object and an Attribute
        List<ObjectAPI<O,String>> binaryIncidences = new ArrayList<>();

        //Go through each Pair of the dual entities of the multi-valued attribute
        for(Pair<MultiValuedObject<O,A,V>, List<V>> p : attribute.getDualEntities()){
            ObjectAPI<O,String> objectAPI = new FCAObject<>(p.getLeft().getObjectID());
            for(V v : p.getRight()){
                String s;
                if(unique){
                    s = v.toString();
                }else{
                    s = "["+attribute.getAttributeID().toString()+"]".concat(v.toString());
                }
                objectAPI.addAttribute(s);
            }
            binaryIncidences.add(objectAPI);
        }
        return binaryIncidences;
    }

    //TODO
    @SuppressWarnings("unchecked")
    static <O,A,V> Context<O,String> dichotomScaleContext(MultiValuedContext<O,A,V> multiValuedContext){

        //Create Context to be returned
        Context<O,String> context = new FCAFormalContext<>(){};

        //Iterate over each multi valued attribute of the context and scale it dichotome
        for(MultiValuedAttribute<O,A,V> multiValuedAttribute : multiValuedContext.getContextAttributes()){

            //Create new Attributes and add them to the context object
            Attribute<O,String> atrEpsilon = new FCAAttribute<>("("+multiValuedAttribute.getAttributeID()+",ε)");
            Attribute<O,String> atrPhi = new FCAAttribute<>("("+multiValuedAttribute.getAttributeID()+",φ)");

            context.addAttribute(atrEpsilon);
            context.addAttribute(atrPhi);

            //Iterate over the objects
            for(MultiValuedObject<O,A,V> mvObject: multiValuedContext.getContextObjects()){

                //Check if Object is already contained
                ObjectAPI<O,String> objectAPI;

                if(context.containsObject(mvObject.getObjectID())){
                    objectAPI = context.getObject(mvObject.getObjectID());
                }else{
                    //Create new (binary) Object and it to the context object
                    objectAPI = new FCAObject<>(mvObject.getObjectID());
                    context.addObject(objectAPI);
                }

                //Check if the current multivalued object has the current multivalued attribute
                if(mvObject.containsAttribute(multiValuedAttribute)){
                    //Add the Incidence between object and attribute
                    context.getObject(objectAPI.getObjectID()).addAttribute("("+multiValuedAttribute.getAttributeID()+",φ)");
                    context.getAttribute("("+multiValuedAttribute.getAttributeID()+",φ)").addObject(objectAPI.getObjectID());
                }else{
                    context.getObject(objectAPI.getObjectID()).addAttribute("("+multiValuedAttribute.getAttributeID()+",ε)");
                    context.getAttribute("("+multiValuedAttribute.getAttributeID()+",ε)").addObject(objectAPI.getObjectID());
                }

            }//end for objects

        }//end for attributes

        return context;
    }

    //TODO
    static <O,A,V> void scaleAttribute(MultiValuedAttribute<O,A,V> attribute, Context<O,A> scale){

    }
}
