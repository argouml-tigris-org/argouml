// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import org.argouml.uml.diagram.layout.*;
import org.argouml.uml.diagram.ui.*;
import org.tigris.gef.presentation.*;

import java.awt.*;

/**
 *
 * @author  mkl
 */
public class ClassdiagramAssociationEdge extends ClassdiagramEdge {

    
    public ClassdiagramAssociationEdge(FigEdge edge) {
        super(edge);
    }
    

    public void layout() {
        // self associations are special cases. No need to let the maze
        // runner find the way.
        if (destFigNode == sourceFigNode) {
            Point centerRight = getCenterRight((FigNode) sourceFigNode);
            int yoffset = (int) ((sourceFigNode.getSize().getHeight() / 2));
            yoffset = java.lang.Math.min(30, yoffset);
            underlyingFig.addPoint(centerRight.x, centerRight.y);
            // move more right
            underlyingFig.addPoint(centerRight.x + 30, centerRight.y);
            // move down
            underlyingFig.addPoint(centerRight.x + 30, centerRight.y + yoffset);
            // move left
            underlyingFig.addPoint(centerRight.x, centerRight.y + yoffset);
            
            underlyingFig.setFilled(false);
            underlyingFig.setSelfLoop(true);
            currentEdge.setFig(underlyingFig);  
        }
        else {
            // brute force rectangular layout
            /*
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
            */
        }
    }   

    /** return a point which is just right of the center.
     */
    private Point getCenterRight(FigNode fig) {
        Point center = fig.center();
        return new Point((int) (center.x + fig.getSize().getWidth() / 2), center.y);
    }

    /** return a Point which is just lower than the center of the
     * Fig.
     */
    private Point getCenterLow(FigNode fig) {
        Point center = fig.center();
        return new Point(center.x, (int) (center.y + (fig.getSize().getHeight() / 2)));
    }
}







