package com.cognescent.ddd.dci;

import java.util.HashSet;
import java.util.Set;

public class Context {

	private Set<Role> roles;
	
	public Context() {
		this.roles = new HashSet<Role>();
	}
	
	public Set<Role> getRoles() {
		return this.roles;
	}
	
}
