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

import org.argouml.uml.diagram.layout.*;
import org.tigris.gef.presentation.*;

/** This class is an abstract implementation of all edges which are
 *  layoutable in the classdiagram.
 *  @author Markus Klink
 *  @since 0.11.1
 */
public abstract class ClassdiagramEdge implements LayoutedEdge {

    /** the layout is oriented on a grid. These are our grid spaces.
     */
    private static int vGap;
    private static int hGap;


    private FigEdge currentEdge = null;
    /** the underlying fig of the edge we want to layout */
    private FigPoly underlyingFig = null;

    /** each fig has a source and a destination port
     */
    private Fig destFigNode;
    private Fig sourceFigNode;


    /** Constructor.
     * @param edge the Edge to layout
     */
    public ClassdiagramEdge(FigEdge edge) {
        currentEdge = edge;
        underlyingFig = new FigPoly();
        underlyingFig.setLineColor(edge.getFig().getLineColor());

        destFigNode = edge.getDestFigNode();
        sourceFigNode = edge.getSourceFigNode();
    }

    /**
     * Abstract method to layout the edge.
     *
     * @see org.argouml.uml.diagram.layout.LayoutedEdge#layout()
     */
    public abstract void layout();

    /**
     * @param h the horizontal gap
     */
    public static void setHGap(int h) { hGap = h; }

    /**
     * @param v the vertical gap
     */
    public static void setVGap(int v) { vGap = v; }

    /**
     * @return the horizontal gap
     */
    public static int getHGap() { return hGap; }

    /**
     * @return the vertical gap
     */
    public static int getVGap() { return vGap; }

    /**
     * @return Returns the destFigNode.
     */
    Fig getDestFigNode() {
        return destFigNode;
    }

    /**
     * @return Returns the sourceFigNode.
     */
    Fig getSourceFigNode() {
        return sourceFigNode;
    }

    /**
     * @return Returns the currentEdge.
     */
    protected FigEdge getCurrentEdge() {
        return currentEdge;
    }

    /**
     * @return Returns the underlyingFig.
     */
    protected FigPoly getUnderlyingFig() {
        return underlyingFig;
    }
}

