package core.model.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Statement {

	private Context context;
	private Subject subject;
	private Property property;
	private ModelObject object;
	
	@XmlElement
	public Context getContext() {
		return context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	@XmlElement
	public Subject getSubject() {
		return subject;
	}
	
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	@XmlElement
	public Property getProperty() {
		return property;
	}
	
	public void setProperty(Property property) {
		this.property = property;
	}
	
	@XmlElement
	public ModelObject getObject() {
		return object;
	}
	
	public void setObject(ModelObject object) {
		this.object = object;
	}
	
}
