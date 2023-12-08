package core.model;

import java.util.HashSet;
import java.util.Set;

public class KindStatements {

	private static KindStatements instance;
	public static KindStatements getInstance() {
		if(instance == null)
			instance = new KindStatements();
		return instance;
	}
	
	protected KindStatements() {
		
	}

	/**
	 * Once Aggregated (FCA), Resource Occurrences Kinds merged / updated.
	 * Populate corresponding Kind Statements from Kinds in Statements resources.
	 * @param Optional context, subject, property, object Resource filters.
	 * @return Aggregated Kinds schema KindStatement(s).
	 */
	public Set<KindStatementImpl> getKindStatements(ContextKind context, SubjectKind subject, PropertyKind property, ModelObjectKind object) {
		Set<KindStatementImpl> ret = new HashSet<KindStatementImpl>();
		Set<Statement> stats = Statements.getInstance().getStatements();
		for(Statement stat : stats) {
			if(context != null && !context.equals(stat.getContext().getKind()))
				continue;
			if(subject != null && !subject.equals(stat.getSubject().getKind()))
				continue;
			if(property != null && !property.equals(stat.getProperty().getKind()))
				continue;
			if(object != null && !object.equals(stat.getObject().getKind()))
				continue;
			KindStatementImpl kstat = new KindStatementImpl();
			kstat.setContext((ContextKind)stat.getContext().getKind());
			kstat.setSubject((SubjectKind)stat.getSubject().getKind());
			kstat.setProperty((PropertyKind)stat.getProperty().getKind());
			kstat.setObject((ModelObjectKind)stat.getObject().getKind());
			ret.add(kstat);
		}
		return ret;
	}
	
	public Set<SubjectKind> getSubjectKinds(ContextKind context, SubjectKind subject, PropertyKind property, ModelObjectKind object) {
		Set<SubjectKind> ret = new HashSet<SubjectKind>();
		Set<KindStatementImpl> stats = getKindStatements(context, subject, property, object);
		for(KindStatementImpl stat : stats) {
			ret.add(stat.getSubject());
		}
		return ret;
	}

}
