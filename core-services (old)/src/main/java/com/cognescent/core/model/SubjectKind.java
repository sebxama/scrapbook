package com.cognescent.core.model;

import java.util.HashSet;
import java.util.Set;

public class SubjectKind extends Kind<StatementSubject, StatementPredicate, StatementObject> {

	private static Set<SubjectKind> subjectKinds = new HashSet<SubjectKind>();
	
	protected SubjectKind() {

	}
	
	public static SubjectKind getByIRI(IRI iri) {
		for(SubjectKind k : subjectKinds)
			if(k.getIRI().equals(iri))
				return k;
		SubjectKind ret = new SubjectKind();
		ret.setIRI(iri);
		subjectKinds.add(ret);
		return ret;
	}
	
	public static Set<SubjectKind> getSubjectKinds() {
		return subjectKinds;
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
