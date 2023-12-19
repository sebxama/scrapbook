package core.model;

public class ModelObjectImpl extends ResourceOccurrenceImpl implements ModelObject {
	
	public ModelObjectImpl(Resource iri) {
		super(iri);
	}
	
	@Override
	public ModelObjectKind getKind() {
		return (ModelObjectKind) super.getKind();
	}
	
}
