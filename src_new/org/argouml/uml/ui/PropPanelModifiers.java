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

// File: PropPanelModifiers.java
// Classes: PropPanelModifiers
// Original Author: mail@jeremybennett.com
// $Id$

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Created to help with
// writing PropPanels.


package org.argouml.uml.ui;

import javax.swing.*;
import java.awt.*;
import org.argouml.uml.ui.*;


/**
 * <p>A class to simplify the creation of modifier check box arrays in
 *   PropPanels.</p>
 *
 * <p>A modifier panel is restricted to a specific number of columns. It only
 *   adds {@link UMLCheckBox} entities.
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.foundation.core.PropPanelClassifier#_modifiersPanel},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class PropPanelModifiers extends JPanel {
    
    /**
     * <p>Creates a {@link JPanel} based on a {@link GridLayout} with a
     *   specified number of columns and arbitrary number of rows.<p>
     *
     * @param numCols  The number of colums in the {@link GridLayout}
     */
    public PropPanelModifiers(int numCols) {
        // Just invoke the parent constructor with the GridLayout.
        super(new GridLayout(0, numCols));
    }

    /**
     * <p>Overloading of the add method, that adds a UMLCheckBox with specified
     *   parameters and {@link UMLReflectionBooleanProperty}.</p>
     *
     * @param propertyName  Name of the reflection property associated with
     *                      this modifier.
     *
     * @param elementClass  NSUML class that the modifier is describing
     *
     * @param getMethod     Name of the get method for the reflection property
     *
     * @param setMethod     Name of the set method for the reflection property
     *
     * @param label         Label to go alongside the checkbox
     *
     * @param container     PropPanel that contains this PropPanelModifiers
     *                      panel.
     */
    public void add(String propertyName, Class elementClass, String getMethod,
                    String setMethod, String label, PropPanel container) {
        // Build up the check box
        UMLReflectionBooleanProperty rbp;
        UMLCheckBox                  cb;

        rbp = new UMLReflectionBooleanProperty(propertyName, elementClass,
                                               getMethod, setMethod);
        cb  = new UMLCheckBox(container.localize(label), container, rbp);

        // Now invoke the parent add method
        add(cb);
    }
}  /* end class PropPanelModifiers */

