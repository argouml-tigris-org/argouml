// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.language.helpers;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.Notation;
import org.argouml.application.api.NotationName;
import org.argouml.application.api.NotationProvider2;
import org.argouml.model.ModelFacade;

/** This class is the abstract super class that defines a code
 * generation framework.  It is basically a depth-first traversal of
 * the UML model that generates strings as it goes.  This framework
 * should probably be redesigned to separate the traversal logic from
 * the generation logic.  See the <a href=
 * "http://hillside.net/patterns/">Vistor design
 * pattern</a> in "Design Patterns", and the <a href=
 * "http://www.ccs.neu.edu/research/demeter/"> Demeter project</a>. */

public abstract class NotationHelper
    implements NotationProvider2
{

    private NotationName notationName;

    /**
     * The constructor.
     * 
     * @param nn the notation name
     */
    public NotationHelper(NotationName nn) {
	notationName = nn;
    }


    /**
     * @see org.argouml.application.api.NotationProvider2#getNotation()
     */
    public NotationName getNotation() {
	return notationName;
    }

    /**
     * @param o the object to be generated
     * @return the generated string
     */
    public String generate(Object o) {
	if (o == null)
	    return "";
	if (ModelFacade.isAOperation(o))
	    return generateOperation(o);
	if (ModelFacade.isAAttribute(o))
	    return generateAttribute(o);
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
	if (ModelFacade.isATaggedValue(o))
	    return generateTaggedValue(o);
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

	if (ModelFacade.isAModelElement(o))
	    return generateName(ModelFacade.getName(o));

	if (o == null) return "";

	return o.toString();
    }

    /**
     * @param op the given object
     * @return the generated string
     */
    public abstract String generateOperation(Object op);

    /**
     * @param attr the given object
     * @return the generated string
     */
    public abstract String generateAttribute(Object attr);

    /**
     * @param param the given object
     * @return the generated string
     */
    public abstract String generateParameter(Object param);

    /**
     * @param p the given object
     * @return the generated string
     */
    public abstract String generatePackage(Object p);

    /**
     * @param cls the given object
     * @return the generated string
     */
    public abstract String generateClassifier(Object cls);
    
    // public abstract String generateStereotype(Object s);

    /**
     * @param s the given object
     * @return the generated string
     */
    public abstract String generateTaggedValue(Object s);

    /**
     * @param a the given object
     * @return the generated string
     */
    public abstract String generateAssociation(Object a);

    /**
     * @param ae the given object
     * @return the generated string
     */
    public abstract String generateAssociationEnd(Object ae);

    /**
     * @param m the given object
     * @return the generated string
     */
    public abstract String generateMultiplicity(Object m);

    /**
     * @param m the given object
     * @return the generated string
     */
    public abstract String generateState(Object m);

    /**
     * @param m the given object
     * @return the generated string
     */
    public abstract String generateTransition(Object m);

    /**
     * @param m the given object
     * @return the generated string
     */
    public abstract String generateAction(Object m);

    /**
     * @param m the given object
     * @return the generated string
     */
    public abstract String generateGuard(Object m);

    /**
     * @param m the given object
     * @return the generated string
     */
    public abstract String generateMessage(Object m);

    /**
     * @return the left pointing guillemot, i.e. << or the one-character symbol
     */
    public static String getLeftGuillemot() {
	return (Configuration.getBoolean(Notation.KEY_USE_GUILLEMOTS, false))
	    ? "\u00ab"
	    : "<<";

    }

    /**
     * @return the right pointing guillemot, i.e. >> or the one-character symbol
     */
    public static String getRightGuillemot() {
	return (Configuration.getBoolean(Notation.KEY_USE_GUILLEMOTS, false))
	    ? "\u00bb"
	    : ">>";
    }


    /**
     * @param s the given object
     * @return the generated string
     */
    public String generateStereotype(Object s) {
	return getLeftGuillemot()
	    + generateName(ModelFacade.getName(s))
	    + getRightGuillemot();
    }


    /**
     * @param expr the given object
     * @return the generated string
     */
    public String generateExpression(Object expr) {
	if (expr == null)
	    return "";
	if (ModelFacade.isAExpression(expr))
	    return generateUninterpreted((String) ModelFacade.getBody(expr));
	if (ModelFacade.isAConstraint(expr))
	    return generateExpression(ModelFacade.getBody(expr));
	return "";
    }


    /**
     * @param n the given object
     * @return the generated string
     */
    public String generateName(String n) {
	return n;
    }


    /**
     * @param un the given object
     * @return the generated string
     */
    public String generateUninterpreted(String un) {
	if (un == null) return "";
	return un;
    }


    /**
     * @param cls the given object
     * @return the generated string
     */
    public String generateClassifierRef(Object cls) {
	if (cls == null) return "";
	return ModelFacade.getName(cls);
    }

} /* end class NotationHelper */