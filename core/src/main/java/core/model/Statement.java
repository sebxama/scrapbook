package core.model;

public interface Statement extends HasResource {

	public Context getContext();
	
	public Subject getSubject();
	
	public Property getProperty();
	
	public ModelObject getObject();
	
}
