// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: StylePanelFigUseCase.java
// Classes: StylePanelFigUseCase
// Original Author: mail@jeremybennett.com
// $Id$

// 12 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Created to support
// optional display of extension points.

package org.argouml.uml.diagram.use_case.ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.argouml.ui.StylePanelFigNodeModelElement;

/**
 * <p>
 * A class to provide a style panel for use cases.
 * </p>
 * 
 * <p>
 * This adds a check box to control the display of he extension point
 * compartment.
 * </p>
 */
public class StylePanelFigUseCase extends StylePanelFigNodeModelElement {

    /**
     * <p>
     * The check box for extension points.
     * </p>
     */
    private JCheckBox epCheckBox = new JCheckBox("Extension Points");

    /**
     * The label alongside the check box for extension points.
     */
    private JLabel displayLabel = new JLabel("Display: ");

    /**
     * Flag to indicate that a refresh is going on.
     */
    private boolean refreshTransaction = false;

    /**
     * <p>
     * Build a style panel. Just layout the relevant boxes.
     * </p>
     */
    public StylePanelFigUseCase() {

        // Invoke the parent constructor first

        super();

        // Get the layout and its current constraints and set the constraints
        // that will apply to everything we now add.

        GridBagLayout gb = (GridBagLayout) getLayout();
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 0;
        c.ipady = 0;

        // Set constraints for the display label, and then add it.

        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 0;
        c.weightx = 0.0;

        gb.setConstraints(displayLabel, c);
        add(displayLabel);

        // Create the check box, set constraints for it, and then add it.

        JPanel pane = new JPanel();

        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        pane.add(epCheckBox);

        c.gridx = 1;
        c.gridwidth = 1;
        c.gridy = 0;
        c.weightx = 0.0;

        gb.setConstraints(pane, c);
        add(pane);

        // By default we don't show the attribute check box. Mark this object
        // as a listener for the check box.

        epCheckBox.setSelected(false);
        epCheckBox.addItemListener(this);
    }

    /**
     * <p>
     * Refresh the display. This means setting the check box from the target use
     * case fig.
     * </p>
     */
    public void refresh() {

        refreshTransaction = true;

        // Invoke the parent refresh first

        super.refresh();

        FigUseCase target = (FigUseCase) getTarget();

        epCheckBox.setSelected(target.isExtensionPointVisible());

        refreshTransaction = false;
    }

    /**
     * <p>
     * Something has changed, check if its the check box.
     * </p>
     * 
     * @param e
     *            The event that triggeed us.
     */
    public void itemStateChanged(ItemEvent e) {
        if (!refreshTransaction) {
            Object src = e.getSource();

            // If it was the check box, reset it, otherwise invoke the parent.

            if (src == epCheckBox) {
                FigUseCase target = (FigUseCase) getTarget();

                target.setExtensionPointVisible(epCheckBox.isSelected());

                markNeedsSave();
            } else {
                super.itemStateChanged(e);
            }
        }
    }

} /* end class StylePanelFigUseCase */
