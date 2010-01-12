/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JComboBox;

import org.argouml.uml.ui.UMLListCellRenderer2;


/**
 * ComboBox for UML modelelements. <p>
 */
class UMLComboBox extends JComboBox {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -7556056847962715552L;
    
    /**
     * This action will be called whenever the user changes the selected item
     * in the combo.
     */
    private Action action;
    
    /**
     * Constructor for UMLComboBox. Via the given action, the
     * action for this combobox is done.
     * @param model the ComboBoxModel
     * @param showIcon true if an icon should be shown in front of the items
     */
    public UMLComboBox(UMLComboBoxModel model, boolean showIcon) {
        super(model);
        this.action = model.getAction();
        if (this.action != null) {
            addActionListener(this.action);
        }
        setRenderer(new UMLListCellRenderer2(showIcon));
        addPopupMenuListener(model);
    }

    /**
     * The constructor.
     *
     * @param model the ComboBoxModel
     */
    public UMLComboBox(UMLComboBoxModel model) {
        this(model, true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        int i = getSelectedIndex();
        if (i >= 0) {
            doIt(arg0);
        }
    }

    /**
     * The 'body' of the actionPerformed method. Is only called if there is
     * actually a selection made.
     *
     * @param event the event
     */
    protected void doIt(ActionEvent event) { }

    /**
     * Utility method to get the current target.
     *
     * @return Object
     */
    public Object getTarget() {
        return ((UMLComboBoxModel) getModel()).getTarget();
    }
    
    @Override
    public void removeNotify() {
        ((UMLComboBoxModel) getModel()).removeModelEventListener();
        if (action != null) {
            removeActionListener(action);
        }
        removePopupMenuListener((UMLComboBoxModel) getModel());
    }
}
