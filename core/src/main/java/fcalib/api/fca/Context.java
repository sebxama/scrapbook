package fcalib.api.fca;

import java.util.List;

/**
 * Interface describing a Context in FCA.
 * A context class should have a List of Attributes,
 * as well as a List of Objects. These Lists can be
 * of type of the Interfaces (ObjectAPI and Attribute)
 * or of a own Class implementing these interfaces.
 * @param <O> Type of the Objects.
 * @param <A> Type of the Attributes.
 * @author Leon Geis
 */
public interface Context<O,A> {

    /**
     * Get all Objects, which correspond to the List of IDs.
     * Note: If an ID is not present in the Context,
     * <code>null</code> will be added instead.
     * @param IDs List of IDs
     * @return List of all Objects, corresponding to the List
     *          of IDs.
     */
    List<ObjectAPI<O,A>> getObjects(List<O> IDs);

    /**
     * Get all Attributes, which correspond to the List of IDs.
     * Note: If an ID is not present in the Context,
     * <code>null</code> will be added instead.
     * @param IDs List of IDs.
     * @return List of all Attributes, corresponding to the List
     *          of IDs.
     */
    List<Attribute<O,A>> getAttributes(List<A> IDs);

    /**
     * @return context ID of the context.
     */
    int getContextID();

    /**
     * Creates a new Object and adds it to the Context.
     * Note: The class used to create an object needs to have
     * a default constructor without parameter, which will be
     * invoked when executing this method.
     * @param o ID of the Object.
     * @param clazz Class of the Object.
     * @param <T> Class implementing the ObjectAPI interface or
     *           subclass of the FCAObject class.
     */
    <T extends ObjectAPI<O,A>> void createObject(O o, Class<T> clazz);

    /**
     * Creates a new Attribute and adds it to the Context.
     * Note: The class used to create an attribute needs to have
     * a default constructor without parameter, which will be
     * invoked when executing this method.
     * @param a ID of the Attribute.
     * @param clazz Class of the Attribute.
     * @param <T> Class implementing the Attribute interface or
     *           subclass of the FCAAttribute class.
     */
    <T extends Attribute<O,A>> void createAttribute(A a, Class<T> clazz);

    /**
     * Checks if a given Attribute ID is contained in the Context.
     * @param a Attribute ID
     * @return <code>true</code> if Attribute ID is in List, <code>false</code>
     * otherwise.
     */
    boolean containsAttribute(A a);

    /**
     * Checks if a given Object ID is contained in the Context.
     * @param o Object ID
     * @return <code>true</code> if Object ID is in List, <code>false</code>
     * otherwise.
     */
    boolean containsObject(O o);

    /**
     * Adds an ObjectAPI object to the Context, as well as the Attributes
     * the Object has. All existing Objects in the Context, will be checked,
     * if they also have this Attribute. If they have, it will be added to
     * their Attribute List.
     * Note: If an Attribute is not present in the List of Attributes of
     * the context, a new FCAAttribute will be created.
     * @param o Object to be added
     */
    <T extends ObjectAPI<O,A>> void addObject(T o);

    /**
     * Adds an Attribute Object to the Context, as well as all the
     * corresponding Objects the Attribute has.
     * All existing Attributes in the Context, will be checked,
     * if they also have this Object. If they have, it will be added to
     * their Object List.
     * Note: If an Object is not present in the List of Objects of
     * the context, a new FCAObject will be created.
     * @param a Attribute to be added
     */
    <T extends Attribute<O,A>> void addAttribute(T a);

    /**
     * Get all Objects of the context.
     * @return List of Objects.
     */
    List<ObjectAPI<O,A>> getContextObjects();

    /**
     * Get all Attributes of the context.
     * @return List of Attributes.
     */
    List<Attribute<O,A>> getContextAttributes();

    /**
     * Returns a single Attribute of the Context.
     * @param a The ID of the Attribute to be returned.
     * @return Attribute Object, if the Attribute is in the
     * Attribute List of the Context, <code>null</code> otherwise.
     */
    Attribute<O,A> getAttribute(A a);

    /**
     * Returns a single Object of the Context.
     * @param o The ID of the Object to be returned.
     * @return Object, if the Object is in the
     * Object List of the Context, <code>null</code> otherwise.
     */
    ObjectAPI<O,A> getObject(O o);



}
