package core.model;

public class SubjectImpl extends ResourceOccurrenceImpl implements Subject {
	
	public SubjectImpl(Resource iri) {
		super(iri);
	}
	
	@Override
	public SubjectKind getKind() {
		return (SubjectKind) super.getKind();
	}
	
}
