package core.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ResourceOccurrence {

	private Resource resource;
	private Statement context;
	private Kind kind;

	@XmlElement
	public Resource getResource() {
		return resource;
	}
	
	public void setResource(Resource res) {
		this.resource = res;
	}
	
	@JsonBackReference
	public Statement getContextStatement() {
		return context;
	}
	
	public void setContextStatement(Statement context) {
		this.context = context;
	}
	
	@XmlElement
	public Kind getKind() {
		return this.kind;
	}
	
	public void setKind(Kind kind) {
		this.kind = kind;
	}
	
}
