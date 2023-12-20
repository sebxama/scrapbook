package core.inference;

import core.model.Context;
import core.model.Statement;
import core.model.Subject;

public class StatementSubjectRule extends InferenceRule<Statement, Subject> {

	public StatementSubjectRule() {
		
	}
	
	@Override
	public Subject perform(Statement arg) {
		return arg.getSubject();
	}
	
}
