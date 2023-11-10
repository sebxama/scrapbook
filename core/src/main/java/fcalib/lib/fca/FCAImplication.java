package fcalib.lib.fca;
/*
 * This library is an enhanced version of the
 * FCAlib
 * @see <a href="https://github.com/julianmendez/fcalib">https://github.com/julianmendez/fcalib</a>
 * @author Leon Geis
 * @version 0.1
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fcalib.api.fca.Attribute;
import fcalib.api.fca.Implication;

/**
 * Describes an Implication of Attributes in
 * a Context.
 * @author Leon Geis
 */
public class FCAImplication<O,A> implements Implication<O,A> {

	private BigDecimal support;
	private BigDecimal confidence;
	
    /**
     * Premise of an Implication.
     */
    private List<? extends Attribute<O,A>> premise;

    /**
     * Conclusion of an Implication
     */
    private List<? extends Attribute<O,A>> conclusion;

    /**
     * Constructor of the class.
     */
    public FCAImplication(){
        this.premise = new ArrayList<>();
        this.conclusion = new ArrayList<>();
    }

    /**
     * Constructor of the class, which creates copies of the
     * parameters and sets them accordingly.
     * @param newPrem Premise, List of Attributes
     * @param newCon Conclusion, List of Attributes
     */
    public FCAImplication(List<? extends Attribute<O,A>> newPrem, List<? extends Attribute<O,A>> newCon){
        this.premise = new ArrayList<>(newPrem);
        this.conclusion = new ArrayList<>(newCon);
    }

    /**
     * @return List of Attributes of the Premise
     */
    public List<? extends Attribute<O, A>> getPremise() {
        return premise;
    }

    public void setPremise(List<? extends Attribute<O,A>> premise){
        this.premise=premise;
    }

    public void setConclusion(List<? extends Attribute<O,A>> conclusion){
        this.conclusion=conclusion;
    }

    /**
     * @return List of Attributes of the Conclusion
     */
    public List<? extends Attribute<O, A>> getConclusion() {
        return conclusion;
    }

    /**
     * @return A String representation of the current Implication.
     */
    public String toString(){
        return "(Support: "+this.support+"; Confidence: "+this.confidence+") "+this.premise.stream().map(Attribute::getAttributeID).collect(Collectors.toList())+"->"+this.conclusion.stream().map(Attribute::getAttributeID).collect(Collectors.toList());
    }

	@Override
	public void setSupport(BigDecimal support) {
		// TODO Auto-generated method stub
		this.support = support;
	}

	@Override
	public BigDecimal getSupport() {
		// TODO Auto-generated method stub
		return this.support;
	}

	@Override
	public void setConfidence(BigDecimal confidence) {
		// TODO Auto-generated method stub
		this.confidence = confidence;
	}

	@Override
	public BigDecimal getConfidence() {
		// TODO Auto-generated method stub
		return this.confidence;
	}

}
