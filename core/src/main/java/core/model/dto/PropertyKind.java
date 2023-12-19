package core.model.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

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
	
	@JacksonXmlElementWrapper(useWrapping = false)
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

	@JacksonXmlElementWrapper(useWrapping = false)
	public Set<PropertyKind> getSuperKinds() {
		return superKinds;
	}

	public void setSuperKinds(Set<PropertyKind> superKinds) {
		this.superKinds = superKinds;
	}

	@JacksonXmlElementWrapper(useWrapping = false)
	public Set<PropertyKind> getSubKinds() {
		return subKinds;
	}

	public void setSubKinds(Set<PropertyKind> subKRinds) {
		this.subKinds = subKinds;
	}
	
}
