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

// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Defaults;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;

/**
 * Action to add an attribute to a Classifier or AssociationEnd.<p>
 * 
 * This class shall be the only one that knows 
 * when this tool should be downlighted or not.
 */
@UmlModelMutator
public class ActionAddAttribute extends UndoableAction {

    private static ActionAddAttribute targetFollower;

    /**
     * The constructor for this class.
     */
    public ActionAddAttribute() {
        super(Translator.localize("button.new-attribute"),
                ResourceLoaderWrapper.lookupIcon("button.new-attribute"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("button.new-attribute"));
    }

    public static ActionAddAttribute getTargetFollower() {
        if (targetFollower == null) {
            targetFollower  = new ActionAddAttribute();
            TargetManager.getInstance().addTargetListener(new TargetListener() {
                public void targetAdded(TargetEvent e) {
                    setTarget();
                }
                public void targetRemoved(TargetEvent e) {
                    setTarget();
                }

                public void targetSet(TargetEvent e) {
                    setTarget();
                }
                private void setTarget() {
                    targetFollower.setEnabled(targetFollower.shouldBeEnabled());
                }
            });
            targetFollower.setEnabled(targetFollower.shouldBeEnabled());
        }
        return targetFollower;
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {

        super.actionPerformed(ae);

        Object target = TargetManager.getInstance().getSingleModelTarget();
        Object classifier = null;

        if (Model.getFacade().isAClassifier(target)
                || Model.getFacade().isAAssociationEnd(target)) {
            classifier = target;
        } else if (Model.getFacade().isAFeature(target)) {
            classifier = Model.getFacade().getOwner(target);
        } else {
            return;
        }

        Project project = ProjectManager.getManager().getCurrentProject();
        Defaults defaults = project.getDefaults();
        Object attr = Model.getUmlFactory().buildNode(Model.getMetaTypes().getAttribute(), classifier, null, defaults);
        TargetManager.getInstance().setTarget(attr);
    }

    /**
     * @return true if this tool should be enabled
     */
    public boolean shouldBeEnabled() {
        Object target = TargetManager.getInstance().getSingleModelTarget();
        if (target == null) {
            return false;
        }
        return Model.getFacade().isAClassifier(target)
            || Model.getFacade().isAFeature(target)
            || Model.getFacade().isAAssociationEnd(target);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -111785878370086329L;
} /* end class ActionAddAttribute */
