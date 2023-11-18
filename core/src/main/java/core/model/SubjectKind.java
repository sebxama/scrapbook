package core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface SubjectKind extends Context, Subject, Property, ModelObject, Kind {
	
	public Set<Subject> getInstanceSubjects();
	
	public Set<Property> getAttributeProperties(Resource instance);
	
	public Set<ModelObject> getValueObjects(Resource instance, Resource attribute);
	
}
