package core.aggregation;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.spring.support.RDF4JTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.model.Context;
import core.model.ContextImpl;
import core.model.ContextKind;
import core.model.ContextKindImpl;
import core.model.ContextKinds;
import core.model.Contexts;
import core.model.KindStatement;
import core.model.KindStatements;
import core.model.Subject;
import core.model.SubjectImpl;
import core.model.SubjectKind;
import core.model.SubjectKindImpl;
import core.model.SubjectKinds;
import core.model.Subjects;
import fcalib.api.fca.Attribute;
import fcalib.api.fca.Computation;
import fcalib.api.fca.Concept;
import fcalib.api.fca.Implication;
import fcalib.api.fca.ObjectAPI;
import fcalib.api.utils.OutputPrinter;
import fcalib.lib.fca.FCAAttribute;
import fcalib.lib.fca.FCAFormalContext;
import fcalib.lib.fca.FCAImplication;
import fcalib.lib.fca.FCAObject;
import reactor.core.publisher.Flux;
import core.model.Property;
import core.model.PropertyImpl;
import core.model.PropertyKind;
import core.model.PropertyKindImpl;
import core.model.PropertyKinds;
import core.model.Statement;
import core.model.StatementImpl;
import core.model.ModelObject;
import core.model.ModelObjectImpl;
import core.model.ModelObjectKind;
import core.model.ModelObjectKindImpl;
import core.model.ModelObjectKinds;
import core.model.ModelObjects;
import core.model.Properties;
import core.model.Statements;

@Service
public class AggregationService {

	private RDF4JTemplate rdf4jTemplate;
	
	public AggregationService(@Autowired RDF4JTemplate rdf4jTemplate) {
		this.rdf4jTemplate = rdf4jTemplate;
	}

	public void loadRDFData(String data, RDFFormat format) {
		Reader reader = new StringReader(data);
		rdf4jTemplate.consumeConnection(con -> {
			try { con.add(reader, format, (Resource[])null); } catch(Throwable t) { t.printStackTrace(); }
		});
	}
	
	public void loadRepositoryStatements(String sparqlQuery, String[] sparqlRules /* SPIN Like*/) {

		if(sparqlRules != null) {
			for(String rule : sparqlRules) {
				Model model = rdf4jTemplate.graphQuery(rule)
						.evaluateAndConvert()
						.toModel();
				rdf4jTemplate.consumeConnection(con -> {
					try { con.add(model, (Resource[])null); } catch(Throwable t) { t.printStackTrace(); }
				});
			}
		}
		
		// FIXME: Query including contexts (quads)
		if(sparqlQuery == null) { // Retrieve all statements
			sparqlQuery = "CONSTRUCT {?s ?p ?o} WHERE { $s $p $o }";
		}
		
		Model model = rdf4jTemplate.graphQuery(sparqlQuery)
									.evaluateAndConvert()
									.toModel();

		Statements.getInstance().getStatements().clear();
		
		System.out.println("MODEL SIZE:" + model.size());
		
		for(org.eclipse.rdf4j.model.Statement st : model) {
			
			// FIXME: Initial Context (Subject Hash?)
			String ctx = st.getContext() != null ? st.getContext().stringValue() : "urn:context:root";
			Context context = new ContextImpl(core.model.Resource.getContextResource(ctx));
			
			String sub = st.getSubject().stringValue();
			Subject subject = new SubjectImpl(core.model.Resource.getSubjectResource(sub));
			
			String pred = st.getPredicate().stringValue();
			Property property = new PropertyImpl(core.model.Resource.getPropertyResource(pred));
			
			// FIXME: Handle Literals
			String obj = st.getObject().isLiteral() ? ((Literal)st.getObject()).stringValue() : st.getObject().stringValue();
			ModelObject object = new ModelObjectImpl(core.model.Resource.getObjectResource(obj));

			StatementImpl stat = Statements.getInstance().addStatement(context, subject, property, object);

			// FIXME: Default initial Kinds
			
			ContextKind ck = ContextKindImpl.getInstance(context.getResource());
			ck.getInstances().add(context.getResource());
			ck.getAttributes(context.getResource()).add(property.getResource());
			ck.getValues(context.getResource(), property.getResource()).add(object.getResource());
			context.setKind(ck);
			
			SubjectKind sk = SubjectKindImpl.getInstance(subject.getResource());
			sk.getInstances().add(subject.getResource());
			sk.getAttributes(subject.getResource()).add(property.getResource());
			sk.getValues(subject.getResource(), property.getResource()).add(object.getResource());
			subject.setKind(sk);
			
			PropertyKind pk = PropertyKindImpl.getInstance(property.getResource());
			pk.getInstances().add(property.getResource());
			pk.getAttributes(property.getResource()).add(subject.getResource());
			pk.getValues(property.getResource(), subject.getResource()).add(object.getResource());
			property.setKind(pk);

			ModelObjectKind ok = ModelObjectKindImpl.getInstance(object.getResource());
			ok.getInstances().add(object.getResource());
			ok.getAttributes(object.getResource()).add(property.getResource());
			ok.getValues(object.getResource(), property.getResource()).add(subject.getResource());
			object.setKind(ok);
			
		}
	}

	public Set<KindStatement> performContextKindsAggregation() {

		System.out.println("CONTEXTS");
		
		fcalib.api.fca.Context<String, String> fcaContext = new FCAFormalContext<String, String>() {};
		for(ContextKind kind : ContextKinds.getInstance().getContextKinds(null, null, null, null)) {
			for(core.model.Resource inst : kind.getInstances()) {
				ObjectAPI<String,String> ob1 = new FCAObject<>(inst.getIRI());
				for(core.model.Resource attr : kind.getAttributes(inst)) {
					Attribute<String,String> atr1 = new FCAAttribute<>(attr.getIRI());
					ob1.addAttribute(attr.getIRI());
					atr1.addObject(inst.getIRI());
				}
				fcaContext.addObject(ob1);
			}
		}
				
		System.out.println("DONE FCA");
		
		OutputPrinter.printCrosstableToConsole(fcaContext);
		OutputPrinter.printConceptsToConsole(fcaContext);
		OutputPrinter.printStemBaseToConsole(fcaContext);
		
		for(Implication<String,String> impl : Computation.computeStemBase(fcaContext)){
			List<ObjectAPI<String, String>> list = Computation.computePrimeOfAttributes(impl.getPremise(), fcaContext);
			for(ObjectAPI<String, String> obj : list) {
				for(Attribute<String, String> attr : impl.getConclusion()) {
					obj.addAttribute(attr.getAttributeID());
				}
			}
			// TODO: Association Rule Mining. Assert Attributes by Support / Confidence.
            System.out.println("Support: "+impl.toString()+": "+Computation.computeImplicationSupport(impl, fcaContext));
            System.out.println("Confidence: "+impl.toString()+": "+Computation.computeConfidence(impl, fcaContext));
        }
		
		// Attributes Objects has in common
		// Computation.computePrimeOfObjects(null, null);
		
		// Objects Attributes has in common.
		// Computation.computePrimeOfAttributes(null, null);
		
		System.out.println("DONE PRINT");
		
        List<List<Attribute<String,String>>> closures = Computation.computeAllClosures(fcaContext);
        List<Concept<String,String>> concepts = Computation.computeAllConcepts(closures, fcaContext);
        for(Concept<String,String> concept : concepts) {
        	// TODO: Merged Kinds Resource IRIs
        	core.model.Resource kindRes = core.model.Resource.get(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
        	ContextKind kind = ContextKindImpl.getInstance(kindRes);
        	System.out.println("ContextKind: "+kind);
        	for(ObjectAPI<String, String> extent : concept.getExtent()) {
        		core.model.Resource inst = core.model.Resource.get(extent.getObjectID().toString());
        		kind.getInstances().add(inst);
        		System.out.println("Instance: "+inst);
        		for(Attribute<String, String> attribute : concept.getIntent()) {
        			core.model.Resource attr = core.model.Resource.get(attribute.getAttributeID().toString());
        			kind.getAttributes(inst).add(attr);
        			System.out.println("Attribute: "+attr);
        			for(ModelObject obj : ModelObjects.getInstance().getObjects(inst, null, attr, null)) {
        				core.model.Resource val = obj.getResource();
        				kind.getValues(inst, attr).add(val);
        				System.out.println("Value: "+val);
        			}
        		}
        		for(Context ctx : Contexts.getInstance().getContexts(inst, null, null, null)) {
        			// TODO: Merged Kinds Resource IRIs
        			// kind.setResource(inst);
        			ctx.setKind(kind);
        			// kind.setKind(kind); TODO: Super Kind subset of this Concept Attributes.
        		}
        	}
        	//System.out.println(concept.getExtent().stream().map(ObjectAPI::getObjectID).collect(Collectors.toList())+";");
            System.out.println(concept.getIntent().stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"\n");
        }
		
        System.out.println();
        
        for(ContextKind kind : ContextKinds.getInstance().getContextKinds(null, null, null, null)) {
        	System.out.println("KIND: "+kind);
        	for(core.model.Resource inst : kind.getInstances()) {
        		System.out.println("INSTANCE: "+inst);
        		for(core.model.Resource attr : kind.getAttributes(inst)) {
        			System.out.println("ATTRIBUTE:"+attr);
        			for(core.model.Resource value : kind.getValues(inst, attr)) {
        				System.out.println("VALUE:"+value);
        			}
        		}
        	}
        }
        
		return KindStatements.getInstance().getKindStatements(null, null, null, null);
	}
	
	public Set<KindStatement> performSubjectKindsAggregation() {

		System.out.println("SUBJECTS");
		
		fcalib.api.fca.Context<String, String> fcaContext = new FCAFormalContext<String, String>() {};
		for(SubjectKind kind : SubjectKinds.getInstance().getSubjectKinds(null, null, null, null)) {
			for(core.model.Resource inst : kind.getInstances()) {
				ObjectAPI<String,String> ob1 = new FCAObject<>(inst.getIRI());
				for(core.model.Resource attr : kind.getAttributes(inst)) {
					Attribute<String,String> atr1 = new FCAAttribute<>(attr.getIRI());
					ob1.addAttribute(attr.getIRI());
					atr1.addObject(inst.getIRI());
				}
				fcaContext.addObject(ob1);
			}
		}
		
		// Computation.reduceContext(fcaContext);

		System.out.println("CONCEPTS ASSOCIATION RULE MINING IMPLICATIONS");
		
        List<List<Attribute<String,String>>> closures2 = Computation.computeAllClosures(fcaContext);
        List<Concept<String,String>> concepts2 = Computation.computeAllConcepts(closures2, fcaContext);
        List<Implication<String, String>> implications2 = Computation.computeConceptsImplications(concepts2, fcaContext);
        for(Implication<String, String> impl : implications2) {
        	System.out.println(impl);
        	List<ObjectAPI<String, String>> listObj = Computation.computePrimeOfAttributes(impl.getPremise(), fcaContext);
        	for(ObjectAPI<String, String> obj : listObj) {
        		for(Attribute<String, String> attr : impl.getConclusion()) {
        			obj.addAttribute(attr.getAttributeID());
        			attr.addObject(obj.getObjectID());
        		}
        	}
        }
		
        /*
		System.out.println("FCA4J IMPLICATIONS:");
		for(Implication impl : Computation.computeStemBase2(fcaContext)) {
			System.out.println("Implication: "+impl);
		}
		*/
		
//		System.out.println("FCALib2 Implications: ");
//		for(Implication<String,String> impl : Computation.computeStemBase(fcaContext)) {
//			List<ObjectAPI<String, String>> list = Computation.computePrimeOfAttributes(impl.getPremise(), fcaContext);
//			for(ObjectAPI<String, String> obj : list) {
//				System.out.println("Implication object: "+obj.getObjectID());
//				for(Attribute<String, String> attr : impl.getConclusion()) {
//					obj.addAttribute(attr.getAttributeID());
//					attr.addObject(obj.getObjectID());
//					System.out.println("Implication addAttribute "+attr.getAttributeID());
//				}
//			}
//			// TODO: Association Rule Mining. Assert Attributes by Support / Confidence.
//			System.out.println("Implication: "+impl);
//        }
		
		// Attributes Objects has in common
		// Computation.computePrimeOfObjects(null, null);
		
		// Objects Attributes has in common.
		// Computation.computePrimeOfAttributes(null, null);

		OutputPrinter.printCrosstableToConsole(fcaContext);
		OutputPrinter.printConceptsToConsole(fcaContext);
		OutputPrinter.printStemBaseToConsole(fcaContext);
		
		System.out.println("DONE PRINT");

        List<List<Attribute<String,String>>> closures = Computation.computeAllClosures(fcaContext);
        List<Concept<String,String>> concepts = Computation.computeAllConcepts(closures, fcaContext);
        for(Concept<String,String> concept : concepts) {
        	// TODO: Merged Kinds Resource IRIs
        	System.out.println(concept);
        	System.out.println(concept.getExtent().stream().map(ObjectAPI::getObjectID).collect(Collectors.toList())+";");
            System.out.println(concept.getIntent().stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"\n");
        	core.model.Resource kindRes = core.model.Resource.get(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
        	SubjectKind kind = SubjectKindImpl.getInstance(kindRes);
        	System.out.println("SubjectKind: "+kind+"; SuperKind: "+kind.getSuperKind());
        	for(ObjectAPI<String, String> extent : concept.getExtent()) {
        		core.model.Resource inst = core.model.Resource.get(extent.getObjectID().toString());
        		kind.getInstances().add(inst);
        		System.out.println("Instance: "+inst);
        		for(Attribute<String, String> attribute : concept.getIntent()) {
        			core.model.Resource attr = core.model.Resource.get(attribute.getAttributeID().toString());
        			kind.getAttributes(inst).add(attr);
        			System.out.println("Attribute: "+attr);
        			for(ModelObject obj : ModelObjects.getInstance().getObjects(null, inst, attr, null)) {
        				core.model.Resource val = obj.getResource();
        				kind.getValues(inst, attr).add(val);
        				System.out.println("Value: "+val);
        			}
        		}
        		for(Subject subj : Subjects.getInstance().getSubjects(null, inst, null, null)) {
        			// TODO: Merged Kinds Resource IRIs
        			// kind.setResource(inst);
        			subj.setKind(kind);
        			// kind.setKind(kind); TODO: Super Kind subset of this Concept Attributes.
        		}
        	}
        	System.out.println();
        	//System.out.println(concept.getExtent().stream().map(ObjectAPI::getObjectID).collect(Collectors.toList())+";");
            //System.out.println(concept.getIntent().stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"\n");
        	for(Concept<String, String> concept2 : concepts) {
        		if(concept2.getExtent().equals(concept.getExtent()) && concept2.getIntent().equals(concept.getIntent()))
        			continue;
        		if(Computation.subsumes(concept2, concept)) {
                	core.model.Resource sKindRes = core.model.Resource.get(concept2.getExtent().hashCode() + ":" + concept2.getIntent().hashCode());
                	SubjectKind skind = SubjectKindImpl.getInstance(sKindRes);
                	kind.setSuperKind(skind);
        		}
        	}
        }
		
        System.out.println();
        
        for(SubjectKind kind : SubjectKinds.getInstance().getSubjectKinds(null, null, null, null)) {
        	System.out.println("KIND: "+kind+"; SUPERKIND: "+kind.getSuperKind());
        	for(core.model.Resource inst : kind.getInstances()) {
        		System.out.println("INSTANCE: "+inst);
        		for(core.model.Resource attr : kind.getAttributes(inst)) {
        			System.out.println("ATTRIBUTE:"+attr);
        			for(core.model.Resource value : kind.getValues(inst, attr)) {
        				System.out.println("VALUE:"+value);
        			}
        		}
        	}
            System.out.println();
        }
        
        System.out.println();
        System.out.println("STATEMENTS: "+Statements.getInstance().getStatements().size());
        for(Statement stat : Statements.getInstance().getStatements()) {
        	System.out.println("Statement: "+stat);
        }
        
        System.out.println();
        System.out.println("KINDSTATEMENTS: "+KindStatements.getInstance().getKindStatements(null, null, null, null).size());
        for(KindStatement kstat : KindStatements.getInstance().getKindStatements(null, null, null, null)) {
        	System.out.println("KindStatement: "+kstat.hashCode());
        }
        
		return KindStatements.getInstance().getKindStatements(null, null, null, null);
	}
	
	public Set<KindStatement> performPropertyKindsAggregation() {

		System.out.println("PROPERTIES");
		
		fcalib.api.fca.Context<String, String> fcaContext = new FCAFormalContext<String, String>() {};
		for(PropertyKind kind : PropertyKinds.getInstance().getPropertyKinds(null, null, null, null)) {
			for(core.model.Resource inst : kind.getInstances()) {
				ObjectAPI<String,String> ob1 = new FCAObject<>(inst.getIRI());
				for(core.model.Resource attr : kind.getAttributes(inst)) {
					Attribute<String,String> atr1 = new FCAAttribute<>(attr.getIRI());
					ob1.addAttribute(attr.getIRI());
					atr1.addObject(inst.getIRI());
				}
				fcaContext.addObject(ob1);
			}
		}
				
		System.out.println("DONE FCA");
		
		OutputPrinter.printCrosstableToConsole(fcaContext);
		OutputPrinter.printConceptsToConsole(fcaContext);
		OutputPrinter.printStemBaseToConsole(fcaContext);
		
		for(Implication<String,String> impl : Computation.computeStemBase(fcaContext)){
			List<ObjectAPI<String, String>> list = Computation.computePrimeOfAttributes(impl.getPremise(), fcaContext);
			for(ObjectAPI<String, String> obj : list) {
				for(Attribute<String, String> attr : impl.getConclusion()) {
					obj.addAttribute(attr.getAttributeID());
				}
			}
			// TODO: Association Rule Mining. Assert Attributes by Support / Confidence.
            System.out.println("Support: "+impl.toString()+": "+Computation.computeImplicationSupport(impl, fcaContext));
            System.out.println("Confidence: "+impl.toString()+": "+Computation.computeConfidence(impl, fcaContext));
        }
		
		// Attributes Objects has in common
		// Computation.computePrimeOfObjects(null, null);
		
		// Objects Attributes has in common.
		// Computation.computePrimeOfAttributes(null, null);
		
		System.out.println("DONE PRINT");
		
        List<List<Attribute<String,String>>> closures = Computation.computeAllClosures(fcaContext);
        List<Concept<String,String>> concepts = Computation.computeAllConcepts(closures, fcaContext);
        for(Concept<String,String> concept : concepts) {
        	// TODO: Merged Kinds Resource IRIs
        	core.model.Resource kindRes = core.model.Resource.get(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
        	PropertyKind kind = PropertyKindImpl.getInstance(kindRes);
        	System.out.println("PropertyKind: "+kind);
        	for(ObjectAPI<String, String> extent : concept.getExtent()) {
        		core.model.Resource inst = core.model.Resource.get(extent.getObjectID().toString());
        		kind.getInstances().add(inst);
        		System.out.println("Instance: "+inst);
        		for(Attribute<String, String> attribute : concept.getIntent()) {
        			core.model.Resource attr = core.model.Resource.get(attribute.getAttributeID().toString());
        			kind.getAttributes(inst).add(attr);
        			System.out.println("Attribute: "+attr);
        			for(ModelObject obj : ModelObjects.getInstance().getObjects(null, attr, inst, null)) {
        				core.model.Resource val = obj.getResource();
        				kind.getValues(inst, attr).add(val);
        				System.out.println("Value: "+val);
        			}
        		}
        		for(Property prop : Properties.getInstance().getProperties(null, null, inst, null)) {
        			// TODO: Merged Kinds Resource IRIs
        			// kind.setResource(inst);
        			prop.setKind(kind);
        			// kind.setKind(kind); TODO: Super Kind subset of this Concept Attributes.
        		}
        	}
        	//System.out.println(concept.getExtent().stream().map(ObjectAPI::getObjectID).collect(Collectors.toList())+";");
            System.out.println(concept.getIntent().stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"\n");
        }
		
        System.out.println();
        
        for(PropertyKind kind : PropertyKinds.getInstance().getPropertyKinds(null, null, null, null)) {
        	System.out.println("KIND: "+kind);
        	for(core.model.Resource inst : kind.getInstances()) {
        		System.out.println("INSTANCE: "+inst);
        		for(core.model.Resource attr : kind.getAttributes(inst)) {
        			System.out.println("ATTRIBUTE:"+attr);
        			for(core.model.Resource value : kind.getValues(inst, attr)) {
        				System.out.println("VALUE:"+value);
        			}
        		}
        	}
        }
        
		return KindStatements.getInstance().getKindStatements(null, null, null, null);
	}

	public Set<KindStatement> performObjectKindsAggregation() {

		System.out.println("OBJECTS");
		
		fcalib.api.fca.Context<String, String> fcaContext = new FCAFormalContext<String, String>() {};
		for(ModelObjectKind kind : ModelObjectKinds.getInstance().getObjectKinds(null, null, null, null)) {
			for(core.model.Resource inst : kind.getInstances()) {
				ObjectAPI<String,String> ob1 = new FCAObject<>(inst.getIRI());
				for(core.model.Resource attr : kind.getAttributes(inst)) {
					Attribute<String,String> atr1 = new FCAAttribute<>(attr.getIRI());
					ob1.addAttribute(attr.getIRI());
					atr1.addObject(inst.getIRI());
				}
				fcaContext.addObject(ob1);
			}
		}
				
		System.out.println("DONE FCA");
		
		OutputPrinter.printCrosstableToConsole(fcaContext);
		OutputPrinter.printConceptsToConsole(fcaContext);
		OutputPrinter.printStemBaseToConsole(fcaContext);
		
		for(Implication<String,String> impl : Computation.computeStemBase(fcaContext)){
			List<ObjectAPI<String, String>> list = Computation.computePrimeOfAttributes(impl.getPremise(), fcaContext);
			for(ObjectAPI<String, String> obj : list) {
				for(Attribute<String, String> attr : impl.getConclusion()) {
					obj.addAttribute(attr.getAttributeID());
				}
			}
			// TODO: Association Rule Mining. Assert Attributes by Support / Confidence.
            System.out.println("Support: "+impl.toString()+": "+Computation.computeImplicationSupport(impl, fcaContext));
            System.out.println("Confidence: "+impl.toString()+": "+Computation.computeConfidence(impl, fcaContext));
        }
		
		// Attributes Objects has in common
		// Computation.computePrimeOfObjects(null, null);
		
		// Objects Attributes has in common.
		// Computation.computePrimeOfAttributes(null, null);
		
		System.out.println("DONE PRINT");
		
        List<List<Attribute<String,String>>> closures = Computation.computeAllClosures(fcaContext);
        List<Concept<String,String>> concepts = Computation.computeAllConcepts(closures, fcaContext);
        for(Concept<String,String> concept : concepts) {
        	// TODO: Merged Kinds Resource IRIs
        	core.model.Resource kindRes = core.model.Resource.get(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
        	ModelObjectKind kind = ModelObjectKindImpl.getInstance(kindRes);
        	System.out.println("ObjectKind: "+kind);
        	for(ObjectAPI<String, String> extent : concept.getExtent()) {
        		core.model.Resource inst = core.model.Resource.get(extent.getObjectID().toString());
        		kind.getInstances().add(inst);
        		System.out.println("Instance: "+inst);
        		for(Attribute<String, String> attribute : concept.getIntent()) {
        			core.model.Resource attr = core.model.Resource.get(attribute.getAttributeID().toString());
        			kind.getAttributes(inst).add(attr);
        			System.out.println("Attribute: "+attr);
        			for(Subject subj : Subjects.getInstance().getSubjects(null, null, attr, inst)) {
        				core.model.Resource val = subj.getResource();
        				kind.getValues(inst, attr).add(val);
        				System.out.println("Value: "+val);
        			}
        		}
        		for(ModelObject obj : ModelObjects.getInstance().getObjects(null, null, null, inst)) {
        			// TODO: Merged Kinds Resource IRIs
        			// kind.setResource(inst);
        			obj.setKind(kind);
        			// kind.setKind(kind); TODO: Super Kind subset of this Concept Attributes.
        		}
        	}
        	//System.out.println(concept.getExtent().stream().map(ObjectAPI::getObjectID).collect(Collectors.toList())+";");
            System.out.println(concept.getIntent().stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"\n");
        }
		
        System.out.println();
        
        for(ModelObjectKind kind : ModelObjectKinds.getInstance().getObjectKinds(null, null, null, null)) {
        	System.out.println("KIND: "+kind);
        	for(core.model.Resource inst : kind.getInstances()) {
        		System.out.println("INSTANCE: "+inst);
        		for(core.model.Resource attr : kind.getAttributes(inst)) {
        			System.out.println("ATTRIBUTE:"+attr);
        			for(core.model.Resource value : kind.getValues(inst, attr)) {
        				System.out.println("VALUE:"+value);
        			}
        		}
        	}
        }
        
		return KindStatements.getInstance().getKindStatements(null, null, null, null);
	}
	
}
