package fcalib.api.fca;

import java.util.List;

import fcalib.lib.utils.Pair;
//TODO JAVADOC
public interface MultiValuedObject<O,A,V>{

    public O getObjectID();

    void setObjectID(O o);

    boolean addAttribute(MultiValuedAttribute<O,A,V> a, List<V> v);

    boolean containsAttribute(MultiValuedAttribute<O,A,V> a);

    Pair<MultiValuedAttribute<O,A,V>,List<V>> getAttribute(MultiValuedAttribute<O,A,V> a);

    List<Pair<MultiValuedAttribute<O,A,V>,List<V>>> getDualEntities();

}
