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

    private NotationName _notationName;

    public NotationHelper(NotationName notationName) {
	_notationName = notationName;
    }


    public NotationName getNotation() {
	return _notationName;
    }

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

    public abstract String generateOperation(Object op);
    public abstract String generateAttribute(Object attr);
    public abstract String generateParameter(Object param);
    public abstract String generatePackage(Object p);
    public abstract String generateClassifier(Object cls);
    // public abstract String generateStereotype(Object s);
    public abstract String generateTaggedValue(Object s);
    public abstract String generateAssociation(Object a);
    public abstract String generateAssociationEnd(Object ae);
    public abstract String generateMultiplicity(Object m);
    public abstract String generateState(Object m);
    public abstract String generateTransition(Object m);
    public abstract String generateAction(Object m);
    public abstract String generateGuard(Object m);
    public abstract String generateMessage(Object m);

    public static String getLeftGuillemot() {

	return (Configuration.getBoolean(Notation.KEY_USE_GUILLEMOTS, false))
	    ? "\u00ab"
	    : "<<";

    }

    public static String getRightGuillemot() {
	return (Configuration.getBoolean(Notation.KEY_USE_GUILLEMOTS, false))
	    ? "\u00bb"
	    : ">>";
    }

    public String generateStereotype(Object s) {
	return getLeftGuillemot()
	    + generateName(ModelFacade.getName(s))
	    + getRightGuillemot();
    }

    public String generateExpression(Object expr) {
	if (expr == null)
	    return "";
	if (ModelFacade.isAExpression(expr))
	    return generateUninterpreted((String)ModelFacade.getBody(expr));
	if (ModelFacade.isAConstraint(expr))
	    return generateExpression(ModelFacade.getBody(expr));
	return "";
    }

    public String generateName(String n) {
	return n;
    }

    public String generateUninterpreted(String un) {
	if (un == null) return "";
	return un;
    }

    public String generateClassifierRef(Object cls) {
	if (cls == null) return "";
	return ModelFacade.getName(cls);
    }

} /* end class NotationHelper */