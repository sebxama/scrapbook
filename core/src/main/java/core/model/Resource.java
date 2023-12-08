package core.model;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Resource: IRI plus 'PrimeID' for FCA Concepts
 * Context Lattice object x attributes Factors.
 *
 */
public class Resource {

	private static Map<String, Resource> resources = new HashMap<String, Resource>();
	
	private String iri;
	private Set<ResourceOccurrence> resourceOccurrences;
	
	private Resource(String iri) {
		this.iri = iri;
		this.resourceOccurrences = new HashSet<ResourceOccurrence>();
	}
	
	public static Resource getResource(String iri) {
		Resource res = resources.get(iri);
		if(res == null) {
			res = new Resource(iri);
			resources.put(iri, res);
		}
		return res;
	}
	
	public String getIRI() {
		return this.iri;
	}
	
	public void setIRI(String iri) {
		this.iri = iri;
	}
	
	public Set<ResourceOccurrence> getResourceOccurrences() {
		return this.resourceOccurrences;
	}
	
	public String toString() {
		return this.iri;
	}
	
	public int hashCode() {
		return this.iri.hashCode();
	}
	
	public boolean equals(ModelObject obj) {
		if(obj instanceof Resource)
			return obj.hashCode() == this.hashCode();
		return false;
	}

}
