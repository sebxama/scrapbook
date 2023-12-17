package core.model.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Statement {

	private Context context;
	private Subject subject;
	private Property property;
	private ModelObject object;
	
	public Context getContext() {
		return context;
	}
	
	@XmlElement
	public void setContext(Context context) {
		this.context = context;
	}
	
	public Subject getSubject() {
		return subject;
	}
	
	@XmlElement
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	public Property getProperty() {
		return property;
	}
	
	@XmlElement
	public void setProperty(Property property) {
		this.property = property;
	}
	
	public ModelObject getObject() {
		return object;
	}
	
	@XmlElement
	public void setObject(ModelObject object) {
		this.object = object;
	}
	
}
