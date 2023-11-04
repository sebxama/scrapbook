package core.model;

import java.util.Set;

public interface ModelObjectKind extends Context, Subject, Property, ModelObject, Kind {

	public Set<ModelObject> getInstanceObjects(Resource resource);
	
	public Set<Property> getAttributeProperties(Resource resource);
	
	public Set<Subject> getValueSubjects(Resource resource);
	
}
