package core.model;

import java.util.Set;

public interface SubjectKind extends Kind {
	
	public Set<Subject> getInstances();
	
	public Set<Property> getAttributes(Subject instance);
	
	public Set<ModelObject> getValues(Subject instance, Property attribute);
	
}
