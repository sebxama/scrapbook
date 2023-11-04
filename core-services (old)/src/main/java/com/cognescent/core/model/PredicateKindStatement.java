package com.cognescent.core.model;

public class PredicateKindStatement extends KindStatement<StatementPredicate, StatementSubject, StatementObject> {
	
	public PredicateKindStatement(Statement stat, Kind<StatementPredicate, StatementSubject, StatementObject> kind, StatementPredicate instance, StatementSubject attribute, StatementObject value) {
		super(kind, instance, attribute, value);
		this.setStatement(stat);
		kind.addStatement(this);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"className\": \"");
		sb.append(this.getClass().getCanonicalName());
		sb.append("\", ");
		sb.append("\"kind\" : ");
		sb.append(this.getKind().getIRI().toString());
		sb.append(", ");
		sb.append("\"statement\" : ");
		sb.append(this.getStatement().toString());
		sb.append(", ");
		sb.append("\"instance\" : ");
		sb.append(this.getInstance().getIRI().toString());
		sb.append(", ");
		sb.append("\"attribute\" : ");
		sb.append(this.getAttribute().getIRI().toString());
		sb.append(", ");
		sb.append("\"value\" : ");
		sb.append(this.getValue().getIRI().toString());
		sb.append("}");
		return sb.toString();
	}
	
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	public boolean equals(Object obj) {
		return this.hashCode() == obj.hashCode();
	}
	
}
