package core.model;

public interface Statement extends Context, Subject, Property, ModelObject {

	public Context getContext();
	
	public Subject getSubject();
	
	public Property getProperty();
	
	public ModelObject getObject();
	
}
