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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Class in a diagram.<p>
 * 
 * A Class may show stereotypes, a name and compartments for
 * attributes and operations.
 */
public class FigClass extends FigClassifierBox {

    /**
     * Constructor for a {@link FigClass} during file load.<p>
     *
     * Parent {@link org.argouml.uml.diagram.ui.FigNodeModelElement}
     * will have created the main box {@link #getBigPort()} and its
     * name {@link #getNameFig()} and stereotype
     * (@link #getStereotypeFig()}. This constructor
     * creates a box for the attributes and operations.<p>
     *
     * The properties of all these graphic elements are adjusted
     * appropriately. The main boxes are all filled and have
     * outlines. 
     * TODO: which is wrong, since the bigPort is filled, too.
     * 
     * @param element model element to be represented by this fig.
     * @param bounds rectangle describing bounds
     * @param settings rendering settings
     */
    public FigClass(Object element, Rectangle bounds, 
            DiagramSettings settings) {
        super(element, bounds, settings);
        constructFigs(bounds);
    }

    private void constructFigs(Rectangle bounds) {
        enableSizeChecking(false);
        setSuppressCalcBounds(true);
        
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
        
        /* Set the drop location in the case of D&D: */
        if (bounds != null) {
            setLocation(bounds.x, bounds.y);
        }
        
        setSuppressCalcBounds(false);
        setBounds(getBounds());
        enableSizeChecking(true);
    }

    @Override
    public Object clone() {
        FigClass figClone = (FigClass) super.clone();
        Iterator thisIter = this.getFigs().iterator();
        Iterator cloneIter = figClone.getFigs().iterator();
        while (thisIter.hasNext()) {
            Fig thisFig = (Fig) thisIter.next();
            Fig cloneFig = (Fig) cloneIter.next();
        }
        return figClone;
    }

    public Selection makeSelection() {
        return new SelectionClass(this);
    }

    protected Object buildModifierPopUp() {
        return buildModifierPopUp(ABSTRACT | LEAF | ROOT | ACTIVE);
    }

    /**
     * @param fgVec the FigGroup
     * @param ft    the Figtext
     * @param i     get the fig before fig i
     * @return the FigText
     */
    protected FigText getPreviousVisibleFeature(FigGroup fgVec,
						FigText ft, int i) {
        if (fgVec == null || i < 1) {
            return null;
        }
        FigText ft2 = null;
        List figs = fgVec.getFigs();
        if (i >= figs.size() || !((FigText) figs.get(i)).isVisible()) {
            return null;
        }
        do {
            i--;
            while (i < 1) {
                if (fgVec == getCompartment(Model.getMetaTypes().getAttribute())) {
                    fgVec = getCompartment(Model.getMetaTypes().getOperation());
                } else {
                    fgVec = getCompartment(Model.getMetaTypes().getAttribute());
                }
                figs = fgVec.getFigs();
                i = figs.size() - 1;
            }
            ft2 = (FigText) figs.get(i);
            if (!ft2.isVisible()) {
                ft2 = null;
            }
        } while (ft2 == null);
        return ft2;
    }

    /**
     * @param fgVec the FigGroup
     * @param ft    the FigText
     * @param i     get the fig after fig i
     * @return the FigText
     */
    protected FigText getNextVisibleFeature(FigGroup fgVec, FigText ft, int i) {
        if (fgVec == null || i < 1) {
            return null;
        }
        FigText ft2 = null;
        List v = fgVec.getFigs();
        if (i >= v.size() || !((FigText) v.get(i)).isVisible()) {
            return null;
        }
        do {
            i++;
            while (i >= v.size()) {
                if (fgVec == getCompartment(Model.getMetaTypes().getAttribute())) {
                    fgVec = getCompartment(Model.getMetaTypes().getOperation());
                } else {
                    fgVec = getCompartment(Model.getMetaTypes().getAttribute());
                }
                v = new ArrayList(fgVec.getFigs());
                i = 1;
            }
            ft2 = (FigText) v.get(i);
            if (!ft2.isVisible()) {
                ft2 = null;
            }
        } while (ft2 == null);
        return ft2;
    }

    public void setEnclosingFig(Fig encloser) {
        if (encloser == getEncloser()) {
            return;
        }
        if (encloser == null
                || (encloser != null
                && !Model.getFacade().isAInstance(encloser.getOwner()))) {
            super.setEnclosingFig(encloser);
        }
        if (!(Model.getFacade().isAUMLElement(getOwner()))) {
            return;
        }
        if (encloser != null
                && (Model.getFacade().isAComponent(encloser.getOwner()))) {
            moveIntoComponent(encloser);
            super.setEnclosingFig(encloser);
        }

    }

    @Override
    protected void updateNameText() {
        super.updateNameText();
        calcBounds();
        setBounds(getBounds());
    }
    
}
