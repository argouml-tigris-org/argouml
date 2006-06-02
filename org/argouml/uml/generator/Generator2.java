// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
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
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.api.PluggableNotation;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.model.Model;
import org.argouml.notation.NotationName;

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
 * @deprecated This class is deprecated in favour of GeneratorManager and
 * the CodeGenerator interface. <p>
 * Explanation by Daniele Tamino:<p>
 * Why Generator2 is deprecated: Because it was replaced 
 * by CodeGenerator and GeneratorManager. 
 * The Generator2 class kept a list of Generator2 objects, 
 * populated during construction, but that list was 
 * not changeable afterward, and this was incompatible 
 * with the new module loader (see issue 3580 
 * <http://argouml.tigris.org/issues/show_bug.cgi?id=3580>), 
 * so here comes GeneratorManager. 
 * Moreover, there was no reasonable way to display 
 * the generated source correctly in the source pane 
 * using Generator2 or FileGenerator, and for this 
 * the CodeGenerator interface was created (issue 
 * 3546<http://argouml.tigris.org/issues/show_bug.cgi?id=3546> ). <p> 
 * 
 * In ArgoUML V0.21.2 Generator2 was still used 
 * for another purpose, i.e. it implemented 
 * some common methods of the NotationProvider2 interface too, 
 * which went away when the new notation architecture 
 * got more and more completed (issue 1207
 * <http://argouml.tigris.org/issues/show_bug.cgi?id=1207> ).
 * @since 0.15.6
 */
public abstract class Generator2
    implements PluggableNotation {

    private static final Logger LOG = Logger.getLogger(Generator2.class);

    private NotationName notationName = null;

    /**
     * Two spaces used for indenting code in classes.
     */
    public static final String INDENT = "  ";

//    private static Map generators = new HashMap();

    /**
     * Access method that finds the correct generator based on a name.
     *
     * @param n The name.
     * @return a generator (or <tt>null</tt> if not found).
     */
    public static Generator2 getGenerator(NotationName n) {
        //return (Generator2) generators.get(n);
        CodeGenerator fg = GeneratorManager.getInstance()
                .getGenerator(n.getConfigurationValue());
        try {
            return (Generator2) fg;
        } catch (ClassCastException cce) {
            return null;
        }
    }

    /**
     * Constructor that sets the name of this notation.
     *
     * @param nn The NotationName object.
     */
    public Generator2(NotationName nn) {
        notationName = nn;
        String cv = nn.getConfigurationValue();
        Language lang =
            GeneratorHelper.makeLanguage(cv, nn.getTitle(), nn.getIcon());
        try {
            CodeGenerator wrapper =
                new FileGeneratorAdapter((FileGenerator) this);
            GeneratorManager.getInstance().addGenerator(lang, wrapper);
        } catch (ClassCastException cce) {
            LOG.warn("Class " + getClass() + " should implement FileGenerator");
        }
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

    public abstract String generateActionState(Object actionState);
    
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

    public abstract String generateObjectFlowState(Object m);

    public abstract String generateState(Object m);

    public abstract String generateSubmachine(Object m);

    public abstract String generateTransition(Object m);

    public abstract String generateAction(Object m);

    public abstract String generateGuard(Object m);

    public abstract String generateMessage(Object m);

    public abstract String generateEvent(Object m);

    public abstract String generateVisibility(Object m);

    public String generateExpression(Object expr) {
        if (Model.getFacade().isAExpression(expr))
            return generateUninterpreted(
                    (String) Model.getFacade().getBody(expr));
        else if (Model.getFacade().isAConstraint(expr))
            return generateExpression(Model.getFacade().getBody(expr));
        return "";
    }

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

    public String generateClassifierRef(Object cls) {
        if (cls == null)
            return "";
        return Model.getFacade().getName(cls);
    }

    public String generateStereotype(Object st) {
        if (st == null)
            return "";
        Project project = 
            ProjectManager.getManager().getCurrentProject();
        ProjectSettings ps = project.getProjectSettings();
        if (Model.getFacade().isAModelElement(st)) {
            if (Model.getFacade().getName(st) == null)
                return ""; // Patch by Jeremy Bennett
            if (Model.getFacade().getName(st).length() == 0)
                return "";
            return ps.getLeftGuillemot()
                + generateName(Model.getFacade().getName(st))
                + ps.getRightGuillemot();
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
                return ps.getLeftGuillemot()
		    + sb.toString()
		    + ps.getRightGuillemot();
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
