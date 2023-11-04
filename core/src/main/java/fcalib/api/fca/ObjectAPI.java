package fcalib.api.fca;

/**
 * Interface describing a single Object.
 * An Object has a List of the Attributes
 * describing the Object and an ID, which can
 * be used as an identifier for this Object.
 * Note: Multiple Objects with the same ID are possible,
 * but these cases are handled by the implementation in the
 * fca package.
 * @param <O> Type of the Object itself.
 * @param <A> Type of the Attributes this Object holds.
 * @author Leon Geis
 */
public interface ObjectAPI<O,A> extends ClosureOperator {

    /**
     * @return ObjectID
     */
    O getObjectID();

    /**
     * @param objectID String of the ID.
     */
    void setObjectID(O objectID);

    /**
     * Adds an Attribute to the Object.
     * @param attribute ID of the Attribute that
     *                 has to be added.
     */
    void addAttribute(A attribute);

}
