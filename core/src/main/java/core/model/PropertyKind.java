package core.model;

import java.util.Set;

public interface PropertyKind extends Context, Subject, Property, ModelObject, Kind {

	public Set<Property> getInstanceProperties();
	
	public Set<Subject> getAttributeSubjects(Resource instance);
	
	public Set<ModelObject> getValueObjects(Resource instance, Resource attribute);
	
}
