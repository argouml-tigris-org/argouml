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
class NavigatePreviousAction extends AbstractAction {

    final Object modelElement;
    
    public NavigatePreviousAction(Object modelElement) {
        super(Translator.localize("action.navigate-forward"),
                ResourceLoaderWrapper.lookupIcon("action.navigate-back"));
        
        this.modelElement = modelElement;
    }
    public void actionPerformed(ActionEvent arg0) {
        final Object owner =
            Model.getFacade().getModelElementContainer(modelElement);
        Object newTarget = null;
        List list = null;
        if (Model.getFacade().isAAttribute(modelElement)) {
            list = Model.getFacade().getAttributes(owner);
        }
        if (Model.getFacade().isAOperation(modelElement)
                || Model.getFacade().isAReception(modelElement)) {
            list = Model.getFacade().getOperationsAndReceptions(owner);
        }
        if (Model.getFacade().isAParameter(modelElement)) {
            list = Model.getFacade().getParametersList(owner);
        }
        if (list != null) {
            final int posn = list.indexOf(modelElement);
            if (posn > 0) {
                newTarget = list.get(posn - 1);
                TargetManager.getInstance().setTarget(newTarget);
            }
        }
    }

}
