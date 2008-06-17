// $Id: svn:keywords $
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence2.ui;

import java.awt.Point;
import java.util.List;

import javax.swing.Icon;

import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.Handle;

/**
 * A custom select object to handle the special requirements of reshaping a
 * classifier role.
 * 
 * @author Bob Tarling
 */
public class SelectionClassifierRole extends SelectionNodeClarifiers2 {

    /**
     * The constructor.
     * 
     * @param f
     *                the fig
     */
    public SelectionClassifierRole(Fig f) {
       super(f);
    }

    /**
     * Makes sure that draging on the CR keeps them all aligned and resizing
     * doesn't force FigMessages overlaying.
     * 
     * @param mX
     *                New X position (aka current mouse X position)
     * @param mY
     *                New Y position (aka current mouse Y position)
     * @param anX
     *                Old X position
     * @param anY
     *                Old Y position
     * @param hand
     *                The handle being dragged
     */
    public void dragHandle(int mX, int mY, int anX, int anY, Handle hand) {

        if (!getContent().isResizable()) {
            return;
        }

        List<Fig> figs = getContent().getLayer().getContents();

        // if this is true all resizing/moving will stop
        boolean stopResize = false;
        
        // vertical resizing
        switch (hand.index) {
        case Handle.NORTHWEST:
        case Handle.NORTH:
        case Handle.NORTHEAST:
            final int dY = mY - getContent().getY();
            /*
             * First check if all CRs can be moved. The resize will take place
             * if: 1. workOnFig is a FifClassifierRole that doesn't contain a
             * Creation Message; 2. doesn't force a FigMessage to move more over
             * another message; 3. doesn't violate minimum size of a CR
             * 
             * TODO: take care of CRs with creation/destruction messages.
             * Depends on issue 5130
             */
            for (Fig workOnFig : figs) {
                if (workOnFig instanceof FigClassifierRole
                        && (workOnFig.getHeight() + workOnFig.getY() - mY) < workOnFig
                        .getMinimumSize().height) {
                    stopResize = true;
                }
            }
                 
            // if everything is OK, go on and move CRs and FigMessages
            if (!stopResize) {
                for (Fig workOnFig : figs) {
                    if (workOnFig instanceof FigClassifierRole) {
                        workOnFig.setHeight(workOnFig.getHeight()
                                + workOnFig.getY() - mY);
                        workOnFig.setY(mY);
                    } else if (workOnFig instanceof FigMessage) {
                 
                        // the array of points from a FigMessage
                        Point[] messagePoints = workOnFig.getPoints();
                        for (Point pt : messagePoints) {
                            pt.y = pt.y + dY;
                            workOnFig.setPoints(messagePoints);
                        }
                    }
                }
            }
            break;
        case Handle.SOUTH:
        case Handle.SOUTHEAST:
        case Handle.SOUTHWEST:
            /*
             * First check if all CRs can be moved. The resize will take place
             * if the lower most FigMessage is not reached
             */
            for (Fig workOnFig : figs) {
                if (workOnFig instanceof FigClassifierRole
                        && (mY - workOnFig.getY() < workOnFig.getMinimumSize().height)) {
                    stopResize = true;
                }
            }
            
            // if everything is OK, go on and move CRs and FigMessages
            if (!stopResize) {
                for (Fig workOnFig : figs) {
                    if (workOnFig instanceof FigClassifierRole) {
                        workOnFig.setHeight(mY - workOnFig.getY());
                    }
                }
            }        
        default:
        }

        Fig workOnFig = getContent();
        // horizontal resizing
        switch (hand.index) {
        case Handle.NORTHWEST:
        case Handle.SOUTHWEST:
            workOnFig.setWidth(workOnFig.getX() - mX + workOnFig.getWidth());
            workOnFig.setX(mX);
            break;
        case Handle.NORTHEAST:
        case Handle.SOUTHEAST:
            workOnFig.setWidth(mX - workOnFig.getX());
            break;
        default:
        }
    }

    @Override
    protected Object getNewNode(int index) {
        return null;
    }

    @Override
    protected Icon[] getIcons() {
        return null;
    }

    @Override
    protected String getInstructions(int index) {
        return null;
    }

    @Override
    protected Object getNewEdgeType(int index) {
        return null;
    }

    @Override
    protected Object getNewNodeType(int index) {
        return null;
    }
}