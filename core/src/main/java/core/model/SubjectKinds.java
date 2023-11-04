package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubjectKinds {

	private static SubjectKinds instance;
	public static SubjectKinds getInstance() {
		if(instance == null)
			instance = new SubjectKinds();
		return instance;
	}
	
	private SubjectKinds() {
		
	}

	// TODO:
	public Set<SubjectKind> getSubjectKinds(Resource context, Resource instance, Resource attribute, Resource value) {
		Set<SubjectKind> ret = new HashSet<SubjectKind>();
		for(Statement stat : Statements.getInstance().getStatements(context, instance, attribute, value))
			ret.add((SubjectKind)stat.getSubject().getKind());
		return ret;
	}
	
	/**
	 * FCA Concepts context lattice building. Resource: FCA Context,
	 * Kind Instance: FCA Context objects, Kind Attribute: FCA Context attributes.
	 * @return Kinds aggregated by distinct Subjects Resource (IRI) for Statement
	 *         Occurrences Kinds Merge.
	 */
	public Map<Resource, Set<SubjectKind>> getSubjectAggregatedKinds() {
		Map<Resource, Set<SubjectKind>> ret = new HashMap<Resource, Set<SubjectKind>>();
		for(Subject subj : Subjects.getInstance().getSubjects()) {
			Set<SubjectKind> set = ret.get(subj.getResource());
			if(set == null)
				set = new HashSet<SubjectKind>();
			set.add((SubjectKind)subj.getKind());
			ret.put(subj.getResource(), set);
		}
		return ret;
	}
	
	/**
	 * Once Aggregated (FCA), Resource Occurrences Kinds merged / updated.
	 * Populate corresponding Kind Statements from Kinds in Statements resources.
	 * @param Optional context, subject, property, object Resource filters.
	 * @return Aggregated Kinds schema KindStatement(s).
	 */
	public Set<KindStatement> getKindStatements(Resource context, Resource subject, Resource property, Resource object) {
		Set<KindStatement> ret = new HashSet<KindStatement>();
		Set<Statement> stats = Statements.getInstance().getStatements(context, subject, property, object);
		for(Statement stat : stats) {
			Resource iri = Resource.getKindStatementResource((ContextKind)stat.getContext().getKind(), (SubjectKind)stat.getSubject().getKind(), (PropertyKind)stat.getProperty().getKind(), (ModelObjectKind)stat.getObject().getKind());
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
