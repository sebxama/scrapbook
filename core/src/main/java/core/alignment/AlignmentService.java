package core.alignment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.rdf4j.spring.support.RDF4JTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.model.KindStatement;
import core.model.KindStatements;
import core.model.ModelObject;
import core.model.ModelObjects;
import core.model.PropertyKindImpl;
import core.model.Statement;
import core.model.Statements;
import core.model.Subject;
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
import fcalib.lib.fca.FCAObject;

/**
 * Infer relationships / properties (link prediction) between Kinds.
 * Uses FCA KindStatement Kinds Instances as FCA Objects, Kinds Attributes
 * as FCA Attributes. TODO: Implications, association rule mining.
 *
 * @returns Aligned Set of <code>KindStatements</code> for Subjects,
 *          Properties and Objects Kinds Alignment.
 */
@Service
public class AlignmentService {

	private RDF4JTemplate rdf4jTemplate;
	
	public AlignmentService(@Autowired RDF4JTemplate rdf4jTemplate) {
		this.rdf4jTemplate = rdf4jTemplate;
	}

	// FIXME: Perform FCA over KindStatement(s) Kinds.
	// Align inferred association rule mining.
	public Set<KindStatement> performSubjectKindsAlignment(Set<KindStatement> stats) {

		System.out.println("SUBJECTS ALIGNMENT");
		
		fcalib.api.fca.Context<String, String> fcaContext = new FCAFormalContext<String, String>() {};
		Map<core.model.Resource, Set<core.model.Resource>> attributes = new HashMap<core.model.Resource, Set<core.model.Resource>>();
		for(KindStatement kind : stats) {
			Set<core.model.Resource> attrs = attributes.get(kind.getSubject().getResource());
			if(attrs == null) {
				attrs = new HashSet<core.model.Resource>();
				attributes.put(kind.getSubject().getResource(), attrs);
			}
			attrs.add(kind.getProperty().getResource());
		}
		
		for(core.model.Resource res : attributes.keySet()) {
			ObjectAPI<String,String> ob1 = new FCAObject<>(res.toString());
			for(core.model.Resource attr : attributes.get(res)) {
				Attribute<String,String> atr1 = new FCAAttribute<>(attr.toString());
				ob1.addAttribute(attr.toString());
				atr1.addObject(res.toString());
			}
			fcaContext.addObject(ob1);
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
        	SubjectKind kind = SubjectKindImpl.getInstance(kindRes);
        	System.out.println("SubjectKind: "+kind);
        	for(ObjectAPI<String, String> extent : concept.getExtent()) {
        		core.model.Resource inst = core.model.Resource.get(extent.getObjectID().toString());
        		kind.getInstances().add(inst);
        		System.out.println("Instance: "+inst);
        		for(Attribute<String, String> attribute : concept.getIntent()) {
        			core.model.Resource attr = core.model.Resource.get(attribute.getAttributeID().toString());
        			kind.getAttributes(inst).add(attr);
        			System.out.println("Attribute: "+attr);
        			for(KindStatement stat : KindStatements.getInstance().getKindStatements(null, SubjectKindImpl.getInstance(inst), PropertyKindImpl.getInstance(attr), null)) {
        				core.model.Resource val = stat.getObject().getResource();
        				kind.getValues(inst, attr).add(val);
        				System.out.println("Value: "+val);
        			}
        		}
        		for(KindStatement stat : KindStatements.getInstance().getKindStatements(null, SubjectKindImpl.getInstance(inst), null, null)) {
        			stat.setSubject(kind);
        		}
        	}
        	//System.out.println(concept.getExtent().stream().map(ObjectAPI::getObjectID).collect(Collectors.toList())+";");
            System.out.println(concept.getIntent().stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"\n");
        }
		
        System.out.println();
        
        for(SubjectKind kind : SubjectKinds.getInstance().getSubjectKinds(null, null, null, null)) {
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
            System.out.println();
        }
        
		return KindStatements.getInstance().getKindStatements(null, null, null, null);
	}
	
}
