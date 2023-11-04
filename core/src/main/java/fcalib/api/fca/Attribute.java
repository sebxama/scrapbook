package fcalib.api.fca;

/**
 * Interface describing a single Attribute.
 * An Attribute has a List of the Objects and
 * a ID, which can be used as an identifier for
 * this Attribute.
 * Note: Multiple Attributes with the same ID are possible,
 * but these cases are handled by the implementation in the
 * fca package.
 * @param <O> Type of the Object this Attribute holds.
 * @param <A> Type of the Attribute itself.
 * @author Leon Geis
 */
public interface Attribute<O,A> extends ClosureOperator {

    /**
     * Get ID of the Attribute.
     * @return Attribute ID.
     */
    A getAttributeID();
    /**
     * Set Name of the Attribute.
     * @param id ID of the name.
     */
    void setAttributeID(A id);
    /**
     * Adds an Object to the Attribute.
     * @param object Object that has to be added.
     */
    void addObject(O object);

}
