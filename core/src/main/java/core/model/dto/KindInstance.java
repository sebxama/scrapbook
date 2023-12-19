package core.model.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class KindInstance {

	private Resource instance;
	private Set<KindAttribute> attributes;
	
	public KindInstance() {
		this.attributes = new HashSet<KindAttribute>();
	}
	
	@XmlElement
	public Resource getInstance() {
		return instance;
	}
	
	public void setInstance(Resource inst) {
		this.instance = inst;
	}
	
	@JacksonXmlElementWrapper(useWrapping = false)
	public Set<KindAttribute> getAttributes() {
		return this.attributes;
	}
	
}
