package core.model;

import java.util.Set;

public interface ContextKind extends Context, Kind {

	public Set<Context> getContexts();
	
	public Set<Context> getInstanceContexts(Resource instance);
	
	public Set<Property> getAttributeProperties(Resource attribute);
	
	public Set<ModelObject> getAttributeValues(Resource value);
	
}
