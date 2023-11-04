package fcalib.lib.fca;
/*
 * This library is an enhanced version of the
 * FCAlib
 * @see <a href="https://github.com/julianmendez/fcalib">https://github.com/julianmendez/fcalib</a>
 * @author Leon Geis
 * @version 0.1
 */

import java.util.ArrayList;
import java.util.List;

import fcalib.api.fca.Attribute;

/**
 * Describes a single Attribute with the
 * corresponding Objects.
 * @author Leon Geis
 */
public class FCAAttribute<O,A> implements Attribute<O,A> {

    /**
     * ID of the Attribute.
     */
    private A attributeID;

    /**
     * List of Objects an Attribute has.
     * Can be seen as a "x" in the given context
     * for that Attribute.
     */
    private List<O> objects;

    /**
     * Default constructor.
     */
    public FCAAttribute(){
        this.objects=new ArrayList<>();
        this.attributeID=null;
    }

    /**
     * Constructor of the class, which creates a new
     * and empty List of Objects.
     */
    public FCAAttribute(A id){
        this.objects = new ArrayList<>();
        this.attributeID = id;
    }

    /**
     * Get ID of the Attribute.
     * @return Attribute ID.
     */
    public A getAttributeID() {
        return this.attributeID;
    }

    /**
     * Set Name of the Attribute.
     * @param id ID of the name.
     */
    public void setAttributeID(A id) {
        this.attributeID = id;
    }

    /**
     * Get all Objects, which have this attribute.
     * @return List of Objects.
     */
    @Override
    public List<O> getDualEntities() {
        return objects;
    }

    /**
     * Adds an Object to the Attribute.
     * @param object Object that has to be added.
     */
    public void addObject(O object){
        this.objects.add(object);
    }

}
