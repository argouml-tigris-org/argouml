// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.argouml.application.api.CommandLineInterface;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.persistence.PersistenceManager;
import org.argouml.ui.FileChooserFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.cmd.GenericArgoMenuBar;
import org.tigris.gef.base.Globals;

/**
 * Action that loads the project.
 * This will throw away the project that we were working with up to this
 * point so some extra caution.
 *
 * @see ActionSaveProject
 */
public class ActionOpenProject extends ActionFileOperations
    implements CommandLineInterface {

    private static final Logger LOG =
        Logger.getLogger(ActionOpenProject.class);

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor for this action.
     */
    public ActionOpenProject() {
        // this is not a "global" action, since it is never downlighted...
        super(Translator.localize("action.open-project"), ResourceLoaderWrapper
                .lookupIconResource(Translator.getImageBinding("OpenProject"),
                        Translator.localize("OpenProject")));
    }

    ////////////////////////////////////////////////////////////////
    // main methods


    /**
     * Performs the action of opening a project.
     *
     * @param e an event
     */
    public void actionPerformed(ActionEvent e) {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();
        PersistenceManager pm = PersistenceManager.getInstance();

        if (!askConfirmationAndSave()) return;

        try {
            // next line does give user.home back but this is not
            // compliant with how the project.url works and therefore
            // open and save project as give different starting
            // directories.  String directory =
            // Globals.getLastDirectory();
            JFileChooser chooser = null;
            if (p != null && p.getURL() != null) {
                File file = new File(p.getURL().getFile());
                if (file.getParentFile() != null) {
                    chooser = FileChooserFactory.getFileChooser(file.getParent());
                }
            } else {
                chooser = FileChooserFactory.getFileChooser();
            }

            if (chooser == null) {
                chooser = FileChooserFactory.getFileChooser();
            }

            chooser.setDialogTitle(
                    Translator.localize("filechooser.open-project"));

            FileFilter allFiles = chooser.getFileFilter();
            chooser.removeChoosableFileFilter(allFiles);

            pm.setFileChooserFilters(chooser);

            int retval = chooser.showOpenDialog(pb);
            if (retval == 0) {
                File theFile = chooser.getSelectedFile();
                if (!theFile.canRead()) {
                    /* Try adding the default extension. */
                    File n = new File(theFile.getPath() + "."
                            + pm.getDefaultExtension());
                    /* The above could have been the selected extension
                     * in the chooser, but I have no direct means
                     * of getting the extension of a FileFilter... */
                    if (n.canRead()) theFile = n;
                }
                if (theFile != null) {
                    String path = theFile.getParent();
                    // TODO: Use something other than Globals to
                    // store last directory. We should rely on GEF
                    // only for Diagrams. Bob Tarling 15 Jan 2004.
                    Globals.setLastDirectory(path);
                    URL url = theFile.toURL();
                    if (url != null) {
                        if (loadProject(url)) {
                            // notification of menu bar
                            GenericArgoMenuBar menuBar =
                                (GenericArgoMenuBar) pb.getJMenuBar();
                            menuBar.addFileSaved(theFile.getCanonicalPath());
                        }
                    }
                }
            }
        } catch (IOException ignore) {
            LOG.error("got an IOException in ActionOpenProject", ignore);
        }
    }

    /**
     * Execute this action from the command line.
     *
     * @see org.argouml.application.api.CommandLineInterface#doCommand(String)
     * @param argument is the url of the project we load.
     * @return true if it is OK.
     */
    public boolean doCommand(String argument) {
        final URL url;
        try {
            url = new URL(argument);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LOG.error("Incorrectly formatted URL.", e);
            return false;
        }
        return loadProject(url);
    }

} /* end class ActionOpenProject */
