package com.cognescent.core.model;

import java.util.HashSet;
import java.util.Set;

public class ObjectKind extends Kind<StatementObject, StatementPredicate, StatementSubject> {

	private static Set<ObjectKind> objectKinds = new HashSet<ObjectKind>();
	
	protected ObjectKind() {

	}

	public static ObjectKind getByIRI(IRI iri) {
		for(ObjectKind k : objectKinds)
			if(k.getIRI().equals(iri))
				return k;
		ObjectKind ret = new ObjectKind();
		ret.setIRI(iri);
		objectKinds.add(ret);
		return ret;
	}
	
	public static Set<ObjectKind> getObjectKinds() {
		return objectKinds;
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
