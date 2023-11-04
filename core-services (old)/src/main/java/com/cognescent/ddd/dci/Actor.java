package com.cognescent.ddd.dci;

import com.cognescent.ddd.dom.Entity;

public class Actor {

	private Entity entity;
	
	public Actor() {
		
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return this.entity;
	}
	
}
