package core.model.dto;

import java.util.HashSet;
import java.util.Set;

public class PropertyKind extends Kind {

	private Set<KindInstance<Property, Subject, ModelObject>> instances;

	private Resource resource;
	private Set<PropertyKind> superKinds;
	private Set<PropertyKind> subKinds;
	
	public PropertyKind() {
		this.instances = new HashSet<>();
		this.superKinds = new HashSet<PropertyKind>();
		this.subKinds = new HashSet<PropertyKind>();
	}
	
	public Set<KindInstance<Property, Subject, ModelObject>> getInstances() {
		return instances;
	}

	public void setInstances(Set<KindInstance<Property, Subject, ModelObject>> instances) {
		this.instances = instances;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Set<PropertyKind> getSuperKinds() {
		return superKinds;
	}

	public void setSuperKinds(Set<PropertyKind> superKinds) {
		this.superKinds = superKinds;
	}

	public Set<PropertyKind> getSubKinds() {
		return subKinds;
	}

	public void setSubKinds(Set<PropertyKind> subKinds) {
		this.subKinds = subKinds;
	}
	
}
