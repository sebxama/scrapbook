package core.inference;

import core.model.Kind;
import core.model.ResourceOccurrence;

public class ResourceOccurrenceKindRule extends InferenceRule<ResourceOccurrence, Kind> {

	public ResourceOccurrenceKindRule() {

	}

	@Override
	public Kind perform(ResourceOccurrence arg) {
		return arg.getKind();
	}
	
}
