package org.argouml.model.uml.foundation.core;

import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.uml.AbstractWellformednessRule;
import ru.novosoft.uml.MBase;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;

/**
 * Checks that there is at most one associationend within an association an 
 * aggregation or composite.
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public class AssociationEndAggregationWellformednessRule
	extends AbstractWellformednessRule {

	/**
	 * Constructor for AssociationEndAggregationWellformednessRule.
	 * @param key
	 */
	public AssociationEndAggregationWellformednessRule(String key) {
		super(key);
	}

	/**
	 * Constructor for AssociationEndAggregationWellformednessRule.
	 */
	public AssociationEndAggregationWellformednessRule() {
		setUserMessageKey("associationend.aggregation");
	}

	/**
	 * Checks that there is at most one associationend within an association an 
	 * aggregation or composite.
	 * @see org.argouml.model.uml.AbstractWellformednessRule#isWellformed(MBase, Object)
	 */
	public boolean isWellformed(MBase element, Object newValue) {
		if (element instanceof MAssociationEnd && newValue instanceof MAggregationKind) {
			MAssociationEnd modelelement = (MAssociationEnd)element;
			MAggregationKind aggregation = (MAggregationKind)newValue;
			if (aggregation.equals(MAggregationKind.NONE)) return true;
			Collection ends = modelelement.getAssociation().getConnections();
			if (ends.size() > 2) return false;
			Iterator it = ends.iterator();
			int counter = 0;
			while (it.hasNext()) {
				MAssociationEnd end = (MAssociationEnd)it.next();
				if (!modelelement.equals(end) && !(end.getAggregation().equals(MAggregationKind.NONE))) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

}
