// $Id: StylePanelFigClass.java 11516 2006-11-25 04:30:15Z tfmorris $
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
import java.beans.PropertyChangeEvent;

import javax.swing.JCheckBox;

import org.argouml.i18n.Translator;
import org.argouml.ui.StylePanelFigNodeModelElement;
import org.argouml.uml.diagram.ui.AttributesCompartmentContainer;
import org.argouml.uml.diagram.ui.OperationsCompartmentContainer;

/**
 * Stylepanel which adds an attributes and operations checkbox and depends on
 * FigClass.
 *
 * @see FigClass
 *
 */
public class StylePanelFigClass extends StylePanelFigNodeModelElement {

    /* TODO: i18n */
    private JCheckBox attrCheckBox =
            new JCheckBox(Translator.localize("checkbox.attributes"));

    private JCheckBox operCheckBox =
            new JCheckBox(Translator.localize("checkbox.operations"));

    /**
     * Flag to indicate that a refresh is going on.
     */
    private boolean refreshTransaction;

    ////////////////////////////////////////////////////////////////
    // contructors

    /**
     * The constructor.
     *
     */
    public StylePanelFigClass() {
        super();

        addToDisplayPane(attrCheckBox);
        addToDisplayPane(operCheckBox);

        attrCheckBox.setSelected(false);
        operCheckBox.setSelected(false);
        attrCheckBox.addItemListener(this);
        operCheckBox.addItemListener(this);
    }

    /*
     * Only refresh the tab if the bounds propertyChange event arrives.
     *
     * @see org.argouml.ui.StylePanel#refresh(java.beans.PropertyChangeEvent)
     */
    public void refresh(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        if (propertyName.equals("bounds")) {
            refresh();
        }
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /*
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        refreshTransaction = true;
        super.refresh();
        AttributesCompartmentContainer ac =
                (AttributesCompartmentContainer) getPanelTarget();
        attrCheckBox.setSelected(ac.isAttributesVisible());
        OperationsCompartmentContainer oc =
                (OperationsCompartmentContainer) getPanelTarget();
        operCheckBox.setSelected(oc.isOperationsVisible());
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

            if (src == attrCheckBox) {
                ((AttributesCompartmentContainer) getPanelTarget())
                    .setAttributesVisible(attrCheckBox.isSelected());
            } else if (src == operCheckBox) {
                ((OperationsCompartmentContainer) getPanelTarget())
                    .setOperationsVisible(operCheckBox.isSelected());
            } else {
                super.itemStateChanged(e);
            }
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 4587367369055254943L;
} /* end class StylePanelFigClass */

