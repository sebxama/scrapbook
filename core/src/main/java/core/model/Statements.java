package core.model;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO: Wrap RDF4J Model
 */
public class Statements {

	private static Statements instance;
	public static Statements getInstance() {
		if(instance == null)
			instance = new Statements();
		return instance;
	}
	
	private Set<Statement> statements;
	
	protected Statements() {
		this.statements = new HashSet<Statement>();
	}

	public Set<Statement> getStatements() {
		return this.statements;
	}
	
	public void addStatement(Statement stat) {
		this.statements.add(stat);
	}

	public StatementImpl addStatement(Context ctx, Subject subj, Property prop, ModelObject obj) {
		Resource iri = Resource.getStatementResource(ctx, subj, prop, obj);
		StatementImpl stat = new StatementImpl(iri);
		stat.setContext(ctx);
		stat.setSubject(subj);
		stat.setProperty(prop);
		stat.setObject(obj);
		addStatement(stat);
		return stat;
	}
	
	public Set<Statement> getStatements(Resource context, Resource subject, Resource predicate, Resource object) {
		String ctxStr = context != null ? context.toString() : null;
		String subjStr = subject != null ? subject.toString() : null;
		String predStr = predicate != null ? predicate.toString() : null;
		String objStr = object != null ? object.toString() : null;
		return this.getStatements(ctxStr, subjStr, predStr, objStr);
	}
	
	public Set<Statement> getStatements(String context, String subject, String predicate, String object) {
		Set<Statement> ret = new HashSet<Statement>();
		for(Statement stat : this.statements) {
			if((context != null && stat.getContext().getResource().toString().equals(context)) && 
				(subject != null && stat.getSubject().getResource().toString().equals(subject)) &&
					(predicate != null && stat.getProperty().getResource().toString().equals(predicate)) &&
						(object != null && stat.getObject().getResource().toString().equals(object))) {
							ret.add(stat);
			}
			if((context == null) && 
					(subject != null && stat.getSubject().getResource().toString().equals(subject)) &&
						(predicate != null && stat.getProperty().getResource().toString().equals(predicate)) &&
							(object != null && stat.getObject().getResource().toString().equals(object))) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getResource().toString().equals(context)) && 
					(subject == null) &&
						(predicate != null && stat.getProperty().getResource().toString().equals(predicate)) &&
							(object != null && stat.getObject().getResource().toString().equals(object))) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject == null) &&
						(predicate != null && stat.getProperty().getResource().toString().equals(predicate)) &&
							(object != null && stat.getObject().getResource().toString().equals(object))) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getResource().toString().equals(context)) && 
					(subject != null && stat.getSubject().getResource().toString().equals(subject)) &&
						(predicate == null) &&
							(object != null && stat.getObject().getResource().toString().equals(object))) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject != null && stat.getSubject().getResource().toString().equals(subject)) &&
						(predicate == null) &&
							(object != null && stat.getObject().getResource().toString().equals(object))) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getResource().toString().equals(context)) && 
					(subject == null) &&
						(predicate == null) &&
							(object != null && stat.getObject().getResource().toString().equals(object))) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject == null) &&
						(predicate == null) &&
							(object != null && stat.getObject().getResource().toString().equals(object))) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getResource().toString().equals(context)) && 
					(subject != null && stat.getSubject().getResource().toString().equals(subject)) &&
						(predicate != null && stat.getProperty().getResource().toString().equals(predicate)) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject != null && stat.getSubject().getResource().toString().equals(subject)) &&
						(predicate != null && stat.getProperty().getResource().toString().equals(predicate)) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getResource().toString().equals(context)) && 
					(subject == null) &&
						(predicate != null && stat.getProperty().getResource().toString().equals(predicate)) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject == null) &&
						(predicate != null && stat.getProperty().getResource().toString().equals(predicate)) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getResource().toString().equals(context)) && 
					(subject != null && stat.getSubject().getResource().toString().equals(subject)) &&
						(predicate == null) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject != null && stat.getSubject().getResource().toString().equals(subject)) &&
						(predicate == null) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getResource().toString().equals(context)) && 
					(subject == null) &&
						(predicate == null) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject == null) &&
						(predicate == null) &&
							(object == null)) {
								ret.add(stat);
			}
		}
		
		return ret;
	}
	
}
