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

        // TODO: Avoid Globals when possible. That's a nasty anti-pattern in GEF.
        // We can get the Layer object from getContent().getLayer() and then get
        // the other contents of the layer from that Layer with getContents().
        List<Fig> figs = Globals.curEditor().getLayerManager().getContents();

        // get the bounds of FigMessages
        int yMax = 65535; // this should be big enough for init
        int yMin = 0;
        // TODO Java 5 style for loop would be nicer
        for (int i = 0; i < figs.size(); i++) {
            // TODO use instanceof rather than equate class name
            if (figs.get(i).getClass() == FigMessage.class) {
                if (figs.get(i).getY() < yMax) {
                    yMax = figs.get(i).getY();
                }
                if (figs.get(i).getY() > yMin) {
                    yMin = figs.get(i).getY();
                }
            }
        }
        // a little buffer to ensure good visibility
        // yMax -= 10; // no required
        yMin += 10;
 
        int headFigHeight = 0;
        Fig workOnFig = null;

        // vertical resizing
        switch (hand.index) {
        case Handle.NORTHWEST:
        case Handle.NORTH:
        case Handle.NORTHEAST:
            // TODO Java 5 style for loop would be nicer
            for (int i = 0; i < figs.size(); i++) {
                workOnFig = figs.get(i);
                // TODO use instanceof rather than equate class name
                if (workOnFig.getClass() == FigClassifierRole.class) {
                    // TODO This looks rather complex and contains knowledge
                    // of how a FigClassifierRole is constructed (uses child
                    // item 3).
                    // Would it be useful to implement getMinimumSize()
                    // on FigClassifierRole and call that here?
                    // The double casting is almost certainly not needed here.
                    headFigHeight = ((Fig) ((FigClassifierRole) (workOnFig))
                            .getFigs().get(3)).getHeight();
                    if ((mY + headFigHeight < yMax)) {
                        workOnFig.setHeight(workOnFig.getHeight()
                                + workOnFig.getY() - mY);
                        workOnFig.setY(mY);
                    }
                }
            }
            break;
        case Handle.SOUTH:
        case Handle.SOUTHEAST:
        case Handle.SOUTHWEST:
            // TODO Java 5 style for loop would be nicer
            for (int i = 0; i < figs.size(); i++) {
                workOnFig = figs.get(i);
                // TODO use instanceof rather than equate class name
                if (workOnFig.getClass() == FigClassifierRole.class
                        && mY > yMin) {
                    workOnFig.setHeight(mY - workOnFig.getY());
                }
            }
        default:
        }

        workOnFig = getContent();
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