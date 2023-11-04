package com.cognescent.core.model;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class IRI {

	private static Set<IRI> iris = new HashSet<IRI>();
	
	private String value;
	private BigInteger embedding;
	private Set<IRIStatementOccurrence> occurrences;
	
	public IRI() {
		this.occurrences = new HashSet<IRIStatementOccurrence>();
	}
	
	public IRI(String value) {
		this();
		this.value = value;
		iris.add(this);
	}

	public static IRI get(String value) {
		for(IRI iri : iris)
			if(iri.getValue().equals(value))
				return iri;
		IRI iri = new IRI(value);
		return iri;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setEmbedding(BigInteger embedding) {
		this.embedding = embedding;
	}
	
	public BigInteger getEmbedding() {
		return this.embedding;
	}
	
	public void setOccurrences(Set<IRIStatementOccurrence> set) {
		this.occurrences = set;
	}
	
	public Set<IRIStatementOccurrence> getOccurrences() {
		return this.occurrences;
	}
	
	public String toString() {
		return this.value;
	}
	
	public int hashCode() {
		return this.value.hashCode();
	}
	
	public boolean equals(Object iri) {
		if(iri instanceof IRI)
			if(((IRI) iri).getValue().equals(this.getValue()))
				return true;
		return false;
	}
	
}
