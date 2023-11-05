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
	public Set<Property> getProperties() {
		Set<Property> ret = new HashSet<Property>();
		for(Resource res : super.getInstances())
			ret.addAll(Properties.getInstance().getProperties(null, null, res, null));
		return ret;
	}
	
	@Override
	public Set<Property> getInstanceProperties(Resource instance) {
		return Properties.getInstance().getProperties(null, null, instance, null);
	}

	@Override
	public Set<Subject> getAttributeSubjects(Resource attribute) {
		return Subjects.getInstance().getSubjects(null, attribute, null, null);
	}

	@Override
	public Set<ModelObject> getValueObjects(Resource value) {
		return ModelObjects.getInstance().getObjects(null, null, null, value);
	}
	
}
