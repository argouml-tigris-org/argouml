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

// $Id$
package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.cognitive.Designer;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;
import org.argouml.xml.argo.ArgoParser;

/**
 * @author jaap.branderhorst@xs4all.nl
 * @since Apr 4, 2003
 */
public class ActionLoadProject extends UMLChangeAction {
    private Category logger = Category.getInstance(this.getClass());

    /**
     * The key for the name-value pair where the callee has to put the url he
     * wants to load in.
     */
    public final static String URL_KEY = "url";

    public static ActionLoadProject SINGLETON = new ActionLoadProject();

    /**
     * Constructor for ActionLoadProject.
     * @param s
     */
    protected ActionLoadProject() {
        super("LoadProject");
    }

    /**
     * Loads the project file at the url in the URL_KEY value.
     * @see java.awt.event.ActionListener#actionPerformed(null)
     * @throws IllegalStateException if URL_KEY is not filled.
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        URL url = (URL)getValue(URL_KEY);
        if (url == null) {
            logger.error(
                "There is no url put as value in the AcionLoadProject valuemap. Not possible to load project therefore.");
            throw new IllegalStateException("There is no url put as value in the AcionLoadProject valuemap. Not possible to load project therefore.");
        }
        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging 
        // of argouml if a project is corrupted. Issue 913
        // made it possible to return to the old project if loading went wrong
        Project oldProject = ProjectManager.getManager().getCurrentProject();
        // This is actually a hack! Some diagram types
        // (like the state diagrams) access the current
        // diagram to get some info. This might cause 
        // problems if there's another state diagram
        // active, so I remove the current project, before
        // loading the new one.
        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging 
        // of argouml if a project is corrupted. Issue 913
        // old code:

        // pb.setProject(Project.makeEmptyProject());

        // new code:
        Designer.disableCritiquing();
        Designer.clearCritiquing();

        // 2002-07-18
        // Jaap Branderhorst
        // changed the loading of the projectfiles to solve hanging 
        // of argouml if a project is corrupted. Issue 913
        // try catch block added
        Project p = null;
        try {
            p = Project.loadProject(url);

            ProjectBrowser.TheInstance.showStatus(
                MessageFormat.format(
                    Argo.localize(
                        "Actions",
                        "template.open_project.status_read"),
                    new Object[] { url.toString()}));
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
        } catch (Exception ex) {
            showErrorPane(
                "Could not load the project "
                    + url.toString()
                    + "\n"
                    + "Project file probably corrupted.\n"
                    + "Exception message:\n"
                    + ex);
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

    private void showErrorPane(String message) {
        JOptionPane.showMessageDialog(
            ProjectBrowser.TheInstance,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

}
