package org.argouml.model.uml.foundation.core;

import java.util.Iterator;

import org.argouml.model.uml.AbstractWellformednessRule;
import ru.novosoft.uml.MBase;
import ru.novosoft.uml.foundation.core.MAssociationEnd;

/**
 * Checks that all associationends have an unique name in an association
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public class AssociationEndNameWellformednessRule
	extends AbstractWellformednessRule {

	/**
	 * Constructor for AssociationEndNameWellformednessRule.
	 * @param key
	 */
	public AssociationEndNameWellformednessRule(String key) {
		super(key);
	}
	
	public AssociationEndNameWellformednessRule() {
		setUserMessageKey("associationend.name");
	}

	/**
	 * Checks that all associationends have an unique name in an association
	 * 
	 * @see org.argouml.model.uml.AbstractWellformednessRule#isWellformed(MBase, Object)
	 */
	public boolean isWellformed(MBase element, Object newValue) {
		if (element instanceof MAssociationEnd && newValue instanceof String) {
			MAssociationEnd modelelement = (MAssociationEnd)element;
			String name = (String)newValue;
			Iterator it = modelelement.getAssociation().getConnections().iterator();
	        while (it.hasNext()) {
	        	MAssociationEnd otherend = ((MAssociationEnd)it.next());
	        	if (otherend.getName() != null && otherend.getName().equals(name)) {
	        		return false;
	        	}
	        }
		} else {
			return false;
		}
        return true;
	}
	
	

}
