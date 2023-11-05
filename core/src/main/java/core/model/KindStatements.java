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
	public Set<KindStatement> getKindStatements(ContextKind context, SubjectKind subject, PropertyKind property, ModelObjectKind object) {
		Set<KindStatement> ret = new HashSet<KindStatement>();
		Set<Statement> stats = Statements.getInstance().getStatements();
		for(Statement stat : stats) {
			Resource iri = Resource.getKindStatementResource((ContextKind)stat.getContext().getKind(), (SubjectKind)stat.getSubject().getKind(), (PropertyKind)stat.getProperty().getKind(), (ModelObjectKind)stat.getObject().getKind());
			if(context != null && !context.equals(stat.getContext().getKind()))
				continue;
			if(subject != null && !subject.equals(stat.getSubject().getKind()))
				continue;
			if(property != null && !property.equals(stat.getProperty().getKind()))
				continue;
			if(object != null && !object.equals(stat.getObject().getKind()))
				continue;
			KindStatement kstat = new KindStatement(iri);
			kstat.setContext((ContextKind)stat.getContext().getKind());
			kstat.setSubject((SubjectKind)stat.getSubject().getKind());
			kstat.setProperty((PropertyKind)stat.getProperty().getKind());
			kstat.setObject((ModelObjectKind)stat.getObject().getKind());
			ret.add(kstat);
		}
		return ret;
	}

}
