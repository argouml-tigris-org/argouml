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

// $header$
package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import junit.framework.TestCase;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.state_machines.MEvent;
import ru.novosoft.uml.behavior.state_machines.MEventImpl;
import ru.novosoft.uml.foundation.core.MClassImpl;

/**
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLCheckBox2 extends TestCase {
    
    private static boolean _buildModelCalled = false;
    private UMLCheckBox2 box = null;
    private MElementEvent event = null;
    private boolean _actionPerformedCalled = false;

    protected class DummyUMLCheckBox2 extends UMLCheckBox2 {                       
        /**
         * Constructor for MockUMLCheckBox2.
         * @param container
         * @param text
         * @param a
         */
        public DummyUMLCheckBox2(
            UMLUserInterfaceContainer container,
            String text,
            Action a) {
            super(container, text, a, "test");
        }

        /**
         * @see org.argouml.uml.ui.UMLCheckBox2#buildModel()
         */
        public void buildModel() {
            _buildModelCalled = true;
        }
    }
    
    protected class MockAction extends AbstractAction {
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            _actionPerformedCalled = true;
        }

}
    
    /**
     * Constructor for TestUMLCheckBox2.
     * @param arg0
     */
    public TestUMLCheckBox2(String arg0) {
        super(arg0);
    }
    
    public void testTargetChanged() {
        box.targetChanged();
        assert("Targetchanged does not work!", _buildModelCalled);
    }
    
    public void testTargetReasserted() {
        box.targetReasserted();
        assert("TargetReasserted does not work!", _buildModelCalled);
    }
    
    public void testPropertySet() {
        box.propertySet(event);
        assert("Propertyset does not work!", _buildModelCalled); 
    }
    
    public void testClick() {
        box.doClick();
        assert("Action not called!", _actionPerformedCalled);
    }
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        box = new DummyUMLCheckBox2(new MockUMLUserInterfaceContainer(), 
                "dummy", new MockAction());
        event = new MElementEvent(new MClassImpl(), "", 1, null, null);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        box = null;
        event = null;
        _buildModelCalled = false;
    }
}

