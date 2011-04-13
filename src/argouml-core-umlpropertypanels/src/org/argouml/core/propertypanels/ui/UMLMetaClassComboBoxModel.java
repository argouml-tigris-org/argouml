/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;

/**
 * A model for selecting a UML metaclass. Originally designed for use in
 * selecting the base class of stereotypes in UML 1.3, but now used to select
 * the type of a TagDefinition.
 * 
 * @author mkl
 */
class UMLMetaClassComboBoxModel extends UMLComboBoxModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = 5941365798992694830L;
    
    private List<String> metaClasses;

    /**
     * Construct a default combo box model.
     */
    public UMLMetaClassComboBoxModel(
            final String propertyName,
            final Object target) {
        super(target, propertyName, true);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#getSelectedModelElement()
     */
    @Override
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            return Model.getFacade().getType(getTarget());
        }
        return null;
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#buildModelList()
     */
    protected void buildModelList() {
        setElements(getMetaClassNames());
    }
    
    private List<String> getMetaClassNames() {
        
	if (metaClasses == null) {
            Collection<String> tmpMetaClasses =
                    Model.getCoreHelper().getAllMetatypeNames();
            
            if (tmpMetaClasses instanceof List) {
                metaClasses = (List<String>) tmpMetaClasses;
            } else {
                metaClasses = new LinkedList<String>(tmpMetaClasses);
            }
            tmpMetaClasses.addAll(Model.getCoreHelper().getAllMetaDatatypeNames());
            try {
                Collections.sort(metaClasses);
            } catch (UnsupportedOperationException e) {
                // We got passed an unmodifiable List.  Copy it and sort the result
                metaClasses = new LinkedList<String>(tmpMetaClasses);
                Collections.sort(metaClasses);
            }
	}
	return metaClasses;
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return getMetaClassNames().contains(element);
    }
    
    public Action getAction() {
        return new ActionSetTagDefinitionType();
    }
    
    /**
     * Action to set the type of a TagDefinition. The tagType attribute of a
     * TagDefinition is a Name of a UML metaclass (ie String).
     */
    private class ActionSetTagDefinitionType extends UndoableAction {

        /**
         * Constructor for ActionSetTagDefinitionType.
         */
        protected ActionSetTagDefinitionType() {
            super(Translator.localize("Set"), null);
            // Set the tooltip string:
            putValue(Action.SHORT_DESCRIPTION, 
                    Translator.localize("Set"));
        }

        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            Object source = e.getSource();
            String oldType = null;
            String newType = null;
            Object tagDef = null;
            UMLComboBox box = (UMLComboBox) source;
            Object t = box.getTarget();
            if (Model.getFacade().isATagDefinition(t)) {
                tagDef = t;
                oldType = (String) Model.getFacade().getType(tagDef);
            }
            newType = (String) box.getSelectedItem();
            if (newType != null && !newType.equals(oldType) && tagDef != null) {
                Model.getExtensionMechanismsHelper().setTagType(tagDef, newType);
            }
        }
    }
}
