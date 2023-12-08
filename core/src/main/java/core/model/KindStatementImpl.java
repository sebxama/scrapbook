package core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Aggregated Kinds Statements. Schema / Reification.
 *
 */
public class KindStatementImpl implements Statement {
	
	private ContextKind context;
	private SubjectKind subject;
	private PropertyKind property;
	private ModelObjectKind object;
	
	public KindStatementImpl() {

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
	public String toString() {
		return this.context.toString() + " : " + this.subject.toString() + " : " + this.property.toString() + " : " + this.object.toString();
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
