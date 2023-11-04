package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PropertyKinds {

	private static PropertyKinds instance;
	public static PropertyKinds getInstance() {
		if(instance == null)
			instance = new PropertyKinds();
		return instance;
	}
	
	protected PropertyKinds() {
		
	}

	// TODO:
	public Set<PropertyKind> getPropertyKinds(Resource context, Resource instance, Resource attribute, Resource value) {
		Set<PropertyKind> ret = new HashSet<PropertyKind>();
		for(Statement stat : Statements.getInstance().getStatements(context, attribute, instance, value))
			ret.add((PropertyKind)stat.getProperty().getKind());
		return ret;
	}
	
	/**
	 * FCA Concepts context lattice building. Kind Instance: Axis,
	 * Kind Attribute: FCA Context objects, Kind Value: FCA Context attributes.
	 * @return Kinds aggregated by distinct Properties Resource (IRI).
	 */
	public Map<Resource, Set<PropertyKind>> getPropertyAggregatedKinds() {
		Map<Resource, Set<PropertyKind>> ret = new HashMap<Resource, Set<PropertyKind>>();
		for(Property prop : Properties.getInstance().getProperties()) {
			Set<PropertyKind> set = ret.get(prop.getResource());
			if(set == null)
				set = new HashSet<PropertyKind>();
			set.add((PropertyKind)prop.getKind());
			ret.put(prop.getResource(), set);
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
