// $Id$
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

import org.argouml.model.Model;
import org.argouml.model.ModelFacade;


/**
 * @author Thierry Lach
 */
public class TestUmlModel extends GenericUmlObjectTestFixture
{
    /**
     * Constructor for FakeUmlModelFacadeTest.
     * @param arg0 test name
     */
    public TestUmlModel(String arg0)
    {
	super(arg0, Uml.MODEL);
	validateTestClassIsGeneric(this);
    }

    /**
     * Legacy test for creating a Namespace.
     */
    public void testNamespaceLegacy() {
	Model.getUmlFactory().setJmiProxyCreated(false);
	Object o = ModelFacade.create(Uml.MODEL);
	assertNotNull("Didn't create object", o);
	assertTrue("Should be a base", ModelFacade.isABase(o));
	assertTrue("Should be a model", ModelFacade.isAModel(o));
	runTruthTests(o);
    }

    /**
     * Test the creation of a namespace.
     */
    public void testNamespace() {
	Model.getUmlFactory().setJmiProxyCreated(true);
	Object o = ModelFacade.create(Uml.MODEL);
	assertNotNull("Didn't create object", o);
	assertTrue("Should be a base", ModelFacade.isABase(o));
	assertTrue("Should be a model", ModelFacade.isAModel(o));
	runTestRefBaseObject(o);
	runTruthTests(o);
    }

    /**
     * @see org.argouml.model.uml.GenericUmlObjectTestFixture#initializeTruth()
     */
    protected void initializeTruth() {
	setTruth(Uml.ELEMENT, true);
	setTruth(Uml.MODEL_ELEMENT, true);
	setTruth(Uml.GENERALIZABLE_ELEMENT, true);
	setTruth(Uml.NAMESPACE, true);
	setTruth(Uml.PACKAGE, true);
	setTruth(Uml.MODEL, true);
    }

}
