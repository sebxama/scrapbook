package core.model.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ModelObjectKind extends Kind {

	private Set<KindInstance> instances;

	private Resource resource;
	private Set<ModelObjectKind> superKinds;
	private Set<ModelObjectKind> subKinds;
	
	public ModelObjectKind() {
		this.instances = new HashSet<>();
		this.superKinds = new HashSet<ModelObjectKind>();
		this.subKinds = new HashSet<ModelObjectKind>();
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
	public Set<ModelObjectKind> getSuperKinds() {
		return superKinds;
	}

	public void setSuperKinds(Set<ModelObjectKind> superKinds) {
		this.superKinds = superKinds;
	}

	@JacksonXmlElementWrapper(useWrapping = false)
	public Set<ModelObjectKind> getSubKinds() {
		return subKinds;
	}

	public void setSubKinds(Set<ModelObjectKind> subKinds) {
		this.subKinds = subKinds;
	}
	
}
