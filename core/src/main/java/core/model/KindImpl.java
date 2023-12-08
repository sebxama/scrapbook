package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class KindImpl<INST extends ResourceOccurrence, ATTR extends ResourceOccurrence, VAL extends ResourceOccurrence> extends ResourceOccurrenceImpl implements Kind {
	
	private Set<INST> instances;
	private Map<INST, Set<ATTR>> attributes;
	private Map<INST, Map<ATTR, Set<VAL>>> values;
	
	private Set<Kind> superKinds;
	private Set<Kind> subKinds;
	
	public KindImpl(Resource iri) {
		super(iri);
		this.instances = new HashSet<>();
		this.attributes = new HashMap<>();
		this.values = new HashMap<>();
		this.superKinds = new HashSet<Kind>();
		this.subKinds = new HashSet<Kind>();
	}
	
	public Set<Kind> getSuperKinds() {
		return this.superKinds;
	}
	
	public Set<Kind> getSubKinds() {
		return this.subKinds;
	}
	
	public Set<INST> getInstances() {
		return instances;
	}

	public Set<ATTR> getAttributes(INST instance) {
		Set<ATTR> ret = attributes.get(instance);
		if(ret == null) {
			ret = new HashSet<ATTR>();
			attributes.put(instance, ret);
		}
		return ret;
	}

	public Set<VAL> getValues(INST instance, ATTR attribute) {
		Map<ATTR, Set<VAL>> inst = values.get(instance);
		if(inst == null) {
			inst = new HashMap<ATTR, Set<VAL>>();
			values.put(instance, inst);
		}
		Set<VAL> vals = inst.get(attribute);
		if(vals == null) {
			vals = new HashSet<VAL>();
			inst.put(attribute, vals);
		}
		return vals;
	}

	@Override
	public Set<Resource> getInstancesResources() {
		Set<Resource> ret = new HashSet<Resource>();
		for(INST inst : instances) {
			ret.add(inst.getResource());
		}
		return ret;
	}

	@Override
	public Set<Resource> getAttributesResources(Resource instance) {
		Set<Resource> ret = new HashSet<Resource>();
		for(INST inst : instances) {
			for(ATTR attr : getAttributes(inst)) {
				if(inst.getResource().getIRI().equals(instance.getIRI()))
					ret.add(attr.getResource());
			}
		}
		return ret;
	}

	@Override
	public Set<Resource> getValuesResources(Resource instance, Resource attribute) {
		Set<Resource> ret = new HashSet<Resource>();
		for(INST inst : instances) {
			if(inst.getResource().getIRI().equals(instance.getIRI())) {
				for(ATTR attr : getAttributes(inst))
					if(attr.getResource().getIRI().equals(attribute.getIRI()))
						for(VAL val : getValues(inst, attr))
							ret.add(val.getResource());				
			}

		}
		return ret;
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
