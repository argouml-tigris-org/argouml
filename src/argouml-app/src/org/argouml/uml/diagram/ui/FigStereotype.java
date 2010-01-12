/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
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

import java.awt.Rectangle;

import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.notation.providers.uml.NotationUtilityUml;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * Fig to show one stereotype within a FigStereotypesGroup.
 * <p>
 * The stereotype is not editable on the fig, hence we
 * do not use a Notation Provider. <p>
 * This Fig listens to model changes regarding the name of the stereotype.
 *
 * @author Bob Tarling
 */
public class FigStereotype extends FigSingleLineText {

    /**
     * Construct a fig for a Stereotype or a keyword (which visually resembles a
     * stereotype).
     * 
     * @param owner owning UML element or null if this fig is being used for a
     *            keyword.
     * @param bounds position and size
     * @param settings render settings
     */
    public FigStereotype(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings, true,
                new String[] {"name"});
        assert owner != null;
        initialize();
        setText();
    }

    private void initialize() {
        setEditable(false);
        setTextColor(TEXT_COLOR);
        setTextFilled(false);
        setJustification(FigText.JUSTIFY_CENTER);
        setRightMargin(3);
        setLeftMargin(3);
    }

    /* Force the line-width to 0, since the FigGroup that contains the 
     * stereotype may want to show a border, but we don't. */
    @Override
    public void setLineWidth(int w) {
        super.setLineWidth(0);
    }

    // Called by propertyChange
    protected void updateLayout(UmlChangeEvent event) {
        assert event != null;

        Rectangle oldBounds = getBounds();

        setText(); // this does a calcBounds()

        if (oldBounds != getBounds()) {
            setBounds(getBounds());
        }

        if (getGroup() != null ) {
            /* TODO: Why do I need to do this? */
            getGroup().calcBounds();
            getGroup().setBounds(getGroup().getBounds());
            if (oldBounds != getBounds()) {
                Fig sg = getGroup().getGroup();
                /* TODO: Why do I need to do this? */
                if (sg != null) {
                    sg.calcBounds();
                    sg.setBounds(sg.getBounds());
                }
            }
        }
        /* Test-case for the above code: 
         * Draw a class. 
         * Create a stereotype for it by clicking on the prop-panel tool, and 
         * name it.
         * Remove the class from the diagram.
         * Drag the class from the explorer on the diagram.
         * Select the stereotype in the explorer, and change
         * its name in the prop-panel to something longer.
         * The longer name does not make the class Fig wider 
         * unless the above code is added.*/
        damage();
    }

    @Override
    protected void setText() {
        setText(Model.getFacade().getName(getOwner()));
    }

    /**
     * Add guillemets to any text set to this Fig.
     * {@inheritDoc}
     */
    public void setText(String text) {
        super.setText(NotationUtilityUml.formatStereotype(text,
                getSettings().getNotationSettings().isUseGuillemets()));
        damage();
    }

}
