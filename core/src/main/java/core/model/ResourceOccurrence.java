package core.model;

public interface ResourceOccurrence {

	public Resource getResource();
	
	public void setResource(Resource res);
	
	public Statement getContextStatement();

	public void setContextStatement(Statement stat);
	
	public Kind getKind();
	
	public void setKind(Kind kind);
	
}
