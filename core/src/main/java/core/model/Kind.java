package core.model;

import java.util.Set;

public interface Kind {
	
	public Resource getResource();
	
	public Set<Kind> getSuperKinds();
	
	public Set<Kind> getSubKinds();
	
	public Set<Resource> getInstancesResources();
	
	public Set<Resource> getAttributesResources(Resource instance);
	
	public Set<Resource> getValuesResources(Resource instance, Resource attribute);
	
}
