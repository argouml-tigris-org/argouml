/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
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
// Copyright (c) 2007-2009 The Regents of the University of California. All
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
import java.util.Vector;

import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigCompartment;
import org.tigris.gef.base.Selection;

/**
 * Class to display graphics for a UML Signal in a diagram.
 * <p>
 * A Signal has a keyword "signal", possibly some stereotypes, and a name. 
 * It may also have attributes - the UML standard document 
 * contains an example diagram showing this.
 * A Signal may have operations.
 * 
 * @author Tom Morris
 */
public class FigSignal extends FigClassifierBox {

    /**
     * Construct a Fig representing a Signal.
     * 
     * @param owner owning Signal
     * @param bounds position and size
     * @param settings render settings
     */
    public FigSignal(Object owner, Rectangle bounds, DiagramSettings settings) {
        super(owner, bounds, settings);
        constructFigs(bounds);
    }

    private void constructFigs(Rectangle bounds) {
        enableSizeChecking(false);
        setSuppressCalcBounds(true);

        getStereotypeFig().setKeyword("signal");
        getStereotypeFig().setVisible(true);
        /* The next line is needed so that we have the right dimension 
         * when drawing this Fig on the diagram by pressing down 
         * the mouse button, even before releasing the mouse button: */
        getNameFig().setTopMargin(
                getStereotypeFig().getMinimumSize().height);

        addFig(getBigPort());
        addFig(getNameFig());
        /* Stereotype covers NameFig: */
        addFig(getStereotypeFig());
        /* Compartments from top to bottom: */
        createCompartments();

        // Make all the parts match the main fig
        setFilled(true);
        setFillColor(FILL_COLOR);
        setLineColor(LINE_COLOR);
        setLineWidth(LINE_WIDTH);
        
        // by default, do not show operations nor attributes:
        FigCompartment ops = getCompartment(Model.getMetaTypes().getOperation());
        setCompartmentVisible(ops, false);
        FigCompartment atts = getCompartment(Model.getMetaTypes().getAttribute());
        setCompartmentVisible(atts, false);

        /* Set the drop location in the case of D&D: */
        if (bounds != null) {
            setLocation(bounds.x, bounds.y);
        }

        setSuppressCalcBounds(false);
        setBounds(getBounds());
        enableSizeChecking(true);
    }
    
    @Override
    public Selection makeSelection() {
        return new SelectionSignal(this);
    }

    @Override
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        
        // TODO: Do we have anything to add here?

        return popUpActions;
    }

} 
