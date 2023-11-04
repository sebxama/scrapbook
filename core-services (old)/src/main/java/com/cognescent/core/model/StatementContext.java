package com.cognescent.core.model;

import java.util.HashSet;
import java.util.Set;

public class StatementContext extends IRIStatementOccurrence {

	private static Set<StatementContext> contexts = new HashSet<StatementContext>();

	protected StatementContext() {

	}
	
	public static StatementContext getByIRI(IRI key) {
		for(StatementContext k : contexts)
			if(k.getIRI().equals(key))
				return k;
		StatementContext ret = new StatementContext();
		ret.setIRI(key);
		contexts.add(ret);
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
