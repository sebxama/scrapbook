package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelObjectKinds {

	private static ModelObjectKinds instance;
	public static ModelObjectKinds getInstance() {
		if(instance == null)
			instance = new ModelObjectKinds();
		return instance;
	}
	
	protected ModelObjectKinds() {
		
	}

	// TODO:
	public Set<ModelObjectKind> getObjectKinds(Resource context, Resource instance, Resource attribute, Resource value) {
		Set<ModelObjectKind> ret = new HashSet<ModelObjectKind>();
		for(Statement stat : Statements.getInstance().getStatements(context, value, attribute, instance))
			ret.add((ModelObjectKind)stat.getObject().getKind());
		return ret;
	}

	/**
	 * FCA Concepts context lattice building. Kind Instance: Axis,
	 * Kind Attribute: FCA Context objects, Kind Value: FCA Context attributes.
	 * @return Kinds aggregated by distinct Objects Resource (IRI).
	 */
	public Map<Resource, Set<ModelObjectKind>> getObjectAggregatedKinds() {
		Map<Resource, Set<ModelObjectKind>> ret = new HashMap<Resource, Set<ModelObjectKind>>();
		for(ModelObject obj : ModelObjects.getInstance().getObjects()) {
			Set<ModelObjectKind> set = ret.get(obj.getResource());
			if(set == null)
				set = new HashSet<ModelObjectKind>();
			set.add((ModelObjectKind)obj.getKind());
			ret.put(obj.getResource(), set);
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
