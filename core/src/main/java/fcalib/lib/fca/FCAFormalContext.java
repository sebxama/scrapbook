package fcalib.lib.fca;
/*
 * This library is an enhanced version of the
 * FCAlib
 * @see <a href="https://github.com/julianmendez/fcalib">https://github.com/julianmendez/fcalib</a>
 * @author Leon Geis
 * @version 0.1
 */

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import fcalib.api.fca.Attribute;
import fcalib.api.fca.Context;
import fcalib.api.fca.ObjectAPI;

/**
 * Describes a single Formal Context with
 * the corresponding Objects and Attributes.
 * The first type parameter describes the type
 * of the objects of the context and the second of
 * the corresponding attributes.
 *
 * @author Leon Geis
 */
public abstract class FCAFormalContext<O,A> implements Context<O,A>{

    /**
     * ID of the Context.
     */
    private static int contextID = 0;

    /**
     * Expert of the Context.
     */
    private FCAExpert<O,A> expert;

    /**
     * List of all Objects of the Context.
     */
    private List<ObjectAPI<O,A>> contextObjects;

    /**
     * List of all Attributes of the Context.
     */
    private List<Attribute<O,A>> contextAttributes;

    /**
     * Type of the Objects of the Context.
     */
    private final Type objectType;

    /**
     * Type of the Attributes of the Context.
     */
    private final Type attributeType;

    /**
     * Constructor of the class creates two empty Lists
     * for the Attributes and the Objects and initializes the
     * FormalContext Object for computation purposes, as well
     * as an Expert Object.
     * Making use of an anonymous inner class to store the type parameter
     * during runtime.
     */
    public FCAFormalContext(){
        this.contextAttributes=new ArrayList<>();
        this.contextObjects=new ArrayList<>();
        //Save Types
        Type type = getClass().getGenericSuperclass();
        this.objectType = ((ParameterizedType)type).getActualTypeArguments()[0];
        this.attributeType = ((ParameterizedType)type).getActualTypeArguments()[1];
        //this.expert = new FCAExpert();
        //Increment ID
        contextID++;
    }

    /**
     * @return context ID of the context.
     */
    public int getContextID() {
        return contextID;
    }

    /**
     * Creates an Object and adds it to the Context.
     * @param o ID of new Object
     */
    //TODO UNCHECKED METHOD INVOCATION WARNING
    public <T extends ObjectAPI<O,A>> void createObject(O o, Class<T> clazz){
            try {
                ObjectAPI<O, A> newObject = clazz.getConstructor().newInstance();
                newObject.setObjectID(o);
                this.contextObjects.add(newObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * Creates an Attribute and adds it to the Context.
     * @param a ID of new Attribute
     */
    //TODO UNCHECKED METHOD INVOCATION WARNING
    public <T extends Attribute<O,A>> void createAttribute(A a, Class<T> clazz){
        try {
            Attribute<O, A> newAttribute = clazz.getConstructor().newInstance();
            newAttribute.setAttributeID(a);
            this.contextAttributes.add(newAttribute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a given Attribute ID is contained in the Context.
     * @param a Attribute ID
     * @return <code>true</code> if Attribute ID is in List, <code>false</code>
     * otherwise.
     */
    public boolean containsAttribute(A a){
        //Create a List for all Attribute IDs
        List<A> ids = new ArrayList<>();
        //Add all Attribute IDs to the List
        for(Attribute<O,A> attr : this.contextAttributes){
            ids.add(attr.getAttributeID());
        }
        //Check if Parameter a is contained.
        return ids.contains(a);
    }

    /**
     * Checks if a given Object ID is contained in the Context.
     * @param o Object ID
     * @return <code>true</code> if Object ID is in List, <code>false</code>
     * otherwise.
     */
    public boolean containsObject(O o){
        //Create a List for all Object IDs
        List<O> ids = new ArrayList<>();
        //Add all Object IDs to the List
        for(ObjectAPI<O,A> obj : this.contextObjects){
            ids.add(obj.getObjectID());
        }
        //Check if Parameter a is contained.
        return ids.contains(o);
    }

    /**
     * Adds an ObjectAPI object to the Context, as well as the Attributes
     * the Object has. All existing Objects in the Context, will be checked,
     * if they also have this Attribute. If they have, it will be added to
     * their Attribute List.
     * Note: If an Attribute is not present in the List of Attributes of
     * the context, a new FCAAttribute will be created.
     * @param o Object to be added
     */
    @SuppressWarnings("unchecked")
    public <T extends ObjectAPI<O,A>> void addObject(T o){
        //Add Object to Object List
        this.contextObjects.add(o);
        //For each Attribute of the new Object, check
        //if Attribute is already present in the Attribute List;
        //if not add the Attribute with the new Object to the Context.
        for(Object attr : o.getDualEntities()){
            if(!this.containsAttribute((A) attr)){
                Attribute<O,A> newAttribute = new FCAAttribute<>((A)attr);
                newAttribute.addObject(o.getObjectID());
                this.contextAttributes.add(newAttribute);
            }else {
                //Check if other Objects have this Attribute
                for(ObjectAPI<O,A> object : this.getContextObjects()){
                    //If an Object has this Attribute add it to the Object
                    //List of the Attribute. Care for the case that the Object List
                    //of the Attribute already contains the Object.
                    if(object.getDualEntities().contains(attr) && !getAttribute((A)attr).getDualEntities().contains(object.getObjectID())){
                        getAttribute((A)attr).addObject(object.getObjectID());
                    }
                }
            }
        }
    }

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
    @SuppressWarnings("unchecked")
    public <T extends Attribute<O,A>> void addAttribute(T a){
        //Add Attribute to Attribute List
        this.contextAttributes.add(a);
        //For each Object of the new Attribute, check
        //if Object is already present in the Object List;
        //if not add the Object with the new Attribute to the Context.
        for(Object obj : a.getDualEntities()){
            if(!this.containsObject(((O)obj))){
                ObjectAPI<O,A> newObject = new FCAObject<>((O)obj);
                newObject.addAttribute(a.getAttributeID());
                this.contextObjects.add(newObject);
            }else{
                //Check if other Attributes have this Object
                for(Attribute<O,A> atr : this.getContextAttributes()){
                    //If an Attribute has this Object add it to the Attribute
                    //List of the Object. Care for the case that the Attribute List
                    //of the Object already contains the Attribute.
                    if(atr.getDualEntities().contains(obj) && !getObject((O)obj).getDualEntities().contains(atr.getAttributeID())){
                        getObject((O)obj).addAttribute(atr.getAttributeID());
                    }
                }
            }
        }
    }

    /**
     * Get all Objects of the context.
     * @return List of FCAObjects.
     */
    public List<ObjectAPI<O,A>> getContextObjects(){
        return this.contextObjects;
    }

    /**
     * Get all Attributes of the context.
     * @return List of FCAAttributes.
     */
    public List<Attribute<O,A>> getContextAttributes(){
        return this.contextAttributes;
    }

    /**
     * Returns a single Attribute of the Context.
     * @param a The ID of the Attribute to be returned.
     * @return Attribute Object, if the Attribute is in the
     * Attribute List of the Context, <code>null</code> otherwise.
     */
    public Attribute<O,A> getAttribute(A a){
        for(Attribute<O,A> attr : this.contextAttributes){
            if(attr.getAttributeID().equals(a))return attr;
        }
        return null;
    }

    /**
     * Returns a single Object of the Context.
     * @param o The ID of the Object to be returned.
     * @return FCAObject Object, if the Object is in the
     * Object List of the Context, <code>null</code> otherwise.
     */
    public ObjectAPI<O,A> getObject(O o){
        for(ObjectAPI<O,A> obj : this.contextObjects){
            if(obj.getObjectID().equals(o))return obj;
        }
        return null;
    }

    /**
     * Get all Attributes, which correspond to the List of IDs.
     * @param IDs List of IDs
     * @return List of all Attributes, corresponding to the List
     *          of IDs.
     */
    public List<Attribute<O,A>> getAttributes(List<A> IDs){
        //First check if the List of IDs is empty
        if(IDs.isEmpty()){
            //If it is return empty List;
            return new ArrayList<>();
            //If not check if Type of the IDs equals the Type of the Attributes
            //of the context.
        }else if(this.attributeType.equals(IDs.get(0).getClass())){
            //Create HashSet to be returned
            HashSet<Attribute<O,A>> set = new HashSet<>();
            //Get each Attribute according to the List of IDs
            for(A a: IDs){
                set.add(getAttribute(a));
            }
            //Return List of all Attributes
            return new ArrayList<>(set);
        }else{
            //If the Type of the List is not equal the Type of the Attributes of this context
            //return an empty List
            return new ArrayList<>();
        }
    }

    /**
     * Get all Objects, which correspond to the List of IDs.
     * @param IDs List of IDs
     * @return List of all Objects, corresponding to the List
     *          of IDs.
     */
    public List<ObjectAPI<O,A>> getObjects(List<O> IDs){
        //First check if the List of IDs is empty
        if(IDs.isEmpty()){
            //If it is return empty List;
            return new ArrayList<>();
            //If not check if Type of the IDs equals the Type of the Objects
            //of the context.
        }else if(this.objectType.equals(IDs.get(0).getClass())){
            //Create HashSet to be returned
            HashSet<ObjectAPI<O,A>> set = new HashSet<>();
            //Get each Object according to the List of IDs
            for(O o: IDs){
                set.add(getObject(o));
            }
            //Return List of all Objects
            return new ArrayList<>(set);
        }else{
            //If the Type of the List is not equal the Type of the Objects of this context
            //return an empty List
            return new ArrayList<>();
        }
    }

    //TODO
    void update(){

    }

}
