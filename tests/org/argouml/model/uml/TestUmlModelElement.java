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

package org.argouml.model.uml;

import javax.jmi.reflect.RefBaseObject;

import org.argouml.model.ModelFacade;


/**
 * @author Thierry Lach
 */
public class TestUmlModelElement extends GenericUmlObjectTestFixture
{
	/**
	 * Constructor for FakeUmlModelFacadeTest.
	 * @param arg0
	 */
	public TestUmlModelElement(String arg0)
	{
		super(arg0, Uml.MODEL_ELEMENT);
		validateTestClassIsGeneric(this);
	}

	public void testModelElementLegacy() {
		UmlFactory.getFactory().setJmiProxyCreated(false);
		Object me = ModelFacade.getFacade().create(Uml.OBJECT);
		assertNotNull("Didn't create object", me);
		assertTrue("Should be a base", ModelFacade.isABase(me));
		assertTrue("Should be a model element", ModelFacade.isAModelElement(me));
		runTruthTests(me);
	}

	public void testModelElement() {
		UmlFactory.getFactory().setJmiProxyCreated(true);
		Object me = ModelFacade.getFacade().create(Uml.OBJECT);
		assertNotNull("Didn't create object", me);
		assertTrue("Should be a base", ModelFacade.isABase(me));
		assertTrue("Should be a model element", ModelFacade.isAModelElement(me));
		runTestRefBaseObject(me);
		runTruthTests(me);
	}

	protected void initializeTruth() {
		setShouldBe(Uml.ELEMENT);
		setShouldBe(Uml.MODEL_ELEMENT);
    }

}
