// $Id$
// Copyright (c) 2004-2005 The Regents of the University of California. All
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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.persistence.LastLoadInfo;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.ProjectFilePersister;
import org.argouml.persistence.VersionException;
import org.argouml.ui.ExceptionDialog;
import org.argouml.ui.ProjectBrowser;

/**
 * This class provides common functions for all FileOperations
 * like new, save, load, and reopen.
 *
 * @author mvw@tigris.org
 *
 */
public abstract class ActionFileOperations extends AbstractAction {
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
     * @param icon the icon to represent this action graphically
     */
    public ActionFileOperations(String name, Icon icon) {
        super(name, icon);
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


        if (p != null && ProjectManager.getManager().needsSave()) {
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

                if (ActionSaveProject.getInstance().isEnabled()) {
                    safe = ActionSaveProject.getInstance().trySave(true);
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
     * @param file the file to open.
     * @return true if the file was successfully opened
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     */
    public boolean loadProject(File file, boolean showUI) {
        LOG.info("Loading project");
        PersistenceManager pm = PersistenceManager.getInstance();
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

        if (!(file.canRead())) {
            reportError("File not found " + file + ".", showUI);
            Designer.enableCritiquing();
            success = false;
        } else {
            try {
                ProjectFilePersister persister =
                    pm.getPersisterFromFileName(file.getName());
                if (persister == null) {
                    success = false;
                    throw new IllegalStateException("Filename "
                            + file.getName()
                            + " is not of a known file type");
                }
                p = persister.doLoad(file);

                ProjectBrowser.getInstance().showStatus(
                    MessageFormat.format(Translator.localize(
                        "label.open-project-status-read"),
                        new Object[] {
                            file.getName(),
                        }));
            } catch (VersionException ex) {
                success = false;
                reportError(ex.getMessage(), showUI);
                p = oldProject;
            } catch (Exception ex) {
                LOG.error("Exception while loading project", ex);
                success = false;
                reportError(
                    "Could not load the project "
                    + file.getName()
                    + " some error was found.\n"
                    + "Please try loading with the latest version of ArgoUML "
                    + "which you can download from http://argouml.tigris.org\n"
                    + "If you have no further success then please report the "
                    + "exception below.",
                    showUI, ex);
                p = oldProject;
            } finally {
                if (!LastLoadInfo.getInstance().getLastLoadStatus()) {
                    p = oldProject;
                    success = false;
                    reportError(
                            "Problem in loading the project "
                            + file.getName()
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
                            + "was actually loaded.\n",
                            showUI);
                } else if (oldProject != null) {
                    // if p equals oldProject there was an exception and we do
                    // not have to gc (garbage collect) the old project
                    if (p != null && !p.equals(oldProject)) {
                        //prepare the old project for gc
                        LOG.info("There are " + oldProject.getMembers().size()
                                + " members in the old project");
                        LOG.info("There are " + p.getMembers().size()
                                + " members in the new project");
                        ProjectManager.getManager().removeProject(oldProject);
                    }
                }
                ProjectManager.getManager().setCurrentProject(p);
                if (p == null) {
                    LOG.info("The current project is null");
                } else {
                    LOG.info("There are " + p.getMembers().size()
                            + " members in the current project");
                }
                Designer.enableCritiquing();
            }
        }
        return success;
    }

    /**
     * Open a Message Dialog with an error message.
     *
     * @param message the message to display.
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     */
    private void reportError(String message, boolean showUI) {
        if (showUI) {
            JOptionPane.showMessageDialog(
                      ProjectBrowser.getInstance(),
                      message,
                      "Error",
                      JOptionPane.ERROR_MESSAGE);
        } else {
            System.err.print(message);
        }
    }

    /**
     * Open a Message Dialog with an error message.
     *
     * @param message the message to display.
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     */
    private void reportError(String message, boolean showUI, Throwable ex) {
        if (showUI) {
            JDialog dialog =
                new ExceptionDialog(
                        ProjectBrowser.getInstance(),
                        "An error occured attempting to load the project.",
                        ex);
            dialog.setVisible(true);
        } else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exception = sw.toString();
            
            message += "\n\n" + exception;
            
            reportError(message, showUI);
        }
    }
}
