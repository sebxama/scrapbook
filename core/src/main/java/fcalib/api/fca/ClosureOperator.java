package fcalib.api.fca;

import java.util.List;

/**
 * Interface describing the Closure Operator
 * in FCA. This Interface is used for receiving
 * the dual entities, which can either be Objects or
 * Attributes.
 * @author Leon Geis
 */
public interface ClosureOperator {

    /**
     * @return List of the dual Entities.
     */
    List<?> getDualEntities();

}
