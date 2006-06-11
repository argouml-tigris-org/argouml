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

import java.awt.Point;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigPoly;

/**
 * This class represents note edges to enable an appropriate positioning
 * of notes.
 *
 * @author  David Gunkel
 */
public class ClassdiagramNoteEdge
    extends ClassdiagramEdge {
    /**
     * The constructor.
     *
     * @param edge the fig edge
     */
    public ClassdiagramNoteEdge(FigEdge edge) {
        super(edge);
    }

    /**
     * NoteEdges are drawn directly between the linked nodes, using vertically
     * centered points on the right- resp. left-hand side of the nodes.
     * @see org.argouml.uml.diagram.static_structure.layout.ClassdiagramEdge#layout()
     */
    public void layout() {
        // use left and right, up and down
        Fig fs = getSourceFigNode();
        Fig fd = getDestFigNode();
        if (fs.getLocation().x < fd.getLocation().x) {
            addPoints(fs, fd);
        } else {
            addPoints(fd, fs);
        }
        FigPoly fig = getUnderlyingFig();
        fig.setFilled(false);
        getCurrentEdge().setFig(fig);
    }

    /**
     * Add points to the underlying FigPoly.
     *
     * @param fs - source Fig of this edge
     * @param fd - destination Fig of this edge
     */
    private void addPoints(Fig fs, Fig fd) {
        FigPoly fig = getUnderlyingFig();
        Point p = fs.getLocation();
        p.translate(fs.getWidth(), fs.getHeight() / 2);
        fig.addPoint(p);
        p = fd.getLocation();
        p.translate(0, fd.getHeight() / 2);
        fig.addPoint(p);
    }
}

