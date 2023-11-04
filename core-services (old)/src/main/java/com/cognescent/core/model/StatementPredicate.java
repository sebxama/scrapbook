package com.cognescent.core.model;

import java.util.HashSet;
import java.util.Set;

public class StatementPredicate extends IRIStatementOccurrence {

	private static Set<StatementPredicate> predicates = new HashSet<StatementPredicate>();

	protected StatementPredicate() {

	}
	
	public static StatementPredicate getByIRI(IRI key) {
		for(StatementPredicate k : predicates)
			if(k.getIRI().equals(key))
				return k;
		StatementPredicate ret = new StatementPredicate();
		ret.setIRI(key);
		predicates.add(ret);
		return ret;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{ \"className\": \"");
		sb.append(this.getClass().getCanonicalName());
		sb.append("\", \"IRI\" : \"");
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

}
