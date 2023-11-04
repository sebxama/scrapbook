package core.model;

import java.util.HashSet;
import java.util.Set;

public class Contexts {

	private static Contexts instance;
	public static Contexts getInstance() {
		if(instance == null)
			instance = new Contexts();
		return instance;
	}
	
	protected Contexts() {
		
	}
	
	public Set<Context> getContexts() {
		Set<Context> ret = new HashSet<Context>();
		for(Statement stat : Statements.getInstance().getStatements())
			ret.add(stat.getContext());
		return ret;
	}

	public Set<Context> getContexts(Resource context, Resource subject, Resource property, Resource object) {
		Set<Context> ret = new HashSet<Context>();
		for(Statement stat : Statements.getInstance().getStatements(context, subject, property, object))
			ret.add(stat.getContext());
		return ret;
	}
	
}
