package org.argouml.model.uml.foundation.core;

import org.argouml.model.uml.AbstractWellformednessRule;
import ru.novosoft.uml.MBase;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * Checks that an associatonends namespace is the same as the owning association has
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public class AssociationEndNamespaceWellformednessRule
	extends AbstractWellformednessRule {

	/**
	 * Constructor for AssociationEndNamespaceWellformednessRule.
	 * @param key
	 */
	public AssociationEndNamespaceWellformednessRule(String key) {
		super(key);
	}

	/**
	 * Constructor for AssociationEndNamespaceWellformednessRule.
	 */
	public AssociationEndNamespaceWellformednessRule() {
		setUserMessageKey("associationend.namespace");
	}

	/**
	 * 
	 * Checks that an associatonends namespace is the same as the owning association has
	 * 
	 * @see org.argouml.model.uml.AbstractWellformednessRule#isWellformed(MBase, Object)
	 */
	public boolean isWellformed(MBase element, Object newValue) {
		if (element instanceof MAssociationEnd && newValue instanceof MNamespace) {
		MAssociation assoc = ((MAssociationEnd)element).getAssociation();
		if (assoc != null && assoc.getNamespace().equals(newValue)) {
			return true;
		}
		}
		return false;
	}

}
