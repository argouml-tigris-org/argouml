/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Michiel van der Wulp
 *******************************************************************************
 *
 * Some portions of this file were previously release using the BSD License:
 */
// $Id$
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

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import org.argouml.uml.diagram.DiagramEdgeSettings;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.PathContainer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigText;

/**
 * An Association Class is represented by 3 separate Figs:
 * <nl>
 * <li>FigAssociationClass is the association edge drawn between two
 * classifiers this displays that association properties of the association
 * class.</li>
 * <li>FigClassAssociationClass is the classifier box that displays the class
 * properties of the association class.</li>
 * </li>
 * FigEdgeAssociationClass is the dashed line that joins these two.</li>
 * </nl>
 *
 * Whenever the user attempts to remove or delete one of these parts then all
 * parts must go. Delete would be handled because the model element is deleted
 * and all parts are listening for such an event and will remove themselves.
 * However if the user attempts to just remove from diagram one of these parts
 * then there is no such event. Hence the removeFromDiagram method is overridden
 * to delegate removal from a single removeFromDiagram method on
 * FigAssociationClass.
 *
 * @author bob.tarling@gmail.com
 */
public class FigAssociationClass
        extends FigAssociation implements PathContainer {

    private static final long serialVersionUID = 3643715304027095083L;

    /**
     * Construct an association class figure for the given AssociationClass
     * model element using the rendering settings.
     * 
     * @param diagramElementSettings the settings for this new diagram element
     * @param settings rendering settings
     */
    public FigAssociationClass(
            final DiagramEdgeSettings diagramElementSettings, 
            final DiagramSettings settings) {
        super(diagramElementSettings, settings);
        setBetweenNearestPoints(true);
        ((FigPoly) getFig()).setRectilinear(false);
        setDashed(false);
    }
    
    /**
     * This overrides the parent class to do nothing here. An association class
     * has no name label but instead has the name and stereotype editable in
     * the association-classes class box.
     * 
     * @param owner owning uml element
     * @param settings rendering settings
     */
    protected void createNameLabel(Object owner, DiagramSettings settings) {
    }
    
    /**
     * Remove entire composite Fig from Diagram. Discover the attached
     * FigEdgeAssociationClass and the FigClassAssociationClass attached to
     * that. Remove them from the diagram before removing this.
     */
    @Override
    protected void removeFromDiagramImpl() {
        FigEdgeAssociationClass figEdgeLink = null;
        List edges = null;

        FigEdgePort figEdgePort = getEdgePort();
        if (figEdgePort != null) {
            edges = figEdgePort.getFigEdges();
        }

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

    /*
     * TODO: Is this required? Why would the fig already be dashed?
     * 
     * @see org.tigris.gef.presentation.FigEdge#setFig(
     *      org.tigris.gef.presentation.Fig)
     */
    @Override
    public void setFig(Fig f) {
        super.setFig(f);
        getFig().setDashed(false);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getNameFig()
     */
    @Override
    protected FigText getNameFig() {
        return null;
    }

    /*
     * Overridden in order to implement PathCompartmentContainer.
     */
    public boolean isPathVisible() {
        if (getAssociationClass() != null) {
            return getAssociationClass().isPathVisible();
        } else {
            return false;
        }
    }

    /*
     * Overridden in order to implement PathCompartmentContainer.
     */
    public void setPathVisible(boolean visible) {
        if (getAssociationClass() != null) {
            getAssociationClass().setPathVisible(visible);
        }
    }

    /**
     * Set fill color of contained FigClassAssociationClass.
     * 
     * @param color new fill color
     */
    @Override
    public void setFillColor(Color color) {
        if (getAssociationClass() != null) {
            getAssociationClass().setFillColor(color);
        }
    }

    /**
     * @return fill color of contained FigClassAssociationClass.
     */
    @Override
    public Color getFillColor() {
        if (getAssociationClass() != null) {
            return getAssociationClass().getFillColor();
        } else {
            return FILL_COLOR;
        }
    }

    /**
     * Set line color of contained FigClassAssociationClass.
     * 
     * @param arg0 new line color
     */
    @Override
    public void setLineColor(Color arg0) {
        super.setLineColor(arg0);
        if (getAssociationClass() != null) {
            getAssociationClass().setLineColor(arg0);
        }
        if (getFigEdgeAssociationClass() != null) {
            getFigEdgeAssociationClass().setLineColor(arg0);
        }
    }

    /**
     * Gets FigClassAssociationClass that is contained in this
     * FigAssociationClass.
     *
     * @return FigClassAssociationClass that is contained in this
     *         FigAssociationClass.
     */
    public FigClassAssociationClass getAssociationClass() {
        FigEdgeAssociationClass figEdgeLink = null;
        List edges = null;

        FigEdgePort figEdgePort = this.getEdgePort();
        if (figEdgePort != null) {
            edges = figEdgePort.getFigEdges();
        }

        if (edges != null) {
            for (Iterator it = edges.iterator(); it.hasNext()
                    && figEdgeLink == null;) {
                Object o = it.next();
                if (o instanceof FigEdgeAssociationClass) {
                    figEdgeLink = (FigEdgeAssociationClass) o;
                }
            }
        }

        FigNode figClassBox = null;
        if (figEdgeLink != null) {
            figClassBox = figEdgeLink.getDestFigNode();
            if (!(figClassBox instanceof FigClassAssociationClass)) {
                figClassBox = figEdgeLink.getSourceFigNode();
            }
        }
        return (FigClassAssociationClass) figClassBox;
    }

    /**
     * Gets FigEdgeAssociationClass that is contained in this
     * FigAssociationClass.
     *
     * @return FigEdgeAssociationClass that is contained in this
     *         FigAssociationClass
     */
    public FigEdgeAssociationClass getFigEdgeAssociationClass() {
        FigEdgeAssociationClass figEdgeLink = null;
        List edges = null;

        FigEdgePort figEdgePort = this.getEdgePort();
        if (figEdgePort != null) {
            edges = figEdgePort.getFigEdges();
        }

        if (edges != null) {
            for (Iterator it = edges.iterator(); it.hasNext()
                    && figEdgeLink == null;) {
                Object o = it.next();
                if (o instanceof FigEdgeAssociationClass) {
                    figEdgeLink = (FigEdgeAssociationClass) o;
                }
            }
        }

        return figEdgeLink;
    }
}
