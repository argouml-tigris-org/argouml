// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;

/**
 * Custom class to group FigTexts in such a way that they don't
 * overlap and that the group is shrinked to fit (no whitespace in
 * group).
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public class FigTextGroup extends FigGroup implements MouseListener {

    private static final int ROWHEIGHT = 17;
    private boolean supressCalcBounds = false;

    /**
     * Adds a FigText to the list with figs. Makes sure that the
     * figtexts do not overlap.
     * @see org.tigris.gef.presentation.FigGroup#addFig(Fig)
     */
    public void addFig(Fig f) {
	super.addFig(f);
        updateFigTexts();
        calcBounds();
    }

    /**
     * Updates the FigTexts. FigTexts without text (equals "") are not shown.
     * The rest of the figtexts are shown non-overlapping. The first figtext
     * added (via addFig) is shown at the bottom of the FigTextGroup.
     */
    protected void updateFigTexts() {
        Iterator it = getFigs().iterator();
        int height = 0;
        while (it.hasNext()) {
            Fig fig = (Fig) it.next();
            int figHeight = fig.getMinimumSize().height;
            fig.setBounds(getX(), getY() + height, fig.getWidth(), figHeight);
            fig.endTrans();
            height += fig.getHeight();
        }
    }


    /**
     * @see org.tigris.gef.presentation.Fig#calcBounds()
     */
    public void calcBounds() {
    	updateFigTexts();
        if (!supressCalcBounds) {
            super.calcBounds();
            // get the widest of all textfigs
            // calculate the total height
            int maxWidth = 0;
            int height = 0;
            Iterator it = getFigs().iterator();
            while (it.hasNext()) {
                Fig fig = (Fig) it.next();
                if (fig.getWidth() > maxWidth) {
                    maxWidth = fig.getWidth();
                }
                fig.setHeight(fig.getMinimumSize().height);
                height += fig.getHeight();
            }
            _w = maxWidth;
            _h = height;
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    public void removeFromDiagram() {
        Iterator it = getFigs().iterator();
        while (it.hasNext()) {
            ((Fig) it.next()).removeFromDiagram();
        }
        super.removeFromDiagram();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#deleteFromModel()
     */
    public void deleteFromModel() {
        Iterator it = getFigs().iterator();
        while (it.hasNext()) {
            ((Fig) it.next()).deleteFromModel();
        }
        super.deleteFromModel();
    }

    ////////////////////////////////////////////////////////////////
    // event handlers - MouseListener implementation

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me) {
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent me) {
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent me) {
    }

    /**
     * If the user double clicks on anu part of this FigGroup, pass it
     * down to one of the internal Figs.  This allows the user to
     * initiate direct text editing.
     *
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        if (me.isConsumed())
            return;
        if (me.getClickCount() >= 2) {
            Fig f = hitFig(new Rectangle(me.getX() - 2, me.getY() - 2, 4, 4));
            if (f instanceof MouseListener)
		((MouseListener) f).mouseClicked(me);
        }
        me.consume();
    }
}
