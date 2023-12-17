package core.model.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Resource {

	private String IRI;
	private Set<ResourceOccurrence> resourceOccurrences;
	
	public Resource() {
		this.resourceOccurrences = new HashSet<ResourceOccurrence>();
	}
	
	public String getIRI() {
		return IRI;
	}
	
	@XmlElement
	public void setIRI(String iRI) {
		IRI = iRI;
	}
	
	public Set<ResourceOccurrence> getResourceOccurrences() {
		return resourceOccurrences;
	}
	
	@XmlElement
	public void setResourceOccurrences(Set<ResourceOccurrence> resourceOccurrences) {
		this.resourceOccurrences = resourceOccurrences;
	}
	
}
