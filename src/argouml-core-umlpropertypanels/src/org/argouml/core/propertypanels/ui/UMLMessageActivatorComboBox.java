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

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLListCellRenderer2;
/**
 * The combobox for activators on the message proppanel. The only reason this
 * combobox implements melementlistener is to conform to UMLChangeDispatch. The
 * combobox serves as a proxy for the
 * model (UMLMessageActivatorComboBoxModel). Kind of strange...
 */
class UMLMessageActivatorComboBox extends UMLComboBox {

    /**
     * The class uid
     */
    private static final long serialVersionUID = 9039049225641202332L;

    /**
     * Constructor for UMLMessageActivatorComboBox.
     * @param container the UI container
     * @param model the model
     */
    public UMLMessageActivatorComboBox(
        final UMLComboBoxModel model, Action action) {
        super(model, false);
        setRenderer(new UMLListCellRenderer2(true));
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBox#doIt(ActionEvent)
     */
    protected void doIt(ActionEvent event) {
        Object o = getModel().getElementAt(getSelectedIndex());
        Object activator = o;
        Object mes = getTarget();
        if (activator != Model.getFacade().getActivator(mes)) {
            Model.getCollaborationsHelper().setActivator(mes, activator);
        }
    }

}
