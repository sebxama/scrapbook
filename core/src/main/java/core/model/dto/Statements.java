package core.model.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Statements {

	private Set<Statement> statement;
	
	public Statements() {
		this.statement = new HashSet<Statement>();
	}
	
	public void setStatement(Set<Statement> set) {
		this.statement = set;
	}
	
	@XmlElementRef
	public Set<Statement> getStatement() {
		return this.statement;
	}
	
}
