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
	public Set<Subject> getSubjects() {
		Set<Subject> ret = new HashSet<Subject>();
		for(Resource res : super.getInstances())
			ret.addAll(Subjects.getInstance().getSubjects(null, res, null, null));
		return ret;
	}
	
	@Override
	public Set<Subject> getInstanceSubjects(Resource instance) {
		return Subjects.getInstance().getSubjects(null, instance, null, null);
	}

	@Override
	public Set<Property> getAttributeProperties(Resource instance, Resource attribute) {
		return Properties.getInstance().getProperties(null, instance, attribute, null);
	}

	@Override
	public Set<ModelObject> getValueObjects(Resource attribute, Resource value) {
		return ModelObjects.getInstance().getObjects(null, null, attribute, value);
	}

}
