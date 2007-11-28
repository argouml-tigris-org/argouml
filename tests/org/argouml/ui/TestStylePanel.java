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

package org.argouml.ui;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

import org.argouml.model.Model;
import org.argouml.profile.InitProfileSubsystem;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.tigris.gef.presentation.FigText;

/**
 * @author jaap.branderhorst@xs4all.nl
 * Jul 27, 2003
 */
public class TestStylePanel extends TestCase {

    class MockStylePanel extends StylePanel {

        private boolean refreshCalled = false;

        public MockStylePanel() {
            super("mock");
        }

        /*
         * @see org.argouml.ui.TabTarget#refresh()
         */
        public void refresh() {
            super.refresh();
            refreshCalled = true;
        }

        /**
         * @return Returns the refreshCalled.
         */
        boolean isRefreshCalled() {
            return refreshCalled;
        }

    }

    /**
     * @param arg0 is the name of the test case.
     */
    public TestStylePanel(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
    }

    /**
     * Test setting a target.
     */
    public void testTargetSet() {

        StylePanel pane = new MockStylePanel();
        Object target = new Object();
        TargetEvent e =
            new TargetEvent(this,
			    TargetEvent.TARGET_SET,
			    new Object[] {
				null,
			    },
			    new Object[] {
				target,
			    });
        pane.targetSet(e);
        // new target is of type object, refresh should not be called
        assertTrue(!((MockStylePanel) pane).isRefreshCalled());
        target = new UMLClassDiagram();
        e = new TargetEvent(this,
			    TargetEvent.TARGET_SET,
			    new Object[] {
				null,
			    },
			    new Object[] {
				target,
			    });
        pane.targetSet(e);
        // new target is of type UMLClassDiagram, refresh should not be called
        assertTrue(!((MockStylePanel) pane).isRefreshCalled());
        target = Model.getCoreFactory().createClass();
        e = new TargetEvent(this,
			    TargetEvent.TARGET_SET,
			    new Object[] {
				null,
			    },
			    new Object[] {
				target,
			    });
        pane.targetSet(e);
        // new target is a modelelement, refresh should not be called
        assertTrue(!((MockStylePanel) pane).isRefreshCalled());
        target = new FigText(0, 0, 0, 0);
        e = new TargetEvent(this,
			    TargetEvent.TARGET_SET,
			    new Object[] {
				null,
			    },
			    new Object[] {
				target,
			    });
        pane.targetSet(e);
        // new target is a fig, refresh should be called
        assertTrue(((MockStylePanel) pane).isRefreshCalled());

    }
}
