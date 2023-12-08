package core.model;

import java.util.Set;

public interface ModelObjectKind extends Context, Subject, Property, ModelObject, Kind {
	
	public Set<ModelObject> getInstances();
	
	public Set<Property> getAttributes(ModelObject instance);
	
	public Set<Subject> getValues(ModelObject instance, Property attribute);
	
}
