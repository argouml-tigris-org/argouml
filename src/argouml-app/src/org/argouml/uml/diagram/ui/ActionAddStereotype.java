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

// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.model.Model;
import org.argouml.notation.providers.uml.NotationUtilityUml;
import org.argouml.ui.UndoableAction;


/**
 * Action to add a stereotype to a model element.
 * 
 * TODO: Appears to be unused?
 * 
 * @author Bob Tarling
 * @deprecated for 0.27.2 by tfmorris.  Use 
 * {@link org.argouml.uml.ActionAddStereotype}.
 */
@Deprecated
@UmlModelMutator
class ActionAddStereotype extends UndoableAction {
    private Object modelElement;
    private Object stereotype;

    /**
     * Constructor.
     *
     * @param me The model element.
     * @param st The stereotype.
     */
    public ActionAddStereotype(Object me, Object st) {
        super(Translator.localize(buildString(st)),
                null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize(buildString(st)));
        modelElement = me;
        stereotype = st;
    }
    
    private static String buildString(Object st) {
        Project p = ProjectManager.getManager().getCurrentProject();
        ProjectSettings ps = p.getProjectSettings();
        return NotationUtilityUml.generateStereotype(st, 
                ps.getNotationSettings().isUseGuillemets());
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
    	super.actionPerformed(ae);
        if (Model.getFacade().getStereotypes(modelElement)
                .contains(stereotype)) {
            Model.getCoreHelper().removeStereotype(modelElement, stereotype);
        } else {
            Model.getCoreHelper().addStereotype(modelElement, stereotype);
        }
        ProjectManager.getManager().updateRoots();
    }

    /*
     * @see javax.swing.Action#getValue(java.lang.String)
     */
    @Override
    public Object getValue(String key) {
        if ("SELECTED".equals(key)) {
            if (Model.getFacade().getStereotypes(modelElement).contains(
                    stereotype)) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
        return super.getValue(key);
    }
}
