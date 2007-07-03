// $Id: CubePortFigRect.java 11516 2006-11-25 04:30:15Z tfmorris $
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Point;
import java.awt.Rectangle;

import org.tigris.gef.base.Geometry;
import org.tigris.gef.presentation.FigRect;

/**
 * The bigport needs to overrule the getClosestPoint,
 * because it is the port of this FigNode(Instance).
 *
 * @author mvw@tigris.org
 */
class CubePortFigRect extends FigRect {
    private int d;

    /**
     * The constructor.
     *
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     * @param depth the depth
     */
    public CubePortFigRect(int x, int y, int w, int h, int depth) {
        super(x, y, w, h);
        d = depth;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getClosestPoint(java.awt.Point)
     */
    public Point getClosestPoint(Point anotherPt) {
        Rectangle r = getBounds();
        int[] xs = {
            r.x,
            r.x + d,
            r.x + r.width,
            r.x + r.width,
            r.x + r.width - d,
            r.x,
            r.x,
        };
        int[] ys = {
            r.y + d,
            r.y,
            r.y,
            r.y + r.height - d,
            r.y + r.height,
            r.y + r.height,
            r.y + d,
        };
        Point p =
            Geometry.ptClosestTo(
                xs,
                ys,
                7 , anotherPt);
        return p;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -136360467045533658L;
}
