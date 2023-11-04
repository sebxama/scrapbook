package fcalib.api.fca;

import java.util.List;
/**
 * Describes a Concept of a Context. A
 * Concept is a pair (A,B), where A is called
 * extent and B intent of this concept.
 * A concept always fulfills: A'= B and B'= A, with
 * A⊆G and B⊆M. (G set of Objects and M set of Attributes)
 * @param <O> Type of the Objects
 * @param <A> Type of the Attributes
 * @author Leon Geis
 */
public interface Concept<O,A> {

    /**
     * Get the Extent of this Concept.
     * @return List of Objects for this Concept.
     */
    List<? extends ObjectAPI<O,A>> getExtent();

    /**
     * Set the Extent of an Concept.
     * @param extent List of Objects for this Concept.
     */
    void setExtent(List<? extends ObjectAPI<O, A>> extent);

    /**
     * Get the Intent of this Concept.
     * @return List of Attributes for this Concept.
     */
    List<? extends Attribute<O,A>> getIntent();

    /**
     * Set the Intent of this Concept.
     * @param intent New List of Attributes for this Concept.
     */
    void setIntent(List<? extends Attribute<O, A>> intent);

    /**
     * Prints first the extent of this Concept
     * and afterwards the intent.
     */
    void printConcept();




}
