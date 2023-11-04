package com.cognescent.core.model;

/*
 * TODO: Kinds IRIStatementOccurrence / Context, IRIs
 */
public class Statement /* extends IRIStatementOccurrence (reification) */ {

	private StatementContext context;
	private StatementSubject subject;
	private StatementPredicate predicate;
	private StatementObject object;
	
	private ContextKind contextKind;
	private SubjectKind subjectKind;
	private PredicateKind predicateKind;
	private ObjectKind objectKind;
	
	private ContextKindStatement contextKindStatement;
	private SubjectKindStatement subjectKindStatement;
	private PredicateKindStatement predicateKindStatement;
	private ObjectKindStatement objectKindStatement;
	
	public Statement() {
		
	}
	
	public Statement(String contextIRI, String subjectIRI, String predicateIRI, String objectIRI) {
		IRI context = IRI.get(contextIRI);
		IRI subject = IRI.get(subjectIRI);
		IRI predicate = IRI.get(predicateIRI);
		IRI object = IRI.get(objectIRI);
		StatementContext statementContext = StatementContext.getByIRI(context);
		StatementSubject statementSubject = StatementSubject.getByIRI(subject);
		StatementPredicate statementPredicate = StatementPredicate.getByIRI(predicate);
		StatementObject statementObject = StatementObject.getByIRI(object);
		context.getOccurrences().add(statementContext);
		subject.getOccurrences().add(statementSubject);
		predicate.getOccurrences().add(statementPredicate);
		object.getOccurrences().add(statementObject);
		this.context = statementContext;
		this.subject = statementSubject;
		this.predicate = statementPredicate;
		this.object = statementObject;
		// Kinds IRIs: Placeholder for Aggregation
		this.contextKind = ContextKind.getByIRI(context);
		this.subjectKind = SubjectKind.getByIRI(subject);
		this.predicateKind = PredicateKind.getByIRI(predicate);
		this.objectKind = ObjectKind.getByIRI(object);
		this.contextKind.getStatementOccurrences().add(this);
		this.subjectKind.getStatementOccurrences().add(this);
		this.predicateKind.getStatementOccurrences().add(this);
		this.objectKind.getStatementOccurrences().add(this);
		this.contextKindStatement = getContextKindStatement();
		this.subjectKindStatement = getSubjectKindStatement();
		this.predicateKindStatement = getPredicateKindStatement();
		this.objectKindStatement = getObjectKindStatement();
		statementContext.getStatementOccurrences().add(this);
		statementSubject.getStatementOccurrences().add(this);
		statementPredicate.getStatementOccurrences().add(this);
		statementObject.getStatementOccurrences().add(this);
		Statements.getInstance().addStatement(this);
	}
	
	public Statement(IRI context, IRI subject, IRI predicate, IRI object) {
		this();
		StatementContext statementContext = StatementContext.getByIRI(context);
		StatementSubject statementSubject = StatementSubject.getByIRI(subject);
		StatementPredicate statementPredicate = StatementPredicate.getByIRI(predicate);
		StatementObject statementObject = StatementObject.getByIRI(object);
		context.getOccurrences().add(statementContext);
		subject.getOccurrences().add(statementSubject);
		predicate.getOccurrences().add(statementPredicate);
		object.getOccurrences().add(statementObject);
		this.context = statementContext;
		this.subject = statementSubject;
		this.predicate = statementPredicate;
		this.object = statementObject;
		// Kinds IRIs: Placeholder for Aggregation
		this.contextKind = ContextKind.getByIRI(context);
		this.subjectKind = SubjectKind.getByIRI(subject);
		this.predicateKind = PredicateKind.getByIRI(predicate);
		this.objectKind = ObjectKind.getByIRI(object);
		this.contextKind.getStatementOccurrences().add(this);
		this.subjectKind.getStatementOccurrences().add(this);
		this.predicateKind.getStatementOccurrences().add(this);
		this.objectKind.getStatementOccurrences().add(this);
		this.contextKindStatement = getContextKindStatement();
		this.subjectKindStatement = getSubjectKindStatement();
		this.predicateKindStatement = getPredicateKindStatement();
		this.objectKindStatement = getObjectKindStatement();
		statementContext.getStatementOccurrences().add(this);
		statementSubject.getStatementOccurrences().add(this);
		statementPredicate.getStatementOccurrences().add(this);
		statementObject.getStatementOccurrences().add(this);
		Statements.getInstance().addStatement(this);
	}

	public Statement(StatementContext context, StatementSubject subject, StatementPredicate predicate, StatementObject object) {
		this.context = context;
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		// Kinds IRIs: Placeholder for Aggregation
		this.contextKind = ContextKind.getByIRI(context.getIRI());
		this.subjectKind = SubjectKind.getByIRI(subject.getIRI());
		this.predicateKind = PredicateKind.getByIRI(predicate.getIRI());
		this.objectKind = ObjectKind.getByIRI(object.getIRI());
		this.contextKind.getStatementOccurrences().add(this);
		this.subjectKind.getStatementOccurrences().add(this);
		this.predicateKind.getStatementOccurrences().add(this);
		this.objectKind.getStatementOccurrences().add(this);
		this.contextKindStatement = getContextKindStatement();
		this.subjectKindStatement = getSubjectKindStatement();
		this.predicateKindStatement = getPredicateKindStatement();
		this.objectKindStatement = getObjectKindStatement();
		context.getStatementOccurrences().add(this);
		subject.getStatementOccurrences().add(this);
		predicate.getStatementOccurrences().add(this);
		object.getStatementOccurrences().add(this);
		Statements.getInstance().addStatement(this);
	}
	
	public StatementContext getContext() {
		return context;
	}

	public StatementSubject getSubject() {
		return subject;
	}

	public StatementPredicate getPredicate() {
		return predicate;
	}

	public StatementObject getObject() {
		return object;
	}
	
	// FIXME: KindStatements Sets
	
	public ContextKindStatement getContextKindStatement() {
		//if(contextKindStatement == null) {
			contextKindStatement = new ContextKindStatement(this, this.contextKind, this.context, this.subject, this.object);
		//}
		return contextKindStatement;
	}
	
	public SubjectKindStatement getSubjectKindStatement() {
		//if(subjectKindStatement == null) {
			subjectKindStatement = new SubjectKindStatement(this, this.subjectKind, this.subject, this.predicate, this.object);
		//}
		return subjectKindStatement;
	}
	
	public PredicateKindStatement getPredicateKindStatement() {
		//if(predicateKindStatement == null) {
			predicateKindStatement = new PredicateKindStatement(this, this.predicateKind, this.predicate, this.subject, this.object);
		//}
		return predicateKindStatement;
	}
	
	public ObjectKindStatement getObjectKindStatement() {
		//if(objectKindStatement == null) {
			objectKindStatement = new ObjectKindStatement(this, this.objectKind, this.object, this.predicate, this.subject);
		//}
		return objectKindStatement;
	}
	
	// Kinds: Placeholder for Aggregation
	
	public ContextKind getContextKind() {
		return this.contextKind;
	}

	public SubjectKind getSubjectKind() {
		return this.subjectKind;
	}
	
	public PredicateKind getPredicateKind() {
		return this.predicateKind;
	}
	
	public ObjectKind getObjectKind() {
		return this.objectKind;
	}

	public void setContextKind(ContextKind kind) {
		this.contextKind = kind;
	}

	public void setSubjectKind(SubjectKind kind) {
		this.subjectKind = kind;
	}
	
	public void setPredicateKind(PredicateKind kind) {
		this.predicateKind = kind;
	}
	
	public void setObjectKind(ObjectKind kind) {
		this.objectKind = kind;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"className\": \"");
		sb.append(this.getClass().getCanonicalName());
		sb.append("\", \"context\" : ");
		sb.append(this.getContext().getIRI().toString());
		sb.append(", \"subject\" : ");
		sb.append(this.getSubject().getIRI().toString());
		sb.append(", \"predicate\" : ");
		sb.append(this.getPredicate().getIRI().toString());
		sb.append(", \"object\" : ");
		sb.append(this.getObject().getIRI().toString());
		sb.append(", \"contextKind\" : ");
		sb.append(this.getContextKind().getIRI().toString());
		sb.append(", \"subjectKind\" : ");
		sb.append(this.getSubjectKind().getIRI().toString());
		sb.append(", \"predicateKind\" : ");
		sb.append(this.getPredicateKind().getIRI().toString());
		sb.append(", \"objectKind\" : ");
		sb.append(this.getObjectKind().getIRI().toString());
		sb.append("}");
		return sb.toString();
	}
	
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	public boolean equals(Object obj) {
		return this.hashCode() == obj.hashCode();
	}
	
}
