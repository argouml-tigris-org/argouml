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

package org.argouml.model.uml;

import org.argouml.application.api.Argo;

import ru.novosoft.uml.MBase;

/**
 * Rule definition for wellformedness of some modelelement. In the UML 1.3 spec
 * so called wellformednessrules are defined. This class is the abstract superclass 
 * of implementations of these rules.
 * <p>
 * In several build methods in the uml factories these rules are used. Furthermore
 * they are used in the proppanels to veto some changes.
 * </p>
 * 
 * @author jaap.branderhorst@xs4all.nl
 * @since argouml 0.11.3
 */
public abstract class AbstractWellformednessRule {
    /**
     * the message key to be looked up to show the message to the user
     */
    private String _key; 
	
    /**
     * Checks if the combination of the element and the newValue give a wellformed
     * result
     */
    public abstract boolean isWellformed(MBase element, Object newValue);
	
    /**
     * Returns the localized user message
     */
    public String getUserMessage() {
	return Argo.localize("UMLMenu", "wellformednessrule." + _key);
    }
	
    /**
     * sets the message key. The message key here is of the form elementname.propertytocheck
     * <p>
     * Example:
     * </p>
     * <p> 
     * Say you want to check the namespace of an association. The key will be here
     * association.namespace
     * </p>
     * Keys are looked up in UMLResourceBundle for the time being. Keys start with
     * wellformednessrule. over there. This is added to the key entered here.
     */
    public void setUserMessageKey(String key) {
	_key = key;
    }
	
    public AbstractWellformednessRule(String key) {
	_key = key;
    }
	
    public AbstractWellformednessRule() { }

}
