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

// File: NotationProvider2.java
// Interfaces: NotationProvider2
// Original Author: Thierry Lach
// $Id$

package org.argouml.application.api;

public interface NotationProvider2 {

    public NotationName getNotation();

    public String generateExtensionPoint(Object op);
    public String generateOperation(Object op, boolean documented);
    public String generateAttribute(Object attr, boolean documented);
    public String generateParameter(Object parameter);
    public String generateName(String name);
    public String generatePackage(Object pkg);
    public String generateExpression(Object expr);
    public String generateClassifier(Object cls);
    public String generateStereotype(Object s);
    public String generateTaggedValue(Object s);
    public String generateAssociation(Object a);
    public String generateAssociationEnd(Object ae);
    public String generateMultiplicity(Object m);
    public String generateState(Object m);
    public String generateStateBody(Object stt);
    public String generateTransition(Object m);
    public String generateVisibility(Object m);
    public String generateAction(Object m);
    public String generateGuard(Object m);
    public String generateMessage(Object m);
    public String generateClassifierRef(Object m);
    public String generateAssociationRole(Object m);

    /** Can the notation be parsed for this object?
     *  @param o the object to be tested.
     *  @return <code>true</code> if it can be parsed
     *          <code>false</code> if not
     */
    public boolean canParse(Object o);

    /** Can the notation be parsed at all?
     *  @return <code>true</code> if it can be parsed
     *          <code>false</code> if not
     */
    public boolean canParse();

    // public void parseExtensionPointCompartment(MUseCase uc, String s);
    // public void parseOperationCompartment(MClassifier cls, String s);
    // public void parseAttributeCompartment(MClassifier cls, String s);
    // public MExtensionPoint parseExtensionPoint(String s);
    // public MOperation parseOperation(String s);
    // public MAttribute parseAttribute(String s);
    // public String parseOutVisibility(MFeature f, String s);
    // public String parseOutKeywords(MFeature f, String s);
    // public String parseOutReturnType(MOperation op, String s);
    // public String parseOutParams(MOperation op, String s);
    // public String parseOutName(MModelElement me, String s);
    // public String parseOutType(MAttribute attr, String s);
    // public String parseOutInitValue(MAttribute attr, String s);
    // public String parseOutColon(String s);
    // public MParameter parseParameter(String s);
    // public Package parsePackage(String s);
    // public MClassImpl parseClassifier(String s);
    // public MStereotype parseStereotype(String s);
    // public MTaggedValue parseTaggedValue(String s);
    // public MAssociation parseAssociation(String s);
    // public MAssociationEnd parseAssociationEnd(String s);
    // public MMultiplicity parseMultiplicity(String s);
    // public MState parseState(String s);
    // public void parseStateBody(MState st, String s);
    // public void parseStateEntyAction(MState st, String s);
    // public void parseStateExitAction(MState st, String s);
    // public MTransition parseTransition(MTransition trans, String s);
    // public void parseClassifierRole(MClassifierRole cls, String s);
    // public void parseMessage(MMessage mes, String s);
    // public void parseStimulus(MStimulus sti, String s);
    // public MAction parseAction(String s);
    // public MGuard parseGuard(String s);
    // public MEvent parseEvent(String s);
    // public void parseObject(MObject obj, String s);
    // public void parseNodeInstance(MNodeInstance noi, String s);
    // public void parseComponentInstance(MComponentInstance coi, String s);
}
