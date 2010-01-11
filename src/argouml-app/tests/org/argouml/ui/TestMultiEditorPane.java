/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.awt.Component;

import javax.swing.JTabbedPane;

import junit.framework.TestCase;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.tigris.gef.presentation.FigText;

/**
 * @author jaap.branderhorst@xs4all.nl
 * @since Apr 13, 2003
 */
public class TestMultiEditorPane extends TestCase {

    /**
     * Constructor for TestMultiEditorPane.
     *
     * @param arg0 is the name of the test case.
     */
    public TestMultiEditorPane(String arg0) {
        super(arg0);
    }

    /**
     * Test setting a target.
     */
    public void testTargetSet() {
        try {
            MultiEditorPane pane = new MultiEditorPane();
            Component[] tabs = pane.getTabs().getComponents();
            Object target = new Object();
            boolean[] shouldBeEnabled = getShouldBeEnabled(target, tabs);
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
            assertEnabled(pane.getTabs(), shouldBeEnabled);
            target = new UMLClassDiagram();
            shouldBeEnabled = getShouldBeEnabled(target, tabs);
            e = new TargetEvent(
				this,
				TargetEvent.TARGET_SET,
				new Object[] {
				    null,
				},
				new Object[] {
				    target
				});
            pane.targetSet(e);
            assertEnabled(pane.getTabs(), shouldBeEnabled);
            target = Model.getCoreFactory().createClass();
            shouldBeEnabled = getShouldBeEnabled(target, tabs);
            e =
                new TargetEvent(this,
				TargetEvent.TARGET_SET,
				new Object[] {
				    null,
				},
				new Object[] {
				    target,
				});
            pane.targetSet(e);
            assertEnabled(pane.getTabs(), shouldBeEnabled);
            target = new FigText(0, 0, 0, 0);
            shouldBeEnabled = getShouldBeEnabled(target, tabs);
            e = new TargetEvent(this,
				TargetEvent.TARGET_SET,
				new Object[] {
				    null,
				},
				new Object[] {
				    target,
				});
            pane.targetSet(e);
            assertEnabled(pane.getTabs(), shouldBeEnabled);
        } catch (Exception ex) {
            // on a headless system (without display) this will crash
        }
    }

    private boolean[] getShouldBeEnabled(Object target, Component[] tabs) {
        boolean[] shouldBeEnabled = new boolean[tabs.length];
        for (int i = 0; i < tabs.length; i++) {
            shouldBeEnabled[i] = ((TabTarget) tabs[i]).shouldBeEnabled(target);
        }
        return shouldBeEnabled;
    }

    private void assertEnabled(
        JTabbedPane tabbedPane,
        boolean[] shouldBeEnabled) {
        for (int i = 0; i < shouldBeEnabled.length; i++) {
            assertEquals(shouldBeEnabled[i], tabbedPane.isEnabledAt(i));
        }
    }


}
