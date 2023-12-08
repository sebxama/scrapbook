package core.model;

import java.util.Set;

public interface PropertyKind extends Context, Subject, Property, ModelObject, Kind {

	public Set<Property> getInstances();
	
	public Set<Subject> getAttributes(Property instance);
	
	public Set<ModelObject> getValues(Property instance, Subject attribute);
	
	
}
