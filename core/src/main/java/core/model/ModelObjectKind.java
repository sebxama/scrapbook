package core.model;

import java.util.Set;

public interface ModelObjectKind extends Context, Subject, Property, ModelObject, Kind {
	
	public Set<ModelObject> getInstanceObjects();
	
	public Set<Property> getAttributeProperties(Resource instance);
	
	public Set<Subject> getValueSubjects(Resource instance, Resource attribute);
	
}
