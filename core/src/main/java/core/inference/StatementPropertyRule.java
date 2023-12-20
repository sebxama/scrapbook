package core.inference;

import core.model.Context;
import core.model.Property;
import core.model.Statement;
import core.model.Subject;

public class StatementPropertyRule extends InferenceRule<Statement, Property> {

	public StatementPropertyRule() {
		
	}
	
	@Override
	public Property perform(Statement arg) {
		return arg.getProperty();
	}
	
}
