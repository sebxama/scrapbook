package com.cognescent.core.model;

import java.util.HashSet;
import java.util.Set;

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
	
	public Set<Statement> getStatements(StatementContext context, StatementSubject subject, StatementPredicate predicate, StatementObject object) {
		IRI ctxIRI = context != null ? context.getIRI() : null;
		IRI subjIRI = subject != null ? subject.getIRI() : null;
		IRI predIRI = predicate != null ? predicate.getIRI() : null;
		IRI objIRI = object != null ? object.getIRI() : null;
		return this.getStatements(ctxIRI, subjIRI, predIRI, objIRI);
	}
	
	public Set<Statement> getStatements(IRI context, IRI subject, IRI predicate, IRI object) {
		String ctxStr = context != null ? context.getValue() : null;
		String subjStr = subject != null ? subject.getValue() : null;
		String predStr = predicate != null ? predicate.getValue() : null;
		String objStr = object != null ? object.getValue() : null;
		return this.getStatements(ctxStr, subjStr, predStr, objStr);
	}
	
	public Set<Statement> getStatements(String context, String subject, String predicate, String object) {
		Set<Statement> ret = new HashSet<Statement>();
		for(Statement stat : this.statements) {
			if((context != null && stat.getContext().getIRI().getValue().equals(context)) && 
				(subject != null && stat.getSubject().getIRI().getValue().equals(subject)) &&
					(predicate != null && stat.getPredicate().getIRI().getValue().equals(predicate)) &&
						(object != null && stat.getObject().getIRI().getValue().equals(object))) {
							ret.add(stat);
			}
			if((context == null) && 
					(subject != null && stat.getSubject().getIRI().getValue().equals(subject)) &&
						(predicate != null && stat.getPredicate().getIRI().getValue().equals(predicate)) &&
							(object != null && stat.getObject().getIRI().getValue().equals(object))) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getIRI().getValue().equals(context)) && 
					(subject == null) &&
						(predicate != null && stat.getPredicate().getIRI().getValue().equals(predicate)) &&
							(object != null && stat.getObject().getIRI().getValue().equals(object))) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject == null) &&
						(predicate != null && stat.getPredicate().getIRI().getValue().equals(predicate)) &&
							(object != null && stat.getObject().getIRI().getValue().equals(object))) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getIRI().getValue().equals(context)) && 
					(subject != null && stat.getSubject().getIRI().getValue().equals(subject)) &&
						(predicate == null) &&
							(object != null && stat.getObject().getIRI().getValue().equals(object))) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject != null && stat.getSubject().getIRI().getValue().equals(subject)) &&
						(predicate == null) &&
							(object != null && stat.getObject().getIRI().getValue().equals(object))) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getIRI().getValue().equals(context)) && 
					(subject == null) &&
						(predicate == null) &&
							(object != null && stat.getObject().getIRI().getValue().equals(object))) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject == null) &&
						(predicate == null) &&
							(object != null && stat.getObject().getIRI().getValue().equals(object))) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getIRI().getValue().equals(context)) && 
					(subject != null && stat.getSubject().getIRI().getValue().equals(subject)) &&
						(predicate != null && stat.getPredicate().getIRI().getValue().equals(predicate)) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject != null && stat.getSubject().getIRI().getValue().equals(subject)) &&
						(predicate != null && stat.getPredicate().getIRI().getValue().equals(predicate)) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getIRI().getValue().equals(context)) && 
					(subject == null) &&
						(predicate != null && stat.getPredicate().getIRI().getValue().equals(predicate)) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject == null) &&
						(predicate != null && stat.getPredicate().getIRI().getValue().equals(predicate)) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getIRI().getValue().equals(context)) && 
					(subject != null && stat.getSubject().getIRI().getValue().equals(subject)) &&
						(predicate == null) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context == null) && 
					(subject != null && stat.getSubject().getIRI().getValue().equals(subject)) &&
						(predicate == null) &&
							(object == null)) {
								ret.add(stat);
			}
			if((context != null && stat.getContext().getIRI().getValue().equals(context)) && 
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
	
	public Set<StatementContext> getStatementContexts() {
		Set<StatementContext> ret = new HashSet<StatementContext>();
		for(Statement stat : this.statements)
			ret.add(stat.getContext());
		return ret;
	}

	public Set<StatementSubject> getStatementSubjects() {
		Set<StatementSubject> ret = new HashSet<StatementSubject>();
		for(Statement stat : this.statements)
			ret.add(stat.getSubject());
		return ret;
	}

	public Set<StatementPredicate> getStatementPredicates() {
		Set<StatementPredicate> ret = new HashSet<StatementPredicate>();
		for(Statement stat : this.statements)
			ret.add(stat.getPredicate());
		return ret;
	}

	public Set<StatementObject> getStatementObjects() {
		Set<StatementObject> ret = new HashSet<StatementObject>();
		for(Statement stat : this.statements)
			ret.add(stat.getObject());
		return ret;
	}
	
}
