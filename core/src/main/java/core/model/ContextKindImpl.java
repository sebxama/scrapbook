package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContextKindImpl extends KindImpl<Context, Property, ModelObject> implements ContextKind {
	
	static Map<Resource, ContextKind> instances = new HashMap<>();
	public static ContextKind getInstance(Resource key) {
		ContextKind ret = instances.get(key);
		if(ret == null) {
			ret = new ContextKindImpl(key);
			instances.put(key, ret);
		}
		return ret;
	}
	
	private ContextKindImpl(Resource iri) {
		super(iri);
	}

}
