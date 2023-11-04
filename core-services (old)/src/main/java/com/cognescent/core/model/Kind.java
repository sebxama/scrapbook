package com.cognescent.core.model;

import java.util.HashSet;
import java.util.Set;

public abstract class Kind<INST extends IRIStatementOccurrence, ATTR extends IRIStatementOccurrence, VAL extends IRIStatementOccurrence> {

	private IRI iri;
	private Kind parent;
	private Set<Statement> statements;
	private Set<KindStatement<INST, ATTR, VAL>> kindStatements;
	
	private Set<INST> instances;
	private Set<ATTR> attributes;
	private Set<VAL> values;
	
	protected Kind() {
		statements = new HashSet<Statement>();
		kindStatements = new HashSet<KindStatement<INST, ATTR, VAL>>();
		instances = new HashSet<INST>();
		attributes = new HashSet<ATTR>();
		values = new HashSet<VAL>();
	}

	public Set<INST> getInstances() {
		return instances;
	}
	
	public Set<ATTR> getAttributes() {
		return this.attributes;
	}
	
	public Set<VAL> getValues() {
		return this.values;
	}

	public Set<StatementObject> getValues(INST inst, ATTR attr) {
		Set<VAL> ret = new HashSet<VAL>();
		Set<KindStatement<INST, ATTR, VAL>> stats = getStatements(inst, attr, null);
		for(KindStatement<INST, ATTR, VAL> stat : stats)
			ret.add(stat.getValue());
		return null;
	}

	public Set<ATTR> getAttributes(VAL val) {
		Set<ATTR> ret = new HashSet<ATTR>();
		Set<KindStatement<INST, ATTR, VAL>> stats = getStatements(null, null, val);
		for(KindStatement<INST, ATTR, VAL> stat : stats)
			ret.add(stat.getAttribute());
		return ret;
	}

	public IRI getIRI() {
		return this.iri;
	}
	
	/**
	 * Update Aggregation placeholder Kinds
	 * @param iri
	 */
	public void setIRI(IRI iri) {
		this.iri = iri;
	}

	public void setParent(Kind parent) {
		this.parent = parent;
	}
	
	public Kind getParent() {
		return this.parent;
	}	
	
	public Set<Statement> getStatementOccurrences() {
		return this.statements;
	}
	
	public void addStatement(KindStatement<INST, ATTR, VAL> stat) {
		this.kindStatements.add(stat);
	}
	
	public Set<KindStatement<INST, ATTR, VAL>> getStatements() {
		return this.kindStatements;
	}
	
	public Set<KindStatement<INST, ATTR, VAL>> getStatements(INST inst, ATTR attr, VAL val) {
		Set<KindStatement<INST, ATTR, VAL>> ret = new HashSet<KindStatement<INST, ATTR, VAL>>();
		for(KindStatement<INST, ATTR, VAL> i : this.kindStatements) {
			if(inst != null && attr != null && val != null) {
				if(i.getInstance().getIRI().equals(inst.getIRI()))
					if(i.getAttribute().getIRI().equals(attr.getIRI()))
						if(i.getValue().getIRI().equals(val.getIRI()))
							ret.add(i);
			}
			if(inst != null && attr != null && val == null) {
				if(i.getInstance().getIRI().equals(inst.getIRI()))
					if(i.getAttribute().getIRI().equals(attr.getIRI()))
						ret.add(i);
			}
			if(inst != null && attr == null && val != null) {
				if(i.getInstance().getIRI().equals(inst.getIRI()))
					if(i.getValue().getIRI().equals(val.getIRI()))
						ret.add(i);
			}
			if(inst != null && attr == null && val == null) {
				if(i.getInstance().getIRI().equals(inst.getIRI()))
					ret.add(i);
			}
			if(inst == null && attr != null && val != null) {
				if(i.getAttribute().getIRI().equals(attr.getIRI()))
					if(i.getValue().getIRI().equals(val.getIRI()))
						ret.add(i);
			}
			if(inst == null && attr != null && val == null) {
				if(i.getAttribute().getIRI().equals(attr.getIRI()))
					ret.add(i);
			}
			if(inst == null && attr == null && val != null) {
				if(i.getValue().getIRI().equals(val.getIRI()))
					ret.add(i);
			}
			if(inst == null && attr == null && val == null) {
				ret.add(i);
			}
		}

		return ret;
	}

}
