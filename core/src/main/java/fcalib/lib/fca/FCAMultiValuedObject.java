package fcalib.lib.fca;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fcalib.api.fca.MultiValuedAttribute;
import fcalib.api.fca.MultiValuedObject;
import fcalib.lib.utils.Pair;

/**
 * Class which represents an Object
 * in a multi-valued Context.
 * @param <O> Type of the Object itself.
 * @param <A> Type of the Attributes the Object has.
 * @param <V> Type of the Values the Object has w.r.t
 *           its Attributes.
 */
public class FCAMultiValuedObject<O,A,V> implements MultiValuedObject<O,A,V> {

    /**
     * ID of the Object.
     */
    private O objectID;

    /**
     * List of Pairs of the Object.
     * The first element of the pair is
     * an Attribute this Object has
     * and the second a List of values this Object
     * has w.r.t. the first element (Attribute).
     */
    private List<Pair<MultiValuedAttribute<O,A,V>, List<V>>> attributes;

    public FCAMultiValuedObject(){
        this.objectID = null;
        this.attributes = new ArrayList<>();
    }

    public FCAMultiValuedObject(O o){
        this.objectID = o;
        this.attributes = new ArrayList<>();
    }

    public boolean addAttribute(MultiValuedAttribute<O,A,V> a, List<V> v){
        if(containsAttribute(a)){
            return false;
        }else{
            this.attributes.add(new Pair<>(a,v));
            return true;
        }
    }

    public O getObjectID(){
        return this.objectID;
    }

    public void setObjectID(O o){
        this.objectID = o;
    }

    public boolean containsAttribute(MultiValuedAttribute<O,A,V> a){
        if(this.attributes.stream().map(Pair::getLeft).collect(Collectors.toList()).stream().map(MultiValuedAttribute::getAttributeID).
                collect(Collectors.toList()).contains(a.getAttributeID())){
            return true;
        }else{
            return false;
        }
    }

    public Pair<MultiValuedAttribute<O,A,V>,List<V>> getAttribute(MultiValuedAttribute<O,A,V> a){
        if(containsAttribute(a)){
            int index = this.attributes.stream().map(Pair::getLeft).collect(Collectors.toList()).
                    stream().map(MultiValuedAttribute::getAttributeID).collect(Collectors.toList()).indexOf(a.getAttributeID());
            return this.attributes.get(index);
        }else{
            return null;
        }
    }

    public List<Pair<MultiValuedAttribute<O,A,V>,List<V>>> getDualEntities() {
        return this.attributes;
    }
}
