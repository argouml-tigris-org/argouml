// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.ui;

import java.io.File;

import javax.swing.UIManager;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.util.ArgoFrame;
import org.tigris.gef.undo.UndoManager;

/**
 * The specialized SwingWorker used for saving projects
 * @deprecated in 0.29.1 by Bob Tarling. This will not be deleted but reduced
 * in visibility to package visibility only. It is currently only used by
 * ProjectBrowser and any client calling should use methods there for save.
 */
class SaveSwingWorker extends SwingWorker {
@Deprecated

    private final boolean overwrite;
    private final File file;
    private boolean result;
    private Project project;
    private boolean exitAfterSave;

    /**
     * Deprecated constructor for SaveSwingWorker.
     *
     * @param aFile        the file that's going to be saved
     * @param aOverwrite   whether to show the UI or not
     * @deprecated in 0.29.1 Use constructor taking a Project
     */
    public SaveSwingWorker(boolean aOverwrite, File aFile) {
        super("ArgoSaveProjectThread");
        overwrite = aOverwrite;
        file = aFile;
    }

    /**
     * This is the only constructor for SaveSwingWorker.
     *
     * @param project   the project to save
     * @param aFile        the file that's going to be saved
     */
    public SaveSwingWorker(
            final Project project,
            final File aFile,
            final boolean exit) {
        super("ArgoSaveProjectThread");
        overwrite = true;
        file = aFile;
        this.project = project;
        exitAfterSave = exit;
    }

    /**
     * Implements org.argouml.swingext.SwingWorker#construct(); this is
     * the main method for this SwingWorker.
     * In this case, it simply loads the project.
     *
     * @param pmw       the ProgressMonitorWindow used by ProjectBrowser
     * @return          always null
     */
    public Object construct(ProgressMonitor pmw) {
        // Save project at slightly lower priority to keep UI responsive
        Thread currentThread = Thread.currentThread();
        currentThread.setPriority(currentThread.getPriority() - 1);
        // saves the project
        if (project == null) {
            // TODO: When the constructor with no Project is removed we can
            // delete this block and make project final.
            project = ProjectManager.getManager().getCurrentProject();
        }
        result = ProjectBrowser.getInstance().trySave(file, pmw, project);
        return null;
    }

    /**
     * Implements org.argouml.swingext.SwingWorker#initProgressMonitorWindow();
     * it just creates an instance of ProgressMonitor.
     *
     * @return  an instance of ProgressMonitor
     */
    public ProgressMonitor initProgressMonitorWindow() {
        Object[] msgArgs = new Object[] {file.getPath()};
        UIManager.put("ProgressMonitor.progressText", 
                Translator.localize("filechooser.save-as-project"));
        return new ProgressMonitorWindow(ArgoFrame.getFrame(),
                Translator.messageFormat("dialog.saveproject.title", msgArgs));
    }

    /**
     * Overrides the finished method of the SwingWorker class to update the GUI
     */
    public void finished() {
        super.finished();
        if (result) {
            if (exitAfterSave) {
                ProjectBrowser.getInstance().exit();
            } else {
                ProjectBrowser.getInstance().buildTitleWithCurrentProjectName();
                // TODO: Why isn't this done in save?
                UndoManager.getInstance().empty();
            }
        }
    }
}
