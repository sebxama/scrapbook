package core.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContextKinds {

	private static ContextKinds instance;
	public static ContextKinds getInstance() {
		if(instance == null)
			instance = new ContextKinds();
		return instance;
	}
	
	protected ContextKinds() {
		
	}

	public Set<ContextKind> getContextKinds(Resource context, Resource instance, Resource attribute, Resource value) {
		Set<ContextKind> ret = new HashSet<ContextKind>();
		for(Statement stat : Statements.getInstance().getStatements(context, instance, attribute, value))
			ret.add((ContextKind)stat.getContext().getKind());
		return ret;
	}
	
	/**
	 * FCA Concepts context lattice building. Kind Instance: Axis,
	 * Kind Attribute: FCA Context objects, Kind Value: FCA Context attributes.
	 * @return Kinds aggregated by distinct Contexts Resource (IRI).
	 */
	public Map<Resource, Set<ContextKind>> getContextAggregatedKinds() {
		Map<Resource, Set<ContextKind>> ret = new HashMap<Resource, Set<ContextKind>>();
		for(Context ctx : Contexts.getInstance().getContexts()) {
			Set<ContextKind> set = ret.get(ctx.getResource());
			if(set == null)
				set = new HashSet<ContextKind>();
			set.add((ContextKind)ctx.getKind());
			ret.put(ctx.getResource(), set);
		}
		return ret;
	}
	
}
