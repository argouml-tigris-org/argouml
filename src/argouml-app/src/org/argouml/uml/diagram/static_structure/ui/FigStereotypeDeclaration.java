/* $Id$
 *******************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    Bob Tarling
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.Action;

import org.argouml.model.Model;
import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.ActionCompartmentDisplay;
import org.argouml.uml.diagram.ui.ActionEdgesDisplay;
import org.argouml.uml.diagram.ui.FigCompartmentBox;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewTagDefinition;
import org.tigris.gef.base.Selection;

/**
 * Class to display a Stereotype declaration figure using
 * Classifier box notation.<p>
 *
 * TODO: This is just a place-holder right now! - tfm
 * This needs to show tags and constraints.
 */
public class FigStereotypeDeclaration extends FigCompartmentBox {

    private void constructFigs(Rectangle bounds) {
        // Put all the bits together, suppressing bounds calculations until
        // we're all done for efficiency.
        enableSizeChecking(false);
        setSuppressCalcBounds(true);

        getStereotypeFig().setKeyword("stereotype");
        getStereotypeFig().setVisible(true);
        /* The next line is needed so that we have the right dimension 
         * when drawing this Fig on the diagram by pressing down 
         * the mouse button, even before releasing the mouse button: */
        getNameFig().setTopMargin(
                getStereotypeFig().getMinimumSize().height);

        addFig(getBigPort());
        addFig(getNameFig());
        // stereotype fig covers the name fig:
        addFig(getStereotypeFig());

        // TODO: Need named Tags and Constraints compartments here
//        addFig(tagsFig);
//        addFig(constraintsFig);

        // Make all the parts match the main fig
        setFilled(true);
        setFillColor(FILL_COLOR);
        setLineColor(LINE_COLOR);
        setLineWidth(LINE_WIDTH);

        /* Set the drop location in the case of D&D: */
        if (bounds != null) {
            setLocation(bounds.x, bounds.y);
        }

        setSuppressCalcBounds(false);
        setBounds(getBounds());
    }

    /**
     * Construct a Fig for a Stereotype on a diagram.
     * 
     * @param owner owning stereotype
     * @param bounds position and size
     * @param settings render settings
     */
    public FigStereotypeDeclaration(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        constructFigs(bounds);
        enableSizeChecking(true);
    }
    
    @Override
    public Selection makeSelection() {
        return new SelectionStereotype(this);
    }

    /**
     * Build a collection of menu items relevant for a right-click
     * pop-up menu on a Stereotype.
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

    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        
        Set<Object[]> listeners = new HashSet<Object[]>();
        if (newOwner != null) {
            listeners.add(new Object[] {newOwner, null});
            // register for tagDefinitions:
            for (Object td : Model.getFacade().getTagDefinitions(newOwner)) {
                listeners.add(new Object[] {td,
                    new String[] {"name", "tagType", "multiplicity"}});
            }
            /* TODO: constraints, ... */
        }
        updateElementListeners(listeners);
    }
}
