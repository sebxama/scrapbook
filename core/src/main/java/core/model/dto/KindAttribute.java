package core.model.dto;

import java.util.Set;

public class KindAttribute<INST extends ResourceOccurrence, ATTR extends ResourceOccurrence, VAL extends ResourceOccurrence> {

	private ATTR attribute;
	private Set<KindValue<INST, ATTR, VAL>> values;
	
	public ATTR getAttribute() {
		return attribute;
	}
	
	public void setAttribute(ATTR attr) {
		this.attribute = attr;
	}
	
	public Set<KindValue<INST, ATTR, VAL>> getValues() {
		return this.values;
	}
	
}
