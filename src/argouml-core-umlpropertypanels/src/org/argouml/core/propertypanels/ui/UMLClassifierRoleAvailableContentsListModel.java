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

// Copyright (c) 2002-2006 The Regents of the University of California. All
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

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;

/**
 * List model which implements allAvailableContents operation for a
 * ClassifierRole as described in the well formedness rules.
 *
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLClassifierRoleAvailableContentsListModel
    extends UMLModelElementListModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = 7868841277838127325L;



    /**
     * Constructor for UMLClassifierRoleAvailableContentsListModel.
     */
    public UMLClassifierRoleAvailableContentsListModel(final Object modelElement) {
        super();
        setTarget(modelElement);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        setAllElements(
            Model.getCollaborationsHelper().allAvailableContents(getTarget()));
    }

    /*
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {
        if (e instanceof AddAssociationEvent) {
            if (e.getPropertyName().equals("base")
                    && e.getSource() == getTarget()) {
                Object clazz = getChangedElement(e);
                addAll(Model.getFacade().getOwnedElements(clazz));
                Model.getPump().addModelEventListener(
                                      this,
                                      clazz,
                                      "ownedElement");
            } else if (
                    e.getPropertyName().equals("ownedElement")
                    && Model.getFacade().getBases(getTarget()).contains(
                            e.getSource())) {
                addElement(getChangedElement(e));
            }
        } else if (e instanceof RemoveAssociationEvent) {
            if (e.getPropertyName().equals("base")
                    && e.getSource() == getTarget()) {
                Object clazz = getChangedElement(e);
                Model.getPump().removeModelEventListener(
                        this,
                        clazz,
                    "ownedElement");
            } else if (
                e.getPropertyName().equals("ownedElement")
                && Model.getFacade().getBases(getTarget()).contains(
                       e.getSource())) {
                removeElement(getChangedElement(e));
            }
        } else {
            super.propertyChange(e);
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#setTarget(java.lang.Object)
     */
    protected void setTarget(Object theNewTarget) {
        assert (getTarget() == null);
        assert (Model.getFacade().isAElement(theNewTarget));
        
        setListTarget(theNewTarget);
        
        Collection bases = Model.getFacade().getBases(getTarget());
        Iterator it = bases.iterator();
        while (it.hasNext()) {
            Object base =  it.next();
            Model.getPump().addModelEventListener(
                this,
                base,
                "ownedElement");
        }
        
        // make sure we know it when a classifier is added as a base
        Model.getPump().addModelEventListener(
            this,
            getTarget(),
            "base");
        removeAllElements();
        setBuildingModel(true);
        buildModelList();
        setBuildingModel(false);
        
        if (getSize() > 0) {
            fireIntervalAdded(this, 0, getSize() - 1);
        }
    }
    
    public void removeModelEventListener() {
        Collection bases = Model.getFacade().getBases(getTarget());
        Iterator it = bases.iterator();
        while (it.hasNext()) {
            Object base =  it.next();
            Model.getPump().removeModelEventListener(
                this,
                base,
                "ownedElement");
        }
        
        // make sure we know it when a classifier is added as a base
        Model.getPump().removeModelEventListener(
            this,
            getTarget(),
            "base");
    }

    

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return false;
    }
}
