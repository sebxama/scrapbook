package core.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

public abstract class ResourceOccurrenceImpl implements ResourceOccurrence {

	private Resource iri;
	private Statement context;
	private Kind kind;
	
	protected ResourceOccurrenceImpl(Resource iri) {
		this.iri = iri;
	}
	
	public Resource getResource() {
		return iri;
	}

	public void setResource(Resource res) {
		this.iri = res;
	}
	
	public void setContextStatement(Statement context) {
		this.context = context;
	}
	
	@JsonManagedReference
	public Statement getContextStatement() {
		return this.context;
	}

	@Override
	public Kind getKind() {
		return this.kind;
	}
	
	@Override
	public void setKind(Kind kind) {
		this.kind = kind;
		kind.getResource().getResourceOccurrences().add(this);
	}
	
	@Override
	public String toString() {
		return this.getResource().toString();
	}
	
	/*
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals(java.lang.Object obj) {
		return this.hashCode() == obj.hashCode();
	}
	*/
	
}
