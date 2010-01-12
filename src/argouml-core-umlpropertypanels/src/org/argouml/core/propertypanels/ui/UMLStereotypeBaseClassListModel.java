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

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionRemoveElement;

/**
 * The list model for the BaseClasses of the stereotype.
 *
 * @author Michiel
 */
class UMLStereotypeBaseClassListModel extends UMLModelElementListModel {

    /**
     * Construct the model, listen to changes of "baseClass".
     */
    public UMLStereotypeBaseClassListModel(Object modelElement) {
        super("baseClass",
                modelElement.getClass(),
                new ActionAddStereotypeBaseClass(),
                new ActionDeleteStereotypeBaseClass());
        setTarget(modelElement);
    }

    @Override
    protected void buildModelList() {
        removeAllElements();
        if (Model.getFacade().isAStereotype(getTarget())) {
            // keep them sorted
            LinkedList<String> lst = new LinkedList<String>(
                    Model.getFacade().getBaseClasses(getTarget()));
            Collections.sort(lst);
            addAll(lst);
        }
    }

    @Override
    protected boolean isValidElement(Object element) {
        if (Model.getFacade().isAStereotype(element)) {
            return true;
        }
        return false;
    }
    
    /**
     * The Action to add a baseclass to the stereotype.
     *
     * @author Michiel
     */
    private static class ActionAddStereotypeBaseClass extends AbstractActionAddModelElement2 {

        private List<String> metaClasses;
        
        public ActionAddStereotypeBaseClass() {
            super();
            initMetaClasses();
        }
        
        /**
         * Initialize the meta-classes list. <p>
         * 
         * All this code is necessary to be independent of 
         * model repository implementation, 
         * i.e. to ensure that we have a 
         * sorted list of strings.
         */
        void initMetaClasses() {
            Collection<String> tmpMetaClasses = 
                Model.getCoreHelper().getAllMetatypeNames();
            if (tmpMetaClasses instanceof List) {
                metaClasses = (List<String>) tmpMetaClasses;
            } else {
                metaClasses = new LinkedList<String>(tmpMetaClasses);
            }
            try {
                Collections.sort(metaClasses);
            } catch (UnsupportedOperationException e) {
                // We got passed an unmodifiable List.  Copy it and sort the result
                metaClasses = new LinkedList<String>(tmpMetaClasses);
                Collections.sort(metaClasses);
            }
        }
        
        @Override
        protected List<String> getChoices() {
            return Collections.unmodifiableList(metaClasses);
        }

        @Override
        protected String getDialogTitle() {
            return Translator.localize("dialog.title.add-baseclasses");
        }

        @Override
        protected List<String> getSelected() {
            List<String> result = new ArrayList<String>();
            if (Model.getFacade().isAStereotype(getTarget())) {
                Collection<String> bases = 
                    Model.getFacade().getBaseClasses(getTarget());
                result.addAll(bases);
            }
            return result;
        }

        @Override
        protected void doIt(Collection selected) {
            Object stereo = getTarget();
            Set<Object> oldSet = new HashSet<Object>(getSelected());
            Set toBeRemoved = new HashSet<Object>(oldSet);

            for (Object o : selected) {
                if (oldSet.contains(o)) {
                    toBeRemoved.remove(o);
                } else {
                    Model.getExtensionMechanismsHelper()
                            .addBaseClass(stereo, o);
                }
            }
            for (Object o : toBeRemoved) {
                Model.getExtensionMechanismsHelper().removeBaseClass(stereo, o);
            }
        }
        
    }
    
    /**
     * The Action to remove a baseclass from a stereotype.
     *
     * @author Michiel
     */
    private static class ActionDeleteStereotypeBaseClass extends AbstractActionRemoveElement {

        public ActionDeleteStereotypeBaseClass() {
            super(Translator.localize("menu.popup.remove"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object baseclass = getObjectToRemove();
            if (baseclass != null) {
                Object st = getTarget();
                if (Model.getFacade().isAStereotype(st)) {
                    Model.getExtensionMechanismsHelper().removeBaseClass(st,
                            baseclass);
                }
            }
        }
    }
}
