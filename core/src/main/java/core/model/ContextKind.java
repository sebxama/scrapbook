package core.model;

import java.util.Set;

public interface ContextKind extends Context, Kind {
	
	public Set<Context> getInstanceContexts();
	
	public Set<Property> getAttributeProperties(Resource instance);
	
	public Set<ModelObject> getValueObjects(Resource instance, Resource attribute);
	
}
