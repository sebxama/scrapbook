package core.model.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class KindAttribute {

	private Resource attribute;
	private Set<KindValue> values;
	
	public KindAttribute() {
		this.values = new HashSet<KindValue>();
	}
	
	@XmlElement
	public Resource getAttribute() {
		return attribute;
	}
	
	public void setAttribute(Resource attr) {
		this.attribute = attr;
	}
	
	@XmlElementRef
	public Set<KindValue> getValues() {
		return this.values;
	}
	
}
