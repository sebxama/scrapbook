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
	
}
