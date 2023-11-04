package fcalib.lib.fca;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fcalib.api.fca.MultiValuedAttribute;
import fcalib.api.fca.MultiValuedContext;
import fcalib.api.fca.MultiValuedObject;
import fcalib.lib.utils.Pair;

//TODO JAVADOC
public abstract class FCAMultiValuedFormalContext<O,A,V> implements MultiValuedContext<O,A,V> {

    private static int contextID = 0;

    private List<V> values;

    private List<MultiValuedObject<O,A,V>> contextObjects;

    private List<MultiValuedAttribute<O,A,V>> contextAttributes;

    /**
     * Type of the Objects of the Context.
     */
    private final Type objectType;

    /**
     * Type of the Attributes of the Context.
     */
    private final Type attributeType;

    public FCAMultiValuedFormalContext(){
        this.contextObjects = new ArrayList<>();
        this.contextAttributes = new ArrayList<>();
        this.values = new ArrayList<>();
        //Safe types
        Type type = getClass().getGenericSuperclass();
        this.objectType = ((ParameterizedType)type).getActualTypeArguments()[0];
        this.attributeType = ((ParameterizedType)type).getActualTypeArguments()[1];
        //Increment ID
        contextID++;
    }
    public int getContextID(){
        return contextID;
    }
    public List<MultiValuedObject<O,A,V>> getContextObjects(){
        return this.contextObjects;
    }
    public List<MultiValuedAttribute<O,A,V>> getContextAttributes(){
        return this.contextAttributes;
    }
    public List<V> getValues(){
        return this.values;
    }

    public boolean containsMultiValuedObject(MultiValuedObject<O,A,V> object){
        return this.contextObjects.stream().map(MultiValuedObject::getObjectID).collect(Collectors.toList()).contains(object.getObjectID());
    }

    public boolean containsMultiValuedAttribute(MultiValuedAttribute<O,A,V> attribute){
        return this.contextAttributes.stream().map(MultiValuedAttribute::getAttributeID).collect(Collectors.toList()).contains(attribute.getAttributeID());
    }

    public <T extends MultiValuedObject<O,A,V>> boolean addMultiValuedObject(T object){
        //Check if object is already contained
        if(!containsMultiValuedObject(object)){
            this.contextObjects.add(object);;
            //For each MultiValuedAttribute of the Object
            //Check if it is contained in the context
            //If not create a new MultiValuedAttribute Object
            for(Pair<MultiValuedAttribute<O,A,V>, List<V>> pair : object.getDualEntities()){
                //If the ID of the Attribute is not present in the Context
                if(!containsMultiValuedAttribute(pair.getLeft())){
                    //Create new MultiValuedAttribute Object and add all values
                    MultiValuedAttribute<O,A,V> atr = new FCAMultiValuedAttribute<>(pair.getLeft().getAttributeID());
                    atr.addObject(object, pair.getRight());
                    //Add the new MVAttribute, as well as all values to the context
                    this.contextAttributes.add(atr);
                }else{
                    //Get the corresponding MVAttribute Object with the same ID
                    MultiValuedAttribute<O,A,V> a = this.contextAttributes.get(this.contextAttributes.stream().map(MultiValuedAttribute::getAttributeID).
                            collect(Collectors.toList()).indexOf(pair.getLeft().getAttributeID()));
                    //If this MVAttribute does not have this MVObject in its List
                    if(!a.containsObject(object)){
                        //Add it to its list, as well as the List of Values
                        a.addObject(object,pair.getRight());
                    }
                }
                //Add all Values to the Context
                for(V v : pair.getRight()){
                    if(!this.values.contains(v)){
                        this.values.add(v);
                    }
                }
            }
            return true;
        }else{
            //Get the Object
            MultiValuedObject<O,A,V> cxtObject = this.contextObjects.get(this.contextObjects.stream().map(MultiValuedObject::getObjectID).
                    collect(Collectors.toList()).indexOf(object.getObjectID()));
            //Add the new Attributes and do not change the already existing ones
            for(Pair<MultiValuedAttribute<O,A,V>, List<V>> pair : object.getDualEntities()){
                if(!cxtObject.containsAttribute(pair.getLeft()) && !containsMultiValuedAttribute(pair.getLeft())){
                    MultiValuedAttribute<O,A,V> newAttribute = new FCAMultiValuedAttribute<>(pair.getLeft().getAttributeID());
                    newAttribute.addObject(cxtObject,pair.getRight());
                    cxtObject.addAttribute(newAttribute,pair.getRight());
                    this.contextAttributes.add(newAttribute);
                }else if(!cxtObject.containsAttribute(pair.getLeft()) && containsMultiValuedAttribute(pair.getLeft())){
                    //Get the Attribute of the Context
                    MultiValuedAttribute<O,A,V> cxtAttribute = this.contextAttributes.get(this.contextAttributes.stream().map(MultiValuedAttribute::getAttributeID).
                            collect(Collectors.toList()).indexOf(pair.getLeft().getAttributeID()));

                    cxtObject.addAttribute(cxtAttribute,pair.getRight());
                    cxtAttribute.addObject(cxtObject,pair.getRight());

                }
                //Add all Values to the Context
                for(V v : pair.getRight()){
                    if(!this.values.contains(v)){
                        this.values.add(v);
                    }
                }
            }
        }
        return false;
    }


    public <T extends MultiValuedAttribute<O,A,V>> boolean addMultiValuedAttribute(T attribute){
        //Check if attribute is already contained
        if(!containsMultiValuedAttribute(attribute)){
            this.contextAttributes.add(attribute);
            //For each MultiValuedAttribute of the Object
            //Check if it is contained in the context
            //If not create a new MultiValuedAttribute Object
            for(Pair<MultiValuedObject<O,A,V>, List<V>> pair : attribute.getDualEntities()){
                //If the ID of the Attribute is not present in the Context
                if(!containsMultiValuedObject(pair.getLeft())){
                    //Create new MultiValuedAttribute Object and add all values
                    MultiValuedObject<O,A,V> ob = new FCAMultiValuedObject<>(pair.getLeft().getObjectID());
                    ob.addAttribute(attribute,pair.getRight());
                    //Add the new MVAttribute, as well as all values to the context
                    this.contextObjects.add(ob);
                }else{
                    //Get the corresponding MVObject with the same ID
                    MultiValuedObject<O,A,V> ob = this.contextObjects.get(this.contextObjects.stream().map(MultiValuedObject::getObjectID).
                            collect(Collectors.toList()).indexOf(pair.getLeft().getObjectID()));
                    //If this MVAttribute does not have this MVObject in its List
                    if(!ob.containsAttribute(attribute)){
                        //Add it to its list, as well as the List of Values
                        ob.addAttribute(attribute, pair.getRight());
                    }
                }
                for(V v : pair.getRight()){
                    if(!this.values.contains(v)){
                        this.values.add(v);
                    }
                }
            }
            return true;
        }else{
            //Get the Attribute
            MultiValuedAttribute<O,A,V> cxtAttribute = this.contextAttributes.get(this.contextAttributes.stream().map(MultiValuedAttribute::getAttributeID).
                    collect(Collectors.toList()).indexOf(attribute.getAttributeID()));
            //Add the new Objects and do not change the already existing ones
            for(Pair<MultiValuedObject<O,A,V>, List<V>> pair : attribute.getDualEntities()) {
                if (!cxtAttribute.containsObject(pair.getLeft()) && !containsMultiValuedObject(pair.getLeft())) {
                    MultiValuedObject<O, A, V> newObject = new FCAMultiValuedObject<>(pair.getLeft().getObjectID());
                    newObject.addAttribute(cxtAttribute, pair.getRight());
                    cxtAttribute.addObject(newObject, pair.getRight());
                    this.contextObjects.add(newObject);
                } else if (!cxtAttribute.containsObject(pair.getLeft()) && containsMultiValuedObject(pair.getLeft())) {
                    //Get the Object of the Context
                    MultiValuedObject<O, A, V> cxtObject = this.contextObjects.get(this.contextObjects.stream().map(MultiValuedObject::getObjectID).
                            collect(Collectors.toList()).indexOf(pair.getLeft().getObjectID()));
                    cxtObject.addAttribute(cxtAttribute, pair.getRight());
                    cxtAttribute.addObject(cxtObject, pair.getRight());
                }
                //Add all Values to the Context
                for (V v : pair.getRight()) {
                    if (!this.values.contains(v)) {
                        this.values.add(v);
                    }
                }
            }
        }
        return false;
    }

    //TODO
    void update(){

    }

}
