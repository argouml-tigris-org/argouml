// Copyright (c) 1996-2002 The Regents of the University of California. All
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

import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;
import org.argouml.util.FileFilters;
import org.argouml.util.osdep.OsUtil;

/** Action to save project under name.
 * @stereotype singleton
 */
public class ActionSaveProjectAs extends ActionSaveProject {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionSaveProjectAs SINGLETON = new ActionSaveProjectAs();

    public static final String separator = "/";
    //System.getProperty("file.separator");

    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionSaveProjectAs() {
        super("action.save-project-as", NO_ICON);
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent e) {
        trySave(false);
    }

    public boolean trySave(boolean overwrite) {
        File f = getNewFile();
        if (f == null)
            return false;
        boolean success = trySave(overwrite, f);
        if (success) {
            ProjectBrowser.getInstance().setTitle(
                ProjectManager.getManager().getCurrentProject().getName());
        }
        return success;
    }

    protected File getNewFile() {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();

        JFileChooser chooser = null;
        URL url = p.getURL();
        if ((url != null) && (url.getFile().length() > 0)) {
            chooser = OsUtil.getFileChooser(url.getFile());
        }
        if (chooser == null) {
            chooser = OsUtil.getFileChooser();
        }

        if (url != null) {
            chooser.setSelectedFile(new File(url.getFile()));
        }

        String sChooserTitle =
            Argo.localize("Actions", "filechooser.save-as-project");
        chooser.setDialogTitle(sChooserTitle + p.getName());
        chooser.setFileFilter(FileFilters.CompressedFileFilter);

        int retval = chooser.showSaveDialog(pb);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                String name = file.getName();
                if (!name.endsWith(Project.COMPRESSED_FILE_EXT)) {
                    file =
                        new File(
                            file.getParent(),
                            name + Project.COMPRESSED_FILE_EXT);
                }
            }
            return file;
        } else {
            return null;
        }
    }

    public boolean shouldBeEnabled() {
        return true;
    }
} /* end class ActionSaveProjectAs */
