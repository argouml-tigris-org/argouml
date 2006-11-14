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

package org.argouml.uml.diagram.ui;

import java.util.Iterator;

import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;


/**
 * An Association Class is represented by 3 seperate Figs
 * FigAssociationClass is the association edge drawn between two classifiers
 * this displays that association properties of the association class.
 * FigClassAssociationClass is the classifier box that displays the class
 * properties of the association class.
 * FigEdgeAssociationClass is the dashed line that joins these two.
 * 
 * Whenever the user attempts to remove or delete one of these parts then all
 * parts must go.
 * Delete would be handled because the model element is deleted and all parts
 * are listening for such an event and will remove themselves.
 * However if the user attempts to just remove from diagram one of these parts
 * then there is no such event. Hence the removeFromDiagram method is
 * overridden to delegate removal from a single removeFromDiagram method on
 * FigAssociationClass.
 *
 * @author bob.tarling@gmail.com
 */
public class FigAssociationClass extends FigAssociation {

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3643715304027095083L;

    /**
     * Construct a new FigAssociationClass during load from PGML.
     */
    public FigAssociationClass() {
        super();
        setBetweenNearestPoints(true);
        ((FigPoly) getFig()).setRectilinear(false);
        setDashed(false);
    }

    /**
     * Construct a new FigAssociationClass from user interaction.
     *
     * @param ed the edge
     * @param lay the layer
     */
    public FigAssociationClass(Object ed, Layer lay) {
        this();
        setLayer(lay);
        setOwner(ed);
    }
    
    /**
     * Discover the attached FigEdgeAssociationClass and the
     * FigClassAssociationClass attached to that. Remove them from the diagram
     * before removing this.
     */
    protected void removeFromDiagramImpl() {
        FigEdgePort figEdgePort = getEdgePort();
        
        FigEdgeAssociationClass figEdgeLink = null;
        Iterator it = figEdgePort.getFigEdges().iterator();
        while (it.hasNext() && figEdgeLink == null) {
            Object o = it.next();
            if (o instanceof FigEdgeAssociationClass) {
                figEdgeLink = (FigEdgeAssociationClass) o;
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
     * @see org.tigris.gef.presentation.FigEdge#setFig(
     *         org.tigris.gef.presentation.Fig)
     * TODO: Is this required? Why would the fig already be dashed?
     */
    public void setFig(Fig f) {
        super.setFig(f);
        getFig().setDashed(false);
    }
} /* end class FigAssociationClass */

