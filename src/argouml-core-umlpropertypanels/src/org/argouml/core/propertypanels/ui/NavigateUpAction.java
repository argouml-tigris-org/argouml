/* $Id$
 *******************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
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
class NavigateUpAction extends AbstractAction {

    final Object modelElement;
    
    public NavigateUpAction(Object modelElement) {
        super(Translator.localize("action.navigate-up"),
                ResourceLoaderWrapper.lookupIcon("action.navigate-up"));
        
        this.modelElement = modelElement;
    }
    public void actionPerformed(ActionEvent arg0) {
        TargetManager.getInstance().setTarget(
                Model.getFacade().getModelElementContainer(modelElement));
    }

}
