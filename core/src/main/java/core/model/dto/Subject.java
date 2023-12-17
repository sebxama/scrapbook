package core.model.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Subject extends ResourceOccurrence {

	private SubjectKind subjectKind;
	
	public SubjectKind getContextKind() {
		return subjectKind;
	}
	
	public SubjectKind getSubjectKind() {
		return this.subjectKind;
	}
	
	@XmlElement
	public void setSubjectKind(SubjectKind subjectKind) {
		this.subjectKind = subjectKind;
	}
	
}
