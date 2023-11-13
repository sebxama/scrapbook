package core.model;

import java.util.Set;

public interface Kind extends ResourceOccurrence {
	
	public Set<Resource> getInstances();
	
	public Set<Resource> getAttributes(Resource instance);
	
	public Set<Resource> getValues(Resource instance, Resource attribute);
	
	public void setSuperKind(Kind kind);
	
	public Kind getSuperKind();
	
}
