// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.swingext;

import java.io.File;

import javax.swing.UIManager;

import org.argouml.i18n.Translator;
import org.argouml.ui.ProjectBrowser;

/**
 * The specialized SwingWorker used for saving projects 
 */
public class SaveSwingWorker extends SwingWorker {

    private boolean overwrite;
    private File file;
    private Boolean result;

    /**
     * This is the only constructor for SaveSwingWorker.
     * 
     * @param aFile        the file that's going to be saved
     * @param aOverwrite   whether to show the UI or not
     */
    public SaveSwingWorker(boolean aOverwrite, File aFile) {
        this.overwrite = aOverwrite;
        this.file = aFile;
    }
        
    /**
     * Implements org.argouml.swingext.SwingWorker#construct(); this is
     * the main method for this SwingWorker.
     * In this case, it simply loads the project.
     * 
     * @param pmw       the ProgressMonitorWindow used by ProjectBrowser
     * @return          always null
     */
    public Object construct(ProgressMonitorWindow pmw) {
        // saves the project
        this.result = 
            new Boolean(
                    ProjectBrowser.getInstance().trySave(overwrite, file, pmw));
        return null;
    }

    /**
     * Implements org.argouml.swingext.SwingWorker#initProgressMonitorWindow(); 
     * it just creates an instance of ProgressMonitorWindow.
     * 
     * @return  an instance of ProgressMonitorWindow
     */
    public ProgressMonitorWindow initProgressMonitorWindow() {
        Object[] msgArgs = new Object[] {this.file.getPath()};
        UIManager.put("ProgressMonitor.progressText", 
                Translator.localize("filechooser.save-as-project"));
        return new ProgressMonitorWindow(ProjectBrowser.getInstance(),
                Translator.messageFormat("dialog.saveproject.title", msgArgs));
    }
    
    /**
     * Overrides the finished method of the SwingWorker class to update the GUI
     */
    public void finished() {
        super.finished();
        try {
            if (this.result != null && this.result.booleanValue()) {
                ProjectBrowser.getInstance().buildTitleWithCurrentProjectName();
            }
        } catch (Exception exc) {
            //TODO: what should we do here?
        }
    }
}
