// $Id: FigSignal.java 12570 2007-05-09 07:06:07Z tfmorris $
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigAttributesCompartment;
import org.argouml.uml.diagram.ui.FigStereotypesCompartment;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * Class to display graphics for a UML Signal in a diagram.
 * 
 * @author Tom Morris
 */
public class FigSignal extends FigClassifierBox {
    
    private FigAttributesCompartment attributesFigCompartment;

    /**
     * Default constructor for a {@link FigSignal}.
     */
    public FigSignal() {
        super();
        FigStereotypesCompartment fsc =
            (FigStereotypesCompartment) getStereotypeFig();
        fsc.setKeyword("signal");

        enableSizeChecking(false);
        setSuppressCalcBounds(true);
        addFig(getBigPort());
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(borderFig);
        attributesFigCompartment =
            new FigAttributesCompartment(10, 30, 60, ROWHEIGHT + 2);
        addFig(attributesFigCompartment);
        
        setOperationsVisible(false);
        enableSizeChecking(true);
        setSuppressCalcBounds(false);
//        setBounds(getBounds());     
    }

    /**
     * Constructor for use if this figure is created for an
     * existing interface node in the metamodel.
     *
     * @param gm   Not actually used in the current implementation
     *
     * @param node The UML object being placed.
     */
    public FigSignal(GraphModel gm, Object node) {
        this();
        enableSizeChecking(true);
        setOwner(node);
        setBounds(getBounds());
    }

    /**
     * Construct a FigSignal owned by the given Signal and with
     * bounds specified.
     *
     * @param node The UML object being placed.
     * @param x X coordinate
     * @param y Y coordinate
     * @param w width
     * @param h height
     */
    public FigSignal(Object node, int x, int y, int w, int h) {
        this(null, node);
        setBounds(x, y, w, h);
    }
    
    /*
     * @see org.argouml.uml.diagram.static_structure.ui.FigDataType#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionSignal(this);
    }


    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension aSize = getNameFig().getMinimumSize();
        aSize.height += 4; // +2 padding above and below name
        aSize.height = Math.max(21, aSize.height);

        aSize = addChildDimensions(aSize, getStereotypeFig());
        aSize = addChildDimensions(aSize, getOperationsFig());

        aSize.width = Math.max(40, aSize.width);

        return aSize;
    }

    /**
     * Add size of a child component to overall size.  Width is maximized
     * with child's width and child's height is added to the overall height.
     * If the child figure is not visible, it's size is not added.
     * 
     * @param size current dimensions
     * @param child child figure
     * @return new Dimension with child size added
     */
    private Dimension addChildDimensions(Dimension size, Fig child) {
        if (child.isVisible()) {
            Dimension childSize = child.getMinimumSize();
            size.width = Math.max(size.width, childSize.width);
            size.height += childSize.height;
        }
        return size;
    }

    /*
     * @see org.argouml.uml.diagram.static_structure.ui.FigClassifierBox#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        
        // TODO: Do we have anything to add here?

        return popUpActions;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
        }
    }
    
    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        if (newOwner != null) {
            // add the listeners to the newOwner
            addElementListener(newOwner);
            // and its stereotypes
            Collection c = new ArrayList(
                    Model.getFacade().getStereotypes(newOwner));
            // And now add listeners to them all:
            Iterator it2 = c.iterator();
            while (it2.hasNext()) {
                addElementListener(it2.next());
            }
        }
    }


    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setStandardBounds(final int x, final int y, final int w,
            final int h) {

        // Save our old boundaries so it can be used in property message later
        Rectangle oldBounds = getBounds();

        // set bounds of big box
        getBigPort().setBounds(x, y, w, h);
        borderFig.setBounds(x, y, w, h);

        getNameFig().setLineWidth(0);

        int currentHeight = 0;

        if (getStereotypeFig().isVisible()) {
            int stereotypeHeight = getStereotypeFig().getMinimumSize().height;
            getStereotypeFig().setBounds(
                    x,
                    y,
                    w,
                    stereotypeHeight);
            currentHeight += stereotypeHeight;
        }

        int nameHeight = getNameFig().getMinimumSize().height;
        getNameFig().setBounds(x, y + currentHeight, w, nameHeight);
        currentHeight += nameHeight;


        // Now force calculation of the bounds of the figure, update the edges
        // and trigger anyone who's listening to see if the "bounds" property
        // has changed.

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

} 
