package com.cognescent.core.services.aggregation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cognescent.core.model.IRI;
import com.cognescent.core.model.IRIStatementOccurrence;
import com.cognescent.core.model.Statement;
import com.cognescent.core.model.StatementContext;
import com.cognescent.core.model.StatementObject;
import com.cognescent.core.model.StatementPredicate;
import com.cognescent.core.model.StatementSubject;
import com.cognescent.core.services.aggregation.AggregationKind.ContextAggregationKind;
import com.cognescent.core.services.aggregation.AggregationKind.ObjectAggregationKind;
import com.cognescent.core.services.aggregation.AggregationKind.PredicateAggregationKind;
import com.cognescent.core.services.aggregation.AggregationKind.SubjectAggregationKind;

public abstract class AggregationInstance<INST extends IRIStatementOccurrence, ATTR extends IRIStatementOccurrence, VAL extends IRIStatementOccurrence> {
	
	private static Set<ContextAggregationInstance> contextAggregations
		= new HashSet<ContextAggregationInstance>();
	private static Set<SubjectAggregationInstance> subjectAggregations
		= new HashSet<SubjectAggregationInstance>();
	private static Set<PredicateAggregationInstance> predicateAggregations
		= new HashSet<PredicateAggregationInstance>();
	private static Set<ObjectAggregationInstance> objectAggregations
		= new HashSet<ObjectAggregationInstance>();
	
	private IRI iri;
	private INST instance;
	private Map<Statement, AggregationKind<INST, ATTR, VAL>> roles;
	
	protected AggregationInstance(INST instance) {
		this.iri = instance.getIRI();
		this.instance = instance;
		this.roles = new HashMap<Statement, AggregationKind<INST, ATTR, VAL>>();
	}
	
	public INST getInstance() {
		return this.instance;
	}

	public IRI getIRI() {
		return iri;
	}

	public void setIRI(IRI iri) {
		this.iri = iri;
	}
	
	public Map<Statement, AggregationKind<INST, ATTR, VAL>> getRoles() {
		return this.roles;
	}
	
	public static ContextAggregationInstance getContextAggregationInstance(StatementContext ctx) {
		for(ContextAggregationInstance c : contextAggregations)
			if(c.getIRI().equals(ctx.getIRI()))
				return c;
		ContextAggregationInstance ret = new ContextAggregationInstance(ctx);
		return ret;
	}

	public static SubjectAggregationInstance getSubjectAggregationInstance(StatementSubject subj) {
		for(SubjectAggregationInstance s : subjectAggregations)
			if(s.getIRI().equals(subj.getIRI()))
				return s;
		SubjectAggregationInstance ret = new SubjectAggregationInstance(subj);
		return ret;
	}

	public static PredicateAggregationInstance getPredicateAggregationInstance(StatementPredicate pred) {
		for(PredicateAggregationInstance p : predicateAggregations)
			if(p.getIRI().equals(pred.getIRI()))
				return p;
		PredicateAggregationInstance ret = new PredicateAggregationInstance(pred);
		return ret;
	}

	public static ObjectAggregationInstance getObjectAggregationInstance(StatementObject obj) {
		for(ObjectAggregationInstance o : objectAggregations)
			if(o.getIRI().equals(obj.getIRI()))
					return o;
		ObjectAggregationInstance ret = new ObjectAggregationInstance(obj);
		return ret;
	}
	
	static class ContextAggregationInstance extends AggregationInstance<StatementContext, StatementSubject, StatementObject> {

		public ContextAggregationInstance(StatementContext ctx) {
			super(ctx);
			contextAggregations.add(this);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"className\": \"");
			sb.append(this.getClass().getCanonicalName());
			sb.append("\", "); 
			sb.append("\"IRI\" : \"");
			sb.append(this.getIRI().getValue());
			sb.append("\", ");
			sb.append("\"instance\" : ");
			sb.append(this.getInstance().getIRI().toString());
			sb.append("}");
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

		public static ContextAggregationInstance getByIRI(IRI iri) {
			return getContextAggregationInstance(StatementContext.getByIRI(iri));
		}

	}
	
	static class SubjectAggregationInstance extends AggregationInstance<StatementSubject, StatementPredicate, StatementObject> {

		public SubjectAggregationInstance(StatementSubject subj) {
			super(subj);
			subjectAggregations.add(this);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"className\": \"");
			sb.append(this.getClass().getCanonicalName());
			sb.append("\", "); 
			sb.append("\"IRI\" : \"");
			sb.append(this.getIRI().getValue());
			sb.append("\", ");
			sb.append("\"instance\" : ");
			sb.append(this.getInstance().getIRI().toString());
			sb.append("}");
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

		public static SubjectAggregationInstance getByIRI(IRI iri) {
			return getSubjectAggregationInstance(StatementSubject.getByIRI(iri));
		}
		
	}
	
	static class PredicateAggregationInstance extends AggregationInstance<StatementPredicate, StatementSubject, StatementObject> {

		public PredicateAggregationInstance(StatementPredicate pred) {
			super(pred);
			predicateAggregations.add(this);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"className\": \"");
			sb.append(this.getClass().getCanonicalName());
			sb.append("\", "); 
			sb.append("\"IRI\" : \"");
			sb.append(this.getIRI().getValue());
			sb.append("\", ");
			sb.append("\"instance\" : ");
			sb.append(this.getInstance().getIRI().toString());
			sb.append("}");
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

		public static PredicateAggregationInstance getByIRI(IRI iri) {
			return getPredicateAggregationInstance(StatementPredicate.getByIRI(iri));
		}
		
	}
	
	static class ObjectAggregationInstance extends AggregationInstance<StatementObject, StatementPredicate, StatementSubject> {

		public ObjectAggregationInstance(StatementObject obj) {
			super(obj);
			objectAggregations.add(this);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"className\": \"");
			sb.append(this.getClass().getCanonicalName());
			sb.append("\", "); 
			sb.append("\"IRI\" : \"");
			sb.append(this.getIRI().getValue());
			sb.append("\", ");
			sb.append("\"instance\" : ");
			sb.append(this.getInstance().getIRI().toString());
			sb.append("}");
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

		public static ObjectAggregationInstance getByIRI(IRI iri) {
			return getObjectAggregationInstance(StatementObject.getByIRI(iri));
		}
		
	}
	
}
