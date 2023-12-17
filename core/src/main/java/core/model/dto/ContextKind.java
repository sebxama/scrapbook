package core.model.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContextKind extends Kind {

	private Set<KindInstance<Context, Property, ModelObject>> instances;

	private Resource resource;
	private Set<ContextKind> superKinds;
	private Set<ContextKind> subKinds;
	
	public ContextKind() {
		this.instances = new HashSet<>();
		this.superKinds = new HashSet<ContextKind>();
		this.subKinds = new HashSet<ContextKind>();
	}
	
	public Set<KindInstance<Context, Property, ModelObject>> getInstances() {
		return instances;
	}

	public void setInstances(Set<KindInstance<Context, Property, ModelObject>> instances) {
		this.instances = instances;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Set<ContextKind> getSuperKinds() {
		return superKinds;
	}

	public void setSuperKinds(Set<ContextKind> superKinds) {
		this.superKinds = superKinds;
	}

	public Set<ContextKind> getSubKinds() {
		return subKinds;
	}

	public void setSubKinds(Set<ContextKind> subKinds) {
		this.subKinds = subKinds;
	}
	
}
