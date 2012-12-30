/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    dthompson
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.FigModifyingModeImpl;
import org.tigris.gef.base.PathItemPlacementStrategy;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;

/**
 * Mode for dragging (i.e. user repositioning) of Text labels connected to 
 * FigEdges.
 * @author Dave Thompson
 */
public class ModeLabelDrag extends FigModifyingModeImpl {
    
    /**
     * The text label that is currently being dragged, or
     * null if nothing is being dragged.
     */
    private Fig dragFig = null;
    
    /**
     * The FigEdge that contains the label that is currently
     * being dragged.  We have to keep track of this because
     * the FigEdge is the only object that holds references to the
     * PathItems that place it's child figs.  The child Figs do not
     * have these references. 
     * figEdge = null if nothing is being dragged.
     */
    private FigEdge figEdge = null;
    
    /**
     * Point for tracking a mouse drag, indicates the last recorded mouse 
     * point.  When a new mouse drag event occurs, the difference between
     * the current point and dragBasePoint is the delta in position.
     */
    private Point dragBasePoint = new Point(0, 0);
   
    /**
     * X-offset for storing the location of the mouse click on a label
     * relative to the centre of the label.  Used while dragging to 
     * ensure that the user can click anywhere to start the drag, and things
     * still behave sensibly.  X component.
     */
    private int deltax = 0;
    
    /**
     * Y-offset for storing the location of the mouse click on a label
     * relative to the centre of the label.  Used while dragging to 
     * ensure that the user can click anywhere to start the drag, and things
     * still behave sensibly.  X component.
     */
    private int deltay = 0;
    
    /**
     * Constructor for creating the ModeLabelDrag instance.
     * @param editor The editor which will own this mode.
     */
    public ModeLabelDrag(Editor editor) {
        super(editor);
    }
    
    /**
     * Constructor for creating the ModeLabelDrag instance.
     */
    public ModeLabelDrag() {
        super();
    }

    /** 
     * Reply a string of instructions that should be shown in the
     * statusbar when this mode starts.
     * @return The text to display in the status bar.
     * @override 
     */
    public String instructions() {
        return "  ";
    }
    /**
     * Grabs label to begin movement. Turns cursor into a hand.
     * @param me The mouse event to process.
     * @override
     * @see org.tigris.gef.base.ModeImpl#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me) {
        Point clickPoint = me.getPoint(); 
        Fig underMouse = editor.hit(clickPoint);
        if (underMouse instanceof FigEdge) {
            List<Fig> figList = ((FigEdge) underMouse).getPathItemFigs();
            for (Fig fig : figList) {
                if (fig.contains(clickPoint)) {
                    // Consume to stop other modes from trying to take over
                    me.consume();
                    dragFig = fig;
                    dragBasePoint = fig.getCenter();
                    deltax = clickPoint.x - dragBasePoint.x;
                    deltay = clickPoint.y - dragBasePoint.y;
                    figEdge = (FigEdge) underMouse;
                    break;
                }
            }
        }
    }
    
    /**
     * Handle mouseReleased events.
     * @param me The mouse event to process.
     * @see org.tigris.gef.base.ModeImpl#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        if (dragFig != null) {
            dragFig = null;
        }
    }
    
    /**
     * Handle mouseDragged events.
     * @param me The mouse event to process.
     * @see org.tigris.gef.base.ModeImpl#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent me) {
        if (dragFig != null) {
            me = editor.translateMouseEvent(me);
            Point newPoint = me.getPoint();
            // Subtract the the offset of the click, to take account of user
            // having not initially clicked in the centre.
            newPoint.translate(-deltax, -deltay);
            PathItemPlacementStrategy pips 
                = figEdge.getPathItemPlacementStrategy(dragFig);
            pips.setPoint(newPoint);
            newPoint = pips.getPoint();
            int dx = newPoint.x - dragBasePoint.x;
            int dy = newPoint.y - dragBasePoint.y;
            dragBasePoint.setLocation(newPoint);
            dragFig.translate(dx, dy);
            me.consume();
            editor.damaged(dragFig);
        }
    }

}
