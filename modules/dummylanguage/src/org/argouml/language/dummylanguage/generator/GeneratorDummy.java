// $Id$
// Copyright (c) 2001-2003 The Regents of the University of California. All
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

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.model_management.*;

import org.argouml.application.api.*;
import org.argouml.uml.generator.*;

/** Generator subclass to generate text for display in diagrams in in
 * text fields in the ArgoUML user interface.
 * The generated code is a dummy language. The purpose is to help other
 * on how the Genetaror mechanism is supposed to work.
 *
 * @author Linus Tolke
 */
public class GeneratorDummy extends Generator implements PluggableNotation {
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
    public String generateExtensionPoint(MExtensionPoint ep) {
        return "ExtensionPoint(" + ep.getName() + ")";
    }

    public String generateOperation(MOperation op, boolean documented) {
	return "Operation(" + op.getName() + ")";
    }
    public String generateAttribute(MAttribute attr, boolean documented) {
	return "Attribute(" + attr.getName() + ")";
    }
    public String generateParameter(MParameter param) {
	return "Parameter(" + param.getName() + ")";
    }
    public String generatePackage(MPackage p) {
	return "Package(" + p.getName() + ")";
    }
    public String generateClassifier(MClassifier cls) {
	return "Classifier(" + cls.getName() + ")";
    }
    public String generateTaggedValue(MTaggedValue s) {
	return "TaggedValue(" + s.getTag() + ")";
    }
    public String generateAssociation(MAssociation a) {
	return "Association(" + a.getName() + ")";
    }
    public String generateAssociationEnd(MAssociationEnd ae) {
	return "AssociationEnd(" + ae.getName() + ")";
    }
    public String generateAssociationRole(MAssociationRole ar) {
	return "AssociationRole(" + ar.getName() + ")";
    }
    public String generateMultiplicity(MMultiplicity m) {
	return "Multiplicity(" + m + ")";
    }
    public String generateState(MState m) {
	return "State(" + m.getName() + ")";
    }
    public String generateStateBody(MState m) {
	return "StateBody(" + m.getName() + ")";
    }
    public String generateTransition(MTransition m) {
	return "Transition(" + m.getName() + ")";
    }
    public String generateAction(MAction m) {
	return "Action(" + m.getName() + ")";
    }
    public String generateGuard(MGuard m) {
	return "Guard(" + m.getName() + ")";
    }
    public String generateMessage(MMessage m) {
	return "Message(" + m.getName() + ")";
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

