// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;

/**
 * Action to trigger creation of a new project.
 */
public class ActionNew extends AbstractAction {

    /**
     * The constructor.
     */
    public ActionNew() {
        // Set the name and icon:
        super(Translator.localize("action.new"),
                ResourceLoaderWrapper.lookupIcon("action.new"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION,
                Translator.localize("action.new"));
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Model.getPump().flushModelEvents();
        Model.getPump().stopPumpingEvents();
        Model.getPump().flushModelEvents();
        Project p = ProjectManager.getManager().getCurrentProject();

        if (getValue("non-interactive") == null) {
            if (!ProjectBrowser.getInstance().askConfirmationAndSave()) {
                return;
            }
        }

        ProjectBrowser.getInstance().clearDialogs();
        Designer.disableCritiquing();
        Designer.clearCritiquing();
        // clean the history
        TargetManager.getInstance().cleanHistory();
        p.remove();
        p = ProjectManager.getManager().makeEmptyProject();
        TargetManager.getInstance().setTarget(p.getDiagrams().toArray()[0]);
        Designer.enableCritiquing();
        Model.getPump().startPumpingEvents();
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -3943153836514178100L;
} /* end class ActionNew */
