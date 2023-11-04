package core.model;

import java.util.Set;

public interface PropertyKind extends Context, Subject, Property, ModelObject, Kind {

	public Set<Property> getInstanceProperties(Resource resource);
	
	public Set<Subject> getAttributeSubjects(Resource resource);
	
	public Set<ModelObject> getValueObjects(Resource resource);
	
}
