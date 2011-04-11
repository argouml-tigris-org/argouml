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

// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;

import org.argouml.model.Model;
import org.argouml.ui.StylePanelFigNodeModelElement;
import org.argouml.uml.diagram.ui.FigCompartment;

/**
 * Stylepanel which adds an operation checkbox and depends on FigInterface.
 * @see FigInterface
 *
 * @author mkl
 *
 */
public class StylePanelFigInterface extends StylePanelFigNodeModelElement {

    private JCheckBox operCheckBox = new JCheckBox("Operations");

    /**
     * Flag to indicate that a refresh is going on.
     */
    private boolean refreshTransaction;

    /**
     * The constructor.
     */
    public StylePanelFigInterface() {
        super();

        addToDisplayPane(operCheckBox);
        operCheckBox.setSelected(false);
        operCheckBox.addItemListener(this);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /*
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        refreshTransaction = true;
        super.refresh();
        FigInterface ti = (FigInterface) getPanelTarget();
        operCheckBox.setSelected(
                ti.isCompartmentVisible(Model.getMetaTypes().getOperation()));
        refreshTransaction = false;
    }

    ////////////////////////////////////////////////////////////////
    // event handling

    /*
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        if (!refreshTransaction) {
            Object src = e.getSource();

            if (src == operCheckBox) {
                FigInterface fi = ((FigInterface) getPanelTarget());
                FigCompartment comp = fi.getCompartment(Model.getMetaTypes().getOperation());
                fi.setCompartmentVisible(comp, operCheckBox.isSelected());
            } else {
                super.itemStateChanged(e);
            }
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5908351031706234211L;
} /* end class StylePanelFigInterface */

