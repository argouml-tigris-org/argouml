/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;

/**
 * This action changes the target to the owning model element
 * of the given element.
 *
 * @author Bob Tarling
 */
abstract class NavigateSiblingAction extends AbstractAction {

    final Object modelElement;
    
    public NavigateSiblingAction(Object modelElement, String name) {
        super(Translator.localize(name),
                ResourceLoaderWrapper.lookupIcon(name));
        
        this.modelElement = modelElement;
        setEnabled(getTargetSibling() != null);
    }
    
    public void actionPerformed(ActionEvent arg0) {
        TargetManager.getInstance().setTarget(getTargetSibling());
    }

    abstract protected Object getTargetSibling();
    
    protected List getAllSiblings() {
        List list = null;
        final Object owner =
            Model.getFacade().getModelElementContainer(modelElement);
        if (Model.getFacade().isAAttribute(modelElement)) {
            if (Model.getFacade().isAAssociationEnd(Model.getFacade().getOwner(modelElement))) {
                list = Model.getFacade().getQualifiers(owner);
            } else {
                list = Model.getFacade().getAttributes(owner);
            }
        } else if (Model.getFacade().isAOperation(modelElement)
                || Model.getFacade().isAReception(modelElement)) {
            list = Model.getFacade().getOperationsAndReceptions(owner);
        } else if (Model.getFacade().isAParameter(modelElement)) {
            list = Model.getFacade().getParametersList(owner);
        } else if (Model.getFacade().isAAssociationEnd(modelElement)) {
            list = new ArrayList(
            		Model.getFacade().getConnections(owner));
        }
        return list;
    }
}
