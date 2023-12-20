package core.inference;

import core.model.Context;
import core.model.Statement;

public class StatementContextRule extends InferenceRule<Statement, Context> {

	public StatementContextRule() {
		
	}
	
	@Override
	public Context perform(Statement arg) {
		return arg.getContext();
	}
	
}
