// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

import java.util.Collection;



/**
 * Test to check various aspects of extend relationships
 * 
 * @author Markus Klink
 */
public class TestUmlExtend extends GenericUmlObjectTestFixture {
    
    private Object uc1, uc2;
    private Object ep1, ep2;
    private Object extend;
    private UseCasesHelper helper = Model.getUseCasesHelper();
    
    /**
     * Constructor.
     *
     * @param arg0 test name
     */
    public TestUmlExtend(String arg0) {
	super(arg0, Model.getMetaTypes().getExtend());
	validateTestClassIsGeneric(this);
    }

    /**
     * Check that Usecases have Extends
     */
    public void testUsecasesHaveExtend() {
        Object myextend = helper.getExtends(uc1, uc2);
        assertNotNull(myextend);
        assertSame(extend, myextend);
    }
    
    /**
     * Check that the Extends are correct
     */
    public void testExtendIsCorrect() {
        Object base = 
            Model.getFacade().getBase(extend);
        Object included = 
            Model.getFacade().getExtension(extend);
        assertSame(uc1, base);
        assertSame(uc2, included);
    }
    
    /**
     * Test that ExtensionPoints got created correctly during setup
     */
    public void testUsecaseHasExtensionPoints() {
        Collection eps = Model.getFacade().getExtensionPoints(uc1);
        assertTrue(eps.size() == 3);
        assertTrue(eps.contains(ep1));
        assertTrue(eps.contains(ep2));
        Collection eps2 = Model.getFacade().getExtensionPoints(uc2);
        assertNotNull(eps2);
        assertTrue(eps2.size() == 0);
    }
    
    /**
     * Test setting ExtensionPoints
     */
    public void testSetExtensionPoints() {
        Collection eps = Model.getFacade().getExtensionPoints(uc1);
        helper.setExtensionPoints(extend, eps);
        Collection eps2 = Model.getFacade().getExtensionPoints(extend);
        assertTrue(eps2.containsAll(eps));
        assertTrue(eps.size() == eps2.size());
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        uc1 =
            Model.getUmlFactory().buildNode(Model.getMetaTypes().getUseCase());
        uc2 =
            Model.getUmlFactory().buildNode(Model.getMetaTypes().getUseCase());
        // by default the build already builds one default EP
        extend =
            Model.getUseCasesFactory().buildExtend(uc1, uc2);
        ep1 = 
            Model.getUseCasesFactory().buildExtensionPoint(uc1);
        ep2 =
            Model.getUseCasesFactory().buildExtensionPoint(uc1);
    }

}
