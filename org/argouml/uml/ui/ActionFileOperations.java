// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.kernel.LastLoadInfo;
import org.argouml.kernel.OpenException;
import org.argouml.kernel.PersisterManager;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectFilePersister;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;

/**
 * This class provides common functions for all FileOperations 
 * like new, save, load, and reopen.
 * 
 * @author MVW
 *
 */
public abstract class ActionFileOperations extends UMLAction {
    private static final Logger LOG =
        Logger.getLogger(ActionFileOperations.class);

    /**
     * The constructor.
     * 
     * @param name the name of the action
     */
    public ActionFileOperations(String name) {
        super(name);
    }

    /**
     * The constructor.
     * 
     * @param name the name of the action
     * @param hasIcon true if an icon should be shown
     */
    public ActionFileOperations(String name, boolean hasIcon) {
        super(name, hasIcon);
    }

    /**
     * The constructor.
     * 
     * @param name the name of the action
     * @param global if this is a global action, then it has to be added 
     *               to the list of such actions in the class Actions
     * @param hasIcon true if an icon should be shown
     */
    public ActionFileOperations(String name, boolean global, boolean hasIcon) {
        super(name, global, hasIcon);
    }

    /**
     * If the current project is dirty (needs saving) then this function will
     * ask confirmation from the user. 
     * If the user indicates that saving is needed, then saving is attempted.
     *  
     * @return true if we can continue with opening
     */
    protected boolean askConfirmationAndSave() {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();


        if (p != null && p.needsSave()) {
            String t = MessageFormat.format(Translator.localize(
                        "optionpane.open-project-save-changes-to"),
                        new Object[] {p.getName()});

            int response = JOptionPane.showConfirmDialog(pb, t, t, 
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (response == JOptionPane.CANCEL_OPTION 
                    || response == JOptionPane.CLOSED_OPTION) {
                return false;
            }
            if (response == JOptionPane.YES_OPTION) {
                boolean safe = false;

                if (ActionSaveProject.SINGLETON.shouldBeEnabled()) {
                    safe = ActionSaveProject.SINGLETON.trySave(true);
                }
                if (!safe) {
                    safe = ActionSaveProjectAs.SINGLETON.trySave(false);
                }
                if (!safe) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Loads the project file and opens all kinds of error message windows
     * if it doesn't work for some reason. In those cases it preserves
     * the old project.
     * 
     * @param url the url to open.
     * @return true if the file was successfully opened
     */
    public boolean loadProject(URL url) {
        PersisterManager pm = new PersisterManager();
        Project oldProject = ProjectManager.getManager().getCurrentProject();
        boolean success = true;
        
        // TODO:
        // This is actually a hack! Some diagram types
        // (like the statechart diagrams) access the current
        // diagram to get some info. This might cause 
        // problems if there's another statechart diagram
        // active, so I remove the current project, before
        // loading the new one.

        Designer.disableCritiquing();
        Designer.clearCritiquing();
        Project p = null;

        if (!(new File(url.getFile()).canRead())) {
            showErrorPane("File not found " + url.toString() + ".");
            Designer.enableCritiquing();
            success = false;
        } else {
            try {
                ProjectFilePersister persister = 
                    pm.getPersisterFromFileName(url.getFile());
                if (persister == null) {
                    success = false;
                    throw new IllegalStateException("Filename " + url.getFile() 
                            + " is not of a known file type");
                }
                p = persister.loadProject(url);
                
                ProjectBrowser.getInstance().showStatus(
                        MessageFormat.format(Translator.localize(
                        "label.open-project-status-read"),
                        new Object[] {
                                url.toString()
                        }));
            } catch (OpenException ex) {
                LOG.error("Exception while loading project", ex);
                success = false;
                showErrorPane(
                        "Could not load the project "
                        + url.toString()
                        + " due to parser configuration errors.\n"
                        + "Please read the instructions at www.argouml.org "
                        + "on the "
                        + "requirements of argouml and how to install it.");
                p = oldProject;
            } finally {
                if (!LastLoadInfo.getInstance().getLastLoadStatus()) {
                    p = oldProject;
                    success = false;
                    showErrorPane(
                            "Problem in loading the project "
                            + url.toString()
                            + "\n"
                            + "Project file probably corrupt from "
                            + "an earlier version or ArgoUML.\n"
                            + "Error message:\n"
                            + LastLoadInfo.getInstance().getLastLoadMessage()
                            + "\n"
                            + "Since the project was incorrectly "
                            + "saved some things might be missing "
                            + "from before you saved it.\n"
                            + "These things cannot be restored. "
                            + "You can continue working with what "
                            + "was actually loaded.\n");
                }
                else if (oldProject != null) {
                    // if p equals oldProject there was an exception and we do
                    // not have to gc (garbage collect) the old project
                    if (p != null && !p.equals(oldProject)) {
                        //prepare the old project for gc
                        ProjectManager.getManager().removeProject(oldProject);
                    }
                }
                ProjectManager.getManager().setCurrentProject(p);
                Designer.enableCritiquing();
            }
        }
        return success;
    }

    /**
     * Open a Message Dialog with an error message.
     *
     * @param message the message to display.
     */
    private void showErrorPane(String message) {
        JOptionPane.showMessageDialog(
                      ProjectBrowser.getInstance(),
                      message,
                      "Error",
                      JOptionPane.ERROR_MESSAGE);
    }


}
