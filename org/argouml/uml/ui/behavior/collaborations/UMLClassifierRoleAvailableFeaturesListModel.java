// $Id$
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

package org.argouml.uml.ui.behavior.collaborations;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.tigris.gef.presentation.Fig;

/**
 * List model which implements allAvailableFeatures operation for a
 * ClassifierRole as described in the well formedness rules.
 * 
 * @since Oct 4, 2002
 * @author jaap.branderhorst@xs4all.nl
 * 
 */
public class UMLClassifierRoleAvailableFeaturesListModel
    extends UMLModelElementListModel2 {

    /**
     * Constructor for UMLClassifierRoleAvailableFeaturesListModel.
     */
    public UMLClassifierRoleAvailableFeaturesListModel() {
        super();
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        setAllElements(Model.getCollaborationsHelper()
                .allAvailableFeatures(getTarget()));
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {
        if (e instanceof AddAssociationEvent) {
            if (e.getPropertyName().equals("base")
                    && e.getSource() == getTarget()) {
                Object clazz = /*(MClassifier)*/ getChangedElement(e);
                addAll(Model.getFacade().getFeatures(clazz));
                Model.getPump().addModelEventListener(
                                      this,
                                      clazz,
                                      "feature");
            } else if (
                e.getPropertyName().equals("feature")
                && Model.getFacade().getBases(getTarget()).contains(
                    e.getSource())) {
                addElement(getChangedElement(e));
            }
        } else if (e instanceof RemoveAssociationEvent) {
            if (e.getPropertyName().equals("base")
                    && e.getSource() == getTarget()) {
                Object clazz = /*(MClassifier)*/ getChangedElement(e);
                Model.getPump().removeModelEventListener(
                                     this,
                                     clazz,
                                     "feature");
            } else if (
                e.getPropertyName().equals("feature")
                && Model.getFacade().getBases(getTarget()).contains(
                       e.getSource())) {
                removeElement(getChangedElement(e));
            }
        } else {
            super.propertyChange(e);
        }
    }


    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#setTarget(java.lang.Object)
     */
    public void setTarget(Object target) {
        if (getTarget() != null) {
            Enumeration enumeration = elements();
            while (enumeration.hasMoreElements()) {
                Object base = enumeration.nextElement();
                Model.getPump().removeModelEventListener(
                    this,
                    base,
                    "feature");
            }
            Model.getPump().removeModelEventListener(
                this,
                getTarget(),
                "base");
        }
        
        target = target instanceof Fig ? ((Fig) target).getOwner() : target;
        if (!Model.getFacade().isAModelElement(target))
            // TODO - isn't this an error condition? Should we not throw
            // an exception or at least log.
            return;
        
        setListTarget(target);
        if (getTarget() != null) {
            Collection bases = Model.getFacade().getBases(getTarget());
            Iterator it = bases.iterator();
            while (it.hasNext()) {
                Object base = it.next();
                Model.getPump().addModelEventListener(
                    this,
                    base,
                    "feature");
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
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object/*MBase*/ element) {
        return false;
    }
}
