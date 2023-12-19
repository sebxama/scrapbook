package core.model;

public class PropertyImpl extends ResourceOccurrenceImpl implements Property {
	
	public PropertyImpl(Resource iri) {
		super(iri);
	}

	@Override
	public PropertyKind getKind() {
		return (PropertyKind) super.getKind();
	}
	
}
