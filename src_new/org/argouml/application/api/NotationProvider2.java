// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

package org.argouml.application.api;

/**
 * Interface provided by classes that provide a notation.<p>
 * 
 * This interface is used by all elements in the Diagrams whenever
 * some UML-object needs to be converted into a text string.<p>
 * 
 * For UML this interface is implemented by 
 * {@link org.argouml.uml.generator.GeneratorDisplay}. 
 * For Java it is implemented by 
 * {@link org.argouml.language.java.generator.GeneratorJava}.
 * 
 * TODO: {@link org.argouml.uml.generator.ParserDisplay} and this interface 
 * should probably be joined into an editable field instead.
 *  
 * @see org.argouml.language
 */
public interface NotationProvider2 {

    /**
     * @return The name of this notation.
     */
    public NotationName getNotation();

    /**
     * Generate the String representation for an ExtensionPoint.
     *
     * @param op Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateExtensionPoint(Object op);

    /**
     * Generate the String representation for an Operation.
     *
     * @param op Object to generate representation for.
     * @param documented <tt>true</tt> if documentation shall be generated.
     * @return The String representation of the object.
     */
    public String generateOperation(Object op, boolean documented);

    /**
     * Generate the String representation for an Attribute.
     *
     * @param attr Object to generate representation for.
     * @param documented <tt>true</tt> if documentation shall be generated.
     * @return The String representation of the object.
     */
    public String generateAttribute(Object attr, boolean documented);

    /**
     * Generate the String representation for a Parameter.
     *
     * @param parameter Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateParameter(Object parameter);

    /**
     * Convert a String to a name.<p>
     *
     * TODO: What is the purpose of this function? Is it really needed?
     *
     * @param name The String to be converted.
     * @return The name.
     */
    public String generateName(String name);

    /**
     * Generate the String representation for a Package.
     *
     * @param pkg Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generatePackage(Object pkg);

    /**
     * Generate the String representation for an Expression.
     *
     * @param expr Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateExpression(Object expr);

    /**
     * Generate the String representation for a Classifier.
     *
     * @param cls Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateClassifier(Object cls);

    /**
     * Generate the String representation for a Stereotype.
     *
     * @param s Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateStereotype(Object s);

    /**
     * Generate the String representation for a TaggedValue.
     *
     * @param s Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateTaggedValue(Object s);

    /**
     * Generate the String representation for an Association.
     *
     * @param a Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateAssociation(Object a);

    /**
     * Generate the String representation for an AssociationEnd.
     *
     * @param ae Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateAssociationEnd(Object ae);

    /**
     * Generate the String representation for an Multiplicity.
     *
     * @param m Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateMultiplicity(Object m);

    /**
     * Generate the String representation for a State.
     *
     * @param m Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateState(Object m);

    /**
     * Generate the String representation for a StateBody.
     *
     * @param stt Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateStateBody(Object stt);

    /**
     * Generate the String representation for a Transition.
     *
     * @param m Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateTransition(Object m);

    /**
     * Generate the String representation for a Visibility.
     *
     * @param m Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateVisibility(Object m);

    /**
     * Generate the String representation for an Action.
     *
     * @param m Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateAction(Object m);
    
    /**
     * Generate the String representation for an Action State.
     *
     * @param actionState Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateActionState(Object actionState);

    /**
     * Generate the String representation for a Guard.
     *
     * @param m Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateGuard(Object m);

    /**
     * Generate the String representation for a Message.
     *
     * @param m Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateMessage(Object m);

    /**
     * Generate the String representation for an Event.
     *
     * @param m Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateEvent(Object m);

    /**
     * Generate the String representation for a ClassifierRef.
     *
     * @param m Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateClassifierRef(Object m);

    /**
     * Generate the String representation for an AssociationRole.
     *
     * @param m Object to generate representation for.
     * @return The String representation of the object.
     */
    public String generateAssociationRole(Object m);
}
