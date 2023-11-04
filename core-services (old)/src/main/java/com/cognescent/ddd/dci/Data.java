package com.cognescent.ddd.dci;

import java.util.HashSet;
import java.util.Set;

public class Data {

	private Set<Actor> actors;
	
	public Data() {
		this.actors = new HashSet<Actor>();
	}
	
	public Set<Actor> getActors() {
		return this.actors;
	}
	
}
