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

import javax.swing.Action;

import org.argouml.model.Model;

/**
 * The model behind the UMLMessageActivatorComboBox.
 */
class UMLMessageActivatorComboBoxModel extends UMLComboBoxModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = 5785236557511670953L;
    
    private Object interaction = null;

    /**
     * Constructor for UMLMessageActivatorComboBoxModel.
     */
    public UMLMessageActivatorComboBoxModel(
            final String propertyName,
            final Object target) {
        super(target, propertyName, false);
        if (Model.getFacade().isAMessage(target)) {
            interaction = Model.getFacade().getInteraction(target);
            if (interaction != null) {
                Model.getPump().addModelEventListener(
                    this,
                    interaction,
                    "message");
            }
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#buildModelList()
     */
    protected void buildModelList() {
        Object target = getTarget();
        if (Model.getFacade().isAMessage(target)) {
            Object mes = target;
            removeAllElements();
            // fill the list with items
            setElements(Model.getCollaborationsHelper()
                    .getAllPossibleActivators(mes));
        }
    }


    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#isValidElement(Object)
     */
    protected boolean isValidElement(Object m) {
        return ((Model.getFacade().isAMessage(m))
                && m != getTarget()
                && !Model.getFacade().getPredecessors((getTarget())).contains(m)
                && Model.getFacade().getInteraction(m)
                    == Model.getFacade().getInteraction((getTarget())));
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            return Model.getFacade().getActivator(getTarget());
        }
        return null;
    }
    
    public Action getAction() {
        return null;
    }
}

