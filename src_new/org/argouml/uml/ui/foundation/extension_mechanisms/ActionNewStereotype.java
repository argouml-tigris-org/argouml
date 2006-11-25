// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
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
import java.util.Collection;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.tigris.gef.presentation.Fig;


/**
 * This action creates a new Stereotype in the current Model.
 *
 * @author mvw@tigris.org
 */
public class ActionNewStereotype extends AbstractActionNewModelElement {

    /**
     * The constructor.
     */
    public ActionNewStereotype() {
        super("button.new-stereotype");
        putValue(Action.NAME, Translator.localize("button.new-stereotype"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object t = TargetManager.getInstance().getTarget();
        if (t instanceof Fig) t = ((Fig) t).getOwner();
        Project p = ProjectManager.getManager().getCurrentProject();
        Object model = p.getModel();
        Collection models = p.getModels();
        Object newStereo = Model.getExtensionMechanismsFactory()
            .buildStereotype(
                    Model.getFacade().isAModelElement(t) ? t : null,
                    (String) null,
                    model,
                    models
            );
        if (Model.getFacade().isAModelElement(t)) {
            Object ns = Model.getFacade().getNamespace(t);
            if (Model.getFacade().isANamespace(ns)) {
                Model.getCoreHelper().setNamespace(newStereo, ns);
            }
        }
        TargetManager.getInstance().setTarget(newStereo);
        super.actionPerformed(e);
    }
}
