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
 *    Thomas Neustupny
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2004-2009 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;


/**
 * This action creates a new TagDefinition in the current Model or Stereotype or
 * Package.
 *
 * @author rastaman@tigris.org
 */
@UmlModelMutator
public class ActionNewTagDefinition extends UndoableAction {

    /**
     * The constructor.
     */
    public ActionNewTagDefinition() {
        super(Translator.localize("button.new-tagdefinition"),
                ResourceLoaderWrapper.lookupIcon("button.new-tagdefinition"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("button.new-tagdefinition"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(
     *      java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object t = TargetManager.getInstance().getModelTarget();
        Object owner = null;
        Object namespace = null;
        if (Model.getFacade().isAStereotype(t)) {
            owner = t;
        } else if (Model.getFacade().isAPackage(t)) {
            namespace = t;
        } else {
            namespace = Model.getFacade().getInnerContainingModel(t);
        }
        Object newTagDefinition = null;
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            newTagDefinition = Model.getExtensionMechanismsFactory()
                .buildTagDefinition((String) null, owner, namespace);
        } else {
            Object type = null;
            for (Object aType : Model.getExtensionMechanismsHelper()
                    .getCommonTaggedValueTypes()) {
                if ("String".equals(Model.getFacade().getName(aType))) {
                    type = aType;
                    break;
                }
            }
            newTagDefinition = Model.getCoreFactory().buildAttribute2(t, type);
        }
        TargetManager.getInstance().setTarget(newTagDefinition);
        super.actionPerformed(e);
    }

}
