package core.aggregation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.spring.support.RDF4JTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import core.model.Context;
import core.model.ContextImpl;
import core.model.ContextKind;
import core.model.ContextKindImpl;
import core.model.ContextKinds;
import core.model.Contexts;
import core.model.Kind;
import core.model.Subject;
import core.model.SubjectImpl;
import core.model.SubjectKind;
import core.model.SubjectKindImpl;
import core.model.SubjectKinds;
import core.model.Subjects;
import core.model.dto.KindAttribute;
import core.model.dto.KindInstance;
import core.model.dto.KindValue;
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
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import reactor.core.publisher.Flux;
import core.model.Property;
import core.model.PropertyImpl;
import core.model.PropertyKind;
import core.model.PropertyKindImpl;
import core.model.PropertyKinds;
import core.model.ResourceOccurrence;
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
	
	private fcalib.api.fca.Context<String, String> contextsContext;
	private fcalib.api.fca.Context<String, String> subjectsContext;
	private fcalib.api.fca.Context<String, String> propertiesContext;
	private fcalib.api.fca.Context<String, String> objectsContext;
	
	public AggregationService(@Autowired RDF4JTemplate rdf4jTemplate) {
		this.rdf4jTemplate = rdf4jTemplate;
	}

	public fcalib.api.fca.Context<String, String> getContextsContext() {
		return contextsContext;
	}

	public void setContextsContext(fcalib.api.fca.Context<String, String> contextsContext) {
		this.contextsContext = contextsContext;
	}

	public fcalib.api.fca.Context<String, String> getSubjectsContext() {
		return subjectsContext;
	}

	public void setSubjectsContext(fcalib.api.fca.Context<String, String> subjectsContext) {
		this.subjectsContext = subjectsContext;
	}

	public fcalib.api.fca.Context<String, String> getPropertiesContext() {
		return propertiesContext;
	}

	public void setPropertiesContext(fcalib.api.fca.Context<String, String> propertiesContext) {
		this.propertiesContext = propertiesContext;
	}

	public fcalib.api.fca.Context<String, String> getObjectsContext() {
		return objectsContext;
	}

	public void setObjectsContext(fcalib.api.fca.Context<String, String> objectsContext) {
		this.objectsContext = objectsContext;
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
			sparqlQuery = "SELECT ?s ?p ?o ?type WHERE { $s $p $o. ?s rdf:type ?type. FILTER (!regex(str(?type), \"rdf-\")). }";
		}
		
		Set<BindingSet> model = rdf4jTemplate.tupleQuery(sparqlQuery)
									.evaluateAndConvert()
									.toSet(bs -> bs);

		Statements.getInstance().getStatements().clear();
		
		System.out.println("MODEL SIZE:" + model.size());
		
		for(BindingSet st : model) {
			
			// FIXME: Initial Context
			String ctxStr = st.getValue("type") != null ? st.getValue("type").stringValue() : null;
			Context context = new ContextImpl(core.model.Resource.getResource(ctxStr));
			
			String sub = st.getValue("s").stringValue();
			Subject subject = new SubjectImpl(core.model.Resource.getResource(sub));
			
			String pred = st.getValue("p").stringValue();
			Property property = new PropertyImpl(core.model.Resource.getResource(pred));
			
			// FIXME: Handle Literals
			String obj = st.getValue("o").isLiteral() ? ((Literal)st.getValue("o")).stringValue() : st.getValue("o").stringValue();
			ModelObject object = new ModelObjectImpl(core.model.Resource.getResource(obj));

			// FIXME: Default initial Kinds. In the beginning each CSPO has its own Kind.
			//        They are later aggregated.
			
			ContextKind ck = ContextKindImpl.getInstance(context.getResource());
			context.setKind(ck);
			
			SubjectKind sk = SubjectKindImpl.getInstance(subject.getResource());
			subject.setKind(sk);
			
			PropertyKind pk = PropertyKindImpl.getInstance(property.getResource());
			property.setKind(pk);

			ModelObjectKind ok = ModelObjectKindImpl.getInstance(object.getResource());
			object.setKind(ok);

			System.out.println("Statement Context: "+context.getResource().getIRI());
			Statement stat = Statements.getInstance().addStatement(context, subject, property, object);
			
			ck.getInstances().add(context);
			ck.getAttributes(context).add(property);
			ck.getValues(context, property).add(object);
			
			sk.getInstances().add(subject);
			sk.getAttributes(subject).add(property);
			sk.getValues(subject, property).add(object);
			
			pk.getInstances().add(property);
			pk.getAttributes(property).add(subject);
			pk.getValues(property, subject).add(object);
			
			ok.getInstances().add(object);
			ok.getAttributes(object).add(property);
			ok.getValues(object, property).add(subject);
		}
	}

	public Set<Statement> performContextKindsAggregation() {

		System.out.println("CONTEXTS");
		
		fcalib.api.fca.Context<String, String> fcaContext = new FCAFormalContext<String, String>() {};
		for(ContextKind kind : ContextKinds.getInstance().getContextKinds(null, null, null, null)) {
			for(core.model.Resource inst : kind.getInstancesResources()) {
				ObjectAPI<String, String> ob1 = new FCAObject<>(inst.getIRI());
				System.out.println("FCA Object: " + ob1.getObjectID());
				for(core.model.Resource attr : kind.getAttributesResources(inst)) {
					Attribute<String, String> atr1 = new FCAAttribute<>(attr.getIRI());
					System.out.println("FCA Attribute: "+atr1.getAttributeID());
					ob1.addAttribute(attr.getIRI());
					atr1.addObject(inst.getIRI());
				}
				fcaContext.addObject(ob1);
			}
		}
		
		this.contextsContext = fcaContext;
		
		// Computation.reduceContext(fcaContext);

//		System.out.println("CONCEPTS ASSOCIATION RULE MINING IMPLICATIONS");
//		
//        List<List<Attribute<String,String>>> closures2 = Computation.computeAllClosures(fcaContext);
//        List<Concept<String,String>> concepts2 = Computation.computeAllConcepts(closures2, fcaContext);
//        List<Implication<String, String>> implications2 = Computation.computeConceptsImplications(concepts2, fcaContext);
//        for(Implication<String, String> impl : implications2) {
//        	System.out.println(impl);
//        	List<ObjectAPI<String, String>> listObj = Computation.computePrimeOfAttributes(impl.getPremise(), fcaContext);
//        	for(ObjectAPI<String, String> obj : listObj) {
//        		for(Attribute<String, String> attr : impl.getConclusion()) {
//        			obj.addAttribute(attr.getAttributeID());
//        			attr.addObject(obj.getObjectID());
//        		}
//        	}
//        }
		
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
		
		System.out.println("Populating Kinds:");

        List<List<Attribute<String, String>>> closures = Computation.computeAllClosures(fcaContext);
        List<Concept<String, String>> concepts = Computation.computeAllConcepts(closures, fcaContext);
        for(Concept<String, String> concept : concepts) {
        	
        	System.out.println(concept);
        	System.out.println(concept.getExtent().stream().map(ObjectAPI::getObjectID).collect(Collectors.toList())+";");
            System.out.println(concept.getIntent().stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"\n");
        	// TODO: Merged Kinds Resource IRIs (Primes product?)
            core.model.Resource kindRes = core.model.Resource.getResource(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
        	ContextKind kind = ContextKindImpl.getInstance(kindRes);
        	kind.setConcept(concept);
        	System.out.println("ContextKind: "+kind+"; SuperKinds: " + kind.getSuperKinds() +"; SubKinds: "+kind.getSubKinds());

        	for(ObjectAPI<String, String> extent : concept.getExtent()) {
        		for(Attribute<String, String> attribute : concept.getIntent()) {
        			for(Statement stat : Statements.getInstance().getStatements(extent.getObjectID(), null, attribute.getAttributeID(), null)) {
        				kind.getResource().getResourceOccurrences().add(stat.getContext());
        				stat.getContext().setKind(kind);
        				kind.getInstances().add(stat.getContext());
        				kind.getAttributes(stat.getContext()).add(stat.getProperty());
        				kind.getValues(stat.getContext(), stat.getProperty()).add(stat.getObject());
        			}
        		}
        	}
        	
        	System.out.println();

        	for(Concept<String, String> concept2 : concepts) {
        		if(concept2.getExtent().equals(concept.getExtent()) && concept2.getIntent().equals(concept.getIntent()))
        			continue;
        		if(Computation.subsumes(concept2, concept)) {
                	core.model.Resource sKindRes = core.model.Resource.getResource(concept2.getExtent().hashCode() + ":" + concept2.getIntent().hashCode());
                	ContextKind skind = ContextKindImpl.getInstance(sKindRes);
                	kind.getSuperKinds().add(skind);
        		} else if(Computation.subsumes(concept, concept2)) {
                	core.model.Resource sKindRes = core.model.Resource.getResource(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
                	ContextKind skind = ContextKindImpl.getInstance(sKindRes);
                	kind.getSubKinds().add(skind);
        		}
        	}
        }
		
        System.out.println("Populated Kinds:");
        
        for(ContextKind kind : ContextKinds.getInstance().getContextKinds(null, null, null, null)) {
        	System.out.println("KIND: "+kind+"; SUPERKINDS: "+kind.getSuperKinds()+"; SUBKINDS: "+kind.getSubKinds());
        	for(core.model.Resource inst : kind.getInstancesResources()) {
        		System.out.println("INSTANCE: "+inst);
        		for(core.model.Resource attr : kind.getAttributesResources(inst)) {
        			System.out.println("ATTRIBUTE: "+attr);
        			for(core.model.Resource value : kind.getValuesResources(inst, attr)) {
        				System.out.println("VALUE: "+value);
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
        
		return Statements.getInstance().getStatements();
	}
	
	public Set<Statement> performSubjectKindsAggregation() {

		System.out.println("SUBJECTS");
		
		fcalib.api.fca.Context<String, String> fcaContext = new FCAFormalContext<String, String>() {};
		for(SubjectKind kind : SubjectKinds.getInstance().getSubjectKinds(null, null, null, null)) {
			for(core.model.Resource inst : kind.getInstancesResources()) {
				ObjectAPI<String, String> ob1 = new FCAObject<>(inst.getIRI());
				System.out.println("FCA Object: " + ob1.getObjectID());
				for(core.model.Resource attr : kind.getAttributesResources(inst)) {
					Attribute<String, String> atr1 = new FCAAttribute<>(attr.getIRI());
					System.out.println("FCA Attribute: "+atr1.getAttributeID());
					ob1.addAttribute(attr.getIRI());
					atr1.addObject(inst.getIRI());
				}
				fcaContext.addObject(ob1);
			}
		}
		
		this.subjectsContext = fcaContext;
		
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
		
		System.out.println("FCALib2 Implications: ");
		for(Implication<String,String> impl : Computation.computeStemBase(fcaContext)) {
			List<ObjectAPI<String, String>> list = Computation.computePrimeOfAttributes(impl.getPremise(), fcaContext);
			for(ObjectAPI<String, String> obj : list) {
				System.out.println("Implication object: "+obj.getObjectID());
				for(Attribute<String, String> attr : impl.getConclusion()) {
					obj.addAttribute(attr.getAttributeID());
					attr.addObject(obj.getObjectID());
					System.out.println("Implication addAttribute "+attr.getAttributeID());
				}
			}
			// TODO: Association Rule Mining. Assert Attributes by Support / Confidence.
			System.out.println("Implication: "+impl);
        }
		
		// Attributes Objects has in common
		// Computation.computePrimeOfObjects(null, null);
		
		// Objects Attributes has in common.
		// Computation.computePrimeOfAttributes(null, null);

		OutputPrinter.printCrosstableToConsole(fcaContext);
		OutputPrinter.printConceptsToConsole(fcaContext);
		OutputPrinter.printStemBaseToConsole(fcaContext);
		
		System.out.println("Populating Kinds:");

        List<List<Attribute<String, String>>> closures = Computation.computeAllClosures(fcaContext);
        List<Concept<String, String>> concepts = Computation.computeAllConcepts(closures, fcaContext);
        for(Concept<String, String> concept : concepts) {
        	
        	System.out.println(concept);
        	System.out.println(concept.getExtent().stream().map(ObjectAPI::getObjectID).collect(Collectors.toList())+";");
            System.out.println(concept.getIntent().stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"\n");
        	// TODO: Merged Kinds Resource IRIs (Primes product?)
            core.model.Resource kindRes = core.model.Resource.getResource(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
        	SubjectKind kind = SubjectKindImpl.getInstance(kindRes);
        	kind.setConcept(concept);
        	System.out.println("SubjectKind: "+kind+"; SuperKinds: " + kind.getSuperKinds() +"; SubKinds: "+kind.getSubKinds());

        	for(ObjectAPI<String, String> extent : concept.getExtent()) {
        		for(Attribute<String, String> attribute : concept.getIntent()) {
        			for(Statement stat : Statements.getInstance().getStatements(null, extent.getObjectID(), attribute.getAttributeID(), null)) {
        				kind.getResource().getResourceOccurrences().add(stat.getSubject());
        				stat.getSubject().setKind(kind);
        				kind.getInstances().add(stat.getSubject());
        				kind.getAttributes(stat.getSubject()).add(stat.getProperty());
        				kind.getValues(stat.getSubject(), stat.getProperty()).add(stat.getObject());
        			}
        		}
        	}
        	
        	System.out.println();

        	for(Concept<String, String> concept2 : concepts) {
        		if(concept2.getExtent().equals(concept.getExtent()) && concept2.getIntent().equals(concept.getIntent()))
        			continue;
        		if(Computation.subsumes(concept2, concept)) {
                	core.model.Resource sKindRes = core.model.Resource.getResource(concept2.getExtent().hashCode() + ":" + concept2.getIntent().hashCode());
                	SubjectKind skind = SubjectKindImpl.getInstance(sKindRes);
                	kind.getSuperKinds().add(skind);
        		} else if(Computation.subsumes(concept, concept2)) {
                	core.model.Resource sKindRes = core.model.Resource.getResource(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
                	SubjectKind skind = SubjectKindImpl.getInstance(sKindRes);
                	kind.getSubKinds().add(skind);
        		}
        	}
        }
		
        System.out.println("Populated Kinds:");
        
        for(SubjectKind kind : SubjectKinds.getInstance().getSubjectKinds(null, null, null, null)) {
        	System.out.println("KIND: "+kind+"; SUPERKINDS: "+kind.getSuperKinds()+"; SUBKINDS: "+kind.getSubKinds());
        	for(core.model.Resource inst : kind.getInstancesResources()) {
        		System.out.println("INSTANCE: "+inst);
        		for(core.model.Resource attr : kind.getAttributesResources(inst)) {
        			System.out.println("ATTRIBUTE: "+attr);
        			for(core.model.Resource value : kind.getValuesResources(inst, attr)) {
        				System.out.println("VALUE: "+value);
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
        
		return Statements.getInstance().getStatements();
	}

	public Set<Statement> performPropertyKindsAggregation() {

		System.out.println("PROPERTIES");
		
		fcalib.api.fca.Context<String, String> fcaContext = new FCAFormalContext<String, String>() {};
		for(PropertyKind kind : PropertyKinds.getInstance().getPropertyKinds(null, null, null, null)) {
			for(core.model.Resource inst : kind.getInstancesResources()) {
				ObjectAPI<String, String> ob1 = new FCAObject<>(inst.getIRI());
				System.out.println("FCA Object: " + ob1.getObjectID());
				for(core.model.Resource attr : kind.getAttributesResources(inst)) {
					Attribute<String, String> atr1 = new FCAAttribute<>(attr.getIRI());
					System.out.println("FCA Attribute: "+atr1.getAttributeID());
					ob1.addAttribute(attr.getIRI());
					atr1.addObject(inst.getIRI());
				}
				fcaContext.addObject(ob1);
			}
		}
		
		this.propertiesContext = fcaContext;
		
		// Computation.reduceContext(fcaContext);

//		System.out.println("CONCEPTS ASSOCIATION RULE MINING IMPLICATIONS");
//		
//        List<List<Attribute<String,String>>> closures2 = Computation.computeAllClosures(fcaContext);
//        List<Concept<String,String>> concepts2 = Computation.computeAllConcepts(closures2, fcaContext);
//        List<Implication<String, String>> implications2 = Computation.computeConceptsImplications(concepts2, fcaContext);
//        for(Implication<String, String> impl : implications2) {
//        	System.out.println(impl);
//        	List<ObjectAPI<String, String>> listObj = Computation.computePrimeOfAttributes(impl.getPremise(), fcaContext);
//        	for(ObjectAPI<String, String> obj : listObj) {
//        		for(Attribute<String, String> attr : impl.getConclusion()) {
//        			obj.addAttribute(attr.getAttributeID());
//        			attr.addObject(obj.getObjectID());
//        		}
//        	}
//        }
		
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
		
		System.out.println("Populating Kinds:");

        List<List<Attribute<String, String>>> closures = Computation.computeAllClosures(fcaContext);
        List<Concept<String, String>> concepts = Computation.computeAllConcepts(closures, fcaContext);
        for(Concept<String, String> concept : concepts) {
        	
        	System.out.println(concept);
        	System.out.println(concept.getExtent().stream().map(ObjectAPI::getObjectID).collect(Collectors.toList())+";");
            System.out.println(concept.getIntent().stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"\n");
        	// TODO: Merged Kinds Resource IRIs (Primes product?)
            core.model.Resource kindRes = core.model.Resource.getResource(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
        	PropertyKind kind = PropertyKindImpl.getInstance(kindRes);
        	kind.setConcept(concept);
        	System.out.println("PropertyKind: "+kind+"; SuperKinds: " + kind.getSuperKinds() +"; SubKinds: "+kind.getSubKinds());

        	for(ObjectAPI<String, String> extent : concept.getExtent()) {
        		for(Attribute<String, String> attribute : concept.getIntent()) {
        			for(Statement stat : Statements.getInstance().getStatements(null, attribute.getAttributeID(), extent.getObjectID(), null)) {
        				kind.getResource().getResourceOccurrences().add(stat.getProperty());
        				stat.getProperty().setKind(kind);
        				kind.getInstances().add(stat.getProperty());
        				kind.getAttributes(stat.getProperty()).add(stat.getSubject());
        				kind.getValues(stat.getProperty(), stat.getSubject()).add(stat.getObject());
        			}
        		}
        	}
        	
        	System.out.println();

        	for(Concept<String, String> concept2 : concepts) {
        		if(concept2.getExtent().equals(concept.getExtent()) && concept2.getIntent().equals(concept.getIntent()))
        			continue;
        		if(Computation.subsumes(concept2, concept)) {
                	core.model.Resource sKindRes = core.model.Resource.getResource(concept2.getExtent().hashCode() + ":" + concept2.getIntent().hashCode());
                	PropertyKind skind = PropertyKindImpl.getInstance(sKindRes);
                	kind.getSuperKinds().add(skind);
        		} else if(Computation.subsumes(concept, concept2)) {
                	core.model.Resource sKindRes = core.model.Resource.getResource(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
                	PropertyKind skind = PropertyKindImpl.getInstance(sKindRes);
                	kind.getSubKinds().add(skind);
        		}
        	}
        }
		
        System.out.println("Populated Kinds:");
        
        for(PropertyKind kind : PropertyKinds.getInstance().getPropertyKinds(null, null, null, null)) {
        	System.out.println("KIND: "+kind+"; SUPERKINDS: "+kind.getSuperKinds()+"; SUBKINDS: "+kind.getSubKinds());
        	for(core.model.Resource inst : kind.getInstancesResources()) {
        		System.out.println("INSTANCE: "+inst);
        		for(core.model.Resource attr : kind.getAttributesResources(inst)) {
        			System.out.println("ATTRIBUTE: "+attr);
        			for(core.model.Resource value : kind.getValuesResources(inst, attr)) {
        				System.out.println("VALUE: "+value);
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
        
		return Statements.getInstance().getStatements();
	}
	
	public Set<Statement> performModelObjectKindsAggregation() {

		System.out.println("OBJECTS");
		
		fcalib.api.fca.Context<String, String> fcaContext = new FCAFormalContext<String, String>() {};
		for(ModelObjectKind kind : ModelObjectKinds.getInstance().getObjectKinds(null, null, null, null)) {
			for(core.model.Resource inst : kind.getInstancesResources()) {
				ObjectAPI<String, String> ob1 = new FCAObject<>(inst.getIRI());
				System.out.println("FCA Object: " + ob1.getObjectID());
				for(core.model.Resource attr : kind.getAttributesResources(inst)) {
					Attribute<String, String> atr1 = new FCAAttribute<>(attr.getIRI());
					System.out.println("FCA Attribute: "+atr1.getAttributeID());
					ob1.addAttribute(attr.getIRI());
					atr1.addObject(inst.getIRI());
				}
				fcaContext.addObject(ob1);
			}
		}
		
		this.objectsContext = fcaContext;
		
		// Computation.reduceContext(fcaContext);

//		System.out.println("CONCEPTS ASSOCIATION RULE MINING IMPLICATIONS");
//		
//        List<List<Attribute<String,String>>> closures2 = Computation.computeAllClosures(fcaContext);
//        List<Concept<String,String>> concepts2 = Computation.computeAllConcepts(closures2, fcaContext);
//        List<Implication<String, String>> implications2 = Computation.computeConceptsImplications(concepts2, fcaContext);
//        for(Implication<String, String> impl : implications2) {
//        	System.out.println(impl);
//        	List<ObjectAPI<String, String>> listObj = Computation.computePrimeOfAttributes(impl.getPremise(), fcaContext);
//        	for(ObjectAPI<String, String> obj : listObj) {
//        		for(Attribute<String, String> attr : impl.getConclusion()) {
//        			obj.addAttribute(attr.getAttributeID());
//        			attr.addObject(obj.getObjectID());
//        		}
//        	}
//        }
		
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
		
		System.out.println("Populating Kinds:");

        List<List<Attribute<String, String>>> closures = Computation.computeAllClosures(fcaContext);
        List<Concept<String, String>> concepts = Computation.computeAllConcepts(closures, fcaContext);
        for(Concept<String, String> concept : concepts) {
        	
        	System.out.println(concept);
        	System.out.println(concept.getExtent().stream().map(ObjectAPI::getObjectID).collect(Collectors.toList())+";");
            System.out.println(concept.getIntent().stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"\n");
        	// TODO: Merged Kinds Resource IRIs (Primes product?)
            core.model.Resource kindRes = core.model.Resource.getResource(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
        	ModelObjectKind kind = ModelObjectKindImpl.getInstance(kindRes);
        	kind.setConcept(concept);
        	System.out.println("ModelObjectKind: "+kind+"; SuperKinds: " + kind.getSuperKinds() +"; SubKinds: "+kind.getSubKinds());

        	for(ObjectAPI<String, String> extent : concept.getExtent()) {
        		for(Attribute<String, String> attribute : concept.getIntent()) {
        			for(Statement stat : Statements.getInstance().getStatements(null, null, attribute.getAttributeID(), extent.getObjectID())) {
        				kind.getResource().getResourceOccurrences().add(stat.getObject());
        				stat.getObject().setKind(kind);
        				kind.getInstances().add(stat.getObject());
        				kind.getAttributes(stat.getObject()).add(stat.getProperty());
        				kind.getValues(stat.getObject(), stat.getProperty()).add(stat.getSubject());
        			}
        		}
        	}
        	
        	System.out.println();

        	for(Concept<String, String> concept2 : concepts) {
        		if(concept2.getExtent().equals(concept.getExtent()) && concept2.getIntent().equals(concept.getIntent()))
        			continue;
        		if(Computation.subsumes(concept2, concept)) {
                	core.model.Resource sKindRes = core.model.Resource.getResource(concept2.getExtent().hashCode() + ":" + concept2.getIntent().hashCode());
                	ModelObjectKind skind = ModelObjectKindImpl.getInstance(sKindRes);
                	kind.getSuperKinds().add(skind);
        		} else if(Computation.subsumes(concept, concept2)) {
                	core.model.Resource sKindRes = core.model.Resource.getResource(concept.getExtent().hashCode() + ":" + concept.getIntent().hashCode());
                	ModelObjectKind skind = ModelObjectKindImpl.getInstance(sKindRes);
                	kind.getSubKinds().add(skind);
        		}
        	}
        }
		
        System.out.println("Populated Kinds:");
        
        for(ModelObjectKind kind : ModelObjectKinds.getInstance().getObjectKinds(null, null, null, null)) {
        	System.out.println("KIND: "+kind+"; SUPERKINDS: "+kind.getSuperKinds()+"; SUBKINDS: "+kind.getSubKinds());
        	for(core.model.Resource inst : kind.getInstancesResources()) {
        		System.out.println("INSTANCE: "+inst);
        		for(core.model.Resource attr : kind.getAttributesResources(inst)) {
        			System.out.println("ATTRIBUTE: "+attr);
        			for(core.model.Resource value : kind.getValuesResources(inst, attr)) {
        				System.out.println("VALUE: "+value);
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
        
		return Statements.getInstance().getStatements();
	}

	@Autowired
	XmlMapper mapper;
	
	public String marshallStatements() throws JAXBException, IOException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		core.model.dto.Statements stats = new core.model.dto.Statements();
		for(Statement stat : Statements.getInstance().getStatements()) {
			
			core.model.dto.Statement aStat = new core.model.dto.Statement();
			
			core.model.dto.Context context = new core.model.dto.Context();
			core.model.dto.Resource contextRes = new core.model.dto.Resource();
			contextRes.setIRI(stat.getContext().getResource().getIRI());
			core.model.dto.Resource ctxKindRes = new core.model.dto.Resource();
			ctxKindRes.setIRI(stat.getContext().getKind().getResource().getIRI());
			core.model.dto.ContextKind contextKind = new core.model.dto.ContextKind();
			contextKind.setResource(ctxKindRes);
			context.setResource(contextRes);
			context.setContextStatement(aStat);
			context.setKind(contextKind);
			context.getResource().getResourceOccurrences().add(context);
			
			contextKind.getResource().getResourceOccurrences().add(context);
			Set<core.model.dto.KindInstance> instances = new HashSet<core.model.dto.KindInstance>();
			for(core.model.Resource inst : stat.getContext().getKind().getInstancesResources()) {
				core.model.dto.Resource instRes = new core.model.dto.Resource();
				instRes.setIRI(inst.getIRI());
				KindInstance kinst = new KindInstance();
				kinst.setInstance(instRes);
				for(core.model.Resource attr : stat.getContext().getKind().getAttributesResources(inst)) {
					core.model.dto.Resource propRes = new core.model.dto.Resource();
					propRes.setIRI(attr.getIRI());
					KindAttribute kattr = new KindAttribute();
					kattr.setAttribute(propRes);
					kinst.getAttributes().add(kattr);
					for(core.model.Resource val : stat.getContext().getKind().getValuesResources(inst, attr)) {
						core.model.dto.Resource valRes = new core.model.dto.Resource();
						valRes.setIRI(val.getIRI());
						KindValue kval = new KindValue();
						kval.setValue(valRes);
						kattr.getValues().add(kval);
					}
				}
				instances.add(kinst);
			}
			for(Kind kind : stat.getContext().getKind().getSuperKinds()) {
				core.model.dto.Resource kindRes = new core.model.dto.Resource();
				kindRes.setIRI(kind.getResource().getIRI());
				core.model.dto.ContextKind superKind = new core.model.dto.ContextKind();
				superKind.setResource(kindRes);
				contextKind.getSuperKinds().add(superKind);
			}
			for(Kind kind : stat.getContext().getKind().getSubKinds()) {
				core.model.dto.Resource kindRes = new core.model.dto.Resource();
				kindRes.setIRI(kind.getResource().getIRI());
				core.model.dto.ContextKind subKind = new core.model.dto.ContextKind();
				subKind.setResource(kindRes);
				contextKind.getSubKinds().add(subKind);
			}
			contextKind.getInstances().addAll(instances);
			
			core.model.dto.Resource subjKindRes = new core.model.dto.Resource();
			subjKindRes.setIRI(stat.getSubject().getKind().getResource().getIRI());
			core.model.dto.SubjectKind subjectKind = new core.model.dto.SubjectKind();
			subjectKind.setResource(subjKindRes);
			core.model.dto.Subject subject = new core.model.dto.Subject();
			core.model.dto.Resource subjectRes = new core.model.dto.Resource();
			subjectRes.setIRI(stat.getSubject().getResource().getIRI());
			subject.setResource(subjectRes);
			subject.setContextStatement(aStat);
			subject.setKind(subjectKind);
			subject.getResource().getResourceOccurrences().add(subject);
			subjectKind.getResource().getResourceOccurrences().add(subject);
			instances = new HashSet<core.model.dto.KindInstance>();
			for(core.model.Resource inst : stat.getSubject().getKind().getInstancesResources()) {
				core.model.dto.Resource instRes = new core.model.dto.Resource();
				instRes.setIRI(inst.getIRI());
				KindInstance kinst = new KindInstance();
				kinst.setInstance(instRes);
				for(core.model.Resource attr : stat.getSubject().getKind().getAttributesResources(inst)) {
					core.model.dto.Resource propRes = new core.model.dto.Resource();
					propRes.setIRI(attr.getIRI());
					KindAttribute kattr = new KindAttribute();
					kattr.setAttribute(propRes);
					kinst.getAttributes().add(kattr);
					for(core.model.Resource val : stat.getSubject().getKind().getValuesResources(inst, attr)) {
						core.model.dto.Resource valRes = new core.model.dto.Resource();
						valRes.setIRI(val.getIRI());
						KindValue kval = new KindValue();
						kval.setValue(valRes);
						kattr.getValues().add(kval);
					}
				}
				instances.add(kinst);
			}
			for(Kind kind : stat.getSubject().getKind().getSuperKinds()) {
				core.model.dto.Resource kindRes = new core.model.dto.Resource();
				kindRes.setIRI(kind.getResource().getIRI());
				core.model.dto.SubjectKind superKind = new core.model.dto.SubjectKind();
				superKind.setResource(kindRes);
				subjectKind.getSuperKinds().add(superKind);
			}
			for(Kind kind : stat.getSubject().getKind().getSubKinds()) {
				core.model.dto.Resource kindRes = new core.model.dto.Resource();
				kindRes.setIRI(kind.getResource().getIRI());
				core.model.dto.SubjectKind subKind = new core.model.dto.SubjectKind();
				subKind.setResource(kindRes);
				subjectKind.getSubKinds().add(subKind);
			}
			subjectKind.getInstances().addAll(instances);
			
			core.model.dto.Resource propKindRes = new core.model.dto.Resource();
			propKindRes.setIRI(stat.getProperty().getKind().getResource().getIRI());
			core.model.dto.PropertyKind propertyKind = new core.model.dto.PropertyKind();
			propertyKind.setResource(propKindRes);
			core.model.dto.Property property = new core.model.dto.Property();
			core.model.dto.Resource propertyRes = new core.model.dto.Resource();
			propertyRes.setIRI(stat.getProperty().getResource().getIRI());
			property.setResource(propertyRes);
			property.setContextStatement(aStat);
			property.setKind(propertyKind);
			property.getResource().getResourceOccurrences().add(property);
			propertyKind.getResource().getResourceOccurrences().add(property);
			instances = new HashSet<core.model.dto.KindInstance>();
			for(core.model.Resource inst : stat.getProperty().getKind().getInstancesResources()) {
				core.model.dto.Resource instRes = new core.model.dto.Resource();
				instRes.setIRI(inst.getIRI());
				KindInstance kinst = new KindInstance();
				kinst.setInstance(instRes);
				for(core.model.Resource attr : stat.getProperty().getKind().getAttributesResources(inst)) {
					core.model.dto.Resource propRes = new core.model.dto.Resource();
					propRes.setIRI(attr.getIRI());
					KindAttribute kattr = new KindAttribute();
					kattr.setAttribute(propRes);
					kinst.getAttributes().add(kattr);
					for(core.model.Resource val : stat.getProperty().getKind().getValuesResources(inst, attr)) {
						core.model.dto.Resource valRes = new core.model.dto.Resource();
						valRes.setIRI(val.getIRI());
						KindValue kval = new KindValue();
						kval.setValue(valRes);
						kattr.getValues().add(kval);
					}
				}
				instances.add(kinst);
			}
			for(Kind kind : stat.getProperty().getKind().getSuperKinds()) {
				core.model.dto.Resource kindRes = new core.model.dto.Resource();
				kindRes.setIRI(kind.getResource().getIRI());
				core.model.dto.PropertyKind superKind = new core.model.dto.PropertyKind();
				superKind.setResource(kindRes);
				propertyKind.getSuperKinds().add(superKind);
			}
			for(Kind kind : stat.getProperty().getKind().getSubKinds()) {
				core.model.dto.Resource kindRes = new core.model.dto.Resource();
				kindRes.setIRI(kind.getResource().getIRI());
				core.model.dto.PropertyKind subKind = new core.model.dto.PropertyKind();
				subKind.setResource(kindRes);
				propertyKind.getSubKinds().add(subKind);
			}
			propertyKind.getInstances().addAll(instances);
			
			core.model.dto.Resource objKindRes = new core.model.dto.Resource();
			objKindRes.setIRI(stat.getObject().getKind().getResource().getIRI());
			core.model.dto.ModelObjectKind objectKind = new core.model.dto.ModelObjectKind();
			objectKind.setResource(objKindRes);
			core.model.dto.ModelObject object = new core.model.dto.ModelObject();
			core.model.dto.Resource objectRes = new core.model.dto.Resource();
			objectRes.setIRI(stat.getObject().getResource().getIRI());
			object.setResource(objectRes);
			object.setContextStatement(aStat);
			object.setKind(objectKind);
			object.getResource().getResourceOccurrences().add(object);
			objectKind.getResource().getResourceOccurrences().add(object);
			instances = new HashSet<core.model.dto.KindInstance>();
			for(core.model.Resource inst : stat.getObject().getKind().getInstancesResources()) {
				core.model.dto.Resource instRes = new core.model.dto.Resource();
				instRes.setIRI(inst.getIRI());
				KindInstance kinst = new KindInstance();
				kinst.setInstance(instRes);
				for(core.model.Resource attr : stat.getObject().getKind().getAttributesResources(inst)) {
					core.model.dto.Resource propRes = new core.model.dto.Resource();
					propRes.setIRI(attr.getIRI());
					KindAttribute kattr = new KindAttribute();
					kattr.setAttribute(propRes);
					kinst.getAttributes().add(kattr);
					for(core.model.Resource val : stat.getObject().getKind().getValuesResources(inst, attr)) {
						core.model.dto.Resource valRes = new core.model.dto.Resource();
						valRes.setIRI(val.getIRI());
						KindValue kval = new KindValue();
						kval.setValue(valRes);
						kattr.getValues().add(kval);
					}
				}
				instances.add(kinst);
			}
			for(Kind kind : stat.getObject().getKind().getSuperKinds()) {
				core.model.dto.Resource kindRes = new core.model.dto.Resource();
				kindRes.setIRI(kind.getResource().getIRI());
				core.model.dto.ModelObjectKind superKind = new core.model.dto.ModelObjectKind();
				superKind.setResource(kindRes);
				objectKind.getSuperKinds().add(superKind);
			}
			for(Kind kind : stat.getObject().getKind().getSubKinds()) {
				core.model.dto.Resource kindRes = new core.model.dto.Resource();
				kindRes.setIRI(kind.getResource().getIRI());
				core.model.dto.ModelObjectKind subKind = new core.model.dto.ModelObjectKind();
				subKind.setResource(kindRes);
				objectKind.getSubKinds().add(subKind);
			}
			objectKind.getInstances().addAll(instances);
			
			aStat.setContext(context);
			aStat.setSubject(subject);
			aStat.setProperty(property);
			aStat.setObject(object);
			
			stats.getStatement().add(aStat);
		}
		
		return mapper.writeValueAsString(stats);
	}

	public String buildOWLRepresentation() {
		
		// TODO: Use Jena's Ontology APIs
		
		for(Statement stat : Statements.getInstance().getStatements()) {
			
			
			Context context = stat.getContext();
			context.getResource();
			context.getResource().getResourceOccurrences();
			ContextKind contextKind = context.getKind();
			contextKind.getInstances();
			contextKind.getAttributes(null);
			contextKind.getValues(null, null);
			contextKind.getConcept();
			contextKind.getSubKinds();
			contextKind.getSuperKinds();
			contextKind.getResource().getResourceOccurrences();
			
			Subject subject = stat.getSubject();
			Property property = stat.getProperty();
			ModelObject object = stat.getObject();
		
		}
		
		return null;
	}
	
}
