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

package org.argouml.uml.diagram.static_structure.layout;

import java.awt.Point;

import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;

/**
 *
 * @author  mkl
 */
public class ClassdiagramAssociationEdge extends ClassdiagramEdge {

    /**
     * The constructor.
     *
     * @param edge the fig of the edge
     */
    public ClassdiagramAssociationEdge(FigEdge edge) {
        super(edge);
    }

    /**
     * Size of self association edges.
     */
    private static final int SELF_SIZE = 30;
    
    /*
     * @see org.argouml.uml.diagram.layout.LayoutedEdge#layout()
     */
    public void layout() {
        // TODO: Multiple associations between the same pair of elements
        // need to be special cased so that they don't overlap - tfm - 20060228
        
        // self associations are special cases. No need to let the maze
        // runner find the way.
        if (getDestFigNode() == getSourceFigNode()) {
            Point centerRight = getCenterRight((FigNode) getSourceFigNode());
            int yoffset = getSourceFigNode().getHeight() / 2;
            yoffset = java.lang.Math.min(SELF_SIZE, yoffset);
            FigPoly fig = getUnderlyingFig();
            fig.addPoint(centerRight);
            // move more right
            fig.addPoint(centerRight.x + SELF_SIZE, centerRight.y);
            // move down
            fig.addPoint(centerRight.x + SELF_SIZE, centerRight.y + yoffset);
            // move left
            fig.addPoint(centerRight.x, centerRight.y + yoffset);

            fig.setFilled(false);
            fig.setSelfLoop(true);
            getCurrentEdge().setFig(fig);
        }
        /* else {
            // brute force rectangular layout
            Point centerSource = sourceFigNode.center();
            Point centerDest   = destFigNode.center();

            underlyingFig.addPoint(centerSource.x, centerSource.y);
            underlyingFig.addPoint(centerSource.x +
                                   (centerDest.x-centerSource.x)/2,
                                   centerSource.y);
            underlyingFig.addPoint(centerSource.x +
                                   (centerDest.x-centerSource.x)/2,
                                   centerDest.y);
            underlyingFig.addPoint(centerDest.x, centerDest.y);
            underlyingFig.setFilled(false);
            underlyingFig.setSelfLoop(false);
            currentEdge.setFig(underlyingFig);
        }*/
    }

    /**
     * Return a point which is centered vertically on the right hand edge of the
     * figure.
     * 
     * @param fig
     *            The fig.
     * @return A Point.
     */
    private Point getCenterRight(FigNode fig) {
        Point center = fig.getCenter();
        return new Point(center.x + fig.getWidth() / 2, center.y);
    }
}







