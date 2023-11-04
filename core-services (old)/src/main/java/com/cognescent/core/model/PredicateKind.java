package com.cognescent.core.model;

import java.util.HashSet;
import java.util.Set;

public class PredicateKind extends Kind<StatementPredicate, StatementSubject, StatementObject> {

	private static Set<PredicateKind> predicateKinds = new HashSet<PredicateKind>();
	
	protected PredicateKind() {

	}

	public static PredicateKind getByIRI(IRI iri) {
		for(PredicateKind k : predicateKinds)
			if(k.getIRI().equals(iri))
				return k;
		PredicateKind ret = new PredicateKind();
		ret.setIRI(iri);
		predicateKinds.add(ret);
		return ret;
	}
	
	public static Set<PredicateKind> getPredicateKinds() {
		return predicateKinds;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"className\": \"");
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
