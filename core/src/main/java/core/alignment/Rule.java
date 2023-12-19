package core.alignment;

public class Rule<CONSUMES, PRODUCES> {

	private Rule context;
	private CONSUMES consumes;
	private Rule concept;
	private PRODUCES produces;
	
	protected Rule() {
		
	}

	public Rule getContext() {
		return context;
	}

	public void setContext(Rule context) {
		this.context = context;
	}

	public CONSUMES getConsumes() {
		return consumes;
	}

	public void setConsumes(CONSUMES consumes) {
		this.consumes = consumes;
	}

	public Rule getConcept() {
		return concept;
	}

	public void setConcept(Rule concept) {
		this.concept = concept;
	}

	public PRODUCES getProduces() {
		return produces;
	}

	public void setProduces(PRODUCES produces) {
		this.produces = produces;
	}
	
}
