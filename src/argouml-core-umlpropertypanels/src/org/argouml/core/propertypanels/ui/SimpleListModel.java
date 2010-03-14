/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling - Original implementation
 *******************************************************************************
 */

package org.argouml.core.propertypanels.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.argouml.core.propertypanels.model.GetterSetterManager;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.util.CollectionUtil;

/**
 * The simplest model for a list of UML elements
 */
class SimpleListModel
        extends DefaultListModel implements PropertyChangeListener {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -8491023641828449064L;
    
    private static final Logger LOG = Logger.getLogger(SimpleListModel.class);
    
    /**
     * A string indicator of the type of model element this control is to hold
     */
    private final String type;
    
    private final Object umlElement;
    private final String propertyName;

    final private GetterSetterManager getterSetterManager;
    
    SimpleListModel(
            final String propertyName,
            final String type,
            final Object umlElement,
            final GetterSetterManager getterSetterManager) {
        super();
        this.getterSetterManager = getterSetterManager;
        this.type = type;
        this.propertyName = propertyName;
        this.umlElement = umlElement;

        build();
        
        Model.getPump().addModelEventListener(this, umlElement, propertyName);
    }
    
    public void removeModelEventListener() {
        Model.getPump().removeModelEventListener(this, umlElement, propertyName);
    }
    
    public Object getMetaType() {
	return getterSetterManager.getMetaType(propertyName);
    }
    
    /*
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent e) {
        Runnable doWorkRunnable = new Runnable() {
            public void run() {
                try {
                    if (e instanceof RemoveAssociationEvent) {
                        removeElement(
                                ((RemoveAssociationEvent) e).getChangedValue());
                    } else if (e instanceof AddAssociationEvent) {
                        Object newElement = ((AddAssociationEvent) e).getChangedValue();
                        
                        if (Model.getUmlHelper().isMovable(getMetaType())) {
                            final Collection c =
                                (Collection) getterSetterManager.getOptions( 
                                    umlElement, 
                                    propertyName, 
                                    type);
                            final int index =
                                CollectionUtil.indexOf(c, newElement);
                            if (index < 0 || index > getSize() - 1) {
                                LOG.warn(
                                        "Unable to add element at correct position "
                                        + index + " added to end instead");
                                addElement(newElement);
                            } else {
                                add(index, newElement);
                            }
                        } else {
                            addElement(newElement);
                        }
                    }
                } catch (InvalidElementException e) {
                    LOG.debug("propertyChange accessed a deleted element ", e);
                }
            }  
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    /**
     * Delete and rebuild the model list from scratch.
     */
    private void build() {
        try {
            final Collection c = (Collection) getterSetterManager.getOptions( 
                    umlElement, 
                    propertyName, 
                    type);
            for (Object o : c) {
                addElement(o);
            }
        } catch (InvalidElementException exception) {
            LOG.debug("buildModelList threw exception for target " 
                    + umlElement + ": "
                    + exception);
        }
    }

    public Object getUmlElement() {
        return umlElement;
    }
    
}
