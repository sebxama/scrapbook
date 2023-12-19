package core.model;

public class ContextImpl extends ResourceOccurrenceImpl implements Context {
	
	public ContextImpl(Resource iri) {
		super(iri);
	}
	
	@Override
	public ContextKind getKind() {
		return (ContextKind) super.getKind();
	}
	
}
