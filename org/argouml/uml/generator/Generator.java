// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// File: Generator.java
// Classes: Generator
// Original Author:
// $Id$

// 10 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// extension points.

// 25 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Patched
// generateStereotype to handle the case where the stereotype has a null name
// (caused NPE when making a new stereotype).

package org.argouml.uml.generator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.argouml.application.api.NotationName;
import org.argouml.application.api.NotationProvider;
import org.argouml.application.api.PluggableNotation;
import org.argouml.language.helpers.NotationHelper;
import org.argouml.model.ModelFacade;

import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MPackage;

/** This class is the abstract super class that defines a code
 * generation framework.  It is basically a depth-first traversal of
 * the UML model that generates strings as it goes.  This framework
 * should probably be redesigned to separate the traversal logic from
 * the generation logic.  See the <a href=
 * "http://hillside.net/patterns/patterns.html">Vistor design
 * pattern</a> in "Design Patterns", and the <a href=
 * "http://www.ccs.neu.edu/research/demeter/"> Demeter project</a>. */

public abstract class Generator
    implements NotationProvider, PluggableNotation {

    /**
     * Special modus for testing using the JUnit module.
     */
    private boolean _testModus = false;

    private NotationName _notationName = null;

    /** Two spaces used for indenting code in classes. */
    public static String INDENT = "  ";

    private static Map s_generators = new HashMap();

    /**
     * suffix placed behind the tag defining a testcase for an element to be generated
     */
    public final static String TEST_SUFFIX = "test";
    public static Generator getGenerator(NotationName n) {
        return (Generator) s_generators.get(n);
    }

    public Generator(NotationName notationName) {
        _notationName = notationName;
        s_generators.put(_notationName, this);
    }

    public NotationName getNotation() {
        return _notationName;
    }

    /**
     * Generates code for some modelelement. Subclasses should implement this to generate code for different notations.
     * @param o the element to be generated
     * @return String the generated code
     */
    public String generate(Object o) {
        if (o == null)
            return "";
        if (o instanceof MExtensionPoint)
            return generateExtensionPoint((MExtensionPoint) o);
        if (o instanceof MOperation)
            return generateOperation((MOperation) o, false);
        if (o instanceof MAttribute)
            return generateAttribute((MAttribute) o, false);
        if (o instanceof MParameter)
            return generateParameter((MParameter) o);
        if (o instanceof MPackage)
            return generatePackage((MPackage) o);
        if (o instanceof MClassifier)
            return generateClassifier((MClassifier) o);
        if (o instanceof MExpression)
            return generateExpression((MExpression) o);
        if (o instanceof String)
            return generateName((String) o);
        if (o instanceof String)
            return generateUninterpreted((String) o);
        if (o instanceof MStereotype)
            return generateStereotype((MStereotype) o);
        if (o instanceof MTaggedValue) {
            /*
             * 2002-11-07
             * Jaap Branderhorst
             * Added the if statement to test for the testtag. Did it here and not in (for example) GeneratorJava
             * to have a single point of definition instead of all the generators.
             * If in the generation of an owner of a taggedvalue, the taggedvalue must be generated the method generating
             * the owner should call generate(sometag) and not generateTaggedValue(sometag)
             */
            if (_testModus
                && ((MTaggedValue) o).getTag().equals(
                    getNotation().getName() + TEST_SUFFIX)) {
                return "";
            }
            return generateTaggedValue((MTaggedValue) o);
        }
        if (o instanceof MAssociation)
            return generateAssociation((MAssociation) o);
        if (o instanceof MAssociationEnd)
            return generateAssociationEnd((MAssociationEnd) o);
        if (o instanceof MMultiplicity)
            return generateMultiplicity((MMultiplicity) o);
        if (o instanceof MState)
            return generateState((MState) o);
        if (o instanceof MTransition)
            return generateTransition((MTransition) o);
        if (ModelFacade.isAAction(o))
            return generateAction(o);
        if (o instanceof MCallAction)
            return generateAction(o);
        if (o instanceof MGuard)
            return generateGuard((MGuard) o);
        if (o instanceof MMessage)
            return generateMessage((MMessage) o);

        if (o instanceof MModelElement)
            return generateName(((MModelElement) o).getName());

        if (o == null)
            return "";

        return o.toString();
    }

    public abstract String generateExtensionPoint(MExtensionPoint op);
    public abstract String generateOperation(MOperation op, boolean documented);
    public abstract String generateAttribute(
        MAttribute attr,
        boolean documented);
    public abstract String generateParameter(MParameter param);
    public abstract String generatePackage(MPackage p);
    public abstract String generateClassifier(MClassifier cls);
    public abstract String generateTaggedValue(MTaggedValue s);
    public abstract String generateAssociation(MAssociation a);
    public abstract String generateAssociationEnd(MAssociationEnd ae);
    public abstract String generateMultiplicity(MMultiplicity m);
    public abstract String generateState(MState m);
    public abstract String generateTransition(MTransition m);
    public abstract String generateAction(Object m);
    public abstract String generateGuard(MGuard m);
    public abstract String generateMessage(MMessage m);

    public String generateExpression(MExpression expr) {
        if (expr == null)
            return "";
        return generateUninterpreted(expr.getBody());
    }

    public String generateExpression(MConstraint expr) {
        if (expr == null)
            return "";
        return generateExpression(expr.getBody());
    }

    public String generateName(String n) {
        return n;
    }

    public String generateUninterpreted(String un) {
        if (un == null)
            return "";
        return un;
    }

    public String generateClassifierRef(MClassifier cls) {
        if (cls == null)
            return "";
        return cls.getName();
    }

    public String generateStereotype(MStereotype st) {
        if (st == null)
            return "";
        if (st.getName() == null)
            return ""; // Patch by Jeremy Bennett
        if (st.getName().length() == 0)
            return "";
        return NotationHelper.getLeftGuillemot()
            + generateName(st.getName())
            + NotationHelper.getRightGuillemot();
    }

    // Module stuff
    public Vector getModulePopUpActions(Vector v, Object o) {
        return null;
    }
    public boolean shutdownModule() {
        return true;
    }
    public boolean initializeModule() {
        return true;
    }
    public void setModuleEnabled(boolean enabled) {
    }
    public boolean inContext(Object[] o) {
        return false;
    }

    /**
     * Returns the _testModus.
     * @return boolean
     */
    public boolean isTestModus() {
        return _testModus;
    }

    /**
     * Sets the _testModus.
     * @param _testModus The _testModus to set
     */
    public void setTestModus(boolean _testModus) {
        this._testModus = _testModus;
    }

    /**
     * Gets the path of the code base for a model element, otherwise null.
     * @param me The model element
     * @return String
     */
    public static String getCodePath(Object me) {
        String s =
            ModelFacade.getValueOfTag(
                ModelFacade.getTaggedValue(me, "src_path"));
        if (s != null)
            return s.trim();
        return null;
    }

    /**   
     * @deprecated must be added to all leaf classes from this class, these are the modules not this abstract class
     * @see org.argouml.application.api.ArgoModule#isModuleEnabled()
     */
    public boolean isModuleEnabled() {
        return true;
    }

} /* end class Generator */
