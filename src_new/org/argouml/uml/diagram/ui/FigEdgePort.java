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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Rectangle;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigGroup;

/**
 * This node cannot be dragged and manipulated in the same way as other
 * FigNodes in ArgoUML. It is actually an optional child fig (or more precisely
 * a path item) of a FigEdgeModelElement.
 * This allows the dashed edge of an association class to connect the
 * association edge and allows the dashed edge of a comment edge to attach a
 * comment to some other edge type.
 * GEF can only attach edges to nodes, by making this fig both a node and
 * containing it as a child of an edge we push the rules to allow edge to edge
 * connections.
 * TODO: We are inheriting a lot of functionality here that we don't really
 * require. We should attempt to make FigEdgePort extend FigNode.
 *
 * @author Bob Tarling
 */
public class FigEdgePort extends FigNodeModelElement {
    private FigCircle bigPort;

    /**
     * Constructor.
     */
    public FigEdgePort() {
        invisibleAllowed = true;
        bigPort = new FigCircle(0, 0, 1, 1, Color.black, Color.white);
        addFig(bigPort);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#hit(java.awt.Rectangle)
     */
    public boolean hit(Rectangle r) {
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object own) {
        bigPort.setOwner(own);
        super.setOwner(own);
    }
    
    public Object getOwner() {
	if (super.getOwner() != null) {
	    return super.getOwner();
	}
        Fig group = this;
        while (group != null && !(group instanceof FigEdge)) {
            group = group.getGroup();
        }
        if (group == null) {
            return null;
        } else {
            return group.getOwner();
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#classNameAndBounds()
     */
    // USED BY PGML.tee
    public String classNameAndBounds() {
        return getClass().getName()
            + "[" + getX() + ", " + getY() + ", "
            + getWidth() + ", " + getHeight() + "]";
    }

    /*
     * @see org.tigris.gef.presentation.FigNode#hitPort(int, int)
     */
    public Object hitPort(int x, int y) {
        return null;
    }

    /*
     * @see org.tigris.gef.presentation.FigGroup#hitFig(java.awt.Rectangle)
     */
    public Fig hitFig(Rectangle r) {
        return null;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#isSelectable()
     */
    public boolean isSelectable() {
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.FigNode#getPortFig(java.lang.Object)
     */
    public Fig getPortFig(Object port) {
        return bigPort;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3091219503512470458L;
}
