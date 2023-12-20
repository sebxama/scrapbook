package core.model;

public interface ResourceOccurrence extends HasResource {
	
	public Statement getContextStatement();

	public void setContextStatement(Statement stat);
	
	public Kind getKind();
	
	public void setKind(Kind kind);
	
}
