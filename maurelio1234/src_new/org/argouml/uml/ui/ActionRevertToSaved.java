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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ArgoFrame;
import org.argouml.ui.ProjectBrowser;

/**
 * Action that reverts to the previously saved version of the project.
 *
 * @see ActionOpenProject
 */
public class ActionRevertToSaved extends AbstractAction {

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor.
     */
    public ActionRevertToSaved() {
        super(Translator.localize("action.revert-to-saved"));
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * Performs the action.
     *
     * @param e an event
     */
    public void actionPerformed(ActionEvent e) {
        Project p = ProjectManager.getManager().getCurrentProject();

        if (p == null 
                || !ProjectBrowser.getInstance().getSaveAction().isEnabled()) {
            return;
        }

        String message =
            MessageFormat.format(
                    Translator.localize(
                       "optionpane.revert-to-saved-confirm"),
		    new Object[] {
			p.getName(),
		    });

        int response =
            JOptionPane.showConfirmDialog(
                  ArgoFrame.getInstance(),
                  message,
                  Translator.localize(
                      "optionpane.revert-to-saved-confirm-title"),
                  JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            ProjectBrowser.getInstance().loadProjectWithProgressMonitor(
                    new File(p.getURI()), true);
        }
    }
}
