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

// File: Generator2.java
// Classes: Generator2

package org.argouml.uml.generator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.application.api.NotationName;
import org.argouml.application.api.NotationProvider2;
import org.argouml.application.api.PluggableNotation;
import org.argouml.language.helpers.NotationHelper;
import org.argouml.model.ModelFacade;

/** This class is the abstract super class that defines a code
 * generation framework.  It is basically a depth-first traversal of
 * the UML model that generates strings as it goes.  This framework
 * should probably be redesigned to separate the traversal logic from
 * the generation logic.  See the <a href=
 * "http://hillside.net/patterns/">Vistor design
 * pattern</a> in "Design Patterns", and the <a href=
 * "http://www.ccs.neu.edu/research/demeter/"> Demeter project</a>.
 */
public abstract class Generator2
    implements NotationProvider2, PluggableNotation {

    /**
     * Special modus for testing using the JUnit module.
     */
    private boolean _testModus = false;

    private NotationName _notationName = null;

    /** Two spaces used for indenting code in classes. */
    public static String INDENT = "  ";

    private static Map s_generators = new HashMap();

    /**
     * suffix placed behind the tag defining a testcase for an element
     * to be generated
     */
    public final static String TEST_SUFFIX = "test";
    public static Generator2 getGenerator(NotationName n) {
        return (Generator2) s_generators.get(n);
    }

    public Generator2(NotationName notationName) {
        _notationName = notationName;
        s_generators.put(_notationName, this);
    }

    public NotationName getNotation() {
        return _notationName;
    }

    /**
     * Generates code for some modelelement. Subclasses should
     * implement this to generate code for different notations.
     * @param o the element to be generated
     * @return String the generated code
     */
    public String generate(Object o) {
        if (o == null)
            return "";
        if (ModelFacade.isAExtensionPoint(o))
            return generateExtensionPoint(o);
        if (ModelFacade.isAOperation(o))
            return generateOperation(o, false);
        if (ModelFacade.isAAttribute(o))
            return generateAttribute(o, false);
        if (ModelFacade.isAParameter(o))
            return generateParameter(o);
        if (ModelFacade.isAPackage(o))
            return generatePackage(o);
        if (ModelFacade.isAClassifier(o))
            return generateClassifier(o);
        if (ModelFacade.isAExpression(o))
            return generateExpression(o);
        if (o instanceof String)
            return generateName((String) o);
        if (o instanceof String)
            return generateUninterpreted((String) o);
        if (ModelFacade.isAStereotype(o))
            return generateStereotype(o);
        if (ModelFacade.isATaggedValue(o)) {
            /*
             * 2002-11-07 Jaap Branderhorst Added the if statement to
             * test for the testtag. Did it here and not in (for
             * example) GeneratorJava to have a single point of
             * definition instead of all the generators.  If in the
             * generation of an owner of a taggedvalue, the
             * taggedvalue must be generated the method generating the
             * owner should call generate(sometag) and not
             * generateTaggedValue(sometag)
             */
            if (_testModus
                && ModelFacade.getTag(o).equals(
                    getNotation().getName() + TEST_SUFFIX)) {
                return "";
            }
            return generateTaggedValue(o);
        }
        if (ModelFacade.isAAssociation(o))
            return generateAssociation(o);
        if (ModelFacade.isAAssociationEnd(o))
            return generateAssociationEnd(o);
        if (ModelFacade.isAMultiplicity(o))
            return generateMultiplicity(o);
        if (ModelFacade.isAState(o))
            return generateState(o);
        if (ModelFacade.isATransition(o))
            return generateTransition(o);
        if (ModelFacade.isAAction(o))
            return generateAction(o);
        if (ModelFacade.isACallAction(o))
            return generateAction(o);
        if (ModelFacade.isAGuard(o))
            return generateGuard(o);
        if (ModelFacade.isAMessage(o))
            return generateMessage(o);
        if (ModelFacade.isAVisibilityKind(o))
            return generateVisibility(o);

        if (ModelFacade.isAModelElement(o))
            return generateName(org.argouml.model.ModelFacade.getName(o));

        if (o == null)
            return "";

        return o.toString();
    }

    public abstract String generateExtensionPoint(Object op);
    public abstract String generateOperation(Object op, boolean documented);
    public abstract String generateAttribute(Object attr, boolean documented);
    public abstract String generateParameter(Object param);
    public abstract String generatePackage(Object p);
    public abstract String generateClassifier(Object cls);
    public abstract String generateTaggedValue(Object s);
    public abstract String generateAssociation(Object a);
    public abstract String generateAssociationEnd(Object ae);
    public abstract String generateMultiplicity(Object m);
    public abstract String generateState(Object m);
    public abstract String generateTransition(Object m);
    public abstract String generateAction(Object m);
    public abstract String generateGuard(Object m);
    public abstract String generateMessage(Object m);
    public abstract String generateVisibility(Object m);

    public String generateExpression(Object expr) {
        if (ModelFacade.isAExpression(expr))
            return generateUninterpreted((String)ModelFacade.getBody(expr));
        else if (ModelFacade.isAConstraint(expr))
            return generateExpression(ModelFacade.getBody(expr));
        return "";
    }

    public String generateName(String n) {
        return n;
    }

    public String generateUninterpreted(String un) {
        if (un == null)
            return "";
        return un;
    }

    public String generateClassifierRef(Object cls) {
        if (cls == null)
            return "";
        return ModelFacade.getName(cls);
    }

    public String generateStereotype(Object st) {
        if (st == null)
            return "";
        if (ModelFacade.isAModelElement(st)) {
            if (ModelFacade.getName(st) == null)
                return ""; // Patch by Jeremy Bennett
            if (ModelFacade.getName(st).length() == 0)
                return "";
            return NotationHelper.getLeftGuillemot()
            + generateName(ModelFacade.getName(st))
            + NotationHelper.getRightGuillemot();
        }
        if (st instanceof Collection) {
            Object o;
            StringBuffer sb = new StringBuffer(10);
            boolean first = true;
            Iterator iter = ((Collection)st).iterator();
            while (iter.hasNext()) {
                if (!first)
                    sb.append(',');
                o = iter.next();
                if (o != null) {
                    sb.append(generateName(ModelFacade.getName(o)));
                    first = false;
                }
            }
            if (!first)
                return NotationHelper.getLeftGuillemot()
                + sb.toString()
                + NotationHelper.getRightGuillemot();
        }
        return "";
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
     * @return String representation of "src_path" tagged value or null if empty or not existing
     */
    public static String getCodePath(Object me) {
        if (me == null) return null;
        Object taggedValue = ModelFacade.getTaggedValue(me,"src_path");
        String s;
        if (taggedValue == null) return null;
        s =  ModelFacade.getValueOfTag(taggedValue);
        if (s != null)
            return s.trim();
        return null;
    }

    /**
     * The default for any Generator is to be enabled.
     *
     * @see org.argouml.application.api.ArgoModule#isModuleEnabled()
     * @return that this module is enabled.
     */
    public boolean isModuleEnabled() {
        return true;
    }

} /* end class Generator2 */