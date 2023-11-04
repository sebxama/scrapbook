package com.cognescent.core.model;

import java.util.HashSet;
import java.util.Set;

public class StatementObject extends IRIStatementOccurrence {

	private static Set<StatementObject> objects = new HashSet<StatementObject>();

	protected StatementObject() {

	}
	
	public static StatementObject getByIRI(IRI key) {
		for(StatementObject k : objects)
			if(k.getIRI().equals(key))
				return k;
		StatementObject ret = new StatementObject();
		ret.setIRI(key);
		objects.add(ret);
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
