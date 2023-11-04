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
import com.cognescent.core.services.aggregation.AggregationAttribute.ContextAggregationAttribute;
import com.cognescent.core.services.aggregation.AggregationAttribute.ObjectAggregationAttribute;
import com.cognescent.core.services.aggregation.AggregationAttribute.PredicateAggregationAttribute;
import com.cognescent.core.services.aggregation.AggregationAttribute.SubjectAggregationAttribute;
import com.cognescent.core.services.aggregation.AggregationInstance.SubjectAggregationInstance;

public abstract class AggregationValue<INST extends IRIStatementOccurrence, ATTR extends IRIStatementOccurrence, VAL extends IRIStatementOccurrence> {

	private static Set<AggregationValue.ContextAggregationValue> contextAggregations
		= new HashSet<AggregationValue.ContextAggregationValue>();
	private static Set<AggregationValue.SubjectAggregationValue> subjectAggregations
		= new HashSet<AggregationValue.SubjectAggregationValue>();
	private static Set<AggregationValue.PredicateAggregationValue> predicateAggregations
		= new HashSet<AggregationValue.PredicateAggregationValue>();
	private static Set<AggregationValue.ObjectAggregationValue> objectAggregations
		= new HashSet<AggregationValue.ObjectAggregationValue>();
	
	private IRI iri;
	private VAL value;
	private Map<AggregationAttribute<INST, ATTR, VAL>, AggregationInstance<INST, ATTR, VAL>> instances;
	
	public AggregationValue(VAL value) {
		this.iri = value.getIRI();
		this.value = value;
		this.instances = new HashMap<AggregationAttribute<INST, ATTR, VAL>, AggregationInstance<INST, ATTR, VAL>>();
	}
	
	public VAL getValue() {
		return this.value;
	}

	public IRI getIRI() {
		return iri;
	}

	public void setIRI(IRI iri) {
		this.iri = iri;
	}
	
	public Map<AggregationAttribute<INST, ATTR, VAL>, AggregationInstance<INST, ATTR, VAL>> getInstances() {
		return this.instances;
	}
	
	public static ContextAggregationValue getContextAggregationValue(StatementObject obj) {
		for(ContextAggregationValue c : contextAggregations)
			if(c.getIRI().equals(obj.getIRI()))
				return c;
		ContextAggregationValue ret = new ContextAggregationValue(obj);
		return ret;
	}

	public static SubjectAggregationValue getSubjectAggregationValue(StatementObject obj) {
		for(SubjectAggregationValue s : subjectAggregations)
			if(s.getIRI().equals(obj.getIRI()))
				return s;
		SubjectAggregationValue ret = new SubjectAggregationValue(obj);
		return ret;
	}

	public static PredicateAggregationValue getPredicateAggregationValue(StatementObject obj) {
		for(PredicateAggregationValue p : predicateAggregations)
			if(p.getIRI().equals(obj.getIRI()))
				return p;
		PredicateAggregationValue ret = new PredicateAggregationValue(obj);
		return ret;
	}

	public static ObjectAggregationValue getObjectAggregationValue(StatementSubject subj) {
		for(ObjectAggregationValue o : objectAggregations)
			if(o.getIRI().equals(subj.getIRI()))
				return o;
		ObjectAggregationValue ret = new ObjectAggregationValue(subj);
		return ret;
	}
	
	static class ContextAggregationValue extends AggregationValue<StatementContext, StatementSubject, StatementObject> {
		
		public ContextAggregationValue(StatementObject obj) {
			super(obj);
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
			sb.append("\"value\" : ");
			sb.append(this.getValue().toString());
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

		public static ContextAggregationValue getByIRI(IRI iri) {
			return getContextAggregationValue(StatementObject.getByIRI(iri));
		}

	}
	
	static class SubjectAggregationValue extends AggregationValue<StatementSubject, StatementPredicate, StatementObject> {

		public SubjectAggregationValue(StatementObject obj) {
			super(obj);
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
			sb.append("\"value\" : ");
			sb.append(this.getValue().toString());
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

		public static SubjectAggregationValue getByIRI(IRI iri) {
			return getSubjectAggregationValue(StatementObject.getByIRI(iri));
		}

	}
	
	static class PredicateAggregationValue extends AggregationValue<StatementPredicate, StatementSubject, StatementObject> {

		public PredicateAggregationValue(StatementObject obj) {
			super(obj);
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
			sb.append("\"value\" : ");
			sb.append(this.getValue().toString());
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

		public static PredicateAggregationValue getByIRI(IRI iri) {
			return getPredicateAggregationValue(StatementObject.getByIRI(iri));
		}
		
	}
	
	static class ObjectAggregationValue extends AggregationValue<StatementObject, StatementPredicate, StatementSubject> {

		public ObjectAggregationValue(StatementSubject val) {
			super(val);
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
			sb.append("\"value\" : ");
			sb.append(this.getValue().toString());
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

		public static ObjectAggregationValue getByIRI(IRI iri) {
			return getObjectAggregationValue(StatementSubject.getByIRI(iri));
		}
		
	}

}
