package core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Aggregated Kinds Statements. Schema / Reification.
 *
 */
public class KindStatement extends ResourceOccurrenceImpl implements Statement {
	
	private ContextKind context;
	private SubjectKind subject;
	private PropertyKind property;
	private ModelObjectKind object;
	
	protected KindStatement(Resource iri) {
		super(iri);
		super.setContextStatement(this);
	}
	
	@Override
	@JsonBackReference
	public ContextKind getContext() {
		return context;
	}

	@Override
	@JsonBackReference
	public SubjectKind getSubject() {
		return subject;
	}

	@Override
	@JsonBackReference
	public PropertyKind getProperty() {
		return property;
	}

	@Override
	@JsonBackReference
	public ModelObjectKind getObject() {
		return object;
	}

	public void setContext(ContextKind context) {
		context.setContextStatement(this);
		this.context = context;
	}

	public void setSubject(SubjectKind subject) {
		subject.setContextStatement(this);
		this.subject = subject;
	}

	public void setProperty(PropertyKind property) {
		property.setContextStatement(this);
		this.property = property;
	}

	public void setObject(ModelObjectKind object) {
		object.setContextStatement(this);
		this.object = object;
	}

	@Override
	public Resource getResource() {
		return Resource.getKindStatementResource(this.context, this.subject, this.property, this.object);
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
