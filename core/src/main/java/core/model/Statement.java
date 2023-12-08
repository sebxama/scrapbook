package core.model;

public interface Statement {

	public Context getContext();
	
	public Subject getSubject();
	
	public Property getProperty();
	
	public ModelObject getObject();
	
}
