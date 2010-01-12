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

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Component in a diagram.
 *
 * @author 5eichler
 */
public class FigComponent extends AbstractFigComponent {

    /**
     * Construct a new FigComponent.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     */
    public FigComponent(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
    }
    
    @Override
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp("parsing.help.fig-component");
        }
    }

    @Override
    public Selection makeSelection() {
        return new SelectionComponent(this);
    }

    @Override
    public void setEnclosingFig(Fig encloser) {

        Object comp = getOwner();
        if (encloser != null
                && (Model.getFacade().isANode(encloser.getOwner()) 
                        || Model.getFacade().isAComponent(encloser.getOwner()))
                && getOwner() != null) {
            if (Model.getFacade().isANode(encloser.getOwner())) {
                Object node = encloser.getOwner();
                if (!Model.getFacade().getDeploymentLocations(comp).contains(
                        node)) {
                    Model.getCoreHelper().addDeploymentLocation(comp, node);
                }
            }
            super.setEnclosingFig(encloser);

            if (getLayer() != null) {
                // elementOrdering(figures);
                List contents = new ArrayList(getLayer().getContents());
                Iterator it = contents.iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof FigEdgeModelElement) {
                        FigEdgeModelElement figedge = (FigEdgeModelElement) o;
                        figedge.getLayer().bringToFront(figedge);
                    }
                }
            }
        } else if (encloser == null && getEnclosingFig() != null) {
            Object encloserOwner = getEnclosingFig().getOwner();
            if (Model.getFacade().isANode(encloserOwner)
                    && (Model.getFacade().getDeploymentLocations(comp)
                            .contains(encloserOwner))) {
                Model.getCoreHelper().removeDeploymentLocation(comp,
                        encloserOwner);
            }
            super.setEnclosingFig(encloser);
        }
    }

    /*
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    @Override
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        // Modifiers ...
        popUpActions.add(
                popUpActions.size() - getPopupAddOffset(),
                buildModifierPopUp(ABSTRACT | LEAF | ROOT));
        return popUpActions;
    }

}
