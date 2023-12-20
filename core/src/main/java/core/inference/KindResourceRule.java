package core.inference;

import core.model.Kind;
import core.model.Resource;

public class KindResourceRule extends InferenceRule<Kind, Resource> {

	public KindResourceRule() {
		
	}
	
	@Override
	public Resource perform(Kind arg) {
		return arg.getResource();
	}
	
}
