package fcalib.api.fca;


import java.util.List;
//TODO JAVADOC
public interface MultiValuedContext<O,A,V> {

    int getContextID();

    List<MultiValuedObject<O,A,V>> getContextObjects();

    List<MultiValuedAttribute<O,A,V>> getContextAttributes();

    List<V> getValues();

    boolean containsMultiValuedObject(MultiValuedObject<O,A,V> object);

    boolean containsMultiValuedAttribute(MultiValuedAttribute<O,A,V> attribute);

    <T extends MultiValuedObject<O,A,V>> boolean addMultiValuedObject(T mvobject);

    <T extends MultiValuedAttribute<O,A,V>> boolean addMultiValuedAttribute(T mvattribute);

}
