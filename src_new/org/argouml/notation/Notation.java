// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.notation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.model.Model;

/**
 * Provides centralized methods dealing with notation.
 *
 * @stereotype singleton
 * @author Thierry Lach
 * @since 0.9.4
 */
public final class Notation implements PropertyChangeListener {

    /**
     * Define a static log4j category variable for ArgoUML notation.
     */
    private static final Logger LOG =
        Logger.getLogger(Notation.class);

    /**
     * The name of the default ArgoUML notation.  This notation is
     * part of ArgoUML core distribution.
     */
    private static NotationName notationArgo =
        NotationNameImpl.findNotation("UML 1.4");
    /*
     * Remark:
     * There is also a java-like notation, which is also
     * part of ArgoUML core distribution.
     */

    /**
     * The configuration key for the preferred notation.
     */
    public static final ConfigurationKey KEY_DEFAULT_NOTATION =
        Configuration.makeKey("notation", "default");

    /**
     * The configuration key that indicates whether to show stereotypes
     * in the navigation panel.
     */
    public static final ConfigurationKey KEY_SHOW_STEREOTYPES =
        Configuration.makeKey("notation", "navigation", "show", "stereotypes");

    /**
     * The configuration key that indicates whether to use guillemots
     * or greater/lessthan characters in stereotypes.
     */
    public static final ConfigurationKey KEY_USE_GUILLEMOTS =
        Configuration.makeKey("notation", "guillemots");

    /**
     * Indicates if the user only wants to see UML notation.
     */
    public static final ConfigurationKey KEY_UML_NOTATION_ONLY =
        Configuration.makeKey("notation", "only", "uml");

    /**
     * Indicates if the user wants to see visibility signs (public,
     * private, protected or # + -).
     */
    public static final ConfigurationKey KEY_SHOW_VISIBILITY =
        Configuration.makeKey("notation", "show", "visibility");

    /**
     * Indicates if the user wants to see multiplicity in attributes
     * and classes.
     */
    public static final ConfigurationKey KEY_SHOW_MULTIPLICITY =
        Configuration.makeKey("notation", "show", "multiplicity");

    /**
     * Indicates if the user wants to see the initial value.
     */
    public static final ConfigurationKey KEY_SHOW_INITIAL_VALUE =
        Configuration.makeKey("notation", "show", "initialvalue");

    /**
     * Indicates if the user wants to see the properties (everything
     * between braces), that is for example the concurrency.
     */
    public static final ConfigurationKey KEY_SHOW_PROPERTIES =
        Configuration.makeKey("notation", "show", "properties");

    /**
     * Indicates if the user wants to see the types and parameters
     * of attributes and operations.
     */
    public static final ConfigurationKey KEY_SHOW_TYPES =
        Configuration.makeKey("notation", "show", "types");

    /**
     * Default value for the shadow size of classes, interfaces etc.
     */
    public static final ConfigurationKey KEY_DEFAULT_SHADOW_WIDTH =
        Configuration.makeKey("notation", "default", "shadow-width");

    /**
     * The instance.
     */
    private static final Notation SINGLETON = new Notation();


    /**
     * The constructor.
     */
    private Notation() {
        Configuration.addListener(KEY_USE_GUILLEMOTS, this);
        Configuration.addListener(KEY_DEFAULT_NOTATION, this);
        Configuration.addListener(KEY_UML_NOTATION_ONLY, this);
        Configuration.addListener(KEY_SHOW_TYPES, this);
        Configuration.addListener(KEY_SHOW_MULTIPLICITY, this);
        Configuration.addListener(KEY_SHOW_PROPERTIES, this);
        Configuration.addListener(KEY_SHOW_VISIBILITY, this);
        Configuration.addListener(KEY_SHOW_INITIAL_VALUE, this);
    }

    /**
     * Remove the notation change listener.
     * <code>finalize</code> should never happen, but play it safe.
     *
     * TODO: Explain why we don't call super.finalize()!
     */
    protected void finalize() {
        Configuration.removeListener(KEY_DEFAULT_NOTATION, this);
        Configuration.removeListener(KEY_USE_GUILLEMOTS, this);
        Configuration.removeListener(KEY_UML_NOTATION_ONLY, this);
        Configuration.removeListener(KEY_SHOW_TYPES, this);
        Configuration.removeListener(KEY_SHOW_MULTIPLICITY, this);
        Configuration.removeListener(KEY_SHOW_PROPERTIES, this);
        Configuration.removeListener(KEY_SHOW_VISIBILITY, this);
        Configuration.removeListener(KEY_SHOW_INITIAL_VALUE, this);
    }

    private static NotationProvider2 getProvider(NotationName notation) {
        NotationProvider2 np;
        np = NotationProviderFactory.getInstance().getProvider(notation);
        LOG.debug("getProvider(" + notation + ") returns " + np);
        return np;
    }

    /**
     * @param n the NotationName that will become default
     */
    public static void setDefaultNotation(NotationName n) {
        LOG.info("default notation set to " + n.getConfigurationValue());
        Configuration.setString(
            KEY_DEFAULT_NOTATION,
            n.getConfigurationValue());
    }

    /**
     * Convert a String into a NotationName.
     * @param s the String
     * @return the matching Notationname
     */
    public static NotationName findNotation(String s) {
        return NotationNameImpl.findNotation(s);
    }

    /**
     * Returns the Notation as set in the menu.
     *
     * @return the default NotationName
     */
    public static NotationName getConfigueredNotation() {
        NotationName n =
            NotationNameImpl.findNotation(
                Configuration.getString(
                    KEY_DEFAULT_NOTATION,
                    notationArgo.getConfigurationValue()));
        // This is needed for the case when the default notation is
        // not loaded at this point.
        if (n == null) {
            n = NotationNameImpl.findNotation("UML 1.4");
	}
        LOG.debug("default notation is " + n.getConfigurationValue());
        return n;
    }

    ////////////////////////////////////////////////////////////////
    // class accessors

    /**
     * General accessor for an extension point.<p>
     *
     * @param notation    Name of the notation to be used.
     *
     * @param ep          The extension point to generate for.
     *
     * @return            The generated text.
     */
    protected static String generateExtensionPoint(
        NotationName notation,
        Object/*MExtensionPoint*/ ep) {
        return getProvider(notation)
	    .generateExtensionPoint(ep);
    }

    private static String generateOperation(
        NotationName notation,
        Object/*MOperation*/ op,
        boolean documented) {
        return getProvider(notation).generateOperation(op, documented);
    }

    private static String generateAttribute(
        NotationName notation,
        Object/*MAttribute*/ attr,
        boolean documented) {
        return getProvider(notation).generateAttribute(attr, documented);
    }

    private static String generateParameter(
        NotationName notation,
        Object/*MParameter*/ param) {
        return getProvider(notation).generateParameter(param);
    }

    private static String generateName(NotationName notation, String name) {
        return getProvider(notation).generateName(name);
    }

    private static String generatePackage(NotationName notation,
				     Object/*MPackage*/ pkg) {
        return getProvider(notation).generatePackage(pkg);
    }

    private static String generateExpression(
        NotationName notation,
        Object/*MExpression*/ expr) {
        return getProvider(notation).generateExpression(expr);
    }

    private static String generateClassifier(
        NotationName notation,
        Object/*MClassifier*/ cls) {
        return getProvider(notation).generateClassifier(cls);
    }

    private static String generateStereotype(NotationName notation,
					Object/*MStereotype*/ s) {
        return getProvider(notation).generateStereotype(s);
    }

    private static String generateTaggedValue(
        NotationName notation,
        Object/*MTaggedValue*/ s) {
        return getProvider(notation).generateTaggedValue(s);
    }

    private static String generateAssociation(
        NotationName notation,
        Object/*MAssociation*/ a) {
        return getProvider(notation).generateAssociation(a);
    }

    private static String generateAssociationEnd(
        NotationName notation,
        Object/*MAssociationEnd*/ ae) {
        return getProvider(notation)
	    .generateAssociationEnd(ae);
    }

    private static String generateMultiplicity(
        NotationName notation,
        Object/*MMultiplicity*/ m) {
        return getProvider(notation).generateMultiplicity(m);
    }

    private static String generateState(NotationName notation,
					  Object/*MState*/ m) {
        return getProvider(notation).generateState(m);
    }

    private static String generateSubmachine(NotationName notation,
                      Object/*MSubmachineState*/ m) {
        return getProvider(notation).generateSubmachine(m);
    }

    private static String generateObjectFlowState(NotationName notation,
          Object/*MObjectFlowState*/ m) {
        return getProvider(notation).generateObjectFlowState(m);
    }

    private static String generateClassifierInState(NotationName notation,
            Object/*MObjectFlowState*/ m) {
        Collection c = Model.getFacade().getInStates(m);
        // MVW: I have no idea how to handle multiple states,
        // so we go for the 1st one..
        Iterator i = c.iterator();
        if (i.hasNext()) {
            return getProvider(notation).generateState(i.next());
        }
        return "";
    }

    private static String generateStateBody(NotationName notation,
                                        Object/*MState*/ stt) {
        return getProvider(notation).generateStateBody(stt);
    }

    private static String generateTransition(NotationName notation,
					Object/*MTransition*/ m) {
        return getProvider(notation).generateTransition(m);
    }

    private static String generateVisibility(NotationName notation,
					Object /*MVisibilityKind*/ m) {
        return getProvider(notation).generateVisibility(m);
    }

    private static String generateAction(NotationName notation, Object m) {
        return getProvider(notation).generateAction(m);
    }

    private static String generateActionState(NotationName notation, Object m) {
        return getProvider(notation).generateActionState(m);
    }

    private static String generateGuard(NotationName notation,
					  Object/*MGuard*/ m) {
        return getProvider(notation).generateGuard(m);
    }

    private static String generateMessage(NotationName notation,
				     Object/*MMessage*/ m) {
        return getProvider(notation).generateMessage(m);
    }

    private static String generateClassifierRef(
        NotationName notation,
        Object/*MClassifier*/ m) {
        return getProvider(notation).generateClassifierRef(m);
    }

    private static String generateAssociationRole(
        NotationName notation,
        Object/*MAssociationRole*/ m) {
        return getProvider(notation)
	    .generateAssociationRole(m);
    }

    ////////////////////////////////////////////////////////////////
    // static accessors

    /**
     * @return the singleton
     */
    public static Notation getInstance() {
        return SINGLETON;
    }

    /**
     * Static accessor for extension point generation. Invokes our protected
     * accessor from the singleton instance with the "documented" flag set
     * false.<p>
     *
     * @param ctx  Context used to identify the notation
     *
     * @param ep   The extension point to generate for.
     *
     * @return     The generated text.
     */
    public static String generateExtensionPoint(
        NotationContext ctx,
        Object/*MExtensionPoint*/ ep) {
        return generateExtensionPoint(Notation.getNotation(ctx), ep);
    }

    /**
     * Static accessor for operation generation.  Invokes our protected
     * accessor from the singleton instance with the "documented" flag set
     * false.<p>
     *
     * @param ctx  Context used to identify the notation
     * @param op   The operation to generate for.
     * @return     The generated text.
     */
    public static String generateOperation(
        NotationContext ctx,
        Object/*MOperation*/ op) {
        return generateOperation(Notation.getNotation(ctx), op, false);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param op   The operation to generate for.
     * @param documented <code>true</code> if documentation shall be generated.
     * @return     The generated text.
     */
    public static String generateOperation(
        NotationContext ctx,
        Object/*MOperation*/ op,
        boolean documented) {
        return generateOperation(Notation.getNotation(ctx), op, documented);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param attr   The attribute to generate for.
     * @return     The generated text.
     */
    public static String generateAttribute(
        NotationContext ctx,
        Object/*MAttribute*/ attr) {
        return generateAttribute(Notation.getNotation(ctx), attr, false);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param attr   The attribute to generate for.
     * @param documented <code>true</code> if documentation shall be generated.
     * @return     The generated text.
     */
    public static String generateAttribute(
        NotationContext ctx,
        Object/*MAttribute*/ attr,
        boolean documented) {
        return generateAttribute(Notation.getNotation(ctx), attr, documented);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param param   The parameter to generate for.
     * @return     The generated text.
     */
    public static String generateParameter(
        NotationContext ctx,
        Object/*MParameter*/ param) {
        return generateParameter(Notation.getNotation(ctx), param);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param p   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generatePackage(NotationContext ctx,
					 Object/*MPackage*/ p) {
        return generatePackage(Notation.getNotation(ctx), p);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param cls   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateClassifier(
        NotationContext ctx,
        Object/*MClassifier*/ cls) {
        return generateClassifier(Notation.getNotation(ctx), cls);
    }

    /**
     * There is no need for a version that handles a Collection of
     * Stereotypes, since that is only needed for display, 
     * and hence already handled in ParserDisplay, e.g. for attributes.
     * 
     * @param ctx  Context used to identify the notation
     * @param s   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateStereotype(
            NotationContext ctx,
            Object/*MStereotype*/ s) {
        return generateStereotype(Notation.getNotation(ctx), s);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param s   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateTaggedValue(
        NotationContext ctx,
        Object/*MTaggedValue*/ s) {
        return generateTaggedValue(Notation.getNotation(ctx), s);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param a   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateAssociation(
        NotationContext ctx,
        Object/*MAssociation*/ a) {
        return generateAssociation(Notation.getNotation(ctx), a);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param ae   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateAssociationEnd(
        NotationContext ctx,
        Object/*MAssociationEnd*/ ae) {
        return generateAssociationEnd(Notation.getNotation(ctx), ae);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param m   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateMultiplicity(
        NotationContext ctx,
        Object/*MMultiplicity*/ m) {
        return generateMultiplicity(Notation.getNotation(ctx), m);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param m   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateState(NotationContext ctx,
				       Object/*MState*/ m) {
        return generateState(Notation.getNotation(ctx), m);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param m   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateStateBody(NotationContext ctx,
					   Object/*MState*/ m) {
        return generateStateBody(Notation.getNotation(ctx), m);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param m   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateTransition(
        NotationContext ctx,
        Object/*MTransition*/ m) {
        return generateTransition(Notation.getNotation(ctx), m);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param m   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateVisibility(
        NotationContext ctx,
        Object m) {
        return generateVisibility(Notation.getNotation(ctx), m);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param m   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateAction(NotationContext ctx, Object m) {
        return generateAction(Notation.getNotation(ctx), m);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param m   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateGuard(NotationContext ctx,
				       Object/*MGuard*/ m) {
        return generateGuard(Notation.getNotation(ctx), m);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param m   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateMessage(NotationContext ctx,
					 Object/*MMessage*/ m) {
        return generateMessage(Notation.getNotation(ctx), m);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param cls   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateClassifierRef(
        NotationContext ctx,
        Object/*MClassifier*/ cls) {
        return generateClassifierRef(Notation.getNotation(ctx), cls);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param m   The UML element to generate for.
     * @return     The generated text.
     */
    public static String generateAssociationRole(
        NotationContext ctx,
        Object/*MAssociationRole*/ m) {
        return generateAssociationRole(Notation.getNotation(ctx), m);
    }

    /**
     * General purpose static generator for any object that wishes to set
     * the documented flag.<p>
     *
     * Uses the class of the object to determine which method to
     * invoke. Only actually looks for MOperation and MAttribute. All others
     * invoke the simpler version with no documented flag, so taking the
     * default version.
     *
     * @param ctx        The context to look up the notation generator.
     *
     * @param o          The object to generate.
     *
     * @param documented  This flag only has any effect for
     * Operations and Attributes.
     * See {@link GeneratorJava#generateConstraintEnrichedDocComment()}.
     *
     * @return            The generated string.
     */
    public static String generate(
        NotationContext ctx,
        Object o,
        boolean documented) {
        if (o == null) {
            return "";
	}
        return generate(getNotation(ctx), o, documented);
    }

    /**
     * General purpose static generator for any object that wishes to set
     * the documented flag.<p>
     *
     * Uses the class of the object to determine which method to
     * invoke. Only actually looks for MOperation and MAttribute. All others
     * invoke the simpler version with no documented flag, so taking the
     * default version.
     *
     * @param nn         The notation name.
     *
     * @param o          The object to generate.
     *
     * @param documented  This flag only has any effect for Operations and
     * Attributes.
     * See {@link GeneratorJava#generateConstraintEnrichedDocComment()}.
     *
     * @return            The generated string.
     */
    public static String generate(
        NotationName nn,
        Object o,
        boolean documented) {
        if (o == null) {
            return "";
	}
        if (Model.getFacade().isAOperation(o)) {
            return generateOperation(nn, /*(MOperation)*/ o,
                    		     documented);
	}
        if (Model.getFacade().isAAttribute(o)) {
            return generateAttribute(nn, /*(MAttribute)*/ o,
				     documented);
	}
        return generate(nn, o);
    }

    /**
     * @param ctx  Context used to identify the notation
     * @param o    The UML element to generate for.
     * @return     The generated string.
     */
    public static String generate(NotationContext ctx, Object o) {
        if (o == null) {
            return "";
	}
        return generate(getNotation(ctx), o);
    }

    /**
     * @param nn   The NotationName to be used for the generation
     * @param o    The UML element to generate for.
     * @return     The generated string.
     */
    public static String generate(NotationName nn, Object o) {
        if (o == null) {
            return "";
	}
        //added to support association roles
        if (Model.getFacade().isAAssociationRole(o)) {
            return generateAssociationRole(nn, o);
        }

        // Added to support extension points
        if (Model.getFacade().isAExtensionPoint(o)) {
            return generateExtensionPoint(nn, o);
        }

        if (Model.getFacade().isAOperation(o)) {
            return generateOperation(nn, o, false);
	}
        if (Model.getFacade().isASubmachineState(o)) {
            return generateSubmachine(nn, o);
	}
        if (Model.getFacade().isAAttribute(o)) {
            return generateAttribute(nn, o, false);
	}
        if (Model.getFacade().isAParameter(o)) {
            return generateParameter(nn, o);
	}
        if (Model.getFacade().isAPackage(o)) {
            return generatePackage(nn, o);
	}
        if (Model.getFacade().isAClassifier(o)) {
            return generateClassifier(nn, o);
	}
        if (Model.getFacade().isAExpression(o)) {
            return generateExpression(nn, o);
	}
        if (o instanceof String) {
            return generateName(nn, (String) o);
	}
        // if (o instanceof String) {
        //     return generateUninterpreted(nn,(String) o);
	// }
        if (Model.getFacade().isAStereotype(o)) {
            return generateStereotype(nn, o);
	}
        if (Model.getFacade().isATaggedValue(o)) {
            return generateTaggedValue(nn, o);
	}
        if (Model.getFacade().isAAssociation(o)) {
            return generateAssociation(nn, o);
	}
        if (Model.getFacade().isAAssociationEnd(o)) {
            return generateAssociationEnd(nn, o);
	}
        if (Model.getFacade().isAMultiplicity(o)) {
            return generateMultiplicity(nn, o);
	}
        if (Model.getFacade().isAActionState(o)) {
            return generateActionState(nn, o);
        }
        if (Model.getFacade().isAObjectFlowState(o)) {
            return generateObjectFlowState(nn, o);
        }
        if (Model.getFacade().isAClassifierInState(o)) {
            return generateClassifierInState(nn, o);
        }
        if (Model.getFacade().isAState(o)) {
            return generateState(nn, o);
	}
        if (Model.getFacade().isATransition(o)) {
            return generateTransition(nn, o);
	}
        if (Model.getFacade().isAAction(o)) {
            return generateAction(nn, o);
	}
        if (Model.getFacade().isACallAction(o)) {
            return generateAction(nn, o);
	}
        if (Model.getFacade().isAGuard(o)) {
            return generateGuard(nn, o);
	}
        if (Model.getFacade().isAMessage(o)) {
            return generateMessage(nn, o);
	}
        if (Model.getFacade().isAVisibilityKind(o)) {
            return generateVisibility(nn, o);
	}

        if (Model.getFacade().isAModelElement(o)) {
            return generateName(nn, Model.getFacade().getName(o));
	}
        return o.toString();
    }

    /**
     * @param context the notation context
     * @return the notation name
     */
    public static NotationName getNotation(NotationContext context) {
        // UML is the default language
        if (Configuration.getBoolean(Notation.KEY_UML_NOTATION_ONLY, false)) {
            return notationArgo;
        }
        // Otherwise we use the given context
        return context.getContextNotation();
    }

    /**
     * Called after the notation default property gets changed.
     *
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        LOG.info(
            "Notation change:"
                + pce.getOldValue()
                + " to "
                + pce.getNewValue());
        ArgoEventPump.fireEvent(
            new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, pce));
    }

    ////////////////////////////////////////////////////////////////
    // Static workers for dealing with notation names.

    /**
     * Get list of available notations.
     *
     * @return list of available notations
     */
    public static ArrayList getAvailableNotations() {
        return NotationNameImpl.getAvailableNotations();
    }

    /**
     * Create a versioned notation name with an icon.
     *
     * @param k1 the name (e.g. UML)
     * @param k2 the version (e.g. 1.3)
     * @param icon the icon
     * @return the notation name
     */
    public static NotationName makeNotation(String k1, String k2, Icon icon) {
        NotationName nn = NotationNameImpl.makeNotation(k1, k2, icon);
        // Making the first created notation the default.
        if (notationArgo == null) {
            notationArgo = nn;
        }
        return nn;
    }

    /**
     * @return <code>true</code> if guillemots (&laquo; and &raquo;) are used
     * instead of &lt;&lt; and &gt;&gt;.
     */
    public static boolean getUseGuillemots() {
        return Configuration.getBoolean(KEY_USE_GUILLEMOTS, false);
    }

    /**
     * @param useGuillemots <code>true</code> if guillemots (&laquo;
     * and &raquo;) shall be used instead of &lt;&lt; and &gt;&gt;.
     */
    public static void setUseGuillemots(boolean useGuillemots) {
        Configuration.setBoolean(KEY_USE_GUILLEMOTS, useGuillemots);
    }

    /**
     * Get the default width for Fig shadows.
     *
     * @return the default width for Fig shadows
     */
    public static int getDefaultShadowWidth() {
        return Configuration.getInteger(KEY_DEFAULT_SHADOW_WIDTH, 1);
    }

    /**
     * Set the default width for Fig Shadow.
     *
     * @param width    the Fig shadow width
     */
    public static void setDefaultShadowWidth(int width) {
        Configuration.setInteger(KEY_DEFAULT_SHADOW_WIDTH, width);
    }

} // END NOTATION
