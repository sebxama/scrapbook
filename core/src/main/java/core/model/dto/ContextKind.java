package core.model.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContextKind extends Kind {

	private Set<KindInstance> instances;

	private Resource resource;
	private Set<ContextKind> superKinds;
	private Set<ContextKind> subKinds;
	
	public ContextKind() {
		this.instances = new HashSet<>();
		this.superKinds = new HashSet<ContextKind>();
		this.subKinds = new HashSet<ContextKind>();
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
	public Set<ContextKind> getSuperKinds() {
		return superKinds;
	}

	public void setSuperKinds(Set<ContextKind> superKinds) {
		this.superKinds = superKinds;
	}

	@XmlElementRef
	public Set<ContextKind> getSubKinds() {
		return subKinds;
	}

	public void setSubKinds(Set<ContextKind> subKinds) {
		this.subKinds = subKinds;
	}
	
}
