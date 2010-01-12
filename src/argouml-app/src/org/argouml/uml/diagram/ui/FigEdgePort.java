/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigEdge;

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

    private void initialize() {
        invisibleAllowed = true;
        bigPort = new FigCircle(0, 0, 1, 1, LINE_COLOR, FILL_COLOR);
        addFig(bigPort);
    }

    /**
     * @param owner owning uml element
     * @param bounds ignored
     * @param settings ignored
     */
    public FigEdgePort(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initialize();
        bigPort.setOwner(owner);
    }
    
    /*
     * @see org.tigris.gef.presentation.Fig#hit(java.awt.Rectangle)
     */
    @Override
    public boolean hit(Rectangle r) {
        return false;
    }
    
    @Override
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
    @Override
    public String classNameAndBounds() {
        return getClass().getName()
            + "[" + getX() + ", " + getY() + ", "
            + getWidth() + ", " + getHeight() + "]";
    }

    /*
     * @see org.tigris.gef.presentation.FigNode#hitPort(int, int)
     */
    @Override
    public Object hitPort(int x, int y) {
        return null;
    }

    /*
     * @see org.tigris.gef.presentation.FigGroup#hitFig(java.awt.Rectangle)
     */
    @Override
    public Fig hitFig(Rectangle r) {
        return null;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#isSelectable()
     */
    @Override
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
