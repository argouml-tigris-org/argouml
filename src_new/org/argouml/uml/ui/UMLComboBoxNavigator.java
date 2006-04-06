// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.ui.targetmanager.TargetManager;

/**
 * This class implements a panel that adds a navigation button to the right of
 * the combo box
 * 
 * @author Curt Arnold
 * @since 0.9
 */
public class UMLComboBoxNavigator extends JPanel implements ActionListener,
        ItemListener {

    private static ImageIcon icon = ResourceLoaderWrapper
            .lookupIconResource("ComboNav");

    private UMLUserInterfaceContainer theContainer;

    private JComboBox theComboBox;

    private JButton theButton;

    /**
     * Constructor
     * 
     * @param container
     *            Container, typically a PropPanel
     * @param tooltip
     *            Tooltip key for button
     * @param box
     *            Associated combo box
     */
    public UMLComboBoxNavigator(UMLUserInterfaceContainer container,
            String tooltip, JComboBox box) {
        super(new BorderLayout());
        theButton = new JButton(icon);
        theContainer = container;
        theComboBox = box;
        theButton.setPreferredSize(new Dimension(icon.getIconWidth() + 6, icon
                .getIconHeight() + 6));
        theButton.setToolTipText(tooltip);
        theButton.addActionListener(this);
        box.addActionListener(this);
        box.addItemListener(this);
        add(theComboBox, BorderLayout.CENTER);
        add(theButton, BorderLayout.EAST);
        Object item = theComboBox.getSelectedItem();
        setButtonEnabled(item);
    }

    /**
     * Fired when the button is pushed. Navigates to the currently selected item
     * in the combo box.
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final java.awt.event.ActionEvent event) {
        // button action:
        if (event.getSource() == theButton) {
            Object item = theComboBox.getSelectedItem();
            if (item instanceof UMLComboBoxEntry) {
                UMLComboBoxEntry entry = (UMLComboBoxEntry) item;
                if (!entry.isPhantom()) {
                    Object/* MModelElement */target = entry.getElement(null);
                    if (target != null) {
                        TargetManager.getInstance().setTarget(target);
                    }
                }
            } else if (item != null) {
                TargetManager.getInstance().setTarget(item);
            }

        }
        if (event.getSource() == theComboBox) {
            Object item = theComboBox.getSelectedItem();
            setButtonEnabled(item);
        }
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == theComboBox) {
            Object item = theComboBox.getSelectedItem();
            setButtonEnabled(item);

        }
    }

    private void setButtonEnabled(Object item) {
        if (item instanceof UMLComboBoxEntry) {
            UMLComboBoxEntry entry = (UMLComboBoxEntry) item;
            if (!entry.isPhantom()) {
                Object/* MModelElement */target = entry.getElement(null);
                if (target != null) {
                    theButton.setEnabled(true);
                } else
                    theButton.setEnabled(false);
            }
        } else if (item != null) {
            theButton.setEnabled(true);
        } else {
            theButton.setEnabled(false);
        }
    }
}