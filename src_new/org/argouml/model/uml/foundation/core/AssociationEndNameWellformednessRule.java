// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

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
	    MAssociationEnd modelelement = (MAssociationEnd) element;
	    String name = (String) newValue;
	    Iterator it = modelelement.getAssociation().getConnections().iterator();
	    while (it.hasNext()) {
		MAssociationEnd otherend = ((MAssociationEnd) it.next());
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
