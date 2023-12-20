package core.inference;

import core.model.ModelObject;
import core.model.Statement;

public class StatementObjectRule extends InferenceRule<Statement, ModelObject> {

	public StatementObjectRule() {
		
	}
	
	@Override
	public ModelObject perform(Statement arg) {
		return arg.getObject();
	}
	
}
