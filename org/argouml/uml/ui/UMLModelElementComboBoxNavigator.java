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


// File: UMLModelElementComboBoxNavigator.java
// Classes: UMLModelElementComboBoxNavigator
// Original Author: mail@jeremybennett.com
// $Id$

// 26 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Created to support
// UMLModelElementComboBox.


package org.argouml.uml.ui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import org.tigris.gef.util.*;

import ru.novosoft.uml.foundation.core.*;


/**
 * <p>Implements a navigation button to the right of a  {@link
 *   UMLModelElementComboBox}.</p>
 *
 * @author Jeremy Bennett (mail@jeremybennett.com), 26 Apr 2002.
 */

public class UMLModelElementComboBoxNavigator
    extends    JPanel
    implements ActionListener {


    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>The icon we use for the navigator.</p>
     */

    private static ImageIcon _icon =
        ResourceLoader.lookupIconResource("ComboNav");


    /**
     * <p>The container where we are found.</p>
     */

    private UMLUserInterfaceContainer _container;

    /**
     * <p>The adjacent combo box.</p>
     */

    private UMLModelElementComboBox _box;


    ///////////////////////////////////////////////////////////////////////////
    //
    // Constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
    * <p>Build a new navigator.</p>
    *
    * @param container  Container, typically a {@link PropPanel}
    *
    * @param tooltip    Tooltip key for button
    *
    * @param box        Associated combo box.
    */

    public UMLModelElementComboBoxNavigator(
        UMLUserInterfaceContainer container, String tooltip,
        UMLModelElementComboBox box) {

        // Use the super constructor for the basic construction

        super(new BorderLayout());

        // Save the arguments in instance variables

        _container = container;
        _box = box;

        // Set up a button for the navigator and add a tool tip and layout.

        JButton button = new JButton(_icon);

        button.setPreferredSize(new Dimension(_icon.getIconWidth() + 6,
                                              _icon.getIconHeight() + 6));
        button.setToolTipText(container.localize(tooltip));
        button.addActionListener(this);

        add(box,BorderLayout.CENTER);
        add(button,BorderLayout.EAST);
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Event handlers
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Fired when the button is pushed.  Navigates to the currently
     *   selected item in the combo box.</p>
     *
     * @param event  The event that caused us to be fired.
     */

    public void actionPerformed(ActionEvent event) {

        // Get the selected item, and navigate there if we can

        Object item = _box.getSelectedItem();

        if(item instanceof UMLModelElementComboBoxEntry) {
            UMLModelElementComboBoxEntry entry =
                (UMLModelElementComboBoxEntry) item;

            MModelElement target = entry.getModelElement();

            if ((target != null) && (_container != null)) {
                _container.navigateTo(target);
            }
        }
    }

}  /* End of UMLModelElementComboBoxNavigator */
