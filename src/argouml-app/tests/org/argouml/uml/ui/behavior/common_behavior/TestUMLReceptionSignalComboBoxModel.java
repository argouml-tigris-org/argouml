// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.common_behavior;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.util.ThreadHelper;

/**
 * @since Nov 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLReceptionSignalComboBoxModel extends TestCase {

    /**
     * The number of elements used in the tests.
     */
    private static final int NO_OF_ELEMENTS = 10;

    /**
     * The elements that we use for the test.
     */
    private Object[] signals;

    /**
     * The tested model.
     */
    private UMLReceptionSignalComboBoxModel model;

    /**
     * The element.
     */
    private Object elem;

    /**
     * Constructor for TestUMLReceptionSignalComboBoxModel.
     *
     * @param arg0
     *            is the name of the test case.
     */
    public TestUMLReceptionSignalComboBoxModel(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        Project p = ProjectManager.getManager().getCurrentProject();
        elem = Model.getCommonBehaviorFactory().createReception();
        signals = new Object[NO_OF_ELEMENTS];
        Object m = Model.getModelManagementFactory().createModel();
        Collection roots = new ArrayList();
        roots.add(m);
        p.setRoots(roots);
        Model.getCoreHelper().setNamespace(elem, m);
        for (int i = 0; i < NO_OF_ELEMENTS; i++) {
            signals[i] = Model.getCommonBehaviorFactory().createSignal();
            Model.getCoreHelper().addOwnedElement(m, signals[i]);
        }
        model = new UMLReceptionSignalComboBoxModel();
        model.targetSet(new TargetEvent(this, "set", new Object[0],
                new Object[] {elem}));
        ThreadHelper.synchronize();
    }

    /*
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        for (int i = 0; i < NO_OF_ELEMENTS; i++) {
            Model.getUmlFactory().delete(signals[i]);
        }
        model = null;
    }

    /**
     * Set up the test.
     */
    public void testSetUp() {
        assertEquals(NO_OF_ELEMENTS, model.getSize());
        assertTrue(model.contains(signals[NO_OF_ELEMENTS / 2]));
        assertTrue(model.contains(signals[0]));
        assertTrue(model.contains(signals[NO_OF_ELEMENTS - 1]));
    }

    /**
     * Test setSignal().
     * 
     * @throws InvocationTargetException test failure
     * @throws InterruptedException test failure
     */
    public void testSetSignal() throws InterruptedException, 
    InvocationTargetException {

        Model.getCommonBehaviorHelper().setSignal(elem, signals[0]);
        ThreadHelper.synchronize();
        // One can only do this by changing target,
        // so let's simulate that:
        Object dummy = Model.getCommonBehaviorFactory().createReception();
        model.targetSet(new TargetEvent(this,
                TargetEvent.TARGET_SET,
                new Object[] {elem},
                new Object[] {dummy})
        );
        model.targetSet(new TargetEvent(this,
                TargetEvent.TARGET_SET,
                new Object[] {dummy},
                new Object[] {elem}));
        ThreadHelper.synchronize();
        assertTrue(model.getSelectedItem() == signals[0]);
    }

    /**
     * Test removing signals.
     * 
     * @throws InvocationTargetException test failure
     * @throws InterruptedException test failure
     */
    public void testRemoveSignal() throws InterruptedException, 
    InvocationTargetException {
        
        Model.getUmlFactory().delete(signals[NO_OF_ELEMENTS - 1]);
        ThreadHelper.synchronize();
        assertEquals(NO_OF_ELEMENTS - 1, model.getSize());
        assertTrue(!model.contains(signals[NO_OF_ELEMENTS - 1]));
    }

}
