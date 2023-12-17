package core.model.dto;

import java.util.HashSet;
import java.util.Set;

public class SubjectKind extends Kind {

	private Set<KindInstance<Subject, Property, ModelObject>> instances;

	private Resource resource;
	private Set<SubjectKind> superKinds;
	private Set<SubjectKind> subKinds;
	
	public SubjectKind() {
		this.instances = new HashSet<>();
		this.superKinds = new HashSet<SubjectKind>();
		this.subKinds = new HashSet<SubjectKind>();
	}
	
	public Set<KindInstance<Subject, Property, ModelObject>> getInstances() {
		return instances;
	}

	public void setInstances(Set<KindInstance<Subject, Property, ModelObject>> instances) {
		this.instances = instances;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Set<SubjectKind> getSuperKinds() {
		return superKinds;
	}

	public void setSuperKinds(Set<SubjectKind> superKinds) {
		this.superKinds = superKinds;
	}

	public Set<SubjectKind> getSubKinds() {
		return subKinds;
	}

	public void setSubKinds(Set<SubjectKind> subKinds) {
		this.subKinds = subKinds;
	}
	
}
