package core.model.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SubjectKind extends Kind {

	private Set<KindInstance> instances;

	private Resource resource;
	private Set<SubjectKind> superKinds;
	private Set<SubjectKind> subKinds;
	
	public SubjectKind() {
		this.instances = new HashSet<>();
		this.superKinds = new HashSet<SubjectKind>();
		this.subKinds = new HashSet<SubjectKind>();
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
	public Set<SubjectKind> getSuperKinds() {
		return superKinds;
	}

	public void setSuperKinds(Set<SubjectKind> superKinds) {
		this.superKinds = superKinds;
	}

	@JacksonXmlElementWrapper(useWrapping = false)
	public Set<SubjectKind> getSubKinds() {
		return subKinds;
	}

	public void setSubKinds(Set<SubjectKind> subKinds) {
		this.subKinds = subKinds;
	}
	
}
