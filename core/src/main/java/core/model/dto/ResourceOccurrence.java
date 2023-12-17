package core.model.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ResourceOccurrence {

	private Resource resource;
	private Statement context;
	private Kind kind;
	
	public Resource getResource() {
		return resource;
	}
	
	@XmlElement
	public void setResource(Resource res) {
		this.resource = res;
	}
	
	public Statement getContext() {
		return context;
	}
	
	@XmlTransient
	public void setContext(Statement context) {
		this.context = context;
	}
	
	public Kind getKind() {
		return this.kind;
	}
	
	@XmlElement
	public void setKind(Kind kind) {
		this.kind = kind;
	}
	
}
