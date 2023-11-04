package core.model;

import java.util.HashMap;
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
	public Set<Subject> getInstanceSubjects(Resource instance) {
		return Subjects.getInstance().getSubjects(null, instance, null, null);
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
