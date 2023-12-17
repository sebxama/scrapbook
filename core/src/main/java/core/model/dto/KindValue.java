package core.model.dto;

public class KindValue<INST extends ResourceOccurrence, ATTR extends ResourceOccurrence, VAL extends ResourceOccurrence> {

	private VAL value;
	
	public VAL getValue() {
		return value;
	}
	
	public void setValue(VAL val) {
		this.value = val;
	}
	
}
