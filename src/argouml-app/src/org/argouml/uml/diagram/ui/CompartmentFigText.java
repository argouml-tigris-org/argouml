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

package org.argouml.uml.diagram.ui;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.SelectionCompartmentText;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

/**
 * A single line FigText class extension for editable 
 * FigClass/FigInterface/FigUseCase
 * compartments that use notation. 
 *
 * @author thn
 */
public abstract class CompartmentFigText extends FigSingleLineTextWithNotation
        implements Clarifiable {
    
    private static final int MARGIN = 3;

    /**
     * Construct a CompartmentFigText.
     * 
     * @param element owning uml element
     * @param bounds position and size
     * @param settings render settings
     */
    public CompartmentFigText(Object element, Rectangle bounds,
            DiagramSettings settings) {
        super(element, bounds, settings, true);

        setJustification(FigText.JUSTIFY_LEFT);
        setRightMargin(MARGIN);
        setLeftMargin(MARGIN);
        // TODO: We'd like these to not be filled, but GEF won't let us
        // select them if we do that.
//        setFilled(false);
    }

    /**
     * Build a new compartment figText of the given dimensions, within the
     * compartment described by <code>aFig</code>.
     * <p>
     * Invoke the parent constructor, then set the reference to the associated
     * compartment figure. The associated FigText is marked as expand only.
     * <p>
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     * @param property The property this Fig should listen for
     */
    public CompartmentFigText(Object owner, Rectangle bounds, 
            DiagramSettings settings, String property) {
        this(owner, bounds, settings, new String[] {property});
    }
    
    /**
     * Build a new compartment figText of the given dimensions, within the
     * compartment described by <code>aFig</code>.
     * <p>
     * Invoke the parent constructor, then set the reference to the associated
     * compartment figure. The associated FigText is marked as expand only.
     * <p>
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     * @param properties The properties this Fig should listen for
     */
    public CompartmentFigText(Object owner, Rectangle bounds, 
            DiagramSettings settings, String[] properties) {
        super(owner, bounds, settings, true, properties);
    }
    
    /*
     * @see org.argouml.uml.diagram.ui.FigSingleLineText#removeFromDiagram()
     */
    @Override
    public void removeFromDiagram() {
        super.removeFromDiagram();
        Fig fg = getGroup();
        if (fg instanceof FigGroup) {
            ((FigGroup) fg).removeFig(this);
            setGroup(null);
        }
    }

    /**
     * @return  Current fill status&mdash;always <code>false</code>.
     */
    @Override
    public boolean isFilled() {
        return false;
    }

    public void paintClarifiers(Graphics g) {
    }
    
    public Selection makeSelection() {
        return new SelectionCompartmentText(this);
    }

    public boolean isResizable() {
        return false;
    }
}
