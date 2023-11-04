package fcalib.lib.fca;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fcalib.api.fca.MultiValuedAttribute;
import fcalib.api.fca.MultiValuedObject;
import fcalib.lib.utils.Pair;

/**
 * Class which represents an Attribute
 * in a multi-valued Context.
 * @param <O> Type of the corresponding Objects.
 * @param <A> Type of the Attribute itself.
 * @param <V> Type of the Values of the Attribute.
 */
public class FCAMultiValuedAttribute<O,A,V> implements MultiValuedAttribute<O,A,V> {

    /**
     * ID of the Attribute.
     */
    private A attributeID;

    /**
     * List of Pairs of the Attribute.
     * The first element of the pair is
     * an object this attribute is contained in
     * and the second a List of values this attribute
     * has w.r.t. this object.
     */
    private List<Pair<MultiValuedObject<O,A,V>, List<V>>> objects;

    public FCAMultiValuedAttribute(){
        this.attributeID = null;
        this.objects = new ArrayList<>();
    }

    public FCAMultiValuedAttribute(A a){
        this.attributeID = a;
        this.objects = new ArrayList<>();
    }

    public A getAttributeID(){
        return this.attributeID;
    }
    public void setAttributeID(A a){
        this.attributeID = a;
    }

    public boolean addObject(MultiValuedObject<O,A,V> o, List<V> v){
        //Check if Object is already contained
        if(containsObject(o)){
            return false;
        }else{
            this.objects.add(new Pair<>(o,v));
            return true;
        }
    }

    public Pair<MultiValuedObject<O,A,V>, List<V>> getObject(MultiValuedObject<O,A,V> o){
        if(containsObject(o)){
            int index = this.objects.stream().map(Pair::getLeft).collect(Collectors.toList()).stream().map(MultiValuedObject::getObjectID)
            .collect(Collectors.toList()).indexOf(o.getObjectID());
            return this.objects.get(index);
        }else{
            return null;
        }
    }

    public boolean containsObject(MultiValuedObject<O,A,V> o){
        if(this.objects.stream().map(Pair::getLeft).collect(Collectors.toList()).stream().map(MultiValuedObject::getObjectID).
                collect(Collectors.toList()).contains(o.getObjectID())){
            return true;
        }else{
            return false;
        }
    }

    public List<Pair<MultiValuedObject<O,A,V>, List<V>>> getDualEntities() {
        return this.objects;
    }
}
