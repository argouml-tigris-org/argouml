// $Id$
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

package org.argouml.uml.diagram.static_structure.layout;

import org.apache.log4j.Logger;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;

/**
 *
 * @author mkl
 */
public abstract class ClassdiagramInheritanceEdge extends ClassdiagramEdge {
    private static final Logger LOG = Logger
            .getLogger(ClassdiagramInheritanceEdge.class);

    /**
     * The figures which are connected by this edge
     */
    private Fig high, low;

    /**
     * Offset used to distribute the lines
     */
    private int offset;

    /**
     * Constructor.
     *
     * @param edge the fig edge
     */
    public ClassdiagramInheritanceEdge(FigEdge edge) {
        super(edge);

        // calculate the higher and lower Figs
        high = getDestFigNode();
        low = getSourceFigNode();
        offset = 0;
    }

    /**
     * @return the vertical offset
     */
    public int getVerticalOffset() {
        return (getVGap() / 2) - 10 + getOffset();
    }

    /**
     * @return the center of the high node
     */
    public int getCenterHigh() {
        return (int) (high.getLocation().getX() + high.getSize().width / 2)
            + getOffset();
    }

    /**
     * @return the center of the low node
     */
    public int getCenterLow() {
        return (int) (low.getLocation().getX() + low.getSize().width / 2)
                + getOffset();
    }

    /**
     * @return the gap with the node one level down
     */
    public int getDownGap() {
        return (int) (low.getLocation().getY() - getVerticalOffset());
    }

    /**
     * @see org.argouml.uml.diagram.layout.LayoutedEdge#layout()
     *
     * Layout the edges in a way that they form a nice inheritance tree. Try to
     * implement these nice zigzag lines between classes and works well when the
     * row difference is one.
     *
     * @author Markus Klink
     * @since 0.9.6
     */
    public void layout() {
        // now we construct the zig zag inheritance line
        //getUnderlyingFig()
        Fig fig = getUnderlyingFig();
        int centerHigh = getCenterHigh();
        int centerLow = getCenterLow();

        // the amount of the "sidestep"
        int difference = centerHigh - centerLow;

        fig.addPoint(centerLow, (int) (low.getLocation().getY()));
        if (LOG.isDebugEnabled()) {
            LOG.debug("Point: x: " + centerLow + " y: " + low.getLocation().y);
        }

        // if the Figs are directly under each other we
        // do not need to add these points
        if (difference != 0) {
            getUnderlyingFig().addPoint(centerHigh - difference, getDownGap());
            getUnderlyingFig().addPoint(centerHigh, getDownGap());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Point: x: " + (centerHigh - difference) + " y: "
                        + getDownGap());
                LOG.debug("Point: x: " + centerHigh + " y: " + getDownGap());
            }

        }

        fig.addPoint(centerHigh, high.getLocation().y + high.getSize().height);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Point x: " + centerHigh + " y: "
                    + (high.getLocation().y + high.getSize().height));
        }
        fig.setFilled(false);
        getCurrentEdge().setFig(getUnderlyingFig());
        // currentEdge.setBetweenNearestPoints(false);
    }

    /**
     * Set the line-offset for this edge
     *
     * @param anOffset
     *            the offset to use for this edge
     */
    public void setOffset(int anOffset) {
        offset = anOffset;
    }

    /**
     * @return Line-offset for this edge
     */
    public int getOffset() {
        return offset;
    }

}

