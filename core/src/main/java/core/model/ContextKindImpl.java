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
	public Set<Context> getInstanceContexts() {
		Set<Context> ret = new HashSet<Context>();
		for(Resource res : super.getInstances())
			ret.addAll(Contexts.getInstance().getContexts(res, null, null, null));
		return ret;
	}

	@Override
	public Set<Property> getAttributeProperties(Resource instance) {
		return Properties.getInstance().getProperties(instance, null, null, null);
	}

	@Override
	public Set<ModelObject> getValueObjects(Resource instance, Resource attribute) {
		return ModelObjects.getInstance().getObjects(instance, null, attribute, null);
	}
	
}
