package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class SubjectKindImpl extends KindImpl<Subject, Property, ModelObject> implements SubjectKind {

	static Map<Resource, SubjectKind> instances = new HashMap<>();
	public static SubjectKind getInstance(Resource key) {
		SubjectKind ret = instances.get(key);
		if(ret == null) {
			ret = new SubjectKindImpl(key);
			instances.put(key, ret);
		}
		return ret;
	}
	
	private SubjectKindImpl(Resource iri) {
		super(iri);
	}

}
