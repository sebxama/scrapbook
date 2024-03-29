package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PropertyKindImpl extends KindImpl<Property, Subject, ModelObject> implements PropertyKind {
	
	static Map<Resource, PropertyKind> instances = new HashMap<>();
	public static PropertyKind getInstance(Resource key) {
		PropertyKind ret = instances.get(key);
		if(ret == null) {
			ret = new PropertyKindImpl(key);
			instances.put(key, ret);
		}
		return ret;
	}
	
	private PropertyKindImpl(Resource iri) {
		super(iri);
	}
	
}
