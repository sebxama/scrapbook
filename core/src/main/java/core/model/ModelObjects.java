package core.model;

import java.util.HashSet;
import java.util.Set;

public class ModelObjects {

	private static ModelObjects instance;
	public static ModelObjects getInstance() {
		if(instance == null)
			instance = new ModelObjects();
		return instance;
	}
	
	protected ModelObjects() {
		
	}
	
	public Set<ModelObject> getObjects() {
		Set<ModelObject> ret = new HashSet<ModelObject>();
		for(Statement stat : Statements.getInstance().getStatements())
			ret.add(stat.getObject());
		return ret;
	}

	public Set<ModelObject> getObjects(Resource context, Resource subject, Resource property, Resource object) {
		Set<ModelObject> ret = new HashSet<ModelObject>();
		for(Statement stat : Statements.getInstance().getStatements(context, subject, property, object))
			ret.add(stat.getObject());
		return ret;
	}
	
}
