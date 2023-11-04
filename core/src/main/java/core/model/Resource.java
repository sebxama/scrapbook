package core.model;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Resource: IRI plus 'PrimeID' for FCA Concepts
 * Context Lattice object x attributes Factors.
 *
 */
public class Resource {

	private static BigInteger lastPrime = BigInteger.TWO;
	private static Map<String, Resource> resources = new HashMap<String, Resource>();
	
	private String iri;
	private BigInteger primeId;
	
	private Resource(String iri) {
		this.iri = iri;
		primeId = lastPrime.nextProbablePrime();
		lastPrime = BigInteger.valueOf(primeId.longValue());
	}
	
	public static Resource get(String iri) {
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
	
	public BigInteger getPrimeID() {
		return this.primeId;
	}
	
	public void setPrimeID(BigInteger primeId) {
		this.primeId = primeId;
	}

	public boolean isFactorOf(BigInteger number) {
		// TODO: Implement.
		return false;
	}
	
	public static Resource getContextResource(String iri) {
		return get(iri);
	}

	public static Resource getSubjectResource(String iri) {
		return get(iri);
	}

	public static Resource getPropertyResource(String iri) {
		return get(iri);
	}

	public static Resource getObjectResource(String iri) {
		return get(iri);
	}
	
	public static Resource getStatementResource(Context context, Subject subject, Property property, ModelObject object) {
		return get("urn:statement:" + context.getResource().getPrimeID() + ":" + subject.getResource().getPrimeID() + ":" + property.getResource().getPrimeID() + ":" + object.getResource().getPrimeID());
	}

	public static Resource getKindStatementResource(ContextKind context, SubjectKind subject, PropertyKind property, ModelObjectKind object) {
		return get("urn:kindStatement:" + context.getResource().getPrimeID() + ":" + subject.getResource().getPrimeID() + ":" + property.getResource().getPrimeID() + ":" + object.getResource().getPrimeID());
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
