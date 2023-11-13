package core.model;

import java.util.Set;

public interface ModelObjectKind extends Context, Subject, Property, ModelObject, Kind {

	public Set<ModelObject> getObjects();
	
	public Set<ModelObject> getInstanceObjects(Resource instance);
	
	public Set<Property> getAttributeProperties(Resource instance, Resource attribute);
	
	public Set<Subject> getValueSubjects(Resource attribute, Resource value);
	
}
