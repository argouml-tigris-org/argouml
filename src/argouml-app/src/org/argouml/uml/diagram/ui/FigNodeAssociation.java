/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2009 The Regents of the University of California. All
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigDiamond;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for N-ary association (association node),
 * i.e. the diamond. <p>
 * 
 * TODO: Improve the location of the stereotypes!
 *
 * @author pepargouml@yahoo.es
 */
public class FigNodeAssociation extends FigNodeModelElement {
    
    private static final int X = 0;
    private static final int Y = 0;

    private FigDiamond head;

    @Override
    protected Fig createBigPortFig() {
        return new FigDiamond(0, 0, 70, 70, DEBUG_COLOR, DEBUG_COLOR);
    }

    private void initFigs() {
        setEditable(false);
        head = new FigDiamond(0, 0, 70, 70, LINE_COLOR, FILL_COLOR);

        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
//      The following does not seem to work - centered the Fig instead.
//        getNameFig().setJustificationByName("center");

        getStereotypeFig().setBounds(X + 10, Y + NAME_FIG_HEIGHT + 1, 
                0, NAME_FIG_HEIGHT);
        getStereotypeFig().setFilled(false);
        getStereotypeFig().setLineWidth(0);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(head);
        if (!Model.getFacade().isAAssociationClass(getOwner())) {
            addFig(getNameFig());
            addFig(getStereotypeFig());
        }

        setBlinkPorts(false); //make port invisible unless mouse enters
        Rectangle r = getBounds();
        setBounds(r);
        setResizable(true);
    }
    
    /**
     * Construct a new FigNodeAssociation.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     */
    public FigNodeAssociation(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initFigs();
    }

    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        FigNodeAssociation figClone = (FigNodeAssociation) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigDiamond) it.next());
        figClone.head = (FigDiamond) it.next();
        figClone.setNameFig((FigText) it.next());
        return figClone;
    }


    /**
     * Called when a model event is received from model subsystem.
     * handles when a n-ary association becomes a binary association.
     *
     * @param mee the event
     */
    @Override
    protected void updateLayout(UmlChangeEvent mee) {
        super.updateLayout(mee);
        if (mee.getSource() == getOwner()
                && mee instanceof RemoveAssociationEvent
                && "connection".equals(mee.getPropertyName())
                && Model.getFacade().getConnections(getOwner()).size() == 2) {
            reduceToBinary();
        }
    }

    /**
     * Called when deletion of an association end reduces the number of ends
     * of an association down to only two. This Fig which represent the diamond
     * node of a n-ary association needs to be replaced by a FigAssociation
     * representing the binary relationship.
     */
    private void reduceToBinary() {
        final Object association = getOwner();
        assert (Model.getFacade().getConnections(association).size() == 2);
        
        // Detach any non-associationend edges (such as comment edges) already
        // attached before this association node is removed.
        // They'll later be re-attached to the new FigAssociation
        final Collection<FigEdge> existingEdges = getFigEdges();
        for (Iterator<FigEdge> it = existingEdges.iterator(); it.hasNext(); ) {
            FigEdge edge = it.next();
            if (edge instanceof FigAssociationEnd) {
                it.remove();
            } else {
                removeFigEdge(edge);
            }
        }
        
        // Now we can remove ourself (which will also remove the
        // attached association ends edges)
        final LayerPerspective lay = (LayerPerspective) getLayer();
        final MutableGraphModel gm = (MutableGraphModel) lay.getGraphModel();
        gm.removeNode(association);
        removeFromDiagram();
        
        // Create the new FigAssociation edge to replace the node
        final GraphEdgeRenderer renderer =
            lay.getGraphEdgeRenderer();
        final FigEdgeModelElement figEdge =
            (FigEdgeModelElement) renderer.getFigEdgeFor(
                gm, lay, association, null);
        lay.add(figEdge);
        gm.addEdge(association);
        
        // Add the non-associationend edges (such as comment edges) that were
        // originally attached to this and attach them to the new
        // FigAssociation and make sure they are positioned correctly.
        for (FigEdge edge : existingEdges) {
            figEdge.makeEdgePort();
            if (edge.getDestFigNode() == this) {
                edge.setDestFigNode(figEdge.getEdgePort());
                edge.setDestPortFig(figEdge.getEdgePort());
            }
            if (edge.getSourceFigNode() == this) {
                edge.setSourceFigNode(figEdge.getEdgePort());
                edge.setSourcePortFig(figEdge.getEdgePort());
            }
        }
        figEdge.computeRoute();
    }
    
    /*
     * Makes sure that the edges stick to the outline of the fig.
     * @see org.tigris.gef.presentation.Fig#getGravityPoints()
     */
    @Override
    public List getGravityPoints() {
        return getBigPort().getGravityPoints();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        head.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return head.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        head.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return head.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    @Override
    public void setFilled(boolean f) {
    }

    @Override
    public boolean isFilled() {
        return true;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        head.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return head.getLineWidth();
    }

    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        Rectangle oldBounds = getBounds();

        Rectangle nm = getNameFig().getBounds();
        /* Center the NameFig, since center justification 
         * does not seem to work. */
        getNameFig().setBounds(x + (w - nm.width) / 2, 
                y + h / 2 - nm.height / 2, 
                nm.width, nm.height);
        // TODO: Replace magic numbers with constants
        if (getStereotypeFig().isVisible()) {
            /* TODO: Test this. */
            getStereotypeFig().setBounds(x, y + h / 2 - 20, w, 15);
            int stereotypeHeight = getStereotypeFig().getMinimumSize().height;
            getStereotypeFig().setBounds(
                    x,
                    y,
                    w,
                    stereotypeHeight);
        }
        
        head.setBounds(x, y, w, h);
        getBigPort().setBounds(x, y, w, h);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension aSize = getNameFig().getMinimumSize();
        if (getStereotypeFig().isVisible()) {
            Dimension stereoMin = getStereotypeFig().getMinimumSize();
            aSize.width = Math.max(aSize.width, stereoMin.width);
            aSize.height += stereoMin.height;
        }
        aSize.width = Math.max(70, aSize.width);
        int size = Math.max(aSize.width, aSize.height);
        aSize.width = size;
        aSize.height = size;
        
        return aSize;
    }
    
    
    /**
     * Remove entire composite Fig from Diagram. Discover the attached
     * FigEdgeAssociationClass and the FigClassAssociationClass attached to
     * that. Remove them from the diagram before removing this.
     */
    @Override
    protected void removeFromDiagramImpl() {
        FigEdgeAssociationClass figEdgeLink = null;
        final List edges = getFigEdges();

        if (edges != null) {
            for (Iterator it = edges.iterator(); it.hasNext()
                    && figEdgeLink == null;) {
                Object o = it.next();
                if (o instanceof FigEdgeAssociationClass) {
                    figEdgeLink = (FigEdgeAssociationClass) o;
                }
            }
        }

        if (figEdgeLink != null) {
            FigNode figClassBox = figEdgeLink.getDestFigNode();
            if (!(figClassBox instanceof FigClassAssociationClass)) {
                figClassBox = figEdgeLink.getSourceFigNode();
            }
            figEdgeLink.removeFromDiagramImpl();
            ((FigClassAssociationClass) figClassBox).removeFromDiagramImpl();
        }

        super.removeFromDiagramImpl();
    }

}

