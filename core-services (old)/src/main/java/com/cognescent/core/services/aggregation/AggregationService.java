package com.cognescent.core.services.aggregation;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.spring.support.RDF4JTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognescent.core.model.ContextKind;
import com.cognescent.core.model.IRI;
import com.cognescent.core.model.IRIStatementOccurrence;
import com.cognescent.core.model.Kind;
import com.cognescent.core.model.ObjectKind;
import com.cognescent.core.model.PredicateKind;
import com.cognescent.core.model.Statement;
import com.cognescent.core.model.StatementContext;
import com.cognescent.core.model.StatementObject;
import com.cognescent.core.model.StatementPredicate;
import com.cognescent.core.model.StatementSubject;
import com.cognescent.core.model.Statements;
import com.cognescent.core.model.SubjectKind;
import com.cognescent.core.services.aggregation.AggregationKind.ContextAggregationKind;
import com.cognescent.core.services.aggregation.AggregationKind.ObjectAggregationKind;
import com.cognescent.core.services.aggregation.AggregationKind.PredicateAggregationKind;
import com.cognescent.core.services.aggregation.AggregationKind.SubjectAggregationKind;

/**
 * Learn Types.
 *
 */
@Service
public class AggregationService {

	@Autowired
	private RDF4JTemplate rdf4JTemplate;

	// FIXME: format from Controller
	public void loadRDFData(String data, RDFFormat format) {
		Reader reader = new StringReader(data);
		rdf4JTemplate.consumeConnection(con -> {
			try { con.add(reader, format, (Resource[])null); } catch(Throwable t) { t.printStackTrace(); }
		});
	}

	public void loadRepositoryStatements(String sparqlQuery, String[] sparqlRules /* SPIN Like*/) {

		if(sparqlRules != null) {
			for(String rule : sparqlRules) {
				Model model = rdf4JTemplate.graphQuery(rule)
						.evaluateAndConvert()
						.toModel();
				rdf4JTemplate.consumeConnection(con -> {
					try { con.add(model, (Resource[])null); } catch(Throwable t) { t.printStackTrace(); }
				});
			}
		}
		
		if(sparqlQuery == null) { // Retrieve all statements
			sparqlQuery = "CONSTRUCT {?s ?p ?o} WHERE { $s $p $o }";
		}
		
		Model model = rdf4JTemplate.graphQuery(sparqlQuery)
									.evaluateAndConvert()
									.toModel();

		for(org.eclipse.rdf4j.model.Statement st : model) {
			// FIXME: Initial Context (Subject Hash?)
			String ctx = st.getContext() != null ? st.getContext().stringValue() : "http://cognescent.com/rootContext";
			String sub = st.getSubject().stringValue();
			String pred = st.getPredicate().stringValue();
			// FIXME: Handle Literals
			String obj = st.getObject().isLiteral() ? st.getObject().stringValue().hashCode()+"" : st.getObject().stringValue();
			Statement stat = new Statement(ctx, sub, pred, obj);
		}
	}
	
	public void addStatements(Set<Statement> stats) {
		for(Statement stat : stats)
			Statements.getInstance().addStatement(stat);
	}
	
	/*
	 * TODO: Kinds IRIStatementOccurrence / Context, Placeholder IRIs (Statement)
	 */
	public void performAggregation() {

		// 1. Retrieve Statements from RDF4J
		// 2. Aggregation: One Context per instance
		// 3. Merge Contexts: Contexts from lesser Attribute
		//    set count to same / increased Attribute set count
		//    Instances: compare contains relationship. Sub Kinds
		//    Attribute sets contains super Kind Attribute sets. 
		//    new Kinds (unseen Attributes). Multiple Kinds (Occurrences).
		// 4. Materialize merged / aggregated Contexts (Kinds)
		//    into Model Kinds / Statements / KindStatements.
		
		// TODO: Convert to Reactor / Reactive Async (Flux).
		
		contextsAggregation();
		aggregateContextKinds();
		aggregateStatementContextKinds();				

		subjectsAggregation();
		aggregateSubjectKinds();
		aggregateStatementSubjectKinds();

		predicatesAggregation();
		aggregatePredicateKinds();
		aggregateStatementPredicateKinds();

		objectsAggregation();
		aggregateObjectKinds();
		aggregateStatementObjectKinds();

	}

	private void contextsAggregation() {
		// Contexts
		for(StatementContext ctx : Statements.getInstance().getStatementContexts()) {

			AggregationKind.ContextAggregationKind kind = AggregationKind.getContextAggregationKind(ctx.getIRI());
			AggregationInstance.ContextAggregationInstance inst = AggregationInstance.getContextAggregationInstance(ctx);
			kind.getInstances().add(inst);

			for(Statement stat2 : Statements.getInstance().getStatements(ctx, null, null, null)) {

				inst.getRoles().put(stat2, kind);
				kind.getStatementOccurrences().add(stat2);
				StatementSubject subj = stat2.getSubject();
				AggregationAttribute.ContextAggregationAttribute attr = AggregationAttribute.getContextAggregationAttribute(subj);
				kind.getAttributes().add(attr);

				for(Statement stat3 : Statements.getInstance().getStatements(ctx, subj, null, null)) {

					StatementObject obj = stat3.getObject();
					AggregationValue.ContextAggregationValue val = AggregationValue.getContextAggregationValue(obj);
					val.getInstances().put(attr, inst);
					attr.getValues().put(inst, val);
					kind.getValues().add(val);

				}
			}
		}
	}

	private void aggregateContextKinds() {

		Set<ContextAggregationKind> kinds = AggregationKind.getContextAggregationKinds();
		ArrayList<ContextAggregationKind> kindsList = new ArrayList<ContextAggregationKind>(kinds);
		Collections.sort(kindsList, new Comparator<ContextAggregationKind>() {
			@Override
			public int compare(ContextAggregationKind o1, ContextAggregationKind o2) {
				if(o1.getAttributes().size() < o2.getAttributes().size())
					return -1;
				if(o1.getAttributes().size() > o2.getAttributes().size())
					return 1;
				return 0;
			}
		});
		System.out.println("Aggregation Kinds Size: "+kindsList.size());
		
//		for(ContextAggregationKind k1 : kinds) {
//			for(ContextAggregationKind k2 : kinds) {

		for(int i=0; i < kindsList.size(); i ++) {
			for(int j = i+1; j < kindsList.size(); j++) {
		
				ContextAggregationKind k1 = kindsList.get(i);
				ContextAggregationKind k2 = kindsList.get(j);
				
				if(k2.getAttributes().containsAll(k1.getAttributes()) || k1.getAttributes().containsAll(k2.getAttributes())) {
					if(k1.getAttributes().size() == k2.getAttributes().size()) {

						// Same Kind.
						// FIXME: Calculate attributes hash (embedding).
						String mergedKindIRIString = "urn:kind://"+k1.getAttributes().hashCode()+":"+k2.getAttributes().hashCode();
						IRI mergedKindIRI = IRI.get(mergedKindIRIString);
						ContextAggregationKind mergedKind = AggregationKind.getContextAggregationKind(mergedKindIRI);
						System.out.println("Aggregation Kinds: " + AggregationKind.getContextAggregationKinds().size());
						
						mergedKind.getInstances().addAll(k1.getInstances());
						mergedKind.getAttributes().addAll(k1.getAttributes());
						mergedKind.getValues().addAll(k1.getValues());
						for(Statement s : k1.getStatementOccurrences()) {
							mergedKind.getIRI().getOccurrences().add(s.getContext());
							mergedKind.getStatementOccurrences().add(s);
						}
						
						mergedKind.getInstances().addAll(k2.getInstances());
						mergedKind.getAttributes().addAll(k2.getAttributes());
						mergedKind.getValues().addAll(k2.getValues());
						for(Statement s : k2.getStatementOccurrences()) {
							mergedKind.getIRI().getOccurrences().add(s.getContext());
							mergedKind.getStatementOccurrences().add(s);
						}
						
						AggregationKind.addContextAggregationKind(mergedKind);
						System.out.println("Aggregation Kinds: " + AggregationKind.getContextAggregationKinds().size());

					} else if(k1.getAttributes().size() < k2.getAttributes().size()) {
						k2.setParent(k1);
					} else if(k2.getAttributes().size() < k1.getAttributes().size()) {
						k1.setParent(k2);
					}
					
				} else { // Attribute sets intersection: parent Kind. Left side / right side differences: sub Kinds.
					Set<AggregationAttribute<StatementContext, StatementSubject, StatementObject>> intersection
						= new HashSet<AggregationAttribute<StatementContext, StatementSubject, StatementObject>>(k1.getAttributes());
					boolean in = intersection.retainAll(k2.getAttributes());
					if(in && intersection.size() > 0) {
						Set<AggregationAttribute<StatementContext, StatementSubject, StatementObject>> leftSideAttrs
							= new HashSet<AggregationAttribute<StatementContext, StatementSubject, StatementObject>>(k1.getAttributes());
						Set<AggregationAttribute<StatementContext, StatementSubject, StatementObject>> rightSideAttrs
							= new HashSet<AggregationAttribute<StatementContext, StatementSubject, StatementObject>>(k2.getAttributes());
						leftSideAttrs.removeAll(intersection);
						rightSideAttrs.removeAll(intersection);
						
						String intersectionIRIString = "urn:kind://" + intersection.hashCode();
						IRI intersectionIRI = IRI.get(intersectionIRIString);
						ContextAggregationKind intersectionKind = AggregationKind.getContextAggregationKind(intersectionIRI);
						System.out.println("Aggregation Kinds 2: " + AggregationKind.getContextAggregationKinds().size());
						for(AggregationAttribute<StatementContext, StatementSubject, StatementObject> attr : intersection) {
							intersectionKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementContext, StatementSubject, StatementObject>, AggregationValue<StatementContext, StatementSubject, StatementObject>> values = attr.getValues();
							for(AggregationInstance<StatementContext, StatementSubject, StatementObject> key : values.keySet()) {
								intersectionKind.getInstances().add(key);
								intersectionKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									intersectionKind.getStatementOccurrences().add(stat);
									intersectionKind.getIRI().getOccurrences().add(stat.getContext());									
								}
							}
						}
						AggregationKind.addContextAggregationKind(intersectionKind);
						System.out.println("Aggregation Kinds 2: " + AggregationKind.getContextAggregationKinds().size());
						
						String leftSideIRIString = "urn:kind://" + leftSideAttrs.hashCode();
						IRI leftSideIRI = IRI.get(leftSideIRIString);
						ContextAggregationKind leftSideKind = AggregationKind.getContextAggregationKind(leftSideIRI);
						System.out.println("Aggregation Kinds 3: " + AggregationKind.getContextAggregationKinds().size());
						for(AggregationAttribute<StatementContext, StatementSubject, StatementObject> attr : leftSideAttrs) {
							leftSideKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementContext, StatementSubject, StatementObject>, AggregationValue<StatementContext, StatementSubject, StatementObject>> values = attr.getValues();
							for(AggregationInstance<StatementContext, StatementSubject, StatementObject> key : values.keySet()) {
								leftSideKind.getInstances().add(key);
								leftSideKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									leftSideKind.getStatementOccurrences().add(stat);
									leftSideKind.getIRI().getOccurrences().add(stat.getContext());
								}
							}
						}
						AggregationKind.addContextAggregationKind(leftSideKind);
						System.out.println("Aggregation Kinds 3: " + AggregationKind.getContextAggregationKinds().size());
						
						String rightSideIRIString = "urn:kind://" + rightSideAttrs.hashCode();
						IRI rightSideIRI = IRI.get(rightSideIRIString);
						ContextAggregationKind rightSideKind = AggregationKind.getContextAggregationKind(rightSideIRI);
						System.out.println("Aggregation Kinds 4: " + AggregationKind.getContextAggregationKinds().size());
						for(AggregationAttribute<StatementContext, StatementSubject, StatementObject> attr : rightSideAttrs) {
							rightSideKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementContext, StatementSubject, StatementObject>, AggregationValue<StatementContext, StatementSubject, StatementObject>> values = attr.getValues();
							for(AggregationInstance<StatementContext, StatementSubject, StatementObject> key : values.keySet()) {
								rightSideKind.getInstances().add(key);
								rightSideKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									rightSideKind.getStatementOccurrences().add(stat);
									rightSideKind.getIRI().getOccurrences().add(stat.getContext());									
								}
							}
						}
						AggregationKind.addContextAggregationKind(rightSideKind);
						System.out.println("Aggregation Kinds 4: " + AggregationKind.getContextAggregationKinds().size());
						
						leftSideKind.setParent(intersectionKind);
						rightSideKind.setParent(intersectionKind);
					}
				}
			}
		}
	}

	private void aggregateStatementContextKinds() {
		
		for(ContextAggregationKind kind : AggregationKind.getContextAggregationKinds()) {
			ContextKind contextKind = ContextKind.getByIRI(kind.getIRI());
			System.out.println("Aggregation Statement Kind: " + AggregationKind.getContextAggregationKinds().size());
			// Populate
			Set<StatementContext> insts = new HashSet<StatementContext>();
			for(AggregationInstance<StatementContext, StatementSubject, StatementObject> aggrInst : kind.getInstances()) {
				StatementContext ctx = aggrInst.getInstance();
				System.out.println("Aggregation StatementContext: " + insts.size());
				insts.add(ctx);
			}
			contextKind.getInstances().addAll(insts);
			Set<StatementSubject> subjs = new HashSet<StatementSubject>();
			for(AggregationAttribute<StatementContext, StatementSubject, StatementObject> aggrAttr : kind.getAttributes()) {
				StatementSubject sub = aggrAttr.getAttribute();
				System.out.println("Aggregation StatementSubject: " + subjs.size());
				subjs.add(sub);
			}
			contextKind.getAttributes().addAll(subjs);
			Set<StatementObject> objs = new HashSet<StatementObject>();
			for(AggregationValue<StatementContext, StatementSubject, StatementObject> aggrVal : kind.getValues()) {
				StatementObject obj = aggrVal.getValue();
				System.out.println("Aggregation StatementObject: " + objs.size());
				objs.add(obj);
			}
			contextKind.getValues().addAll(objs);

			// Parent
			if(kind.getParent() != null) {
				ContextKind parent = ContextKind.getByIRI(kind.getParent().getIRI());
				contextKind.setParent(parent);
			}

			// Occurrences / Kind Statements
			int count = 0;
			for(Statement stat : kind.getStatementOccurrences()) {
				stat.setContextKind(contextKind);
				contextKind.addStatement(stat.getContextKindStatement());
				System.out.println("AggregationKinds " + AggregationKind.getContextAggregationKinds().size());
				System.out.println(kind.getIRI());
				System.out.println("Context Statement ("+(count++)+" / "+kind.getStatementOccurrences().size()+")");
			}
		}
	}

	private void subjectsAggregation() {
		// Subjects
		for(StatementSubject subj : Statements.getInstance().getStatementSubjects()) {

			AggregationKind.SubjectAggregationKind kind = AggregationKind.getSubjectAggregationKind(subj.getIRI());
			AggregationInstance.SubjectAggregationInstance inst = AggregationInstance.getSubjectAggregationInstance(subj);
			kind.getInstances().add(inst);
			System.out.println("subjectsAggregation: "+Statements.getInstance().getStatementSubjects().size());
			System.out.println("kind instances: "+kind.getInstances().size());
			System.out.println("kind: "+kind.getIRI());
			
			for(Statement stat2 : Statements.getInstance().getStatements(null, subj, null, null)) {

				inst.getRoles().put(stat2, kind);
				kind.getStatementOccurrences().add(stat2);
				StatementPredicate pred = stat2.getPredicate();
				AggregationAttribute.SubjectAggregationAttribute attr = AggregationAttribute.getSubjectAggregationAttribute(pred);
				kind.getAttributes().add(attr);

				System.out.println(Statements.getInstance().getStatements(null, subj, null, null));
				System.out.println("kind attributes: "+kind.getAttributes().size());
				
				for(Statement stat3 : Statements.getInstance().getStatements(null, subj, pred, null)) {

					StatementObject obj = stat3.getObject();
					AggregationValue.SubjectAggregationValue val = AggregationValue.getSubjectAggregationValue(obj);
					val.getInstances().put(attr, inst);
					attr.getValues().put(inst, val);
					kind.getValues().add(val);

					System.out.println(Statements.getInstance().getStatements(null, subj, null, null));
					System.out.println("kind values: "+kind.getValues().size());
					
				}
			}
		}
	}

	private void aggregateSubjectKinds() {

		Set<SubjectAggregationKind> kinds = AggregationKind.getSubjectAggregationKinds();
		ArrayList<SubjectAggregationKind> kindsList = new ArrayList<SubjectAggregationKind>(kinds);
		Collections.sort(kindsList, new Comparator<SubjectAggregationKind>() {
			@Override
			public int compare(SubjectAggregationKind o1, SubjectAggregationKind o2) {
				if(o1.getAttributes().size() < o2.getAttributes().size())
					return -1;
				if(o1.getAttributes().size() > o2.getAttributes().size())
					return 1;
				return 0;
			}
		});
		System.out.println("Aggregation Kinds Size: "+kindsList.size());
		
//		for(ContextAggregationKind k1 : kinds) {
//			for(ContextAggregationKind k2 : kinds) {

		for(int i=0; i < kindsList.size(); i ++) {
			for(int j = i+1; j < kindsList.size(); j++) {
		
				SubjectAggregationKind k1 = kindsList.get(i);
				SubjectAggregationKind k2 = kindsList.get(j);
				
				if(k2.getAttributes().containsAll(k1.getAttributes()) || k1.getAttributes().containsAll(k2.getAttributes())) {
					if(k1.getAttributes().size() == k2.getAttributes().size()) {
						
						// Same Kind.
						// FIXME: Calculate attributes hash (embedding).
						String mergedKindIRIString = "urn:kind://"+k1.getAttributes().hashCode()+":"+k2.getAttributes().hashCode();
						IRI mergedKindIRI = IRI.get(mergedKindIRIString);
						SubjectAggregationKind mergedKind = AggregationKind.getSubjectAggregationKind(mergedKindIRI);
						System.out.println("SubjectAggregationKind: merge 1: " + AggregationKind.getSubjectAggregationKinds().size());
						mergedKind.getInstances().addAll(k1.getInstances());
						mergedKind.getAttributes().addAll(k1.getAttributes());
						mergedKind.getValues().addAll(k1.getValues());
						for(Statement s : k1.getStatementOccurrences()) {
							mergedKind.getIRI().getOccurrences().add(s.getSubject());
							mergedKind.getStatementOccurrences().add(s);
						}
						
						mergedKind.getInstances().addAll(k2.getInstances());
						mergedKind.getAttributes().addAll(k2.getAttributes());
						mergedKind.getValues().addAll(k2.getValues());
						for(Statement s : k2.getStatementOccurrences()) {
							mergedKind.getIRI().getOccurrences().add(s.getSubject());
							mergedKind.getStatementOccurrences().add(s);
						}
					
						AggregationKind.addSubjectAggregationKind(mergedKind);
						System.out.println("SubjectAggregationKind: merge 2: " + AggregationKind.getSubjectAggregationKinds().size());
					
					} else if(k1.getAttributes().size() < k2.getAttributes().size()) {
						k2.setParent(k1);
						System.out.println("SubjectAggregationKind: parent: " + AggregationKind.getSubjectAggregationKinds().size());
					} else if(k2.getAttributes().size() < k1.getAttributes().size()) {
						k1.setParent(k2);
						System.out.println("SubjectAggregationKind: parent: " + AggregationKind.getSubjectAggregationKinds().size());
					}					
					
				} else { // Attribute sets intersection: parent Kind. Left side / right side differences: sub Kinds.
					Set<AggregationAttribute<StatementSubject, StatementPredicate, StatementObject>> intersection
						= new HashSet<AggregationAttribute<StatementSubject, StatementPredicate, StatementObject>>(k1.getAttributes());
					boolean in = intersection.retainAll(k2.getAttributes());
					if(in && intersection.size() > 0) {
						Set<AggregationAttribute<StatementSubject, StatementPredicate, StatementObject>> leftSideAttrs
							= new HashSet<AggregationAttribute<StatementSubject, StatementPredicate, StatementObject>>(k1.getAttributes());
						Set<AggregationAttribute<StatementSubject, StatementPredicate, StatementObject>> rightSideAttrs
							= new HashSet<AggregationAttribute<StatementSubject, StatementPredicate, StatementObject>>(k2.getAttributes());
						leftSideAttrs.removeAll(intersection);
						rightSideAttrs.removeAll(intersection);
						
						String intersectionIRIString = "urn:kind://" + intersection.hashCode();
						IRI intersectionIRI = IRI.get(intersectionIRIString);
						SubjectAggregationKind intersectionKind = AggregationKind.getSubjectAggregationKind(intersectionIRI);
						System.out.println("SubjectAggregationKind: intersection 1: " + AggregationKind.getSubjectAggregationKinds().size());
						for(AggregationAttribute<StatementSubject, StatementPredicate, StatementObject> attr : intersection) {
							intersectionKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementSubject, StatementPredicate, StatementObject>, AggregationValue<StatementSubject, StatementPredicate, StatementObject>> values = attr.getValues();	
							for(AggregationInstance<StatementSubject, StatementPredicate, StatementObject> key : values.keySet()) {
								intersectionKind.getInstances().add(key);
								intersectionKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									intersectionKind.getStatementOccurrences().add(stat);
									intersectionKind.getIRI().getOccurrences().add(stat.getSubject());									
								}
							}
						}						
						AggregationKind.addSubjectAggregationKind(intersectionKind);
						System.out.println("SubjectAggregationKind: intersection 1: " + AggregationKind.getSubjectAggregationKinds().size());

						String leftSideIRIString = "urn:kind://" + leftSideAttrs.hashCode();
						IRI leftSideIRI = IRI.get(leftSideIRIString);
						SubjectAggregationKind leftSideKind = AggregationKind.getSubjectAggregationKind(leftSideIRI);
						System.out.println("SubjectAggregationKind: intersection 2: " + AggregationKind.getSubjectAggregationKinds().size());
						for(AggregationAttribute<StatementSubject, StatementPredicate, StatementObject> attr : leftSideAttrs) {
							leftSideKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementSubject, StatementPredicate, StatementObject>, AggregationValue<StatementSubject, StatementPredicate, StatementObject>> values = attr.getValues();
							for(AggregationInstance<StatementSubject, StatementPredicate, StatementObject> key : values.keySet()) {
								leftSideKind.getInstances().add(key);
								leftSideKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									leftSideKind.getStatementOccurrences().add(stat);
									leftSideKind.getIRI().getOccurrences().add(stat.getSubject());									
								}
							}
						}
						AggregationKind.addSubjectAggregationKind(leftSideKind);
						System.out.println("SubjectAggregationKind: intersection 2: " + AggregationKind.getSubjectAggregationKinds().size());

						String rightSideIRIString = "urn:kind://" + rightSideAttrs.hashCode();
						IRI rightSideIRI = IRI.get(rightSideIRIString);
						SubjectAggregationKind rightSideKind = AggregationKind.getSubjectAggregationKind(rightSideIRI);
						System.out.println("SubjectAggregationKind: intersection 3: " + AggregationKind.getSubjectAggregationKinds().size());
						for(AggregationAttribute<StatementSubject, StatementPredicate, StatementObject> attr : rightSideAttrs) {
							rightSideKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementSubject, StatementPredicate, StatementObject>, AggregationValue<StatementSubject, StatementPredicate, StatementObject>> values = attr.getValues();
							for(AggregationInstance<StatementSubject, StatementPredicate, StatementObject> key : values.keySet()) {
								rightSideKind.getInstances().add(key);
								rightSideKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									rightSideKind.getStatementOccurrences().add(stat);
									rightSideKind.getIRI().getOccurrences().add(stat.getSubject());									
								}
							}
						}
						AggregationKind.addSubjectAggregationKind(rightSideKind);
						System.out.println("SubjectAggregationKind: intersection 3: " + AggregationKind.getSubjectAggregationKinds().size());
						
						leftSideKind.setParent(intersectionKind);
						rightSideKind.setParent(intersectionKind);
					}
				}
			}
		}
	}

	private void aggregateStatementSubjectKinds() {
		for(SubjectAggregationKind kind : AggregationKind.getSubjectAggregationKinds()) {
			System.out.println("aggregateStatementSubjectKinds: kinds: "+AggregationKind.getSubjectAggregationKinds().size());
			SubjectKind subjectKind = SubjectKind.getByIRI(kind.getIRI());
			// Populate
			Set<StatementSubject> insts = new HashSet<StatementSubject>();
			System.out.println("aggregateStatementSubjectKinds: insts: "+kind.getInstances().size());
			for(AggregationInstance<StatementSubject, StatementPredicate, StatementObject> aggrInst : kind.getInstances()) {
				StatementSubject ctx = aggrInst.getInstance();
				insts.add(ctx);
			}
			subjectKind.getInstances().addAll(insts);
			System.out.println("aggregateStatementSubjectKinds: insts sk "+subjectKind.getInstances().size());
			Set<StatementPredicate> preds = new HashSet<StatementPredicate>();
			System.out.println("aggregateStatementSubjectKinds: preds: "+kind.getAttributes().size());
			for(AggregationAttribute<StatementSubject, StatementPredicate, StatementObject> aggrAttr : kind.getAttributes()) {
				StatementPredicate sub = aggrAttr.getAttribute();
				preds.add(sub);
			}
			subjectKind.getAttributes().addAll(preds);
			System.out.println("aggregateStatementSubjectKinds: insts: "+subjectKind.getInstances().size());
			Set<StatementObject> objs = new HashSet<StatementObject>();
			System.out.println("aggregateStatementSubjectKinds: objs: "+kind.getValues().size());
			for(AggregationValue<StatementSubject, StatementPredicate, StatementObject> aggrVal : kind.getValues()) {
				StatementObject obj = aggrVal.getValue();
				objs.add(obj);
			}
			subjectKind.getValues().addAll(objs);
			System.out.println("aggregateStatementSubjectKinds: objs: "+subjectKind.getInstances().size());
			
			// Parent
			if(kind.getParent() != null) {
				SubjectKind parent = SubjectKind.getByIRI(kind.getParent().getIRI());
				subjectKind.setParent(parent);
			}

			// Occurrences / Kind Statements
			int count = 0;
			for(Statement stat : kind.getStatementOccurrences()) {
				stat.setSubjectKind(subjectKind);
				subjectKind.addStatement(stat.getSubjectKindStatement());
				System.out.println("aggregateStatementSubjectKinds AggregationKinds " + AggregationKind.getSubjectAggregationKinds().size());
				System.out.println(kind.getIRI());
				System.out.println("Subject Statement ("+(count++)+" / "+kind.getStatementOccurrences().size()+")");
			}
		}
	}

	private void predicatesAggregation() {
		// Predicates
		for(StatementPredicate pred : Statements.getInstance().getStatementPredicates()) {

			AggregationKind.PredicateAggregationKind kind = AggregationKind.getPredicateAggregationKind(pred.getIRI());
			AggregationInstance.PredicateAggregationInstance inst = AggregationInstance.getPredicateAggregationInstance(pred);
			kind.getInstances().add(inst);

			for(Statement stat2 : Statements.getInstance().getStatements(null, null, pred, null)) {

				inst.getRoles().put(stat2, kind);
				kind.getStatementOccurrences().add(stat2);
				StatementSubject subj = stat2.getSubject();
				AggregationAttribute.PredicateAggregationAttribute attr = AggregationAttribute.getPredicateAggregationAttribute(subj);
				kind.getAttributes().add(attr);

				for(Statement stat3 : Statements.getInstance().getStatements(null, subj, pred, null)) {

					StatementObject obj = stat3.getObject();
					AggregationValue.PredicateAggregationValue val = AggregationValue.getPredicateAggregationValue(obj);
					val.getInstances().put(attr, inst);
					attr.getValues().put(inst, val);
					kind.getValues().add(val);

				}
			}
		}
	}

	private void aggregatePredicateKinds() {

		Set<PredicateAggregationKind> kinds = AggregationKind.getPredicateAggregationKinds();
		ArrayList<PredicateAggregationKind> kindsList = new ArrayList<PredicateAggregationKind>(kinds);
		Collections.sort(kindsList, new Comparator<PredicateAggregationKind>() {
			@Override
			public int compare(PredicateAggregationKind o1, PredicateAggregationKind o2) {
				if(o1.getAttributes().size() < o2.getAttributes().size())
					return -1;
				if(o1.getAttributes().size() > o2.getAttributes().size())
					return 1;
				return 0;
			}
		});
		System.out.println("Aggregation Kinds Size: "+kindsList.size());
		
		for(int i=0; i < kindsList.size(); i ++) {
			for(int j = i+1; j < kindsList.size(); j++) {
		
				PredicateAggregationKind k1 = kindsList.get(i);
				PredicateAggregationKind k2 = kindsList.get(j);
				
				if(k2.getAttributes().containsAll(k1.getAttributes()) || k1.getAttributes().containsAll(k2.getAttributes())) {
					if(k1.getAttributes().size() == k2.getAttributes().size()) {
						
						// Same Kind.
						// FIXME: Calculate attributes hash (embedding).
						String mergedKindIRIString = "urn:kind://"+k1.getAttributes().hashCode()+":"+k2.getAttributes().hashCode();
						IRI mergedKindIRI = IRI.get(mergedKindIRIString);
						PredicateAggregationKind mergedKind = AggregationKind.getPredicateAggregationKind(mergedKindIRI);
						
						mergedKind.getInstances().addAll(k1.getInstances());
						mergedKind.getAttributes().addAll(k1.getAttributes());
						mergedKind.getValues().addAll(k1.getValues());
						for(Statement s : k1.getStatementOccurrences()) {
							mergedKind.getIRI().getOccurrences().add(s.getPredicate());
							mergedKind.getStatementOccurrences().add(s);
						}
						
						mergedKind.getInstances().addAll(k2.getInstances());
						mergedKind.getAttributes().addAll(k2.getAttributes());
						mergedKind.getValues().addAll(k2.getValues());
						for(Statement s : k2.getStatementOccurrences()) {
							mergedKind.getIRI().getOccurrences().add(s.getPredicate());
							mergedKind.getStatementOccurrences().add(s);
						}
						
						AggregationKind.addPredicateAggregationKind(mergedKind);
					
					} else if(k1.getAttributes().size() < k2.getAttributes().size()) {
						k2.setParent(k1);
					} else if(k2.getAttributes().size() < k1.getAttributes().size()) {
						k1.setParent(k2);
					}
					
				} else { // Attribute sets intersection: parent Kind. Left side / right side differences: sub Kinds.
					Set<AggregationAttribute<StatementPredicate, StatementSubject, StatementObject>> intersection
						= new HashSet<AggregationAttribute<StatementPredicate, StatementSubject, StatementObject>>(k1.getAttributes());
					boolean in = intersection.retainAll(k2.getAttributes());
					if(in && intersection.size() > 0) {
						Set<AggregationAttribute<StatementPredicate, StatementSubject, StatementObject>> leftSideAttrs
							= new HashSet<AggregationAttribute<StatementPredicate, StatementSubject, StatementObject>>(k1.getAttributes());
						Set<AggregationAttribute<StatementPredicate, StatementSubject, StatementObject>> rightSideAttrs
							= new HashSet<AggregationAttribute<StatementPredicate, StatementSubject, StatementObject>>(k2.getAttributes());
						leftSideAttrs.removeAll(intersection);
						rightSideAttrs.removeAll(intersection);
						
						String intersectionIRIString = "urn:kind://" + intersection.hashCode();
						IRI intersectionIRI = IRI.get(intersectionIRIString);
						PredicateAggregationKind intersectionKind = AggregationKind.getPredicateAggregationKind(intersectionIRI);
						for(AggregationAttribute<StatementPredicate, StatementSubject, StatementObject> attr : intersection) {
							intersectionKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementPredicate, StatementSubject, StatementObject>, AggregationValue<StatementPredicate, StatementSubject, StatementObject>> values = attr.getValues();	
							for(AggregationInstance<StatementPredicate, StatementSubject, StatementObject> key : values.keySet()) {
								intersectionKind.getInstances().add(key);
								intersectionKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									intersectionKind.getStatementOccurrences().add(stat);
									intersectionKind.getIRI().getOccurrences().add(stat.getPredicate());									
								}
							}
						}
						AggregationKind.addPredicateAggregationKind(intersectionKind);
						
						String leftSideIRIString = "urn:kind://" + leftSideAttrs.hashCode();
						IRI leftSideIRI = IRI.get(leftSideIRIString);
						PredicateAggregationKind leftSideKind = AggregationKind.getPredicateAggregationKind(leftSideIRI);
						for(AggregationAttribute<StatementPredicate, StatementSubject, StatementObject> attr : leftSideAttrs) {
							leftSideKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementPredicate, StatementSubject, StatementObject>, AggregationValue<StatementPredicate, StatementSubject, StatementObject>> values = attr.getValues();
							for(AggregationInstance<StatementPredicate, StatementSubject, StatementObject> key : values.keySet()) {
								leftSideKind.getInstances().add(key);
								leftSideKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									leftSideKind.getStatementOccurrences().add(stat);
									leftSideKind.getIRI().getOccurrences().add(stat.getPredicate());									
								}
							}
						}
						AggregationKind.addPredicateAggregationKind(leftSideKind);
						
						String rightSideIRIString = "urn:kind://" + rightSideAttrs.hashCode();
						IRI rightSideIRI = IRI.get(rightSideIRIString);
						PredicateAggregationKind rightSideKind = AggregationKind.getPredicateAggregationKind(rightSideIRI);
						for(AggregationAttribute<StatementPredicate, StatementSubject, StatementObject> attr : rightSideAttrs) {
							rightSideKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementPredicate, StatementSubject, StatementObject>, AggregationValue<StatementPredicate, StatementSubject, StatementObject>> values = attr.getValues();
							for(AggregationInstance<StatementPredicate, StatementSubject, StatementObject> key : values.keySet()) {
								rightSideKind.getInstances().add(key);
								rightSideKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									rightSideKind.getStatementOccurrences().add(stat);
									rightSideKind.getIRI().getOccurrences().add(stat.getPredicate());									
								}
							}
						}
						AggregationKind.addPredicateAggregationKind(rightSideKind);
						
						leftSideKind.setParent(intersectionKind);
						rightSideKind.setParent(intersectionKind);
					}
				}
			}
		}
	}

	private void aggregateStatementPredicateKinds() {
		for(PredicateAggregationKind kind : AggregationKind.getPredicateAggregationKinds()) {
			PredicateKind predicateKind = PredicateKind.getByIRI(kind.getIRI());
			// Populate
			Set<StatementPredicate> insts = new HashSet<StatementPredicate>();
			for(AggregationInstance<StatementPredicate, StatementSubject, StatementObject> aggrInst : kind.getInstances()) {
				StatementPredicate pred = aggrInst.getInstance();
				insts.add(pred);
			}
			predicateKind.getInstances().addAll(insts);
			Set<StatementSubject> subjs = new HashSet<StatementSubject>();
			for(AggregationAttribute<StatementPredicate, StatementSubject, StatementObject> aggrAttr : kind.getAttributes()) {
				StatementSubject sub = aggrAttr.getAttribute();
				subjs.add(sub);
			}
			predicateKind.getAttributes().addAll(subjs);
			Set<StatementObject> objs = new HashSet<StatementObject>();
			for(AggregationValue<StatementPredicate, StatementSubject, StatementObject> aggrVal : kind.getValues()) {
				StatementObject obj = aggrVal.getValue();
				objs.add(obj);
			}
			predicateKind.getValues().addAll(objs);

			// Parent
			if(kind.getParent() != null) {
				PredicateKind parent = PredicateKind.getByIRI(kind.getParent().getIRI());
				predicateKind.setParent(parent);
			}

			// Occurrences / Kind Statements
			int count = 0;
			for(Statement stat : kind.getStatementOccurrences()) {
				stat.setPredicateKind(predicateKind);
				predicateKind.addStatement(stat.getPredicateKindStatement());
				System.out.println("AggregationKinds " + AggregationKind.getPredicateAggregationKinds().size());
				System.out.println(kind.getIRI());
				System.out.println("Predicate Statement ("+(count++)+" / "+kind.getStatementOccurrences().size()+")");
			}
		}
	}

	private void objectsAggregation() {
		// Objects
		for(StatementObject obj : Statements.getInstance().getStatementObjects()) {

			AggregationKind.ObjectAggregationKind kind = AggregationKind.getObjectAggregationKind(obj.getIRI());
			AggregationInstance.ObjectAggregationInstance inst = AggregationInstance.getObjectAggregationInstance(obj);
			kind.getInstances().add(inst);

			for(Statement stat2 : Statements.getInstance().getStatements(null, null, null, obj)) {

				inst.getRoles().put(stat2, kind);
				kind.getStatementOccurrences().add(stat2);
				StatementPredicate pred = stat2.getPredicate();
				AggregationAttribute.ObjectAggregationAttribute attr = AggregationAttribute.getObjectAggregationAttribute(pred);
				kind.getAttributes().add(attr);

				for(Statement stat3 : Statements.getInstance().getStatements(null, null, pred, obj)) {

					StatementSubject subj = stat3.getSubject();
					AggregationValue.ObjectAggregationValue val = AggregationValue.getObjectAggregationValue(subj);
					val.getInstances().put(attr, inst);
					attr.getValues().put(inst, val);
					kind.getValues().add(val);

				}
			}
		}
	}

	private void aggregateObjectKinds() {

		Set<ObjectAggregationKind> kinds = AggregationKind.getObjectAggregationKinds();
		ArrayList<ObjectAggregationKind> kindsList = new ArrayList<ObjectAggregationKind>(kinds);
		Collections.sort(kindsList, new Comparator<ObjectAggregationKind>() {
			@Override
			public int compare(ObjectAggregationKind o1, ObjectAggregationKind o2) {
				if(o1.getAttributes().size() < o2.getAttributes().size())
					return -1;
				if(o1.getAttributes().size() > o2.getAttributes().size())
					return 1;
				return 0;
			}
		});
		System.out.println("Aggregation Kinds Size: "+kindsList.size());
		
		for(int i=0; i < kindsList.size(); i ++) {
			for(int j = i+1; j < kindsList.size(); j++) {
		
				ObjectAggregationKind k1 = kindsList.get(i);
				ObjectAggregationKind k2 = kindsList.get(j);
				
				if(k2.getAttributes().containsAll(k1.getAttributes()) || k1.getAttributes().containsAll(k2.getAttributes())) {
					if(k1.getAttributes().size() == k2.getAttributes().size()) {
						
						// Same Kind.
						// FIXME: Calculate attributes hash (embedding).
						String mergedKindIRIString = "urn:kind://"+k1.getAttributes().hashCode()+":"+k2.getAttributes().hashCode();
						IRI mergedKindIRI = IRI.get(mergedKindIRIString);
						ObjectAggregationKind mergedKind = AggregationKind.getObjectAggregationKind(mergedKindIRI);
						
						mergedKind.getInstances().addAll(k1.getInstances());
						mergedKind.getAttributes().addAll(k1.getAttributes());
						mergedKind.getValues().addAll(k1.getValues());
						for(Statement s : k1.getStatementOccurrences()) {
							mergedKind.getIRI().getOccurrences().add(s.getObject());
							mergedKind.getStatementOccurrences().add(s);
						}
						
						mergedKind.getInstances().addAll(k2.getInstances());
						mergedKind.getAttributes().addAll(k2.getAttributes());
						mergedKind.getValues().addAll(k2.getValues());
						for(Statement s : k2.getStatementOccurrences()) {
							mergedKind.getIRI().getOccurrences().add(s.getObject());
							mergedKind.getStatementOccurrences().add(s);
						}
						
						AggregationKind.addObjectAggregationKind(mergedKind);
					
					} else if(k1.getAttributes().size() < k2.getAttributes().size()) {
						k2.setParent(k1);
					} else if(k2.getAttributes().size() < k1.getAttributes().size()) {
						k1.setParent(k2);
					}
					
				} else { // Attribute sets intersection: parent Kind. Left side / right side differences: sub Kinds.
					Set<AggregationAttribute<StatementObject, StatementPredicate, StatementSubject>> intersection
						= new HashSet<AggregationAttribute<StatementObject, StatementPredicate, StatementSubject>>(k1.getAttributes());
					boolean in = intersection.retainAll(k2.getAttributes());
					if(in && intersection.size() > 0) {
						Set<AggregationAttribute<StatementObject, StatementPredicate, StatementSubject>> leftSideAttrs
							= new HashSet<AggregationAttribute<StatementObject, StatementPredicate, StatementSubject>>(k1.getAttributes());
						Set<AggregationAttribute<StatementObject, StatementPredicate, StatementSubject>> rightSideAttrs
							= new HashSet<AggregationAttribute<StatementObject, StatementPredicate, StatementSubject>>(k2.getAttributes());
						leftSideAttrs.removeAll(intersection);
						rightSideAttrs.removeAll(intersection);
						
						String intersectionIRIString = "urn:kind://" + intersection.hashCode();
						IRI intersectionIRI = IRI.get(intersectionIRIString);
						ObjectAggregationKind intersectionKind = AggregationKind.getObjectAggregationKind(intersectionIRI);
						for(AggregationAttribute<StatementObject, StatementPredicate, StatementSubject> attr : intersection) {
							intersectionKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementObject, StatementPredicate, StatementSubject>, AggregationValue<StatementObject, StatementPredicate, StatementSubject>> values = attr.getValues();	
							for(AggregationInstance<StatementObject, StatementPredicate, StatementSubject> key : values.keySet()) {
								intersectionKind.getInstances().add(key);
								intersectionKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									intersectionKind.getStatementOccurrences().add(stat);
									intersectionKind.getIRI().getOccurrences().add(stat.getObject());									
								}
							}
						}
						AggregationKind.addObjectAggregationKind(intersectionKind);
						
						String leftSideIRIString = "urn:kind://" + leftSideAttrs.hashCode();
						IRI leftSideIRI = IRI.get(leftSideIRIString);
						ObjectAggregationKind leftSideKind = AggregationKind.getObjectAggregationKind(leftSideIRI);
						for(AggregationAttribute<StatementObject, StatementPredicate, StatementSubject> attr : leftSideAttrs) {
							leftSideKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementObject, StatementPredicate, StatementSubject>, AggregationValue<StatementObject, StatementPredicate, StatementSubject>> values = attr.getValues();
							for(AggregationInstance<StatementObject, StatementPredicate, StatementSubject> key : values.keySet()) {
								leftSideKind.getInstances().add(key);
								leftSideKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									leftSideKind.getStatementOccurrences().add(stat);
									leftSideKind.getIRI().getOccurrences().add(stat.getObject());									
								}
							}
						}
						AggregationKind.addObjectAggregationKind(leftSideKind);
						
						String rightSideIRIString = "urn:kind://" + rightSideAttrs.hashCode();
						IRI rightSideIRI = IRI.get(rightSideIRIString);
						ObjectAggregationKind rightSideKind = AggregationKind.getObjectAggregationKind(rightSideIRI);
						for(AggregationAttribute<StatementObject, StatementPredicate, StatementSubject> attr : rightSideAttrs) {
							rightSideKind.getAttributes().add(attr);
							Map<AggregationInstance<StatementObject, StatementPredicate, StatementSubject>, AggregationValue<StatementObject, StatementPredicate, StatementSubject>> values = attr.getValues();
							for(AggregationInstance<StatementObject, StatementPredicate, StatementSubject> key : values.keySet()) {
								rightSideKind.getInstances().add(key);
								rightSideKind.getValues().add(values.get(key));
								for(Statement stat : key.getRoles().keySet()) {
									rightSideKind.getStatementOccurrences().add(stat);
									rightSideKind.getIRI().getOccurrences().add(stat.getObject());									
								}
							}
						}
						AggregationKind.addObjectAggregationKind(rightSideKind);
						
						leftSideKind.setParent(intersectionKind);
						rightSideKind.setParent(intersectionKind);
					}
				}
			}
		}
	}

	private void aggregateStatementObjectKinds() {
		for(ObjectAggregationKind kind : AggregationKind.getObjectAggregationKinds()) {
			ObjectKind objectKind = ObjectKind.getByIRI(kind.getIRI());
			// Populate
			Set<StatementObject> insts = new HashSet<StatementObject>();
			for(AggregationInstance<StatementObject, StatementPredicate, StatementSubject> aggrInst : kind.getInstances()) {
				StatementObject obj = aggrInst.getInstance();
				insts.add(obj);
			}
			objectKind.getInstances().addAll(insts);
			Set<StatementPredicate> preds = new HashSet<StatementPredicate>();
			for(AggregationAttribute<StatementObject, StatementPredicate, StatementSubject> aggrAttr : kind.getAttributes()) {
				StatementPredicate pred = aggrAttr.getAttribute();
				preds.add(pred);
			}
			objectKind.getAttributes().addAll(preds);
			Set<StatementSubject> subjs = new HashSet<StatementSubject>();
			for(AggregationValue<StatementObject, StatementPredicate, StatementSubject> aggrVal : kind.getValues()) {
				StatementSubject subj = aggrVal.getValue();
				subjs.add(subj);
			}
			objectKind.getValues().addAll(subjs);

			// Parent
			if(kind.getParent() != null) {
				ObjectKind parent = ObjectKind.getByIRI(kind.getParent().getIRI());
				objectKind.setParent(parent);
			}

			// Occurrences / Kind Statements
			int count = 0;
			for(Statement stat : kind.getStatementOccurrences()) {
				stat.setObjectKind(objectKind);
				objectKind.addStatement(stat.getObjectKindStatement());
				System.out.println("AggregationKinds " + AggregationKind.getObjectAggregationKinds().size());
				System.out.println(kind.getIRI());
				System.out.println("Object Statement ("+(count++)+" / "+kind.getStatementOccurrences().size()+")");
			}
		}
	}

}
