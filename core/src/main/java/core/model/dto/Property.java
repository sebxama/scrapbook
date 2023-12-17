package core.model.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Property extends ResourceOccurrence {

	private PropertyKind propertyKind;
	
	public PropertyKind getPropertyKind() {
		return propertyKind;
	}
	
	@XmlElement
	public void setPropertyKind(PropertyKind kind) {
		this.propertyKind = kind;
	}
	
}
