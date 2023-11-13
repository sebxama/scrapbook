package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class KindImpl extends ResourceOccurrenceImpl implements Kind {
	
	private Set<Resource> instances;
	private Map<Resource, Set<Resource>> attributes;
	private Map<Resource, Map<Resource, Set<Resource>>> values;
	
	private Kind superKind;
	
	public KindImpl(Resource iri) {
		super(iri);
		this.instances = new HashSet<>();
		this.attributes = new HashMap<>();
		this.values = new HashMap<>();
	}

	@Override
	public void setSuperKind(Kind k) {
		this.superKind = k;
	}
	
	@Override
	public Kind getSuperKind() {
		return this.superKind;
	}
	
	public Set<Resource> getInstances() {
		return instances;
	}

	public Set<Resource> getAttributes(Resource instance) {
		Set<Resource> ret = attributes.get(instance);
		if(ret == null) {
			ret = new HashSet<Resource>();
			attributes.put(instance, ret);
		}
		return ret;
	}

	public Set<Resource> getValues(Resource instance, Resource attribute) {
		Map<Resource, Set<Resource>> inst = values.get(instance);
		if(inst == null) {
			inst = new HashMap<Resource, Set<Resource>>();
			values.put(instance, inst);
		}
		Set<Resource> vals = inst.get(attribute);
		if(vals == null) {
			vals = new HashSet<Resource>();
			inst.put(attribute, vals);
		}
		return vals;
	}

	@Override
	public String toString() {
		String ret = this.getClass().getCanonicalName() + " : " + this.getResource().toString();
		return ret;
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals(java.lang.Object obj) {
		return this.hashCode() == obj.hashCode();
	}
	
}
