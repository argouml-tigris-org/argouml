// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.behavior.common_behavior.MReception;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.model_management.MModel;

/**
 * @since Nov 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLReceptionSignalComboBoxModel extends TestCase {

    private int oldEventPolicy;
    private MSignal[] signals;
    private UMLReceptionSignalComboBoxModel model;
    private MReception elem;
    
    /**
     * Constructor for TestUMLReceptionSignalComboBoxModel.
     * @param arg0 is the name of the test case.
     */
    public TestUMLReceptionSignalComboBoxModel(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        Project p = ProjectManager.getManager().getCurrentProject();
        elem = Model.getCommonBehaviorFactory().createReception();
        oldEventPolicy = MFactoryImpl.getEventPolicy();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        signals = new MSignal[10];
        MModel m = Model.getModelManagementFactory().createModel();
        p.setRoot(m);
        elem.setNamespace(m);
        for (int i = 0; i < 10; i++) {
            signals[i] = Model.getCommonBehaviorFactory().createSignal();
            m.addOwnedElement(signals[i]);
        }      
        model = new UMLReceptionSignalComboBoxModel();
        model.targetSet(new TargetEvent(this, "set", new Object[0], 
                new Object[] {elem}));
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        for (int i = 0; i < 10; i++) {
            Model.getUmlFactory().delete(signals[i]);
        }
        MFactoryImpl.setEventPolicy(oldEventPolicy);
        model = null;
    }
    
    /**
     * Set up the test.
     */
    public void testSetUp() {
        assertEquals(10, model.getSize());
        assertTrue(model.contains(signals[5]));
        assertTrue(model.contains(signals[0]));
        assertTrue(model.contains(signals[9]));
    }
    
    /**
     * Test setSignal().
     */
    public void testSetSignal() {
        elem.setSignal(signals[0]);
        assertTrue(model.getSelectedItem() == signals[0]);
    }
    
    /**
     * Test setSignal() with null argument.
     */
    public void testSetSignalToNull() {
        elem.setSignal(null);
        assertNull(model.getSelectedItem());
    }
    

    /**
     * Test removing signals.
     */
    public void testRemoveSignal() {
        Model.getUmlFactory().delete(signals[9]);
        assertEquals(9, model.getSize());
        assertTrue(!model.contains(signals[9]));
    } 

}
