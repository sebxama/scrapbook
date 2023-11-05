package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContextKindImpl extends KindImpl implements ContextKind {
	
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
	
	@Override
	public Set<Context> getContexts() {
		Set<Context> ret = new HashSet<Context>();
		for(Resource res : super.getInstances())
			ret.addAll(Contexts.getInstance().getContexts(res, null, null, null));
		return ret;
	}
	
	@Override
	public Set<Context> getInstanceContexts(Resource instance) {
		return Contexts.getInstance().getContexts(instance, null, null, null);
	}

	@Override
	public Set<Property> getAttributeProperties(Resource attribute) {
		return Properties.getInstance().getProperties(null, null, attribute, null);
	}

	@Override
	public Set<ModelObject> getAttributeValues(Resource value) {
		return ModelObjects.getInstance().getObjects(null, null, null, value);
	}
	
}
