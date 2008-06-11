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
     * Make sure that the north facing handles cannot be dragged as part of a
     * resize. {@inheritDoc}
     */
    public void dragHandle(int mX, int mY, int anX, int anY, Handle hand) {

        if (!getContent().isResizable()) {
            return;
        }

        List<Fig> figs = getContent().getLayer().getContents();

        // get the bounds of FigMessages
        int yMax = 65535; // this should be big enough for init
        int yMin = 0;
        for (Fig fig : figs) {
            if (fig instanceof FigMessage) {
                if (fig.getY() < yMax) {
                    yMax = fig.getY();
                }
                if (fig.getY() > yMin) {
                    yMin = fig.getY();
                }
            }
        }
        // a little buffer to ensure good visibility
        // yMax -= 10; // not required
        yMin += 10;
 
        // vertical resizing
        switch (hand.index) {
        case Handle.NORTHWEST:
        case Handle.NORTH:
        case Handle.NORTHEAST:
            // TODO Java 5 style for loop would be nicer
            for (Fig workOnFig : figs) {
                /*
                 * the resize will take place if the workOnFig 1. is a
                 * FifClassifierRole 2. doesn't force a FigMessage to move 3.
                 * doesn't violate minimum size of a CR
                 */
                if (workOnFig instanceof FigClassifierRole
                        && mY + workOnFig.getMinimumSize().height < yMax
                        && (workOnFig.getHeight() + workOnFig.getY() - mY) > workOnFig
                                .getMinimumSize().height) {
                    workOnFig.setHeight(workOnFig.getHeight()
                             + workOnFig.getY() - mY);
                    workOnFig.setY(mY);            
                }
            }
            break;
        case Handle.SOUTH:
        case Handle.SOUTHEAST:
        case Handle.SOUTHWEST:
            for (Fig workOnFig : figs) {
                // same conditions here as above
                if (workOnFig instanceof FigClassifierRole
                        && mY > yMin
                        && mY - workOnFig.getY() 
                        > workOnFig.getMinimumSize().height) {                    
                    workOnFig.setHeight(mY - workOnFig.getY());
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