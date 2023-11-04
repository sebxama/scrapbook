package com.cognescent.core.model;

import java.util.HashSet;
import java.util.Set;

public class ContextKind extends Kind<StatementContext, StatementSubject, StatementObject> {

	private static Set<ContextKind> contextKinds = new HashSet<ContextKind>();
	
	protected ContextKind() {

	}

	public static ContextKind getByIRI(IRI iri) {
		for(ContextKind k : contextKinds)
			if(k.getIRI().equals(iri))
				return k;
		ContextKind ret = new ContextKind();
		ret.setIRI(iri);
		contextKinds.add(ret);
		return ret;
	}
	
	public static Set<ContextKind> getContextKinds() {
		return contextKinds;
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
