package core.model;

import java.util.Set;

public interface PropertyKind extends Context, Subject, Property, ModelObject, Kind {

	public Set<Property> getProperties();
	
	public Set<Property> getInstanceProperties(Resource instance);
	
	public Set<Subject> getAttributeSubjects(Resource instance, Resource attribute);
	
	public Set<ModelObject> getValueObjects(Resource attribute, Resource value);
	
}
