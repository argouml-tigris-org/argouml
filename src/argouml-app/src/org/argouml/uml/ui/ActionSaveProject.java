/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;

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
    private static final Logger LOG =
        Logger.getLogger(ActionSaveProject.class.getName());

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

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        LOG.log(Level.INFO, "Performing save action");
        ProjectBrowser.getInstance().trySave(
                ProjectManager.getManager().getCurrentProject() != null
                        && ProjectManager.getManager().getCurrentProject()
                                .getURI() != null);
    }

    /**
     * Set the enabled state of the save action. When we become enabled inform
     * the user by highlighting the title bar with an asterisk. This method is
     * undoable.  This method is synchronized so that it can be used from any
     * thread without external synchronization.
     *
     * @param isEnabled new state for save command
     */
    @Override
    public synchronized void setEnabled(final boolean isEnabled) {
        if (isEnabled == this.enabled) {
            return;
        }
        if (LOG.isLoggable(Level.FINE)) {
            if (!enabled && isEnabled) {
                Throwable throwable = new Throwable();
                throwable.fillInStackTrace();
                LOG.log(Level.FINE, "Save action enabled by  ", throwable);
            } else {
                LOG.log(Level.FINE, "Save state changed from " + enabled + " to "
                        + isEnabled);
            }
        }
        internalSetEnabled(isEnabled);
    }

    /**
     * Set the enabled state of this action and displays the save indicator
     * @param isEnabled true to enable the action
     */
    private void internalSetEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        ProjectBrowser.getInstance().showSaveIndicator();
    }

}
