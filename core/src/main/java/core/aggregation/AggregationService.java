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
			Context context = new ContextImpl(core.model.Resource.getResource(ctx));
			
			String sub = st.getSubject().stringValue();
			Subject subject = new SubjectImpl(core.model.Resource.getResource(sub));
			
			String pred = st.getPredicate().stringValue();
			Property property = new PropertyImpl(core.model.Resource.getResource(pred));
			
			// FIXME: Handle Literals
			String obj = st.getObject().isLiteral() ? ((Literal)st.getObject()).stringValue() : st.getObject().stringValue();
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
        	System.out.println("SubjectKind: "+kind+"; SuperKinds: " + kind.getSuperKinds() +"; SubKinds: "+kind.getSubKinds());

        	for(ObjectAPI<String, String> extent : concept.getExtent()) {
        		for(Attribute<String, String> attribute : concept.getIntent()) {
        			for(Statement stat : Statements.getInstance().getStatements(null, extent.getObjectID(), attribute.getAttributeID(), null)) {
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
        	System.out.println("KIND: "+kind+"; SUPERKIND: "+kind.getSuperKinds());
        	for(core.model.Resource inst : kind.getInstancesResources()) {
        		System.out.println("INSTANCE: "+inst);
        		for(core.model.Resource attr : kind.getAttributesResources(inst)) {
        			System.out.println("ATTRIBUTE:"+attr);
        			for(core.model.Resource value : kind.getValuesResources(inst, attr)) {
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
        
		return Statements.getInstance().getStatements();
	}
	
	// TODO: Implement for Context, Property, ModelObject Kinds.
	
}
