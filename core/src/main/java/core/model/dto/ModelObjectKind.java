package core.model.dto;

import java.util.HashSet;
import java.util.Set;

public class ModelObjectKind extends Kind {

	private Set<KindInstance<ModelObject, Property, Subject>> instances;

	private Resource resource;
	private Set<ModelObjectKind> superKinds;
	private Set<ModelObjectKind> subKinds;
	
	public ModelObjectKind() {
		this.instances = new HashSet<>();
		this.superKinds = new HashSet<ModelObjectKind>();
		this.subKinds = new HashSet<ModelObjectKind>();
	}
	
	public Set<KindInstance<ModelObject, Property, Subject>> getInstances() {
		return instances;
	}

	public void setInstances(Set<KindInstance<ModelObject, Property, Subject>> instances) {
		this.instances = instances;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Set<ModelObjectKind> getSuperKinds() {
		return superKinds;
	}

	public void setSuperKinds(Set<ModelObjectKind> superKinds) {
		this.superKinds = superKinds;
	}

	public Set<ModelObjectKind> getSubKinds() {
		return subKinds;
	}

	public void setSubKinds(Set<ModelObjectKind> subKinds) {
		this.subKinds = subKinds;
	}
	
}
