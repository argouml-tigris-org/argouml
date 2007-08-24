// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.ActionCompartmentDisplay;
import org.argouml.uml.diagram.ui.ActionEdgesDisplay;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.argouml.uml.diagram.ui.FigCompartmentBox;
import org.argouml.uml.diagram.ui.FigStereotypesCompartment;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewTagDefinition;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;

/**
 * Class to display a Stereotype declaration figure using
 * Classifier box notation.<p>
 *
 * TODO: This is just a placeholder right now! - tfm
 */
public class FigStereotypeDeclaration extends FigCompartmentBox {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -2702539988691983863L;
    
    /**
     * Create a new Fig for a stereotype declaration.
     */
    public FigStereotypeDeclaration() {
        FigStereotypesCompartment fsc =
            (FigStereotypesCompartment) getStereotypeFig();
        fsc.setKeyword("stereotype");

        // Put all the bits together, suppressing bounds calculations until
        // we're all done for efficiency.
        enableSizeChecking(false);
        setSuppressCalcBounds(true);
        addFig(getBigPort());
        addFig(getStereotypeFig());
        addFig(getNameFig());

        // TODO: Need named Tags and Constraints compartments here
//        addFig(tagsFig);
//        addFig(constraintsFig);

        addFig(getBorderFig());

        setSuppressCalcBounds(false);
        // Set the bounds of the figure to the total of the above (hardcoded)
        setBounds(10, 10, 60, 22 + 2 * ROWHEIGHT);
    }

    /**
     * Constructor for use if this figure is created for an existing class
     * node in the metamodel.
     *
     * @param gm   Not actually used in the current implementation
     *
     * @param node The UML object being placed.
     */
    public FigStereotypeDeclaration(GraphModel gm, Object node) {
        this();
        setOwner(node);
        enableSizeChecking(true);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionStereotype(this);
    }

    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on a Stereotype.
     * {@inheritDoc}
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);

        // Add...
        ArgoJMenu addMenu = new ArgoJMenu("menu.popup.add");
        // TODO: Add Tags & Constraints
//        addMenu.add(TargetManager.getInstance().getAddAttributeAction());
//        addMenu.add(TargetManager.getInstance().getAddOperationAction());
        addMenu.add(new ActionAddNote());
        addMenu.add(new ActionNewTagDefinition());
        addMenu.add(ActionEdgesDisplay.getShowEdges());
        addMenu.add(ActionEdgesDisplay.getHideEdges());
        popUpActions.insertElementAt(addMenu,
            popUpActions.size() - getPopupAddOffset());

        // Show ...
        ArgoJMenu showMenu = new ArgoJMenu("menu.popup.show");
        Iterator i = ActionCompartmentDisplay.getActions().iterator();
        while (i.hasNext()) {
            showMenu.add((Action) i.next());
        }
        if (showMenu.getComponentCount() > 0) {
            popUpActions.insertElementAt(showMenu,
                    popUpActions.size() - getPopupAddOffset());
        }

        // Modifiers ...
        popUpActions.insertElementAt(
                buildModifierPopUp(ABSTRACT | LEAF | ROOT),
                popUpActions.size() - getPopupAddOffset());

        // Visibility ...
        popUpActions.insertElementAt(buildVisibilityPopUp(),
                popUpActions.size() - getPopupAddOffset());

        return popUpActions;
    }

    /**
     * Gets the minimum size permitted for a class on the diagram.<p>
     *
     * @return  the size of the minimum bounding box.
     */
    public Dimension getMinimumSize() {
        Dimension aSize = getNameFig().getMinimumSize();
        if (getStereotypeFig().isVisible()) {
            Dimension stereoMin = getStereotypeFig().getMinimumSize();
            aSize.width = Math.max(aSize.width, stereoMin.width);
            aSize.height += stereoMin.height;
        }

        // TODO: Allow space for each of the Tags & Constraints we have

        // we want to maintain a minimum width for the class
        aSize.width = Math.max(60, aSize.width);

        return aSize;
    }

    /**
     * Sets the bounds, but the size will be at least the one returned by
     * {@link #getMinimumSize()}, unless checking of size is disabled.<p>
     *
     * If the required height is bigger, then the additional height is
     * equally distributed among all figs (i.e. compartments), such that the
     * cumulated height of all visible figs equals the demanded height<p>.
     *
     * Some of this has "magic numbers" hardcoded in. In particular there is
     * a knowledge that the minimum height of a name compartment is 21
     * pixels.<p>
     *
     * @param x  Desired X coordinate of upper left corner
     *
     * @param y  Desired Y coordinate of upper left corner
     *
     * @param w  Desired width of the FigClass
     *
     * @param h  Desired height of the FigClass
     *
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setBoundsImpl(final int x, final int y,
            final int w, final int h) {
        Rectangle oldBounds = getBounds();

        // set bounds of big box
        getBigPort().setBounds(x, y, w, h);
        getBorderFig().setBounds(x, y, w, h);

        // Save our old boundaries (needed later), and get minimum size
        // info. "whitespace" will be used to maintain a running calculation
        // of our size at various points.

        // final int whitespace = h - getMinimumSize().height;

        getNameFig().setLineWidth(0);
        getNameFig().setLineColor(Color.red);
        int currentHeight = 0;

        if (getStereotypeFig().isVisible()) {
            int stereotypeHeight = getStereotypeFig().getMinimumSize().height;
            getStereotypeFig().setBounds(
                    x,
                    y,
                    w,
                    stereotypeHeight);
            currentHeight = stereotypeHeight;
        }

        int nameHeight = getNameFig().getMinimumSize().height;
        getNameFig().setBounds(x, y + currentHeight, w, nameHeight);
        currentHeight += nameHeight;

        // TODO: Compute size of Tags and Constraints


        // Now force calculation of the bounds of the figure, update the edges
        // and trigger anyone who's listening to see if the "bounds" property
        // has changed.

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /*
     * @see org.argouml.uml.diagram.static_structure.ui.FigCompartmentBox#unhighlight()
     */
    protected CompartmentFigText unhighlight() {
        CompartmentFigText fc = super.unhighlight();
        if (fc == null) {
            // TODO: Try unhighlighting our child compartments
//            fc = unhighlight(getAttributesFig());
        }
        return fc;
    }

    /**
     * Handles changes to the model. Takes into account the event that
     * occurred. If you need to update the whole fig, consider using
     * renderingChanged.
     *
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
            damage();
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
            addElementListener(newOwner);
            // register for tagdefinitions:
            Iterator it =
                Model.getFacade().getTagDefinitions(newOwner).iterator();
            while (it.hasNext()) {
                Object td = it.next();
                addElementListener(td, 
                        new String[] {"name", "tagType", "multiplicity"});
            }
            /* TODO: constraints, ... */
        }
    }
} /* end class FigClass */
