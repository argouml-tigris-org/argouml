// $Id$
// Copyright (c) 2001-2004 The Regents of the University of California. All
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


package org.argouml.language.dummylanguage.generator;

import java.util.*;

import org.argouml.application.api.*;
import org.argouml.model.ModelFacade;
import org.argouml.uml.generator.*;

/** Generator2 subclass to generate text for display in diagrams in in
 * text fields in the ArgoUML user interface.
 * The generated code is a dummy language. The purpose is to help other
 * on how the Genetaror mechanism is supposed to work.
 *
 * @author Linus Tolke
 */
public class GeneratorDummy extends Generator2 implements PluggableNotation {
    private static GeneratorDummy SINGLETON = new GeneratorDummy();
    public static GeneratorDummy getInstance() { return SINGLETON; }

    private GeneratorDummy() {
	super(Notation.makeNotation("Dummy", "1.0", null));
    }

    /**
     * <p>Generate code for an extension point.</p>
     *
     * <p>Provided to comply with the interface, but returns null
     *   since no code will be generated. This should prevent a source tab
     *   being shown.</p>
     *
     * @param ep  The extension point to generate for
     *
     * @return    The generated code string. Always null in this
     *            implementation.
     */
    public String generateExtensionPoint(/*MExtensionPoint*/Object handle) {
        return "ExtensionPoint(" + ModelFacade.getName(handle) + ")";
    }

    public String generateOperation(/*MOperation*/Object handle, boolean documented) {
	return "Operation(" + ModelFacade.getName(handle) + ")";
    }
    public String generateAttribute(/*MAttribute*/Object handle, boolean documented) {
	return "Attribute(" + ModelFacade.getName(handle) + ")";
    }
    public String generateParameter(/*MParameter*/Object handle) {
	return "Parameter(" + ModelFacade.getName(handle) + ")";
    }
    public String generatePackage(/*MPackage*/Object handle) {
	return "Package(" + ModelFacade.getName(handle) + ")";
    }
    public String generateClassifier(/*MClassifier*/Object handle) {
	return "Classifier(" + ModelFacade.getName(handle) + ")";
    }
    public String generateTaggedValue(/*MTaggedValue*/Object handle) {
	return "TaggedValue(" + ModelFacade.getTag(handle) + ")";
    }
    public String generateAssociation(/*MAssociation*/Object handle) {
	return "Association(" + ModelFacade.getName(handle) + ")";
    }
    public String generateAssociationEnd(/*MAssociationEnd*/Object handle) {
	return "AssociationEnd(" + ModelFacade.getName(handle) + ")";
    }
    public String generateAssociationRole(/*MAssociationRole*/Object handle) {
	return "AssociationRole(" + ModelFacade.getName(handle) + ")";
    }
    public String generateMultiplicity(/*MMultiplicity*/Object handle) {
	return "Multiplicity(" + handle.toString() + ")";
    }
    public String generateVisibility(/*MVisibilityKind*/Object handle) {
        if (ModelFacade.isAVisibilityKind(handle)) {
            return "Visibility(" + ModelFacade.getValue(handle) + ")";
        }
        return "Visibility(" + ModelFacade.getName(handle) + ")";
    }
    public String generateState(/*MState*/Object handle) {
	return "State(" + ModelFacade.getName(handle) + ")";
    }
    public String generateStateBody(/*MState*/Object handle) {
	return "StateBody(" + ModelFacade.getName(handle) + ")";
    }
    public String generateTransition(/*MTransition*/Object handle) {
	return "Transition(" + ModelFacade.getName(handle) + ")";
    }
    public String generateAction(/*MAction*/Object handle) {
	if (ModelFacade.isAAction(handle)) {
	    return "Action(" + ModelFacade.getName(handle) + ")";
	}
	return "Action(" + handle.toString() + ")";
    }
    public String generateGuard(/*MGuard*/Object handle) {
	return "Guard(" + ModelFacade.getName(handle) + ")";
    }
    public String generateMessage(/*MMessage*/Object handle) {
	return "Message(" + ModelFacade.getName(handle) + ")";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateObjectFlowState(java.lang.Object)
     */
    public String generateObjectFlowState(Object m) {
        Object c = ModelFacade.getType(m);
        if (c == null) return "";
        return ModelFacade.getName(c);
    }
    
    public boolean canParse() { return false; }
    public boolean canParse(Object o) { return false; }

    public String getModuleName() { return "GeneratorDummy"; }
    public String getModuleDescription() {
        return "Dummy language";
    }
    public String getModuleAuthor() { return "ArgoUML Dummy Language addition"; }
    public String getModuleVersion() { return "0.9.9"; }
    public String getModuleKey() {
	return "module.language.generator.dummylanguage";
    }


} /* end class GeneratorJava */

