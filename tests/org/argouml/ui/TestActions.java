// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.event.ActionEvent;

import junit.framework.TestCase;

import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.ui.UMLAction;

/**
 * Tests for org.argouml.ui.Actions.
 *
 * @author jaap.branderhorst@xs4all.nl
 * Jul 21, 2003
 */
public class TestActions extends TestCase {

    private class MockGlobalAction extends UMLAction {
        public MockGlobalAction() { super("test"); }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {

        }

        /**
         * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
         */
        public boolean shouldBeEnabled() {
            setCalled(true);
            return true;
        }


        private boolean _called = false;

        /**
         * @return true if {@link #shouldBeEnabled(java.lang.Object[])} 
	 *              is called.
         */
        public boolean isCalled() {
            return _called;
        }

        /**
         * @param b true when {@link #shouldBeEnabled(java.lang.Object[])} 
         *          is called.
         */
        public void setCalled(boolean b) {
            _called = b;
        }

    }

    /**
     * @param arg0
     */
    public TestActions(String arg0) {
        super(arg0);
    }

    /**
     * Tests if targetSet on the singleton instance of Actions updates the 
     * registred global actions.    
     */
    public void testTargetSet() {
        MockGlobalAction a = new MockGlobalAction();
        Actions.addAction(a);
        Object o = new Object();
        TargetEvent e =
            new TargetEvent(
                this,
                TargetEvent.TARGET_SET,
                new Object[] { null },
                new Object[] { o });
        Actions.getInstance().targetSet(e);
        assertTrue(a.isCalled());
    }

}
