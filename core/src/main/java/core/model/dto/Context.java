package core.model.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Context extends ResourceOccurrence {

	private ContextKind contextKind;
	
	public Context() {
		
	}
	
	public ContextKind getContextKind() {
		return contextKind;
	}
	
	@XmlElement
	public void setContextKind(ContextKind contextKind) {
		this.contextKind = contextKind;
	}
	
}
