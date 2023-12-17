package core.model.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ModelObject extends ResourceOccurrence {

	private ModelObjectKind objectKind;
	
	public ModelObjectKind getObjectKind() {
		return objectKind;
	}
	
	@XmlElement
	public void setObjectKind(ModelObjectKind kind) {
		this.objectKind = kind;
	}
	
}
