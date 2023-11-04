package fcalib.api.fca;

import java.util.List;

/**
 * Interface describing an Implication in FCA.
 * Each Implication is of the form: A->B,
 * where A and B are subsets of M with M being
 * the set of all Attributes of a Context.
 * @param <O> Type of the Objects of the Attributes.
 * @param <A> Type of the Attributes of the Implication.
 * @author Leon Geis
 */
public interface Implication<O,A> {

    /**
     * Sets the Premise of this Implication.
     * @param premise List of Attributes.
     */
    void setPremise(List<? extends Attribute<O,A>> premise);

    /**
     * Sets the conclusion of this Implication.
     * @param conclusion List of Attributes.
     */
    void setConclusion(List<? extends Attribute<O,A>> conclusion);

    /**
     * Get the Premise of this Implication.
     * @return List of Attributes of the Premise.
     */
    List<? extends Attribute<O, A>> getPremise();

    /**
     * Get the Conclusion of this Implication.
     * @return List of Attributes of the Conclusion.
     */
    List<? extends Attribute<O, A>> getConclusion();

    /**
     * Represents this Implication as a String, with the form:
     * A->B, where the Lists of Attributes of A and B are given.
     * Example: "[a]->[c,d]" with a,c,d ⊆ M (set of all Attributes)
     * Note: The resulting String is inside the quotation marks ("").
     * @return A String representation of the current Implication.
     */
    String toString();

}
