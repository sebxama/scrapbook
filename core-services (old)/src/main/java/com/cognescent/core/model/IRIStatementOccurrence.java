package com.cognescent.core.model;

import java.util.HashSet;
import java.util.Set;

public abstract class IRIStatementOccurrence {

	private IRI iri;
	private Set<Statement> statementOccurrences;
	
	public IRIStatementOccurrence() {
		this.statementOccurrences = new HashSet<Statement>();
	}

	public IRI getIRI() {
		return this.iri;
	}
	
	public void setIRI(IRI iri) {
		this.iri = iri;
	}
	
	public Set<Statement> getStatementOccurrences() {
		return this.statementOccurrences;
	}
	
	public void setStatementOccurrences(Set<Statement> set) {
		this.statementOccurrences = set;
	}
	
}
