package com.cognescent.core.model;

/**
 * TODO: Reified Kind Statements.
 *
 * @param <INST>
 * @param <ATTR>
 * @param <VAL>
 */
public abstract class KindStatement<INST extends IRIStatementOccurrence, ATTR extends IRIStatementOccurrence, VAL extends IRIStatementOccurrence> {

	private Kind kind;
	private Statement statement;
	private INST instance;
	private ATTR attribute;
	private VAL value;
	
	public KindStatement(Kind kind, INST instance, ATTR attribute, VAL value) {
		this.kind = kind;
		this.instance = instance;
		this.attribute = attribute;
		this.value = value;
	}
	
	public Kind getKind() {
		return this.kind;
	}
	
	public void setKind(Kind kind) {
		this.kind = kind;
	}
	
	public Statement getStatement() {
		return this.statement;
	}
	
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	
	public INST getInstance() {
		return instance;
	}

	public void setInstance(INST instance) {
		this.instance = instance;
	}

	public ATTR getAttribute() {
		return attribute;
	}

	public void setAttribute(ATTR attribute) {
		this.attribute = attribute;
	}

	public VAL getValue() {
		return value;
	}

	public void setValue(VAL value) {
		this.value = value;
	}
	
}
