package core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class StatementImpl extends ResourceOccurrenceImpl implements Statement {

	private Context context;
	private Subject subject;
	private Property property;
	private ModelObject object;
	
	protected StatementImpl(Resource iri) {
		super(iri);
		super.setContextStatement(this);
	}
	
	@JsonBackReference
	public Context getContext() {
		return context;
	}

	@JsonBackReference
	public Subject getSubject() {
		return subject;
	}

	@JsonBackReference
	public Property getProperty() {
		return property;
	}

	@JsonBackReference
	public ModelObject getObject() {
		return object;
	}

	public void setContext(Context context) {
		context.setContextStatement(this);
		this.context = context;
	}

	public void setSubject(Subject subj) {
		subj.setContextStatement(this);
		this.subject = subj;
	}

	public void setProperty(Property property) {
		property.setContextStatement(this);
		this.property = property;
	}

	public void setObject(ModelObject object) {
		object.setContextStatement(this);
		this.object = object;
	}

	@Override
	public Resource getResource() {
		return Resource.getStatementResource(this.context, this.subject, this.property, this.object);
	}
	
	@Override
	public String toString() {
		return getResource().getIRI();
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
