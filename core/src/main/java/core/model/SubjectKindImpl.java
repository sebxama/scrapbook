package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class SubjectKindImpl extends KindImpl implements SubjectKind {

	static Map<Resource, SubjectKind> instances = new HashMap<>();
	public static SubjectKind getInstance(Resource key) {
		SubjectKind ret = instances.get(key);
		if(ret == null) {
			ret = new SubjectKindImpl(key);
			ret.setResource(key);
			instances.put(key, ret);
		}
		return ret;
	}
	
	private SubjectKindImpl(Resource iri) {
		super(iri);
	}
	
	@Override
	public Set<Subject> getInstanceSubjects() {
		Set<Subject> ret = new HashSet<Subject>();
		for(Resource res : super.getInstances())
			ret.addAll(Subjects.getInstance().getSubjects(null, res, null, null));
		return ret;
	}

	@Override
	public Set<Property> getAttributeProperties(Resource instance) {
		return Properties.getInstance().getProperties(null, instance, null, null);
	}

	@Override
	public Set<ModelObject> getValueObjects(Resource instance, Resource attribute) {
		return ModelObjects.getInstance().getObjects(null, instance, attribute, null);
	}

}
