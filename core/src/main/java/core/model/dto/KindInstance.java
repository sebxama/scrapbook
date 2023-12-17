package core.model.dto;

import java.util.Set;

public class KindInstance<INST extends ResourceOccurrence, ATTR extends ResourceOccurrence, VAL extends ResourceOccurrence> {

	private INST instance;
	private Set<KindAttribute<INST, ATTR, VAL>> attributes;
	
	public INST getInstance() {
		return instance;
	}
	
	public void setInstance(INST inst) {
		this.instance = inst;
	}
	
	public Set<KindAttribute<INST, ATTR, VAL>> getAttributes() {
		return this.attributes;
	}
	
}
