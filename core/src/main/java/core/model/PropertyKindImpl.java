package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PropertyKindImpl extends KindImpl implements PropertyKind {
	
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
	
	@Override
	public Set<Property> getInstanceProperties() {
		Set<Property> ret = new HashSet<Property>();
		for(Resource res : super.getInstances())
			ret.addAll(Properties.getInstance().getProperties(null, null, res, null));
		return ret;
	}

	@Override
	public Set<Subject> getAttributeSubjects(Resource instance) {
		return Subjects.getInstance().getSubjects(null, null, instance, null);
	}

	@Override
	public Set<ModelObject> getValueObjects(Resource instance, Resource attribute) {
		return ModelObjects.getInstance().getObjects(null, attribute, instance, null);
	}
	
}
