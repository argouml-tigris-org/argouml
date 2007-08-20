// $Id:FigMessagePort.java 13058 2007-07-10 21:31:04Z tfmorris $
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

package org.argouml.uml.diagram.sequence.ui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.argouml.uml.diagram.sequence.MessageNode;
import org.argouml.uml.diagram.ui.ArgoFigGroup;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;

/**
 * The port fig for links.
 *
 * @author jaap.branderhorst
 */
public class FigMessagePort extends ArgoFigGroup {

    private static final long serialVersionUID = -7805833566723101923L;
    
    private MessageNode node;

    /**
     * Creates a new horizontal FigMessagePort that's not displayed.
     *
     * @param owner the message that the FigMessagePort represents
     * @param x first x coordinate.
     * @param y y coordinate.
     * @param x2 second x coordinate.
     */
    public FigMessagePort(Object owner, int x, int y, int x2) {
        super();
        setOwner(owner);
        FigLine myLine = new FigLine(x, y, x2, y);
        addFig(myLine);
        setVisible(false);
    }

    /*
     * @see org.tigris.gef.presentation.FigGroup#addFig(org.tigris.gef.presentation.Fig)
     */
    public void addFig(Fig toAdd) {
        if (toAdd instanceof FigLine && getFigs().size() == 0) {
            toAdd.setVisible(false);
            super.addFig(toAdd);
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {
        if (w != 20) throw new IllegalArgumentException();
        if (getFigs().size() > 0) {
            getMyLine().setShape(x, y, x + w, y);
            calcBounds();
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#calcBounds()
     */
    public void calcBounds() {
        if (getFigs().size() > 0) {
            FigLine line = getMyLine();
            _x = line.getX();
            _y = line.getY();
            _w = line.getWidth();
            _h = 1;
            firePropChange("bounds", null, null);
        }
    }

    /**
     * Creates a new FigMessagePort that's not displayed; used when loading
     * PGML.
     */
    public FigMessagePort(Object owner) {
        setVisible(false);
        setOwner(owner);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getGravityPoints()
     */
    public List getGravityPoints() {
        ArrayList ret = new ArrayList();
        FigLine myLine = getMyLine();
        Point p1 = new Point(myLine.getX(), myLine.getY());
        Point p2 =
	    new Point(myLine.getX() + myLine.getWidth(),
		      myLine.getY() + myLine.getHeight());
        ret.add(p1);
        ret.add(p2);
        return ret;
    }

    MessageNode getNode() {
        if (node == null) {
            ((FigClassifierRole) this.getGroup().getGroup())
                    .setMatchingNode(this);
        }
        return node;
    }

    void setNode(MessageNode n) {
        node = n;
    }

    // TODO: Question - how does this differ to getY?
    public int getY1() {
        return getMyLine().getY1();
    }

    private FigLine getMyLine() {
        return (FigLine) getFigs().get(0);
    }
}
