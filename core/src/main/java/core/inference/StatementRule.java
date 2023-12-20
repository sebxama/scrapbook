package core.inference;

import core.model.Statement;

public class StatementRule extends InferenceRule<Statement, Statement> {

	public StatementRule() {
		
	}
	
	@Override
	public Statement perform(Statement arg) {
		return arg;
	}
	
}
