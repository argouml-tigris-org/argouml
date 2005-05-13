// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

import org.argouml.kernel.ProjectManager;
import org.argouml.ui.StylePanelFigNodeModelElement;

/**
 * Stylepanel which adds an attributes and operations checkbox and depends on
 * FigClass.
 *
 * @see FigClass
 *
 */
public class StylePanelFigClass extends StylePanelFigNodeModelElement {

    private JCheckBox attrCheckBox = new JCheckBox("Attributes");

    private JCheckBox operCheckBox = new JCheckBox("Operations");

    private JLabel displayLabel = new JLabel("Display: ");

    /**
     * Flag to indicate that a refresh is going on.
     */
    private boolean refreshTransaction = false;

    ////////////////////////////////////////////////////////////////
    // contructors

    /**
     * The constructor.
     *
     */
    public StylePanelFigClass() {
        super();

        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        pane.add(attrCheckBox);
        pane.add(operCheckBox);

        //This instead of the label ???
        //pane.setBorder(new TitledBorder(Translator.localize("Display: ")));
        
        displayLabel.setLabelFor(pane);
        add(pane, 0);
        add(displayLabel, 0);

        attrCheckBox.setSelected(false);
        operCheckBox.setSelected(false);
        attrCheckBox.addItemListener(this);
        operCheckBox.addItemListener(this);
    }

    /**
     * Only refresh the tab if the bounds propertyChange event arrives.
     *
     * @see org.argouml.ui.StylePanel#refresh(java.beans.PropertyChangeEvent)
     */
    public void refresh(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        if (propertyName.equals("bounds")) {
            refreshTransaction = true;
            refresh();
            refreshTransaction = false;
        }
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        refreshTransaction = true;
        super.refresh();
        FigClass tc = (FigClass) getPanelTarget();
        attrCheckBox.setSelected(tc.isAttributesVisible());
        operCheckBox.setSelected(tc.isOperationsVisible());
        refreshTransaction = false;
    }

    ////////////////////////////////////////////////////////////////
    // event handling

    /**
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        if (!refreshTransaction) {
            Object src = e.getSource();

            if (src == attrCheckBox) {
                ((FigClass) getPanelTarget()).setAttributesVisible(attrCheckBox
                        .isSelected());
                ProjectManager.getManager().setNeedsSave(true);
            } else if (src == operCheckBox) {
                ((FigClass) getPanelTarget()).setOperationsVisible(operCheckBox
                        .isSelected());
                ProjectManager.getManager().setNeedsSave(true);
            } else
                super.itemStateChanged(e);
        }
    }

} /* end class StylePanelFigClass */

