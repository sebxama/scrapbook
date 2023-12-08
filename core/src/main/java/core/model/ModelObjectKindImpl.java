package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class ModelObjectKindImpl extends KindImpl<ModelObject, Property, Subject> implements ModelObjectKind {

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
	
}
