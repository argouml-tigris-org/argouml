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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.argouml.uml.diagram.ui.EnumLiteralsCompartmentContainer;
import org.argouml.uml.diagram.ui.FigEnumLiteralsCompartment;
import org.argouml.uml.diagram.ui.FigStereotypesCompartment;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Enumeration in a diagram.
 * It depends on FigDataType for most of its behavior.<p>
 * 
 */
public class FigEnumeration extends FigDataType 
    implements EnumLiteralsCompartmentContainer {

    /**
     * Serial version (generated)
     */
    private static final long serialVersionUID = 3333154292883077250L;

    /**
     * The Fig that represents the literals compartment.
     */
    private FigEnumLiteralsCompartment literalsCompartment;

    /**
     * Main constructor for a {@link FigEnumeration}.
     */
    public FigEnumeration() {
        super();
        FigStereotypesCompartment fsc =
            (FigStereotypesCompartment) getStereotypeFig();
        fsc.setKeyword("enumeration");

        enableSizeChecking(true);
        setSuppressCalcBounds(false);

        addFig(getLiteralsCompartment()); // This creates the compartment.
        setBounds(getBounds());
    }

    /**
     * Constructor for use if this figure is created for an
     * existing interface node in the metamodel.
     *
     * @param gm   Not actually used in the current implementation
     *
     * @param node The UML object being placed.
     */
    public FigEnumeration(GraphModel gm, Object node) {
        this();
        enableSizeChecking(true);
        setEnumLiteralsVisible(true);
        setOwner(node);
        literalsCompartment.populate();
        setBounds(getBounds());
    }

    /**
     * @see org.argouml.uml.diagram.static_structure.ui.FigDataType#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionEnumeration(this);
    }

    /**
     * @see java.lang.Object#clone()
     * TODO: Is this actually needed? - tfm
     */
    public Object clone() {
        FigEnumeration clone = (FigEnumeration) super.clone();
        clone.literalsCompartment = 
            (FigEnumLiteralsCompartment) literalsCompartment.clone();
        return clone;
    }
 
    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on an Interface.
     *
     * @param     me     a mouse event
     * @return           a collection of menu items
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);

        // Add ...
        ArgoJMenu addMenu = new ArgoJMenu("menu.popup.add");
        addMenu.add(TargetManager.getInstance()
                .getAddEnumerationLiteralAction());
        popUpActions.insertElementAt(addMenu,
                popUpActions.size() - getPopupAddOffset());

        return popUpActions;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner());
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        if (getOwner() != null) {
            updateEnumLiterals();
        }
        super.renderingChanged();
    }
    
    /**
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
            // and its features
            Iterator it = Model.getFacade().getFeatures(newOwner).iterator();
            while (it.hasNext()) {
                Object feat = it.next();
                c.add(feat);
                // and the stereotypes of its features
                c.addAll(new ArrayList(Model.getFacade().getStereotypes(feat)));
            }
            // and its enumerationLiterals
            c.addAll(Model.getFacade().getEnumerationLiterals(newOwner));
            // And now add listeners to them all:
            Iterator it2 = c.iterator();
            while (it2.hasNext()) {
                addElementListener(it2.next());
            }
        }
    }

    /**
     * Update (i.e. redraw) the compartment with the literals.
     */
    protected void updateEnumLiterals() {
        if (!literalsCompartment.isVisible()) {
            return;
        }
        literalsCompartment.populate();

        // TODO: make setBounds, calcBounds and updateBounds consistent
        setBounds(getBounds());
    }
    
    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        Object cls = /*(Classifier)*/ getOwner();
        if (cls == null) {
            return;
        }
        int i = literalsCompartment.getFigs().indexOf(ft);
        if (i != -1) {
            highlightedFigText = (CompartmentFigText) ft;
            highlightedFigText.setHighlighted(true);
            Model.getCoreHelper().setName(highlightedFigText.getOwner(),
                    highlightedFigText.getText().trim());
            return;
        }
    }

    /**
     * Gets the minimum size permitted for a enumeration on the diagram.<p>
     * 
     * @return  the size of the minimum bounding box.
     */
    public Dimension getMinimumSize() {
        // Start with the minimum for our parent
        Dimension aSize = super.getMinimumSize();

        if (literalsCompartment.isVisible()) {
            Dimension literalsMin = literalsCompartment.getMinimumSize();
            aSize.width = Math.max(aSize.width, literalsMin.width);
            aSize.height += literalsMin.height;
        }
        
        return aSize;
    }
    
    /**
     * Sets the bounds of all components, but the size will be at least the one returned by
     * {@link #getMinimumSize()}, unless checking of size is disabled.<p>
     * 
     * @param x  Desired X coordinate of upper left corner
     *
     * @param y  Desired Y coordinate of upper left corner
     *
     * @param w  Desired width of the figure
     *
     * @param h  Desired height of the figure
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setBoundsImpl(final int x, final int y, final int w,
            final int h) {

        // Save our old boundaries so it can be used in property message later
        Rectangle oldBounds = getBounds();

        // set bounds of big box
        getBigPort().setBounds(x, y, w, h);
        borderFig.setBounds(x, y, w, h);

        getNameFig().setLineWidth(0);
        
        // Vertical whitespace to be distributed
        // TODO: This continually adds more whitespace.  Figure out the problem.
        //final int whitespace = Math.max(0, h - getMinimumSize().height);
        final int whitespace = 0;
                
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

        if (getLiteralsCompartment().isVisible()) {
            int literalsHeight = 
                getLiteralsCompartment().getMinimumSize().height;
            literalsHeight += whitespace / 2;
            getLiteralsCompartment().setBounds(
                    x,
                    y + currentHeight,
                    w,
                    literalsHeight);
            currentHeight += literalsHeight;
        }
        
        if (getOperationsFig().isVisible()) {
            int operationsHeight = getOperationsFig().getMinimumSize().height;
            operationsHeight += whitespace / 2;
            getOperationsFig().setBounds(
                    x,
                    y + currentHeight,
                    w,
                    operationsHeight);
            currentHeight += operationsHeight;
        }

        // Now force calculation of the bounds of the figure, update the edges
        // and trigger anyone who's listening to see if the "bounds" property
        // has changed.

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }



    /**
     * @return the Fig for the EnumerationLiterals compartment
     */
    public FigEnumLiteralsCompartment getLiteralsCompartment() {
        // Set bounds will be called from our superclass constructor before
        // our constructor has run, so make sure this gets set up if needed.
        if (literalsCompartment == null) {
            literalsCompartment = new FigEnumLiteralsCompartment(10, 30, 60,
                    ROWHEIGHT + 2);
        }
        return literalsCompartment;
    }
    
    /**
     * @return true if the literals compartment is visible
     */
    public boolean isEnumLiteralsVisible() {
        return literalsCompartment.isVisible();
    }

    /**
     * @param isVisible true will show the enumeration literal compartment
     */
    public void setEnumLiteralsVisible(boolean isVisible) {
        Rectangle rect = getBounds();
        if (literalsCompartment.isVisible()) {
            if (!isVisible) {
                damage();
                Iterator it = literalsCompartment.getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(false);
                }
                literalsCompartment.setVisible(false);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
                          (int) aSize.getWidth(), (int) aSize.getHeight());
            }
        } else {
            if (isVisible) {
                Iterator it = literalsCompartment.getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(true);
                }
                literalsCompartment.setVisible(true);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
                          (int) aSize.getWidth(), (int) aSize.getHeight());
                damage();
            }
        }
    }
    
    /**
     * @return the bounds of the EnumerationLiterals compartment
     */
    public Rectangle getEnumLiteralsBounds() {
        return literalsCompartment.getBounds();
    }
    

} 
