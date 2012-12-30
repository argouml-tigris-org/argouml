/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
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

// Copyright (c) 2006-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.state.ui;



import java.awt.event.ActionEvent;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.toolbar.toolbutton.ModalAction;

/**
 *
 * @author Michiel
 */
abstract class ButtonActionNewEffect extends UndoableAction implements
        ModalAction {

    public ButtonActionNewEffect() {
        super();
        putValue(NAME, getKeyName());
        putValue(SHORT_DESCRIPTION, Translator.localize(getKeyName()));
        Object icon = ResourceLoaderWrapper.lookupIconResource(getIconName());
        putValue(SMALL_ICON, icon);
//        TargetManager.getInstance().addTargetListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (!isEnabled()) {
            return;
        }
        super.actionPerformed(e);
        Object target = TargetManager.getInstance().getModelTarget();
        Object model = Model.getFacade().getInnerContainingModel(target);
        Object ns = Model.getStateMachinesHelper()
                    .findNamespaceForEvent(target, model);
        Object event = createEvent(ns);
        Model.getStateMachinesHelper().setEventAsTrigger(target, event);
        TargetManager.getInstance().setTarget(event);
    }

    public boolean isEnabled() {
        Object target = TargetManager.getInstance().getModelTarget();
        return Model.getFacade().isATransition(target);
    }
    
    protected abstract Object createEvent(Object ns);
    
    protected abstract String getKeyName();
    protected abstract String getIconName();

}
