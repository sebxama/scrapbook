package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class ModelObjectKindImpl extends KindImpl implements ModelObjectKind {

	static Map<Resource, ModelObjectKind> instances = new HashMap<>();
	public static ModelObjectKind getInstance(Resource key) {
		ModelObjectKind ret = instances.get(key);
		if(ret == null) {
			ret = new ModelObjectKindImpl(key);
			instances.put(key, ret);
		}
		return ret;
	}
	
	private ModelObjectKindImpl(Resource iri) {
		super(iri);
	}
	
	@Override
	public Set<ModelObject> getInstanceObjects() {
		Set<ModelObject> ret = new HashSet<ModelObject>();
		for(Resource res : super.getInstances())
			ret.addAll(ModelObjects.getInstance().getObjects(null, null, null, res));
		return ret;
	}

	@Override
	public Set<Property> getAttributeProperties(Resource instance) {
		return Properties.getInstance().getProperties(null, null, null, instance);
	}

	@Override
	public Set<Subject> getValueSubjects(Resource instance, Resource attribute) {
		return Subjects.getInstance().getSubjects(null, null, attribute, instance);
	}
	
}
