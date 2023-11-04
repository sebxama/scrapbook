package fcalib.lib.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describes a List consisting of Pairs.
 * Each Pair consists of an Element (or Object)
 * on the left and a Integer on the right. The Integer
 * Value is used to create an Index on all Elements.
 * @param <O> Type of the Elements on the Left Side.
 * @author Leon Geis
 */
public class IndexedList<O> {

    /**
     * List of Pairs of Elements and the corresponding
     * Integer Index.
     */
    List<Pair<O,Integer>> indexedList;

    /**
     * Creates a new IndexedList, by using the List
     * given as a parameter, and indexes each of the Element.
     * @param oList List that should be indexed.
     */
    public IndexedList(List<O> oList){
        this.indexedList = new ArrayList<>();
        //Standard here is iterating over the List of type O objects and adding Integer Index
        int i = 0;
        for(O o : oList){
            //Create a new Pair Object for each Object o.
            this.indexedList.add(new Pair<>(o,i));
            i++;
        }
    }

    /**
     * Default Constructor.
     * Note: Necessary when using already indexed Pairs.
     */
    public IndexedList(){
        this.indexedList = new ArrayList<>();
    }

    /**
     * @return List of Pairs of the indexed Elements.
     */
    public List<Pair<O, Integer>> getIndexedList() {
        return this.indexedList;
    }

    /**
     * @param indexedList List of Pairs.
     */
    public void setIndexedList(List<Pair<O, Integer>> indexedList) {
        this.indexedList = indexedList;
    }

    /**
     * Returns a Pair Object of the List based on the specified
     * index.
     * @param index Index of the Element.
     * @return Pair with left being the Object and right the
     * specified index.
     */
    public Pair<O,Integer> getPair(int index){
        return this.indexedList.get(index);
    }

    /**
     * Get the Index of a specified Object.
     * @param o Object
     * @return Index of the Object as Integer, when the Object
     * is not contained, returns <code>null</code>.
     */
    public Integer getIndex(O o){
        for(Pair<O,Integer> pair : this.indexedList){
            if(pair.getLeft().equals(o))return pair.getRight();
        }
        return null;
    }

    /**
     * Returns a List of all Elements of all Pairs of
     * this List (E,I).
     * @return List of Elements.
     */
    public List<O> getObjects(){
        return this.indexedList.stream().map(Pair::getLeft).collect(Collectors.toList());
    }

    /**
     * @return <code>true</code> if this List is empty, <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return this.indexedList.isEmpty();
    }

    /**
     * Checks if an Object exists in the List on the left side
     * of each Pair.
     * @param left Object
     * @return <code>true</code> if Object is contained, <code>false</code> otherwise.
     */
    public boolean contains(O left) {
        return this.indexedList.stream().map(Pair::getLeft).collect(Collectors.toList()).contains(left);
    }

    /**
     * Removes the Pair of the specified Object, if
     * the Pair of the Object is contained in the List.
     * @param left Object
     */
    public void remove(O left) {
        this.indexedList.removeIf(p -> p.getLeft().equals(left));
    }

    /**
     * Adds a Pair to the List.
     * @param pair Pair to be added.
     */
    public void add(Pair<O,Integer> pair){
        this.indexedList.add(pair);
    }

    /**
     * Adds all Pairs of the specified List, that are not
     * already present.
     * @param list List of Pairs to be added.
     */
    public void addAll(List<Pair<O,Integer>> list){
        //Go through each Pair and check if it is contained
        //If not add it to the List
        for(Pair<O,Integer> p : list){
            if(!this.indexedList.contains(p)){
                this.indexedList.add(p);
            }
        }
    }

    /**
     * Checks if the current and the given List are equal.
     * Here ONLY the Left side of each Pair are considered.
     * Hence, not making use of the indices!
     * @param list IndexedList to be checked
     * @return <code>true</code> if they are the same size and
     * contain the same elements, <code>false</code> otherwise.
     */
    public boolean equals(IndexedList<O> list){
        return list.getObjects().equals(this.indexedList);
    }


    public O getMaxElement(){
        //Get List of all Indices
        List<Integer> indexList = new ArrayList<>();
        for(Pair<O,Integer> p : this.indexedList){
            indexList.add(p.getRight());
        }
        //Return Element with the highest Index
        return getPair(Collections.max(indexList)).getLeft();

    }

}
