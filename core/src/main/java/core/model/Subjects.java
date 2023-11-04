package core.model;

import java.util.HashSet;
import java.util.Set;

public class Subjects {

	private static Subjects instance;
	public static Subjects getInstance() {
		if(instance == null)
			instance = new Subjects();
		return instance;
	}
	
	protected Subjects() {
		
	}
	
	public Set<Subject> getSubjects() {
		Set<Subject> ret = new HashSet<Subject>();
		for(Statement stat : Statements.getInstance().getStatements())
			ret.add(stat.getSubject());
		return ret;
	}

	public Set<Subject> getSubjects(Resource context, Resource subject, Resource property, Resource object) {
		Set<Subject> ret = new HashSet<Subject>();
		for(Statement stat : Statements.getInstance().getStatements(context, subject, property, object))
			ret.add(stat.getSubject());
		return ret;
	}
	
}
