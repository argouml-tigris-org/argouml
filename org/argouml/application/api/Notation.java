// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// File: Notation.java
// Classes: Notation
// Original Author: Thierry Lach
// $Id$

// 8 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// extension points.


package org.argouml.application.api;

import org.argouml.application.notation.*;
import org.argouml.application.events.*;

import java.util.*;
import java.beans.*;

import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.model_management.*;

import org.apache.log4j.*;


/** Provides centralized methods dealing with notation.
 *
 *  @stereotype singleton
 *  @author Thierry Lach
 *  @since 0.9.4
 */

public final class Notation
implements PropertyChangeListener {

  /** Define a static log4j category variable for ArgoUML notation.
   */
  public final static Logger cat = Logger.getLogger("org.argouml.application.notation");
    // TODO:  JDK 1.2 seems to not return the package name if
    // not running from a jar.
    //
  // public final static Category cat = Category.getInstance(NotationNameImpl.class.getPackage().getName());

  /** The name of the default Argo notation.  This notation is
   *  part of Argo core distribution.
   */
    private static NotationName NOTATION_ARGO = org.argouml.uml.generator.GeneratorDisplay.getInstance().getNotation();

  /** The name of the Argo java-like notation.  This notation is
   *  part of Argo core distribution.
   */
    // public static final NotationName NOTATION_JAVA =
    // org.argouml.language.java.generator.GeneratorJava.getInstance().getNotation();

  /** The configuration key for the preferred notation
   */
  public static final ConfigurationKey KEY_DEFAULT_NOTATION =
         Configuration.makeKey("notation", "default");

  /** The configuration key that indicates whether to show stereotypes
   *  in the navigation panel
   */
  public static final ConfigurationKey KEY_SHOW_STEREOTYPES = Configuration.makeKey("notation", "navigation", "show", "stereotypes");

  /** The configuration key that indicates whether to use guillemots
   *  or greater/lessthan characters in stereotypes.
   */
  public static final ConfigurationKey KEY_USE_GUILLEMOTS = Configuration.makeKey("notation", "guillemots");

  /**
   * Indicates if the user only wants to see UML notation.
   */
  public static final ConfigurationKey KEY_UML_NOTATION_ONLY = Configuration.makeKey("notation", "only", "uml");

  /**
   * Indicates if the user wants to see visibility signs (public, private, protected or # + -)
   */
  public static final ConfigurationKey KEY_SHOW_VISIBILITY = Configuration.makeKey("notation", "show", "visibility");

  /**
   * Indicates if the user wants to see multiplicity in attributes and classes
   */
  public static final ConfigurationKey KEY_SHOW_MULTIPLICITY = Configuration.makeKey("notation", "show", "multiplicity");

  /**
   * Indicates if the user wants to see the initial value
   */
  public static final ConfigurationKey KEY_SHOW_INITIAL_VALUE = Configuration.makeKey("notation", "show", "initialvalue");

  /**
   * Indicates if the user wants to see the properties (everything between braces), that is for example the concurrency
   */
  public static final ConfigurationKey KEY_SHOW_PROPERTIES = Configuration.makeKey("notation", "show", "properties");

    /**
     * Default value for the shadow size of classes, interfaces etc.
     */
    public static final ConfigurationKey KEY_DEFAULT_SHADOW_WIDTH = Configuration.makeKey("notation","default","shadow-width");

  private static Notation SINGLETON = new Notation();

  // private ArrayList _providers = null;
  // private NotationProvider _defaultProvider = null;

  private Notation() {
      // _providers = new ArrayList();
      // _defaultNotation = org.argouml.language.uml.NotationUml;
      Configuration.addListener(KEY_USE_GUILLEMOTS, this);
      Configuration.addListener(KEY_DEFAULT_NOTATION, this);
      Configuration.addListener(KEY_UML_NOTATION_ONLY, this);
  }

  /** Remove the notation change listener.
   *  <code>finalize</code> should never happen, but play it safe.
   */
  public void finalize() {
      Configuration.removeListener(KEY_DEFAULT_NOTATION, this);
      Configuration.removeListener(KEY_USE_GUILLEMOTS, this);
      Configuration.removeListener(KEY_UML_NOTATION_ONLY, this);
  }

  private NotationProvider getProvider(NotationName notation) {
      NotationProvider np = null;
      np = NotationProviderFactory.getInstance().getProvider(notation);
      cat.debug ("getProvider(" + notation + ") returns " + np);
      return np;
  }

  public static void setDefaultNotation(NotationName n) {
      cat.info ("default notation set to " + n.getConfigurationValue());
      Configuration.setString(KEY_DEFAULT_NOTATION, n.getConfigurationValue());
  }

  public static NotationName findNotation(String s) {
      return NotationNameImpl.findNotation(s);
  }

  private static boolean reportedNotationProblem = false;

  public static NotationName getDefaultNotation() {
    NotationName n = NotationNameImpl.findNotation(Configuration.getString(KEY_DEFAULT_NOTATION, NOTATION_ARGO.getConfigurationValue()));
      // TODO:
      // This is needed for the case when the default notation is
      // not loaded at this point.
      // We need then to refetch the default notation from the configuration
      // at a later stage.
      if (n == null)
	  n = NotationNameImpl.findNotation("Uml.1.3");
      cat.debug ("default notation is " + n.getConfigurationValue());
      return n;
  }
  ////////////////////////////////////////////////////////////////
  // class accessors

    /**
     * <p>General accessor for an extension point.</p>
     *
     * @param notation    Name of the notation to be used.
     *
     * @param ep          The extension point to generate for.
     *
     * @return            The generated text.
     */

    protected String generateExtensionPoint(NotationName notation,
                                            MExtensionPoint ep) {
        return getProvider(notation).generateExtensionPoint(ep);
    }
  protected String generateOperation(NotationName notation, MOperation op,
                                     boolean documented) {
      return getProvider(notation).generateOperation(op, documented);
  }
  protected String generateAttribute(NotationName notation, MAttribute attr,
                                     boolean documented) {
      return getProvider(notation).generateAttribute(attr, documented);
  }
  protected String generateParameter(NotationName notation, MParameter param) {
      return getProvider(notation).generateParameter(param);
  }
  protected String generateName(NotationName notation, String name) {
      return getProvider(notation).generateName(name);
  }
  protected String generatePackage(NotationName notation, MPackage pkg) {
      return getProvider(notation).generatePackage(pkg);
  }
  protected String generateExpression(NotationName notation, MExpression expr) {
      return getProvider(notation).generateExpression(expr);
  }
  protected String generateClassifier(NotationName notation, MClassifier cls) {
      return getProvider(notation).generateClassifier(cls);
  }
  protected String generateStereotype(NotationName notation, MStereotype s) {
      return getProvider(notation).generateStereotype(s);
  }
  protected String generateTaggedValue(NotationName notation, MTaggedValue s) {
      return getProvider(notation).generateTaggedValue(s);
  }
  protected String generateAssociation(NotationName notation, MAssociation a) {
      return getProvider(notation).generateAssociation(a);
  }
  protected String generateAssociationEnd(NotationName notation, MAssociationEnd ae) {
      return getProvider(notation).generateAssociationEnd(ae);
  }
  protected String generateMultiplicity(NotationName notation, MMultiplicity m) {
      return getProvider(notation).generateMultiplicity(m);
  }
  protected String generateState(NotationName notation, MState m) {
      return getProvider(notation).generateState(m);
  }
  protected String generateStateBody(NotationName notation, MState stt) {
      return getProvider(notation).generateStateBody(stt);
  }
  protected String generateTransition(NotationName notation, MTransition m) {
      return getProvider(notation).generateTransition(m);
  }
  protected String generateAction(NotationName notation, MAction m) {
      return getProvider(notation).generateAction(m);
  }
  protected String generateGuard(NotationName notation, MGuard m) {
      return getProvider(notation).generateGuard(m);
  }
  protected String generateMessage(NotationName notation, MMessage m) {
      return getProvider(notation).generateMessage(m);
  }
  protected String generateClassifierRef(NotationName notation, MClassifier m) {
      return getProvider(notation).generateClassifierRef(m);
  }
  protected String generateAssociationRole(NotationName notation, MAssociationRole m){
	return getProvider(notation).generateAssociationRole(m);
  }

  ////////////////////////////////////////////////////////////////
  // static accessors

  public static Notation getInstance() { return SINGLETON; }


    /**
     * <p>Static accessor for extension point generation. Invokes our protected
     *   accessor from the singleton instance with the "documented" flag set
     *   false.</p>
     *
     * @param ctx  Context used to identify the notation
     *
     * @param ep   The extension point to generate for.
     *
     * @return     The generated text.
     */

    public static String generateExtensionPoint(NotationContext ctx,
                                                MExtensionPoint ep) {
        return SINGLETON.generateExtensionPoint(Notation.getNotation(ctx), ep);
    }


  public static String generateOperation(NotationContext ctx, MOperation op) {
      return SINGLETON.generateOperation(Notation.getNotation(ctx), op, false);
  }
  public static String generateOperation(NotationContext ctx,
                                         MOperation op,
					 boolean documented) {
      return SINGLETON.generateOperation(Notation.getNotation(ctx), op,
                                         documented);
  }
  public static String generateAttribute(NotationContext ctx, MAttribute attr) {
      return SINGLETON.generateAttribute(Notation.getNotation(ctx), attr,
                                         false);
  }
  public static String generateAttribute(NotationContext ctx,
                                               MAttribute attr,
					       boolean documented) {
      return SINGLETON.generateAttribute(Notation.getNotation(ctx), attr,
                                         documented);
  }
  public static String generateParameter(NotationContext ctx, MParameter param) {
      return SINGLETON.generateParameter(Notation.getNotation(ctx), param);
  }
  public static String generatePackage(NotationContext ctx, MPackage p) {
      return SINGLETON.generatePackage(Notation.getNotation(ctx), p);
  }
  public static String generateClassifier(NotationContext ctx, MClassifier cls) {
      return SINGLETON.generateClassifier(Notation.getNotation(ctx), cls);
  }
  public static String generateStereotype(NotationContext ctx, MStereotype s) {
      return SINGLETON.generateStereotype(Notation.getNotation(ctx), s);
  }
  public static String generateTaggedValue(NotationContext ctx, MTaggedValue s) {
      return SINGLETON.generateTaggedValue(Notation.getNotation(ctx), s);
  }
  public static String generateAssociation(NotationContext ctx, MAssociation a) {
      return SINGLETON.generateAssociation(Notation.getNotation(ctx), a);
  }
  public static String generateAssociationEnd(NotationContext ctx, MAssociationEnd ae) {
      return SINGLETON.generateAssociationEnd(Notation.getNotation(ctx), ae);
  }
  public static String generateMultiplicity(NotationContext ctx, MMultiplicity m) {
      return SINGLETON.generateMultiplicity(Notation.getNotation(ctx), m);
  }
  public static String generateState(NotationContext ctx, MState m) {
      return SINGLETON.generateState(Notation.getNotation(ctx), m);
  }
  public static String generateStateBody(NotationContext ctx, MState m) {
      return SINGLETON.generateStateBody(Notation.getNotation(ctx), m);
  }
  public static String generateTransition(NotationContext ctx, MTransition m) {
      return SINGLETON.generateTransition(Notation.getNotation(ctx), m);
  }
  public static String generateAction(NotationContext ctx, MAction m) {
      return SINGLETON.generateAction(Notation.getNotation(ctx), m);
  }
  public static String generateGuard(NotationContext ctx, MGuard m) {
      return SINGLETON.generateGuard(Notation.getNotation(ctx), m);
  }
  public static String generateMessage(NotationContext ctx, MMessage m) {
      return SINGLETON.generateMessage(Notation.getNotation(ctx), m);
  }
  public static String generateClassifierRef(NotationContext ctx,
                                             MClassifier cls) {
      return SINGLETON.generateClassifierRef(Notation.getNotation(ctx), cls);
  }
  public static String generateAssociationRole(NotationContext ctx, MAssociationRole m){
        return SINGLETON.generateAssociationRole(Notation.getNotation(ctx), m);
  }

    /**
     * <p>General purpose static generator for any object that wishes to set
     *   the documented flag.</p>
     *
     * <p>Uses the class of the object to determine which method to
     *   invoke. Only actually looks for MOperation and MAttribute. All others
     *   invoke the simpler version with no documented flag, so taking the
     *   default version.</p>
     *
     * @param ctx        The context to look up the notation generator.
     *
     * @param o          The object to generate.
     *
     * @param documented  A flag of unknown meaning. Only has any effect for
     *                    {@link MOperation} and {@link MAttribute}.
     *
     * @return            The generated string.
     */
  public static String generate(NotationContext ctx, Object o,
                                boolean documented) {
    if (o == null)
      return "";
    return generate(getNotation(ctx), o, documented);
  }

    /**
     * <p>General purpose static generator for any object that wishes to set
     *   the documented flag.</p>
     *
     * <p>Uses the class of the object to determine which method to
     *   invoke. Only actually looks for MOperation and MAttribute. All others
     *   invoke the simpler version with no documented flag, so taking the
     *   default version.</p>
     *
     * @param ctx        The notation name.
     *
     * @param o          The object to generate.
     *
     * @param documented  A flag of unknown meaning. Only has any effect for
     *                    {@link MOperation} and {@link MAttribute}.
     *
     * @return            The generated string.
     */
  public static String generate(NotationName nn, Object o,
                                boolean documented) {
    if (o == null)
      return "";
    if (o instanceof MOperation)
      return SINGLETON.generateOperation(nn, (MOperation) o, documented);
    if (o instanceof MAttribute)
      return SINGLETON.generateAttribute(nn, (MAttribute) o, documented);
    return generate(nn, o);
  }

  public static String generate(NotationContext ctx, Object o) {
    if (o == null)
      return "";
    return generate(getNotation(ctx), o);
  }

  public static String generate(NotationName nn, Object o) {
    if (o == null)
      return "";

    //added to support association roles
    if (o instanceof MAssociationRole){
        return SINGLETON.generateAssociationRole(nn, (MAssociationRole)o);
    }

    // Added to support extension points
    if (o instanceof MExtensionPoint) {
        return SINGLETON.generateExtensionPoint(nn,
                                                (MExtensionPoint) o);
    }

    if (o instanceof MOperation)
      return SINGLETON.generateOperation(nn, (MOperation) o, false);
    if (o instanceof MAttribute)
      return SINGLETON.generateAttribute(nn, (MAttribute) o, false);
    if (o instanceof MParameter)
      return SINGLETON.generateParameter(nn,(MParameter) o);
    if (o instanceof MPackage)
      return SINGLETON.generatePackage(nn,(MPackage) o);
    if (o instanceof MClassifier)
      return SINGLETON.generateClassifier(nn,(MClassifier) o);
    if (o instanceof MExpression)
      return SINGLETON.generateExpression(nn,(MExpression) o);
    if (o instanceof String)
      return SINGLETON.generateName(nn,(String) o);
    // if (o instanceof String)
    //   return SINGLETON.generateUninterpreted(nn,(String) o);
    if (o instanceof MStereotype)
      return SINGLETON.generateStereotype(nn,(MStereotype) o);
    if (o instanceof MTaggedValue)
      return SINGLETON.generateTaggedValue(nn,(MTaggedValue) o);
    if (o instanceof MAssociation)
      return SINGLETON.generateAssociation(nn,(MAssociation)o);
    if (o instanceof MAssociationEnd)
      return SINGLETON.generateAssociationEnd(nn,(MAssociationEnd)o);
    if (o instanceof MMultiplicity)
      return SINGLETON.generateMultiplicity(nn,(MMultiplicity)o);
    if (o instanceof MState)
      return SINGLETON.generateState(nn,(MState)o);
    if (o instanceof MTransition)
      return SINGLETON.generateTransition(nn,(MTransition)o);
    if (o instanceof MAction)
      return SINGLETON.generateAction(nn,(MAction)o);
    if (o instanceof MCallAction)
      return SINGLETON.generateAction(nn,(MAction)o);
    if (o instanceof MGuard)
      return SINGLETON.generateGuard(nn,(MGuard)o);
    if (o instanceof MMessage)
      return SINGLETON.generateMessage(nn,(MMessage)o);

    if (o instanceof MModelElement)
      return SINGLETON.generateName(nn,((MModelElement)o).getName());

    return o.toString();
  }

    public static NotationName getNotation(NotationContext context) {
        // TODO: base it on the configuration.
	// Make sure you check the ModelElement to see if it has
	// an override on the notation.

	// UML is the default language
	if (Configuration.getBoolean(Notation.KEY_UML_NOTATION_ONLY, false)) {
            return NOTATION_ARGO;
	}
	return context.getContextNotation();
    }

    /** Called after the notation default property gets changed.
     */
    public void propertyChange(PropertyChangeEvent pce) {
	cat.info ("Notation change:" + pce.getOldValue() + " to " + pce.getNewValue());
        ArgoEventPump.fireEvent(
	             new ArgoNotationEvent(ArgoEvent.NOTATION_CHANGED, pce));
    }

    public NotationProvider getDefaultProvider() {
       return NotationProviderFactory.getInstance().getDefaultProvider();
    }

  ////////////////////////////////////////////////////////////////
  // TODO:  The following accessors are commented out
  //                   and should be uncommented by those initially
  //                   incorporating this code into production,
  //                   only using those methods that are necessary.
  ////////////////////////////////////////////////////////////////

  // public static void parseExtensionPointCompartment(NotationContext ctx, MUseCase uc, String s) {
      // SINGLETON.getParser(Notation.getNotation(ctx)).parseExtensionPointCompartment(uc, s);
  // }

  // public static void parseOperationCompartment(NotationContext ctx, MClassifier cls, String s) {
      // SINGLETON.getParser(Notation.getNotation(ctx)).parseOperationCompartment(cls, s);
  // }

  // public static void parseAttributeCompartment(NotationContext ctx, MClassifier cls, String s) {
      // SINGLETON.getParser(Notation.getNotation(ctx)).parseAttributeCompartment(cls, s);
  // }

  // public static MExtensionPoint parseExtensionPoint(NotationContext ctx, String s) {
      // return SINGLETON.getParser(Notation.getNotation(ctx)).parseExtensionPoint(s);
  // }

  // public static MOperation parseOperation(NotationContext ctx, String s) {
      // return SINGLETON.getParser(Notation.getNotation(ctx)).parseOperation(s);
  // }

  // public static MAttribute parseAttribute(NotationContext ctx, String s) {
      // return SINGLETON.getParser(Notation.getNotation(ctx)).parseAttribute(s);
  // }

  // public static String parseOutVisibility(NotationContext ctx, MFeature f, String s) { }

  // public static String parseOutKeywords(NotationContext ctx, MFeature f, String s) { }

  // public static String parseOutReturnType(NotationContext ctx, MOperation op, String s) { }

  // public static String parseOutParams(NotationContext ctx, MOperation op, String s) { }

  // public static String parseOutName(NotationContext ctx, MModelElement me, String s) { }

  // public static String parseOutType(NotationContext ctx, MAttribute attr, String s) { }

  // public static String parseOutInitValue(NotationContext ctx, MAttribute attr, String s) { }

  // public static String parseOutColon(NotationContext ctx, String s) { }

  // public static MParameter parseParameter(NotationContext ctx, String s) { }

  // public static Package parsePackage(NotationContext ctx, String s) { }

  // public static MClassImpl parseClassifier(NotationContext ctx, String s) { }

  // public static MStereotype parseStereotype(NotationContext ctx, String s) { }

  // public static MTaggedValue parseTaggedValue(NotationContext ctx, String s) { }

   // public static MAssociation parseAssociation(NotationContext ctx, String s) { }

   // public static MAssociationEnd parseAssociationEnd(NotationContext ctx, String s) { }

  // public static MMultiplicity parseMultiplicity(NotationContext ctx, String s) { }

  // public static MState parseState(NotationContext ctx, String s) { }

  // public static void parseStateBody(NotationContext ctx, MState st, String s) { }

  // public static void parseStateEntyAction(NotationContext ctx, MState st, String s) { }

  // public static void parseStateExitAction(NotationContext ctx, MState st, String s) { }

  // public static MTransition parseTransition(NotationContext ctx, MTransition trans, String s) { }

  // public static void parseClassifierRole(NotationContext ctx, MClassifierRole cls, String s) { }

  // public static void parseMessage(NotationContext ctx, MMessage mes, String s) { }

  // public static void parseStimulus(NotationContext ctx, MStimulus sti, String s) { }

  // public static MAction parseAction(NotationContext ctx, String s) { }

  // public static MGuard parseGuard(NotationContext ctx, String s) { }

  // public static MEvent parseEvent(NotationContext ctx, String s) { }

  // public static void parseObject(NotationContext ctx, MObject obj, String s) { }

  // public static void parseNodeInstance(NotationContext ctx, MNodeInstance noi, String s) { }

  // public static void parseComponentInstance(NotationContext ctx, MComponentInstance coi, String s) { }

  ////////////////////////////////////////////////////////////////
  // Static workers for dealing with notation names.

    /** List of available notations.
     */
    public static ArrayList getAvailableNotations() {
	return NotationNameImpl.getAvailableNotations();
    }

  /** Create a versioned notation name with an icon.
   */
  public static NotationName makeNotation(String k1, String k2, Icon icon) {
      NotationName nn = NotationNameImpl.makeNotation(k1, k2, icon);
      // Making the first created notation the default.
      if (NOTATION_ARGO == null) {
	  NOTATION_ARGO = nn;
      }
      return nn;
  }

  public static boolean getUseGuillemots() {
     return Configuration.getBoolean(KEY_USE_GUILLEMOTS, false);
  }

  public static void setUseGuillemots(boolean useGuillemots) {
     Configuration.setBoolean(KEY_USE_GUILLEMOTS, useGuillemots);
  }

    /** get the default width for Fig shadows. */
    public static int getDefaultShadowWidth() {
	return Configuration.getInteger(KEY_DEFAULT_SHADOW_WIDTH, 1);
    }

    /** set the default width for Fig Shadow. */
    public static void setDefaultShadowWidth(int width) {
	Configuration.setInteger(KEY_DEFAULT_SHADOW_WIDTH, width);
    }
} // END NOTATION
