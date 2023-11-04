package com.cognescent.ddd.dom;

import java.util.HashMap;
import java.util.Map;

public class Type extends Entity {

	private Map<String, Attribute> attributes;
	
	public Type() {
		this.attributes = new HashMap<String, Attribute>();
	}
	
	public Type(String iri) {
		super(iri);
		this.attributes = new HashMap<String, Attribute>();
	}
	
	public Map<String, Attribute> getAttributes() {
		return this.attributes;
	}
	
	public void setAttributes(Map<String, Attribute> attributes) {
		this.attributes = attributes;
	}
	
}
