// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import java.text.MessageFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.argouml.application.api.CommandLineInterface;
import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.kernel.AbstractFilePersister;
import org.argouml.kernel.ArgoFilePersister;
import org.argouml.kernel.OpenException;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.XmiFilePersister;
import org.argouml.kernel.ZargoFilePersister;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.cmd.GenericArgoMenuBar;
import org.argouml.util.osdep.OsUtil;
import org.argouml.xml.argo.ArgoParser;
import org.tigris.gef.base.Globals;

/**
 * Action that loads the project.
 * This will throw away the project that we were working with up to this 
 * point so some extra caution.
 *
 * @see ActionSaveProject
 */
public class ActionOpenProject
    extends UMLAction
    implements CommandLineInterface {

    private static final Logger LOG =
        Logger.getLogger(ActionOpenProject.class);
    
    private AbstractFilePersister zargoPersister = new ZargoFilePersister();
    private AbstractFilePersister argoPersister  = new ArgoFilePersister();
    private AbstractFilePersister xmiPersister  = new XmiFilePersister();

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor for this action.
     */
    public ActionOpenProject() {
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
                        Translator.localize(
			        "Actions",
				"optionpane.open-project-save-changes-to"),
			new Object[] {
			    p.getName()
			});

            int response =
                JOptionPane.showConfirmDialog(pb,
					      t,
					      t,
					      JOptionPane.YES_NO_CANCEL_OPTION);

            if (response == JOptionPane.CANCEL_OPTION 
                    || response == JOptionPane.CLOSED_OPTION) {
                return;
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
                    return;
                }
            }
        }

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
                    chooser = OsUtil.getFileChooser(file.getParent());
                }
            } else {
                chooser = OsUtil.getFileChooser();
            }

            if (chooser == null) {
                chooser = OsUtil.getFileChooser();
            }

            chooser.setDialogTitle(
                    Translator.localize("Actions",
						 "filechooser.open-project"));
            
            FileFilter allFiles = chooser.getFileFilter();
            chooser.removeChoosableFileFilter(allFiles);
            
            chooser.addChoosableFileFilter(zargoPersister);
            chooser.addChoosableFileFilter(argoPersister);
            chooser.addChoosableFileFilter(xmiPersister);
            chooser.setFileFilter(zargoPersister);
            

            int retval = chooser.showOpenDialog(pb);
            if (retval == 0) {
                File theFile = chooser.getSelectedFile();
                if (theFile != null) {
                    String path = theFile.getParent();
                    // TODO: Use something other than Globals to
                    // store last directory. We should rely on GEF
                    // only for Diagrams. Bob Tarling 15 Jan 2004.
                    Globals.setLastDirectory(path);
                    URL url = theFile.toURL();
                    if (url != null) {
                        loadProject(url);
                        // notification of menu bar
                        GenericArgoMenuBar menuBar =
                            (GenericArgoMenuBar) pb.getJMenuBar();
                        menuBar.addFileSaved(theFile.getCanonicalPath());
                    }
                }
            }
        } catch (IOException ignore) {
            LOG.error("got an IOException in ActionOpenProject", ignore);
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
        // (like the statechart diagrams) access the current
        // diagram to get some info. This might cause 
        // problems if there's another statechart diagram
        // active, so I remove the current project, before
        // loading the new one.

        Designer.disableCritiquing();
        Designer.clearCritiquing();

        Project p = null;
        try {
            AbstractFilePersister persister = null;
            String file = url.getFile();
            if (file.endsWith("." + zargoPersister.getExtension())) {
                persister = zargoPersister;
            } else if (file.endsWith("." + argoPersister.getExtension())) {
                persister = argoPersister;
            } else if (file.endsWith("." + xmiPersister.getExtension())) {
                persister = xmiPersister;
            } else {
                throw new IllegalStateException("Filename " + url.getFile()  
                                + " is not of a known file type");
            }
            
            p = persister.loadProject(url);

            ProjectBrowser.getInstance().showStatus(
		    MessageFormat.format(Translator.localize(
			    "Actions",
			    "label.open-project-status-read"),
					 new Object[] {
					     url.toString()
					 }));
        } catch (OpenException ex) {
            LOG.error("Exception while loading project", ex);
            showErrorPane(
			  "Could not load the project "
			  + url.toString()
			  + " due to parser configuration errors.\n"
			  + "Please read the instructions at www.argouml.org "
			  + "on the "
			  + "requirements of argouml and how to install it.");
            p = oldProject;
        } finally {
            if (!ArgoParser.getInstance().getLastLoadStatus()) {
                p = oldProject;
                showErrorPane(
			      "Problem in loading the project "
			      + url.toString()
			      + "\n"
			      + "Project file probably corrupt from "
			      + "an earlier version or ArgoUML.\n"
			      + "Error message:\n"
			      + ArgoParser.getInstance().getLastLoadMessage()
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
                // not have to gc the old project
                if (p != null && !p.equals(oldProject)) {
		    //prepare the old project for gc
		    ProjectManager.getManager().removeProject(oldProject);
                }
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
        loadProject(url);
        return true;
    }

} /* end class ActionOpenProject */
