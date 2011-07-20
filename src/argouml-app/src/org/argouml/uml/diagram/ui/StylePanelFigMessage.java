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

package org.argouml.uml.diagram.ui;

import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.argouml.i18n.Translator;
import org.argouml.ui.StylePanelFigNodeModelElement;

/**
 * Stylepanel which allows to set the arrow of a message.
 *
 * @see FigMessage
 */
public class StylePanelFigMessage extends StylePanelFigNodeModelElement {

    private JLabel arrowLabel = 
        new JLabel(Translator.localize("label.localize"));

    private JComboBox arrowField = 
        new JComboBox(FigMessage.getArrowDirections().toArray());


    /**
     * The constructor.
     *
     */
    public StylePanelFigMessage() {
        super();

        arrowField.addItemListener(this);

        arrowLabel.setLabelFor(arrowField);
        add(arrowLabel);
        add(arrowField);

        arrowField.setSelectedIndex(0);

        remove(getFillField());
        remove(getFillLabel());
    }


    /*
     * @see org.argouml.ui.TabTarget#refresh()
     */
    @Override
    public void refresh() {
        super.refresh();
        int direction = ((FigMessage) getPanelTarget()).getArrow();
        arrowField.setSelectedItem(FigMessage.getArrowDirections()
                .get(direction));
    }

    /**
     * Set the arrow direction for the target.
     */
    public void setTargetArrow() {
        String ad = (String) arrowField.getSelectedItem();
        int arrowDirection = FigMessage.getArrowDirections().indexOf(ad);
        if (getPanelTarget() == null || arrowDirection == -1) {
            return;
        }
        ((FigMessage) getPanelTarget()).setArrow(arrowDirection);
        getPanelTarget().endTrans();
    }

    /*
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        Object src = e.getSource();
        if (src == arrowField) {
            setTargetArrow();
        }
        else {
            super.itemStateChanged(e);
        }
    }

}

