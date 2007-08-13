// $Id:TestTabStyle.java 10734 2006-06-11 15:43:58Z mvw $
// Copyright (c) 2003-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.ui;

import junit.framework.TestCase;

import org.argouml.ui.StylePanel;
import org.argouml.ui.StylePanelFigNodeModelElement;
import org.argouml.uml.diagram.state.ui.FigSimpleState;
import org.argouml.uml.diagram.state.ui.FigTransition;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigInstance;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.static_structure.ui.FigLink;
import org.argouml.uml.diagram.static_structure.ui.StylePanelFigClass;
import org.argouml.uml.diagram.static_structure.ui.StylePanelFigInterface;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigGeneralization;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigRealization;
import org.argouml.uml.diagram.ui.SPFigEdgeModelElement;
import org.argouml.uml.diagram.use_case.ui.FigActor;
import org.argouml.uml.diagram.use_case.ui.FigUseCase;
import org.argouml.uml.diagram.use_case.ui.StylePanelFigUseCase;

/**
 * @author mkl
 *
 */
public class TestTabStyle extends TestCase {

    /**
     * The constructor.
     *
     * @param arg the name
     */
    public TestTabStyle(String arg) {
        super(arg);
    }

    /**
     * Test findPanelFor().
     */
    public void testFindPanelFor() {

        TabStyle tabStyle = new TabStyle();

        StylePanel panel;

        panel = tabStyle.findPanelFor(FigClass.class);
        assertTrue(panel instanceof StylePanelFigClass);

        panel = tabStyle.findPanelFor(FigInterface.class);
        assertTrue(panel instanceof StylePanelFigInterface);

        panel = tabStyle.findPanelFor(FigUseCase.class);
        assertTrue(panel instanceof StylePanelFigUseCase);

        panel = tabStyle.findPanelFor(FigNodeModelElement.class);
        assertTrue(panel instanceof StylePanelFigNodeModelElement);

        panel = tabStyle.findPanelFor(FigEdgeModelElement.class);
        assertTrue(panel instanceof SPFigEdgeModelElement);

        panel = tabStyle.findPanelFor(FigSimpleState.class);
        assertTrue(panel instanceof StylePanelFigNodeModelElement);

        panel = tabStyle.findPanelFor(FigTransition.class);
        assertTrue(panel instanceof SPFigEdgeModelElement);

        panel = tabStyle.findPanelFor(FigActor.class);
        assertTrue(panel instanceof StylePanelFigNodeModelElement);

        panel = tabStyle.findPanelFor(FigInstance.class);
        assertTrue(panel instanceof StylePanelFigNodeModelElement);

        panel = tabStyle.findPanelFor(FigLink.class);
        assertTrue(panel instanceof SPFigEdgeModelElement);

        panel = tabStyle.findPanelFor(FigGeneralization.class);
        assertTrue(panel instanceof SPFigEdgeModelElement);

        panel = tabStyle.findPanelFor(FigRealization.class);
        assertTrue(panel instanceof SPFigEdgeModelElement);

        panel = tabStyle.findPanelFor(FigAssociation.class);
        assertTrue(panel instanceof SPFigEdgeModelElement);

    }

}
