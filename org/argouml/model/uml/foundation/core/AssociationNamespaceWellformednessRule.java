package org.argouml.model.uml.foundation.core;

import org.argouml.model.uml.AbstractWellformednessRule;
import ru.novosoft.uml.MBase;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MClassifier;

/**
 * Checks that an associatons namespace is the same as the type it connects to
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public class AssociationNamespaceWellformednessRule
	extends AbstractWellformednessRule {

	/**
	 * Constructor for AssociationNamespaceWellformednessRule.
	 * @param key
	 */
	public AssociationNamespaceWellformednessRule(String key) {
		super(key);
	}

	/**
	 * Constructor for AssociationNamespaceWellformednessRule.
	 */
	public AssociationNamespaceWellformednessRule() {
		setUserMessageKey("association.namespace");
	}

	/**
	 * @see org.argouml.model.uml.AbstractWellformednessRule#isWellformed(MBase, Object)
	 */
	public boolean isWellformed(MBase element, Object newValue) {
		if (element instanceof MAssociation && newValue instanceof MClassifier) {
			if (((MAssociation)element).getNamespace().equals(((MClassifier)newValue).getNamespace())) {
				return true;
			}
		}
		return false;
	}

}
