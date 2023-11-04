package core.model;

import java.util.HashSet;
import java.util.Set;

public class Properties {

	private static Properties instance;
	public static Properties getInstance() {
		if(instance == null)
			instance = new Properties();
		return instance;
	}
	
	protected Properties() {
		
	}
	
	public Set<Property> getProperties() {
		Set<Property> ret = new HashSet<Property>();
		for(Statement stat : Statements.getInstance().getStatements())
			ret.add(stat.getProperty());
		return ret;
	}

	public Set<Property> getProperties(Resource context, Resource subject, Resource property, Resource object) {
		Set<Property> ret = new HashSet<Property>();
		for(Statement stat : Statements.getInstance().getStatements(context, subject, property, object))
			ret.add(stat.getProperty());
		return ret;
	}
	
}
