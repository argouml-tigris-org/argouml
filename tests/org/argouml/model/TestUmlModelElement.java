// Copyright (c) 2003 The Regents of the University of California. All
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

package org.argouml.model;

import org.argouml.model.uml.Uml;

/**
 * @author Thierry Lach
 */
public class TestUmlModelElement extends GenericObjectTestFixture
{
	/**
	 * Constructor for FakeUmlModelFacadeTest.
	 * @param arg0
	 */
	public TestUmlModelElement(String arg0)
	{
		super(arg0);
		validateTestClassIsGeneric(this);
	}

    public void testModelElement() {
    	Object me = ModelFacade.getFacade().create(Uml.OBJECT);
    	assertNotNull("Didn't create object", me);
		assertTrue("Should be a base", ModelFacade.isABase(me));
		assertTrue("Should be a model element", ModelFacade.isAModelElement(me));
    }

    protected void initializeTruth() {
		setTruth(Uml.ABSTRACTION, true);
		setTruth(Uml.ASSOCIATION, true);
		setTruth(Uml.ASSOCIATION_ROLE, true);
		setTruth(Uml.DEPENDENCY, true);
		setTruth(Uml.EXTEND, true);
		setTruth(Uml.GENERALIZATION, true);
		setTruth(Uml.INCLUDE, true);
		setTruth(Uml.LINK, true);
		setTruth(Uml.PERMISSION, true);
		setTruth(Uml.USAGE, true);
		setTruth(Uml.TRANSITION, true);
		setTruth(Uml.ACTOR, true);
		setTruth(Uml.CLASS, true);
		setTruth(Uml.CLASSIFIER, true);
		setTruth(Uml.CLASSIFIER_ROLE, true);
		setTruth(Uml.COMPONENT, true);
		setTruth(Uml.COMPONENT_INSTANCE, true);
		setTruth(Uml.INSTANCE, true);
		setTruth(Uml.INTERFACE , true);
		setTruth(Uml.NODE, true);
		setTruth(Uml.NODE_INSTANCE, true);
		setTruth(Uml.OBJECT, true);
		setTruth(Uml.PACKAGE, true);
		setTruth(Uml.STATE, true);
		setTruth(Uml.COMPOSITE_STATE, true);
		setTruth(Uml.STATE_VERTEX, true);
		setTruth(Uml.PSEUDOSTATE, true);
		setTruth(Uml.USE_CASE, true);
		setTruth(Uml.ACTION, true);
		setTruth(Uml.ACTION_EXPRESSION, true);
		setTruth(Uml.ASSOCIATION_END, true);
		setTruth(Uml.CALL_ACTION, true);
		setTruth(Uml.NAMESPACE, true);
		setTruth(Uml.RECEPTION, true);
		setTruth(Uml.STEREOTYPE, true);
		setTruth(Uml.ATTRIBUTE, true);
		setTruth(Uml.OPERATION, true);
		setTruth(Uml.EVENT, true);
		setTruth(Uml.MODEL_ELEMENT, true);
		setTruth(Uml.DATATYPE , true);
    }

}
