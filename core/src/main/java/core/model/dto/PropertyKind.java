package core.model.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PropertyKind extends Kind {

	private Set<KindInstance> instances;

	private Resource resource;
	private Set<PropertyKind> superKinds;
	private Set<PropertyKind> subKinds;
	
	public PropertyKind() {
		this.instances = new HashSet<>();
		this.superKinds = new HashSet<PropertyKind>();
		this.subKinds = new HashSet<PropertyKind>();
	}
	
	@XmlElementRef
	public Set<KindInstance> getInstances() {
		return instances;
	}

	public void setInstances(Set<KindInstance> instances) {
		this.instances = instances;
	}

	@XmlElement
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@XmlElementRef
	public Set<PropertyKind> getSuperKinds() {
		return superKinds;
	}

	public void setSuperKinds(Set<PropertyKind> superKinds) {
		this.superKinds = superKinds;
	}

	@XmlElementRef
	public Set<PropertyKind> getSubKinds() {
		return subKinds;
	}

	public void setSubKinds(Set<PropertyKind> subKinds) {
		this.subKinds = subKinds;
	}
	
}
