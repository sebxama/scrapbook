package com.cognescent.core.services.aggregation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cognescent.core.model.IRI;
import com.cognescent.core.model.IRIStatementOccurrence;
import com.cognescent.core.model.StatementContext;
import com.cognescent.core.model.StatementObject;
import com.cognescent.core.model.StatementPredicate;
import com.cognescent.core.model.StatementSubject;
import com.cognescent.core.services.aggregation.AggregationInstance.ContextAggregationInstance;
import com.cognescent.core.services.aggregation.AggregationInstance.ObjectAggregationInstance;
import com.cognescent.core.services.aggregation.AggregationInstance.PredicateAggregationInstance;
import com.cognescent.core.services.aggregation.AggregationInstance.SubjectAggregationInstance;

public abstract class AggregationAttribute<INST extends IRIStatementOccurrence, ATTR extends IRIStatementOccurrence, VAL extends IRIStatementOccurrence> {

	private static Set<ContextAggregationAttribute> contextAggregations
		= new HashSet<ContextAggregationAttribute>();
	private static Set<SubjectAggregationAttribute> subjectAggregations
		= new HashSet<SubjectAggregationAttribute>();
	private static Set<PredicateAggregationAttribute> predicateAggregations
		= new HashSet<PredicateAggregationAttribute>();;
	private static Set<ObjectAggregationAttribute> objectAggregations
		= new HashSet<ObjectAggregationAttribute>();;
	
	private IRI iri;
	private ATTR attribute;
	private Map<AggregationInstance<INST, ATTR, VAL>, AggregationValue<INST, ATTR, VAL>> values;
	
	public AggregationAttribute(ATTR attribute) {
		this.iri = attribute.getIRI();
		this.attribute = attribute;
		this.values = new HashMap<AggregationInstance<INST, ATTR, VAL>, AggregationValue<INST, ATTR, VAL>>();
	}

	public ATTR getAttribute() {
		return this.attribute;
	}

	public IRI getIRI() {
		return iri;
	}

	public void setIRI(IRI iri) {
		this.iri = iri;
	}
	
	public Map<AggregationInstance<INST, ATTR, VAL>, AggregationValue<INST, ATTR, VAL>> getValues() {
		return this.values;
	}

	public static ContextAggregationAttribute getContextAggregationAttribute(StatementSubject subj) {
		for(ContextAggregationAttribute c : contextAggregations)
			if(c.getIRI().equals(subj.getIRI()))
				return c;
		ContextAggregationAttribute ret = new ContextAggregationAttribute(subj);
		return ret;
	}

	public static SubjectAggregationAttribute getSubjectAggregationAttribute(StatementPredicate pred) {
		for(SubjectAggregationAttribute s : subjectAggregations)
			if(s.getIRI().equals(pred.getIRI()))
				return s;
		SubjectAggregationAttribute ret = new SubjectAggregationAttribute(pred);
		return ret;
	}

	public static PredicateAggregationAttribute getPredicateAggregationAttribute(StatementSubject subj) {
		for(PredicateAggregationAttribute p : predicateAggregations)
			if(p.getIRI().equals(subj.getIRI()))
				return p;
		PredicateAggregationAttribute ret = new PredicateAggregationAttribute(subj);
		return ret;
	}

	public static ObjectAggregationAttribute getObjectAggregationAttribute(StatementPredicate pred) {
		for(ObjectAggregationAttribute o : objectAggregations)
			if(o.getIRI().equals(pred.getIRI()))
				return o;
		ObjectAggregationAttribute ret = new ObjectAggregationAttribute(pred);
		return ret;
	}
	
	static class ContextAggregationAttribute extends AggregationAttribute<StatementContext, StatementSubject, StatementObject> {

		public ContextAggregationAttribute(StatementSubject subj) {
			super(subj);
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
			sb.append("\"attribute\" : ");
			sb.append(this.getAttribute().getIRI().toString());
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

		public static ContextAggregationAttribute getByIRI(IRI iri) {
			return getContextAggregationAttribute(StatementSubject.getByIRI(iri));
		}
		
	}
	
	static class SubjectAggregationAttribute extends AggregationAttribute<StatementSubject, StatementPredicate, StatementObject> {

		public SubjectAggregationAttribute(StatementPredicate pred) {
			super(pred);
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
			sb.append("\"attribute\" : ");
			sb.append(this.getAttribute().toString());
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

		public static SubjectAggregationAttribute getByIRI(IRI iri) {
			return getSubjectAggregationAttribute(StatementPredicate.getByIRI(iri));
		}
		
	}
	
	static class PredicateAggregationAttribute extends AggregationAttribute<StatementPredicate, StatementSubject, StatementObject> {

		public PredicateAggregationAttribute(StatementSubject subj) {
			super(subj);
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
			sb.append("\"attribute\" : ");
			sb.append(this.getAttribute().getIRI().toString());
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

		public static PredicateAggregationAttribute getByIRI(IRI iri) {
			return getPredicateAggregationAttribute(StatementSubject.getByIRI(iri));
		}
		
	}
	
	static class ObjectAggregationAttribute extends AggregationAttribute<StatementObject, StatementPredicate, StatementSubject> {

		public ObjectAggregationAttribute(StatementPredicate pred) {
			super(pred);
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
			sb.append("\"attribute\" : ");
			sb.append(this.getAttribute().getIRI().toString());
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

		public static ObjectAggregationAttribute getByIRI(IRI iri) {
			return getObjectAggregationAttribute(StatementPredicate.getByIRI(iri));
		}

	}
	
}
