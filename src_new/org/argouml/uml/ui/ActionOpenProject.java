// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.kernel.*;
import org.argouml.cognitive.Designer;
import org.argouml.ui.*;
import org.argouml.xml.argo.*;
import org.argouml.util.*;
import org.argouml.util.osdep.*;

import org.tigris.gef.base.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.text.MessageFormat;

import javax.swing.*;

/**
 * Action that loads the project.
 * This will throw away the project that we were working with up to this 
 * point so some extra caution.
 *
 * @see ActionSaveProject
 * @stereotype singleton
 */
public class ActionOpenProject extends UMLAction {

    protected static Category cat =
        Category.getInstance(org.argouml.uml.ui.ActionOpenProject.class);

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionOpenProject SINGLETON = new ActionOpenProject();

    public static final String separator = System.getProperty("file.separator");

    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionOpenProject() {
        super("action.open-project");
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /** Performs the action.
     *
     * @param e an event
     */
    public void actionPerformed(ActionEvent e) {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();

        if (p != null && p.needsSave()) {
            String t =
                MessageFormat.format(
                    Argo.localize(
                        "Actions",
                        "template.open_project.save_changes_to"),
                    new Object[] { p.getName()});

            int response =
                JOptionPane.showConfirmDialog(
                    pb,
                    t,
                    t,
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (response == JOptionPane.CANCEL_OPTION)
                return;
            if (response == JOptionPane.YES_OPTION) {
                boolean safe = false;

                if (ActionSaveProject.SINGLETON.shouldBeEnabled()) {
                    safe = ActionSaveProject.SINGLETON.trySave(true);
                }
                if (!safe) {
                    safe = ActionSaveProjectAs.SINGLETON.trySave(false);
                }
                if (!safe)
                    return;
            }
        }

        try {
            // next line does give user.home back but this is not compliant with how 
            // the project.url works and therefore open and save project as give 
            // different starting directories.
            // String directory = Globals.getLastDirectory();
            JFileChooser chooser = null;
            if (p != null && p.getURL() != null) {
                File file = new File(p.getURL().getFile());
                if (file.getParentFile() != null)
                    chooser = OsUtil.getFileChooser(file.getParent());
            } else
                chooser = OsUtil.getFileChooser();

            // JFileChooser chooser = OsUtil.getFileChooser (directory);

            if (chooser == null)
                chooser = OsUtil.getFileChooser();

            chooser.setDialogTitle(
                Argo.localize("Actions", "text.open_project.chooser_title"));
            SuffixFilter filter = FileFilters.CompressedFileFilter;
            chooser.addChoosableFileFilter(filter);
            chooser.addChoosableFileFilter(FileFilters.UncompressedFileFilter);
            chooser.addChoosableFileFilter(FileFilters.XMIFilter);
            chooser.setFileFilter(filter);

            int retval = chooser.showOpenDialog(pb);
            if (retval == 0) {
                File theFile = chooser.getSelectedFile();
                if (theFile != null) {

                    String path = theFile.getParent();
                    Globals.setLastDirectory(path);
                    URL url = theFile.toURL();
                    if (url != null) {
			loadProject(url);
                    }

                }
            }
        } catch (IOException ignore) {
            cat.error("got an IOException in ActionOpenProject", ignore);
        }
    }



    /**
     * Loads the project file and opens all kinds of error message windows
     * if it doesn't work for some reason. In those cases it preserves
     * the old project.
     * 
     * @param url the url to open.
     */
    public void loadProject(URL url) {

        Project oldProject = ProjectManager.getManager().getCurrentProject();

	// TODO:
        // This is actually a hack! Some diagram types
        // (like the state diagrams) access the current
        // diagram to get some info. This might cause 
        // problems if there's another state diagram
        // active, so I remove the current project, before
        // loading the new one.

        Designer.disableCritiquing();
        Designer.clearCritiquing();

        Project p = null;
        try {
            p = ProjectManager.getManager().loadProject(url);

            ProjectBrowser.getInstance().showStatus(
                MessageFormat.format(
                    Argo.localize(
                        "Actions",
                        "template.open_project.status_read"),
                    new Object[] { url.toString()}));
        } catch (ParserConfigurationException ex) {
            showErrorPane(
                "Could not load the project "
                    + url.toString()
                    + " due to configuration errors.\n"
                    + "Please read the instructions at www.argouml.org on the"
                    + " requirements of argouml and how to install it.");
            p = oldProject;
        } catch (IllegalFormatException ex) {
            showErrorPane(
                "Could not load the project "
                    + url.toString()
                    + "\n"
                    + "The format of the file is not supported.");
            p = oldProject;
        } catch (java.io.FileNotFoundException ex) {
            showErrorPane(
                "Could not load the project "
                    + url.toString()
                    + "\n"
                    + "The file was not found.");
            p = oldProject;
        } catch (IOException io) {
            // now we have to handle the case of a corrupted XMI file
            showErrorPane(
                "Could not load the project "
                    + url.toString()
                    + "\n"
                    + "Project file probably corrupted.\n"
                    + "Please file a bug report at argouml.tigris.org including"
                    + " the corrupted project file.");
            p = oldProject;
        } catch (SAXException ex) {
            showErrorPane(
                "Could not load the project "
                    + url.toString()
                    + "\n"
                    + "Project file probably corrupted.\n"
                    + "If the problem keeps persisting, please file a bug report at www.argouml.org.\n");
            p = oldProject;
        } finally {
            if (!ArgoParser.SINGLETON.getLastLoadStatus()) {
                p = oldProject;
                showErrorPane(
                    "Problem in loading the project "
                        + url.toString()
                        + "\n"
                        + "Project file probably corrupt from "
                        + "an earlier version or ArgoUML.\n"
                        + "Error message:\n"
                        + ArgoParser.SINGLETON.getLastLoadMessage()
                        + "\n"
                        + "Since the project was incorrectly "
                        + "saved some things might be missing "
                        + "from before you saved it.\n"
                        + "These things cannot be restored. "
                        + "You can continue working with what "
                        + "was actually loaded.\n");
            }
            ProjectManager.getManager().setCurrentProject(p);
            Designer.enableCritiquing();
        }
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

} /* end class ActionOpenProject */
