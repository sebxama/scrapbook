package core.inference;

import java.util.function.Function;

public abstract class InferenceRule<CONSUMES, PRODUCES> implements Function<CONSUMES, PRODUCES> {

	private InferenceRule context;
	private CONSUMES consumes;
	private InferenceRule concept;
	private PRODUCES produces;
	
	protected InferenceRule() {
		
	}

	public <C, P> InferenceRule<C, P> getContext() {
		return context;
	}

	public <C, P> void setContext(InferenceRule<C, P> context) {
		this.context = context;
	}

	public CONSUMES getConsumes() {
		return consumes;
	}

	public void setConsumes(CONSUMES consumes) {
		this.consumes = consumes;
	}

	public <C, P> InferenceRule<C, P> getConcept() {
		return concept;
	}

	public <C, P> void setConcept(InferenceRule<C, P> concept) {
		this.concept = concept;
	}

	public PRODUCES getProduces() {
		return produces;
	}

	public void setProduces(PRODUCES produces) {
		this.produces = produces;
	}

	@Override
	public PRODUCES apply(CONSUMES t) {
		//Function<CONSUMES, PRODUCES> comp = compose(concept);
		//Function<CONSUMES, PRODUCES> then = andThen(context);
		return perform(t);
	}

	protected abstract PRODUCES perform(CONSUMES t);
	
}
