package core.model;

import java.util.Set;

import fcalib.api.fca.Concept;

public interface Kind extends HasResource {
	
	public Set<Kind> getSuperKinds();
	
	public Set<Kind> getSubKinds();
	
	public Concept<String, String> getConcept();
	
	public void setConcept(Concept<String, String> concept);
	
	public Set<Resource> getInstancesResources();
	
	public Set<Resource> getAttributesResources(Resource instance);
	
	public Set<Resource> getValuesResources(Resource instance, Resource attribute);
	
}
