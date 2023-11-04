package com.cognescent.core.services.aggregation;

import java.util.HashSet;
import java.util.Set;

import com.cognescent.core.model.IRI;
import com.cognescent.core.model.IRIStatementOccurrence;
import com.cognescent.core.model.Kind;
import com.cognescent.core.model.Statement;
import com.cognescent.core.model.StatementContext;
import com.cognescent.core.model.StatementObject;
import com.cognescent.core.model.StatementPredicate;
import com.cognescent.core.model.StatementSubject;
import com.cognescent.core.services.aggregation.AggregationInstance.ContextAggregationInstance;

/*
 * TODO: Kinds IRIStatementOccurrence / Context, IRIs
 */
public abstract class AggregationKind<INST extends IRIStatementOccurrence, ATTR extends IRIStatementOccurrence, VAL extends IRIStatementOccurrence> {
	
	private static Set<ContextAggregationKind> contextAggregations 
		= new HashSet<ContextAggregationKind>();
	private static Set<SubjectAggregationKind> subjectAggregations
		= new HashSet<SubjectAggregationKind>();
	private static Set<PredicateAggregationKind> predicateAggregations
		= new HashSet<PredicateAggregationKind>();
	private static Set<ObjectAggregationKind> objectAggregations
		= new HashSet<ObjectAggregationKind>();
	
	private IRI iri;
	private Set<Statement> statements;
	private AggregationKind<INST, ATTR, VAL> parent;
	private Set<AggregationInstance<INST, ATTR, VAL>> instances;
	private Set<AggregationAttribute<INST, ATTR, VAL>> attributes;
	private Set<AggregationValue<INST, ATTR, VAL>> values;
	
	protected AggregationKind(IRI iri) {
		this.iri = iri;
		this.statements = new HashSet<Statement>();
		this.instances = new HashSet<AggregationInstance<INST, ATTR, VAL>>();
		this.attributes = new HashSet<AggregationAttribute<INST, ATTR, VAL>>();
		this.values = new HashSet<AggregationValue<INST, ATTR, VAL>>();
	}

	public IRI getIRI() {
		return iri;
	}

	public void setIRI(IRI iri) {
		this.iri = iri;
	}

	public Set<Statement> getStatementOccurrences() {
		return this.statements;
	}
	
	public AggregationKind<INST, ATTR, VAL> getParent() {
		return parent;
	}

	public void setParent(AggregationKind<INST, ATTR, VAL> parent) {
		this.parent = parent;
	}

	public Set<AggregationInstance<INST, ATTR, VAL>> getInstances() {
		return instances;
	}

	public Set<AggregationAttribute<INST, ATTR, VAL>> getAttributes() {
		return attributes;
	}

	public Set<AggregationValue<INST, ATTR, VAL>> getValues() {
		return values;
	}
	
	public static Set<ContextAggregationKind> getContextAggregationKinds() {
		return contextAggregations;
	}

	public static void addContextAggregationKind(ContextAggregationKind kind) {
		contextAggregations.add(kind);
	}
	
	public static ContextAggregationKind getContextAggregationKind(IRI iri) {
		for(ContextAggregationKind c : contextAggregations)
			if(c.getIRI().equals(iri))
				return c;
		ContextAggregationKind ret = new ContextAggregationKind(iri);
		return ret;
	}

	public static Set<SubjectAggregationKind> getSubjectAggregationKinds() {
		return subjectAggregations;
	}
	
	public static void addSubjectAggregationKind(SubjectAggregationKind kind) {
		subjectAggregations.add(kind);
	}
	
	public static SubjectAggregationKind getSubjectAggregationKind(IRI iri) {
		for(SubjectAggregationKind s : subjectAggregations)
			if(s.getIRI().equals(iri))
				return s;
		SubjectAggregationKind ret = new SubjectAggregationKind(iri);
		return ret;
	}

	public static Set<PredicateAggregationKind> getPredicateAggregationKinds() {
		return predicateAggregations;
	}

	public static void addPredicateAggregationKind(PredicateAggregationKind kind) {
		predicateAggregations.add(kind);
	}
	
	public static PredicateAggregationKind getPredicateAggregationKind(IRI iri) {
		for(PredicateAggregationKind p : predicateAggregations)
			if(p.getIRI().equals(iri))
				return p;
		PredicateAggregationKind ret = new PredicateAggregationKind(iri);
		return ret;
	}

	public static Set<ObjectAggregationKind> getObjectAggregationKinds() {
		return objectAggregations;
	}
	
	public static void addObjectAggregationKind(ObjectAggregationKind kind) {
		objectAggregations.add(kind);
	}
	
	public static ObjectAggregationKind getObjectAggregationKind(IRI iri) {
		for(ObjectAggregationKind o : objectAggregations)
			if(o.getIRI().equals(iri))
				return o;
		ObjectAggregationKind ret = new ObjectAggregationKind(iri);
		return ret;
	}
	
	static class ContextAggregationKind extends AggregationKind<StatementContext, StatementSubject, StatementObject> {

		public ContextAggregationKind(IRI iri) {
			super(iri);
			contextAggregations.add(this);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"className\": \"");
			sb.append(this.getClass().getCanonicalName());
			sb.append("\", ");
			sb.append("\"IRI\" : \"");
			sb.append(this.getIRI().getValue());
			sb.append("\"}");
			return sb.toString();
		}
		
		public int hashCode() {
			StringBuffer sb = new StringBuffer();
			sb.append(this.getClass().getCanonicalName());
			sb.append(" : ");
			sb.append(this.getIRI().getValue());
			return sb.toString().hashCode();
		}
		
		public boolean equals(Object obj) {
			return this.hashCode() == obj.hashCode();
		}

		public static ContextAggregationKind getByIRI(IRI iri) {
			return getContextAggregationKind(iri);
		}
		
	}
	
	static class SubjectAggregationKind extends AggregationKind<StatementSubject, StatementPredicate, StatementObject> {

		public SubjectAggregationKind(IRI iri) {
			super(iri);
			subjectAggregations.add(this);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"className\": \"");
			sb.append(this.getClass().getCanonicalName());
			sb.append("\", ");
			sb.append("\"IRI\" : \"");
			sb.append(this.getIRI().getValue());
			sb.append("\"}");
			return sb.toString();
		}
		
		public int hashCode() {
			StringBuffer sb = new StringBuffer();
			sb.append(this.getClass().getCanonicalName());
			sb.append(" : ");
			sb.append(this.getIRI().getValue());
			return sb.toString().hashCode();
		}
		
		public boolean equals(Object obj) {
			return this.hashCode() == obj.hashCode();
		}
		
		public static SubjectAggregationKind getByIRI(IRI iri) {
			return getSubjectAggregationKind(iri);
		}
		
	}
	
	static class PredicateAggregationKind extends AggregationKind<StatementPredicate, StatementSubject, StatementObject> {

		public PredicateAggregationKind(IRI iri) {
			super(iri);
			predicateAggregations.add(this);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"className\": \"");
			sb.append(this.getClass().getCanonicalName());
			sb.append("\", ");
			sb.append("\"IRI\" : \"");
			sb.append(this.getIRI().getValue());
			sb.append("\"}");
			return sb.toString();
		}
		
		public int hashCode() {
			StringBuffer sb = new StringBuffer();
			sb.append(this.getClass().getCanonicalName());
			sb.append(" : ");
			sb.append(this.getIRI().getValue());
			return sb.toString().hashCode();
		}
		
		public boolean equals(Object obj) {
			return this.hashCode() == obj.hashCode();
		}

		public static PredicateAggregationKind getByIRI(IRI iri) {
			return getPredicateAggregationKind(iri);
		}
		
	}
	
	static class ObjectAggregationKind extends AggregationKind<StatementObject, StatementPredicate, StatementSubject> {

		public ObjectAggregationKind(IRI iri) {
			super(iri);
			objectAggregations.add(this);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"className\": \"");
			sb.append(this.getClass().getCanonicalName());
			sb.append("\", ");
			sb.append("\"IRI\" : \"");
			sb.append(this.getIRI().getValue());
			sb.append("\"}");
			return sb.toString();
		}
		
		public int hashCode() {
			StringBuffer sb = new StringBuffer();
			sb.append(this.getClass().getCanonicalName());
			sb.append(" : ");
			sb.append(this.getIRI().getValue());
			return sb.toString().hashCode();
		}
		
		public boolean equals(Object obj) {
			return this.hashCode() == obj.hashCode();
		}

		public static ObjectAggregationKind getByIRI(IRI iri) {
			return getObjectAggregationKind(iri);
		}
		
	}

}
