// $Id$
// Copyright (c) 2004-2005 The Regents of the University of California. All
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

package org.argouml.uml.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.argouml.application.api.NotationName;
import org.argouml.application.api.NotationProvider2;
import org.argouml.application.api.PluggableNotation;
import org.argouml.language.helpers.NotationHelper;
import org.argouml.model.Model;

/**
 * This class is the abstract super class that defines a code
 * generation framework.  It is basically a depth-first traversal of
 * the UML model that generates strings as it goes.  This framework
 * should probably be redesigned to separate the traversal logic from
 * the generation logic.  See the <a href=
 * "http://hillside.net/patterns/">Visitor design
 * pattern</a> in "Design Patterns", and the <a href=
 * "http://www.ccs.neu.edu/research/demeter/">Demeter project</a>.<p>
 *
 * @since 0.15.6
 */
public abstract class Generator2
    implements NotationProvider2, PluggableNotation {

    private NotationName notationName = null;

    /**
     * Two spaces used for indenting code in classes.
     */
    public static final String INDENT = "  ";

    private static Map generators = new HashMap();

    /**
     * Access method that finds the correct generator based on a name.
     *
     * @param n The name.
     * @return a generator (or <tt>null</tt> if not found).
     */
    public static Generator2 getGenerator(NotationName n) {
        return (Generator2) generators.get(n);
    }

    /**
     * Constructor that sets the name of this notation.
     *
     * @param name The name.
     */
    public Generator2(NotationName name) {
        notationName = name;
        generators.put(notationName, this);
    }

    /**
     * @see NotationProvider2#getNotation()
     */
    public NotationName getNotation() {
        return notationName;
    }

    /**
     * Generates code for some modelelement. Subclasses should
     * implement this to generate code for different notations.
     * @param o the element to be generated
     * @return String the generated code
     */
    public String generate(Object o) {
        if (o == null) {
            return "";
	}
        if (Model.getFacade().isAActionState(o)) {
            return generateActionState(o);
        }
        if (Model.getFacade().isAExtensionPoint(o)) {
            return generateExtensionPoint(o);
	}
        if (Model.getFacade().isAOperation(o)) {
            return generateOperation(o, false);
	}
        if (Model.getFacade().isAAttribute(o)) {
            return generateAttribute(o, false);
	}
        if (Model.getFacade().isAParameter(o)) {
            return generateParameter(o);
	}
        if (Model.getFacade().isAPackage(o)) {
            return generatePackage(o);
	}
        if (Model.getFacade().isAClassifier(o)) {
            return generateClassifier(o);
	}
        if (Model.getFacade().isAExpression(o)) {
            return generateExpression(o);
	}
        if (o instanceof String) {
            return generateName((String) o);
	}
        if (o instanceof String) {
            return generateUninterpreted((String) o);
	}
        if (Model.getFacade().isAStereotype(o)) {
            return generateStereotype(o);
	}
        if (Model.getFacade().isATaggedValue(o)) {
            return generateTaggedValue(o);
        }
        if (Model.getFacade().isAAssociation(o)) {
            return generateAssociation(o);
	}
        if (Model.getFacade().isAAssociationEnd(o)) {
            return generateAssociationEnd(o);
	}
        if (Model.getFacade().isAMultiplicity(o)) {
            return generateMultiplicity(o);
	}
        if (Model.getFacade().isAState(o)) {
            return generateState(o);
	}
        if (Model.getFacade().isATransition(o)) {
            return generateTransition(o);
	}
        if (Model.getFacade().isAAction(o)) {
            return generateAction(o);
	}
        if (Model.getFacade().isACallAction(o)) {
            return generateAction(o);
	}
        if (Model.getFacade().isAGuard(o)) {
            return generateGuard(o);
	}
        if (Model.getFacade().isAMessage(o)) {
            return generateMessage(o);
	}
        if (Model.getFacade().isAEvent(o)) {
            return generateEvent(o);
        }
        if (Model.getFacade().isAVisibilityKind(o)) {
            return generateVisibility(o);
	}

        if (Model.getFacade().isAModelElement(o)) {
            return generateName(Model.getFacade().getName(o));
	}

        if (o == null) {
            return "";
	}

        return o.toString();
    }

    /**
     * @see NotationProvider2#generateExtensionPoint(Object)
     */
    public abstract String generateExtensionPoint(Object op);

    /**
     * @see NotationProvider2#generateOperation(Object, boolean)
     */
    public abstract String generateOperation(Object op, boolean documented);

    /**
     * @see NotationProvider2#generateAttribute(Object, boolean)
     */
    public abstract String generateAttribute(Object attr, boolean documented);

    /**
     * @see NotationProvider2#generateParameter(Object)
     */
    public abstract String generateParameter(Object param);

    /**
     * @see NotationProvider2#generatePackage(Object)
     */
    public abstract String generatePackage(Object p);

    /**
     * @see NotationProvider2#generateClassifier(Object)
     */
    public abstract String generateClassifier(Object cls);

    /**
     * @see NotationProvider2#generateTaggedValue(Object)
     */
    public abstract String generateTaggedValue(Object s);

    /**
     * @see NotationProvider2#generateAssociation(Object)
     */
    public abstract String generateAssociation(Object a);

    /**
     * @see NotationProvider2#generateAssociationEnd(Object)
     */
    public abstract String generateAssociationEnd(Object ae);

    /**
     * @see NotationProvider2#generateMultiplicity(Object)
     */
    public abstract String generateMultiplicity(Object m);

    /**
     * @see NotationProvider2#generateObjectFlowState(Object)
     */
    public abstract String generateObjectFlowState(Object m);

    /**
     * @see NotationProvider2#generateState(Object)
     */
    public abstract String generateState(Object m);

    /**
     * @see NotationProvider2#generateTransition(Object)
     */
    public abstract String generateTransition(Object m);

    /**
     * @see NotationProvider2#generateAction(Object)
     */
    public abstract String generateAction(Object m);

    /**
     * @see NotationProvider2#generateGuard(Object)
     */
    public abstract String generateGuard(Object m);

    /**
     * @see NotationProvider2#generateMessage(Object)
     */
    public abstract String generateMessage(Object m);

    /**
     * @see NotationProvider2#generateEvent(Object)
     */
    public abstract String generateEvent(Object m);

    /**
     * @see NotationProvider2#generateVisibility(Object)
     */
    public abstract String generateVisibility(Object m);

    /**
     * @see NotationProvider2#generateExpression(Object)
     */
    public String generateExpression(Object expr) {
        if (Model.getFacade().isAExpression(expr))
            return generateUninterpreted((String) Model.getFacade().getBody(expr));
        else if (Model.getFacade().isAConstraint(expr))
            return generateExpression(Model.getFacade().getBody(expr));
        return "";
    }

    /**
     * @see NotationProvider2#generateName(String)
     */
    public String generateName(String n) {
        return n;
    }

    /**
     * Make a string non-null.<p>
     *
     * What is the purpose of this function? Shouldn't it be private static?
     *
     * @param un The String.
     * @return a non-null string.
     */
    public String generateUninterpreted(String un) {
        if (un == null)
            return "";
        return un;
    }

    /**
     * @see NotationProvider2#generateClassifierRef(Object)
     */
    public String generateClassifierRef(Object cls) {
        if (cls == null)
            return "";
        return Model.getFacade().getName(cls);
    }

    /**
     * @see NotationProvider2#generateStereotype(Object)
     */
    public String generateStereotype(Object st) {
        if (st == null)
            return "";
        if (Model.getFacade().isAModelElement(st)) {
            if (Model.getFacade().getName(st) == null)
                return ""; // Patch by Jeremy Bennett
            if (Model.getFacade().getName(st).length() == 0)
                return "";
            return NotationHelper.getLeftGuillemot()
            + generateName(Model.getFacade().getName(st))
            + NotationHelper.getRightGuillemot();
        }
        if (st instanceof Collection) {
            Object o;
            StringBuffer sb = new StringBuffer(10);
            boolean first = true;
            Iterator iter = ((Collection) st).iterator();
            while (iter.hasNext()) {
                if (!first)
                    sb.append(',');
                o = iter.next();
                if (o != null) {
                    sb.append(generateName(Model.getFacade().getName(o)));
                    first = false;
                }
            }
            if (!first) {
                return NotationHelper.getLeftGuillemot()
		    + sb.toString()
		    + NotationHelper.getRightGuillemot();
	    }
        }
        return "";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModulePopUpActions(
     *         Vector, Object)
     */
    public Vector getModulePopUpActions(Vector v, Object o) {
        return null;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#shutdownModule()
     */
    public boolean shutdownModule() {
        return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#initializeModule()
     */
    public boolean initializeModule() {
        return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#setModuleEnabled(boolean)
     */
    public void setModuleEnabled(boolean enabled) {
    }


    /**
     * @see org.argouml.application.api.Pluggable#inContext(Object[])
     */
    public boolean inContext(Object[] o) {
        return false;
    }

    /**
     * Gets the path of the code base for a model element.<p>
     * If empty or not existing return <tt>null</tt>.
     *
     * @param me The model element
     * @return String representation of "src_path" tagged value.
     */
    public static String getCodePath(Object me) {
        if (me == null) {
	    return null;
	}

        Object taggedValue = Model.getFacade().getTaggedValue(me, "src_path");
        String s;
        if (taggedValue == null) {
	    return null;
	}
        s =  Model.getFacade().getValueOfTag(taggedValue);
        if (s != null) {
            return s.trim();
	}
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
