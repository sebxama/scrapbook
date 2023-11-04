package core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface SubjectKind extends Context, Subject, Property, ModelObject, Kind {
	
	public Set<Subject> getInstanceSubjects(Resource instance);
	
	public Set<Property> getAttributeProperties(Resource attribute);
	
	public Set<ModelObject> getAttributeValues(Resource value);
	
}
