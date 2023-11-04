package com.cognescent.core.model;

import java.util.HashSet;
import java.util.Set;

public class StatementSubject extends IRIStatementOccurrence {

	private static Set<StatementSubject> subjects = new HashSet<StatementSubject>();
	
	protected StatementSubject() {

	}
	
	public static StatementSubject getByIRI(IRI key) {
		for(StatementSubject k : subjects)
			if(k.getIRI().equals(key))
				return k;
		StatementSubject ret = new StatementSubject();
		ret.setIRI(key);
		subjects.add(ret);
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
