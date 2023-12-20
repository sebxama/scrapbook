package core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class StatementImpl implements Statement {

	private Resource resource;
	
	private Context context;
	private Subject subject;
	private Property property;
	private ModelObject object;
	
	public StatementImpl() {
		// TODO: Implement Resource
	}
	
	public Resource getResource() {
		return this.resource;
	}
	
	public void setResource(Resource res) {
		this.resource = res;
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
		this.context = context;
	}

	public void setSubject(Subject subj) {
		this.subject = subj;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public void setObject(ModelObject object) {
		this.object = object;
	}
	
	@Override
	public String toString() {
		return this.context.getResource() + " : " + this.subject.getResource() + " : " + this.property.getResource() + " : " + this.object.getResource();
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
