package fcalib.api.fca;

import java.util.List;

import fcalib.lib.utils.Pair;
//TODO JAVADOC
public interface MultiValuedAttribute<O,A,V>{

    A getAttributeID();

    void setAttributeID(A a);

    boolean addObject(MultiValuedObject<O,A,V> o, List<V> v);

    Pair<MultiValuedObject<O,A,V>, List<V>> getObject(MultiValuedObject<O,A,V> o);
    boolean containsObject(MultiValuedObject<O,A,V> o);

    List<Pair<MultiValuedObject<O,A,V>, List<V>>> getDualEntities();

}
