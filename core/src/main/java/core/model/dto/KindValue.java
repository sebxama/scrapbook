package core.model.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class KindValue {

	private Resource value;
	
	@XmlElement
	public Resource getValue() {
		return value;
	}
	
	public void setValue(Resource val) {
		this.value = val;
	}
	
}
