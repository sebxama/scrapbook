package core.model;

import java.util.Set;

public interface ContextKind extends Context, Subject, Property, ModelObject, Kind {

	public Set<Context> getInstances();
	
	public Set<Property> getAttributes(Context instance);
	
	public Set<ModelObject> getValues(Context instance, Property attribute);
	
}
