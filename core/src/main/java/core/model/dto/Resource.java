package core.model.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Resource {

	private String IRI;
	private Set<ResourceOccurrence> resourceOccurrences;
	
	public Resource() {
		this.resourceOccurrences = new HashSet<ResourceOccurrence>();
	}
	
	@XmlElement
	public String getIRI() {
		return IRI;
	}
	
	public void setIRI(String iRI) {
		IRI = iRI;
	}
	
	@JsonBackReference
	public Set<ResourceOccurrence> getResourceOccurrences() {
		return resourceOccurrences;
	}
	
	public void setResourceOccurrences(Set<ResourceOccurrence> resourceOccurrences) {
		this.resourceOccurrences = resourceOccurrences;
	}
	
}
