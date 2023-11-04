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

import fcalib.api.fca.ObjectAPI;

/**
 * Describes a single Object with the
 * corresponding Attributes.
 * @author Leon Geis
 */
public class FCAObject<O,A> implements ObjectAPI<O,A> {

    /**
     * ID of the Object.
     */
    private O objectID;

    /**
     * List of Attributes an Object has.
     * Can be seen as a "x" in the given context
     * for that object.
     */
    private List<A> attributes;

    /**
     * Default Constructor of the class.
     */
    public FCAObject(){
        this.attributes=new ArrayList<>();
        this.objectID=null;
    }

    /**
     * Constructor of the class. Creates a new and empty
     * List of Attributes.
     */
    public FCAObject(O o){
        this.attributes=new ArrayList<>();
        this.objectID=o;
    }

    /**
     * @return ObjectID
     */
    public O getObjectID() {
        return objectID;
    }

    /**
     * @param objectID String of the ID.
     */
    public void setObjectID(O objectID) {
        this.objectID = objectID;
    }

    /**
     * Get all FCAAttributes, that this object has.
     * @return List of FCAAttributes.
     */
    @Override
    public List<A> getDualEntities() {
        return attributes;
    }

    /**
     * Add an Attribute to the Object.
     * @param attribute Attribute that has to be added.
     */
    public void addAttribute(A attribute){
        this.attributes.add(attribute);
    }


}
