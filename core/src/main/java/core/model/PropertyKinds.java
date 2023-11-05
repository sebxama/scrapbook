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
	
}
