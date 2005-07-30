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
import java.net.URL;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;
import org.argouml.application.api.Configuration;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.PersistenceManager;
import org.argouml.ui.ProjectBrowser;

/**
 * Action to save project under name.
 *
 * @stereotype singleton
 */
public class ActionSaveProjectAs extends ActionSaveProject {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionSaveProjectAs.class);

    /**
     * The singleton.
     */
    public static final ActionSaveProjectAs SINGLETON =
        new ActionSaveProjectAs();

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    protected ActionSaveProjectAs() {
        super(Translator.localize("action.save-project-as"),
                ResourceLoaderWrapper.lookupIcon("action.save-project-as"));
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        LOG.info("Performing saveas action");
        trySave(false);
    }

    /**
     * @see org.argouml.uml.ui.ActionSaveProject#trySave(boolean)
     */
    public boolean trySave(boolean overwrite) {
        File f = getNewFile();
        if (f == null) {
            return false;
        }
        
        boolean success = ProjectBrowser.getInstance().trySave(overwrite, f);
        if (success) {
            ProjectBrowser.getInstance().setTitle(
                ProjectManager.getManager().getCurrentProject().getName());
        }
        return success;
    }

    /**
     * @return the File to save to
     */
    protected File getNewFile() {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();

        JFileChooser chooser = null;
        URL url = p.getURL();
        if ((url != null) && (url.getFile().length() > 0)) {
            chooser = new JFileChooser(url.getFile());
        }
        if (chooser == null) {
            chooser = new JFileChooser();
        }

        if (url != null) {
            chooser.setSelectedFile(new File(url.getFile()));
        }

        String sChooserTitle =
	    Translator.localize("filechooser.save-as-project");
        chooser.setDialogTitle(sChooserTitle + " " + p.getName());

        chooser.setAcceptAllFileFilterUsed(false);
        PersistenceManager.getInstance().setSaveFileChooserFilters(chooser);

        String fn = Configuration.getString(
                PersistenceManager.KEY_SAVE_PROJECT_PATH);
        if (fn.length() > 0) {
            chooser.setSelectedFile(new File(fn));
        }

        int retval = chooser.showSaveDialog(pb);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            AbstractFilePersister filter =
                (AbstractFilePersister) chooser.getFileFilter();
            if (theFile != null) {
                Configuration.setString(
                        PersistenceManager.KEY_SAVE_PROJECT_PATH,
                        theFile.getPath());

                String name = theFile.getName();
                if (!name.endsWith("." + filter.getExtension())) {
                    theFile =
                        new File(
                            theFile.getParent(),
                            name + "." + filter.getExtension());
                }
            }
            return theFile;
        } 
        return null;
    }

} /* end class ActionSaveProjectAs */
