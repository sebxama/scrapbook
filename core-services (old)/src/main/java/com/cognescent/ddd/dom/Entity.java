package com.cognescent.ddd.dom;

import java.util.HashMap;
import java.util.Map;

public class Entity {

	private String iri;
	private Type type;
	private Map<Attribute, Value> values;
	
	public Entity() {
		this.values = new HashMap<Attribute, Value>();
	}

	public Entity(String iri) {
		this.values = new HashMap<Attribute, Value>();
		this.iri = iri;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public String getIRI() {
		return this.iri;
	}
	
	public void setIRI(String iri) {
		this.iri = iri;
	}
	
	public Map<Attribute, Value> getValues() {
		return this.values;
	}
	
	public void setValues(Map<Attribute, Value> values) {
		this.values = values;
	}
	
}
