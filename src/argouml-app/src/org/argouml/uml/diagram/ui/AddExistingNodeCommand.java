/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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


package org.argouml.uml.diagram.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.base.Command;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.graph.GraphFactory;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;

/**
* ActionAddExistingNode enables pasting of an existing node into a Diagram.
*/
public class AddExistingNodeCommand implements Command, GraphFactory {

    /**
     * The UML object to be added to the diagram.
     */
    private Object object;

    /**
     * the location to drop the node.
     */
    private Point location;

    /**
     * 0 if this is the 1st element dropped here,
     * n if this is the (n+1)-th element dropped here.
     */
    private int count;

    /**
     * The constructor.
     *
     * @param o the UML modelelement to be added
     */
    public AddExistingNodeCommand(Object o) {
        object = o;
    }

    /**
     * The constructor.
     *
     * @param o the UML modelelement to be added
     * @param event the DropTargetDropEvent that caused this.
     *              Also <code>null</code> is acceptable
     * @param cnt 0 if this is the 1st element dropped here,
     *            n if this is the (n+1)-th element dropped here.
     */
    public AddExistingNodeCommand(Object o, DropTargetDropEvent event,
            int cnt) {
        object = o;
        location = event.getLocation();
        count = cnt;
    }

    /**
     * @param o the UML modelelement to be added
     * @param dropLocation the point where to drop the node.
     *               Also <code>null</code> is acceptable.
     * @param cnt 0 if this is the 1st element dropped here,
     *            n if this is the (n+1)-th element dropped here.
     */
    public AddExistingNodeCommand(Object o, Point dropLocation,
            int cnt) {
        object = o;
        location = dropLocation;
        count = cnt;
    }

    /*
     * @see org.tigris.gef.base.Command#execute()
     */
    public void execute() {
        Editor ce = Globals.curEditor();
        GraphModel gm = ce.getGraphModel();
        if (!(gm instanceof MutableGraphModel)) {
            return;
        }

        String instructions = null;
        ModePlace placeMode = null;
        if (object != null) {
            ArgoDiagram activeDiagram = DiagramUtils.getActiveDiagram();
            
            if (activeDiagram instanceof UMLDiagram
                    && ((UMLDiagram) activeDiagram).doesAccept(object)) {
                instructions = ((UMLDiagram) activeDiagram).
                    getInstructions(object);
                placeMode = ((UMLDiagram) activeDiagram).
                    getModePlace(this, instructions);       
                placeMode.setAddRelatedEdges(true);
            } else {
                // TODO: work here !
                instructions =
                    Translator.localize(
                        "misc.message.click-on-diagram-to-add",
                        new Object[] {Model.getFacade().toString(object), });
                placeMode = new ModePlace(this, instructions);       
                placeMode.setAddRelatedEdges(true);
            }
            Globals.showStatus(instructions);
        }
        
        if (location == null) {
            Globals.mode(placeMode, false);
        } else {
            /* Calculate the drop location, and place every n-th element
             * at an offset proportional to n.
             */
            Point p =
                new Point(
                    location.x + (count * 100),
                    location.y);
            /* Take canvas scrolling into account.
             * The implementation below does place the element correctly
             * when the canvas has been scrolled.
             */
            Rectangle r = ce.getJComponent().getVisibleRect();
            p.translate(r.x, r.y);
            /* Simulate a press of the mouse above the calculated point: */
            MouseEvent me =
                new MouseEvent(
                    ce.getJComponent(),
                    0,
                    0,
                    0,
                    p.x,
                    p.y,
                    0,
                    false);
            placeMode.mousePressed(me);
            /* Simulate a release of the mouse: */
            me =
                new MouseEvent(
                    ce.getJComponent(),
                    0,
                    0,
                    0,
                    p.x,
                    p.y,
                    0,
                    false);
            placeMode.mouseReleased(me);

            /* Set the size of the object's fig to minimum.
             * See issue 3410.
             * This binds the use of this Command to the
             * current diagram of the current project!
             */
            ArgoDiagram diagram = DiagramUtils.getActiveDiagram();
            Fig aFig = diagram.presentationFor(object);
            aFig.setSize(aFig.getPreferredSize());
        }
    }

    ////////////////////////////////////////////////////////////////
    // GraphFactory implementation

    /*
     * @see org.tigris.gef.graph.GraphFactory#makeGraphModel()
     */
    public GraphModel makeGraphModel() { return null; }

    /*
     * @see org.tigris.gef.graph.GraphFactory#makeEdge()
     */
    public Object makeEdge() { return null; }

    /*
     * @see org.tigris.gef.graph.GraphFactory#makeNode()
     */
    public Object makeNode() {
        return object;
    }

}
