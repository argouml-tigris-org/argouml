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

/**
 *
 * @author  mkl
 */
public abstract class ClassdiagramInheritanceEdge extends ClassdiagramEdge {
    
    Fig high, low;
    

    public ClassdiagramInheritanceEdge(FigEdge edge) {
        super(edge);
        
	// calculate the higher and lower Figs
        ClassdiagramLayouter.cat.debug("sourceFigNode: "
				       + sourceFigNode.classNameAndBounds());
        ClassdiagramLayouter.cat.debug("destFigNode: "
				       + destFigNode.classNameAndBounds());
        if (sourceFigNode.getLocation().getY()
	    <= destFigNode.getLocation().getY())
	{
            high = destFigNode;
            low = sourceFigNode;
            ClassdiagramLayouter.cat.debug("high is sourcenode, "
					   + "is low destnode");
        }
        else {
            high = destFigNode;
            low = sourceFigNode;
            ClassdiagramLayouter.cat.debug("high is destnode, "
					   + "low is sourcenode");
        }
        ClassdiagramLayouter.cat.debug("High: " + high.classNameAndBounds());
        ClassdiagramLayouter.cat.debug("Low : " + low.classNameAndBounds());
    }
    
    public abstract int getVerticalOffset();
        
    public int getCenterHigh() {
        return (int) 
	    (high.getLocation().getX() + high.getSize().getWidth() / 2);
    }
    
    public int getCenterLow() {
        return (int) (low.getLocation().getX() + low.getSize().getWidth() / 2);
    }
    
    public int getDownGap() {
        return (int) (low.getLocation().getY() - getVerticalOffset());
    }
    
    /** layout the edges in a way that they form a nice inheritance tree.
     * Try to implement these nice zigzag lines between classes and
     * works well when the row difference is one.
     *
     * @author Markus Klink
     * @since 0.9.6
     */
    public void layout() {
        ClassdiagramLayouter.cat.debug("underlyingFig: "
				       + underlyingFig.classNameAndBounds());
	
        // now we construct the zig zag inheritance line
        int centerHigh =  getCenterHigh();
        int centerLow  =  getCenterLow();
        
        ClassdiagramLayouter.cat.debug("centerHigh: " + centerHigh
				       + " centerLow: " + centerLow
				       + " downGap: " + getDownGap());
        
        // the amount of the "sidestep"
        int difference = centerHigh - centerLow;
        
        underlyingFig.addPoint(centerLow, (int)
			       (low.getLocation().getY()));
        ClassdiagramLayouter.cat.debug("Point: x: " + centerLow + " y: "
				       + (int) (low.getLocation().getY()));
        
        // if the Figs are directly under each other we
        // do not need to add these points
        if (difference != 0) { 
            underlyingFig.addPoint(centerHigh - difference,
				   getDownGap());
            ClassdiagramLayouter.cat.debug("Point: x: "
					   + (centerHigh - difference)
					   + " y: " + getDownGap());
            underlyingFig.addPoint(centerHigh,
				   getDownGap());
            ClassdiagramLayouter.cat.debug("Point: x: " + centerHigh + " y: "
					   + getDownGap());
            
        }
        
        underlyingFig.addPoint( centerHigh,
				(int) (high.getLocation().getY() +
				      high.getSize().getHeight()));
        ClassdiagramLayouter.cat.debug("Point x: " + centerHigh + " y: "
				       + (int) (high.getLocation().getY()
						+ high.getSize().getHeight()));
        
        underlyingFig.setFilled(false);
        currentEdge.setFig(underlyingFig);
        // currentEdge.setBetweenNearestPoints(false);
    }   
}

