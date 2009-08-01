// $Id: FigStereotypeDeclaration.java 17045 2009-04-05 16:52:52Z mvw $
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

package org.argouml.class2.diagram;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.Action;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.ActionCompartmentDisplay;
import org.argouml.uml.diagram.ui.ActionEdgesDisplay;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.argouml.uml.diagram.ui.FigCompartmentBox;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewTagDefinition;
import org.tigris.gef.base.Selection;

/**
 * Class to display a Stereotype declaration figure using
 * Classifier box notation.<p>
 *
 * TODO: This is just a placeholder right now! - tfm
 */
public class FigStereotypeDeclaration2 extends FigCompartmentBox {

    private static final long serialVersionUID = -2702539988691983863L;

    private void constructFigs() {
        getStereotypeFig().setKeyword("stereotype");

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
        setBounds(X0, Y0, WIDTH, STEREOHEIGHT + NAME_FIG_HEIGHT);
    }

    /**
     * Construct a Fig for a Stereotype on a diagram.
     * 
     * @param owner owning stereotype
     * @param bounds position and size
     * @param settings render settings
     */
    public FigStereotypeDeclaration2(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        constructFigs();
        enableSizeChecking(true);
    }
    
    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        return new SelectionStereotype(this);
    }

    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on a Stereotype.
     * {@inheritDoc}
     */
    @Override
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
        popUpActions.add(popUpActions.size() - getPopupAddOffset(), addMenu);

        // Show ...
        ArgoJMenu showMenu = new ArgoJMenu("menu.popup.show");
        for (Action action : ActionCompartmentDisplay.getActions()) {
            showMenu.add(action);
        }
        if (showMenu.getComponentCount() > 0) {
            popUpActions.add(popUpActions.size() - getPopupAddOffset(),
                    showMenu);
        }

        // Modifiers ...
        popUpActions.add(popUpActions.size() - getPopupAddOffset(),
                buildModifierPopUp(ABSTRACT | LEAF | ROOT));

        // Visibility ...
        popUpActions.add(popUpActions.size() - getPopupAddOffset(),
                buildVisibilityPopUp());

        return popUpActions;
    }

    /**
     * Gets the minimum size permitted for a class on the diagram.<p>
     *
     * @return  the size of the minimum bounding box.
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension aSize = getNameFig().getMinimumSize();
        
        //TODO: Why does this differ from the other Figs?
        aSize = addChildDimensions(aSize, getStereotypeFig());

        // TODO: Allow space for each of the Tags & Constraints we have

        // we want to maintain a minimum width for the class
        aSize.width = Math.max(WIDTH, aSize.width);

        return aSize;
    }

    /**
     * Sets the bounds, but the size will be at least the one returned by
     * {@link #getMinimumSize()}, unless checking of size is disabled.<p>
     *
     * @param x  Desired X coordinate of upper left corner
     *
     * @param y  Desired Y coordinate of upper left corner
     *
     * @param w  Desired width of the fig
     *
     * @param h  Desired height of the fig
     *
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    @Override
    protected void setStandardBounds(final int x, final int y,
            final int w, final int h) {
        Rectangle oldBounds = getBounds();

        // set bounds of big box
        getBigPort().setBounds(x, y, w, h);
        getBorderFig().setBounds(x, y, w, h);

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
    @Override
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
     * {@inheritDoc}
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    @Override
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
    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        
        Set<Object[]> listeners = new HashSet<Object[]>();
        if (newOwner != null) {
            listeners.add(new Object[] {newOwner, null});
            // register for tagdefinitions:
            for (Object td : Model.getFacade().getTagDefinitions(newOwner)) {
                listeners.add(new Object[] {td,
                    new String[] {"name", "tagType", "multiplicity"}});
            }
            /* TODO: constraints, ... */
        }
        updateElementListeners(listeners);
    }
}
