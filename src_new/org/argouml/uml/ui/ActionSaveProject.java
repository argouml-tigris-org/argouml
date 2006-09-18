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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.ProjectLoadSave;

/**
 * Action that saves the project.
 *
 * @see ActionOpenProject
 */
public class ActionSaveProject extends AbstractAction {
	
    private static final long serialVersionUID = -5579548202585774293L;
	/**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ActionSaveProject.class);

    /**
     * The constructor.
     */
    public ActionSaveProject() {
        super(Translator.localize("action.save-project"),
                ResourceLoaderWrapper.lookupIcon("action.save-project"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.save-project"));
        super.setEnabled(false);
    }

    /**
     * The constructor.
     * @param name the name of the action.
     * @param icon the icon to represent this action graphically.
     */
    protected ActionSaveProject(String name, Icon icon) {
        super(name, icon);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        LOG.info("Performing save action");
        ProjectLoadSave.trySave(
            ProjectManager.getManager().getCurrentProject() != null
                        && ProjectManager.getManager().getCurrentProject()
                                .getURI() != null);
        }

    /**
     * Set the enabled state of the save action.
     * When we become enabled inform the user by highlighting the title bar
     * with an asterisk.
     * @param enabled new state for save command
     */
    public void setEnabled(boolean enabled) {
        if (enabled == this.enabled) {
            return;
        }
        super.setEnabled(enabled);
        // TODO: Have the title bar register for enabled events?
        ProjectBrowser.getInstance().showSaveIndicator();
    }

} /* end class ActionSaveProject */
