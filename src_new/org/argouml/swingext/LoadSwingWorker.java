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
import java.io.IOException;

import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.argouml.application.api.ProgressMonitor;
import org.argouml.i18n.Translator;
import org.argouml.persistence.ProgressListener;
import org.argouml.ui.ArgoFrame;
import org.argouml.ui.ProjectBrowser;

/**
 * The specialized SwingWorker used for loading projects 
 */
public class LoadSwingWorker extends SwingWorker {

    private static final Logger LOG = Logger.getLogger(LoadSwingWorker.class);
    
    private boolean showUi;
    private File file;

    /**
     * This is the only constructor for LoadSwingWorker.
     * 
     * @param aFile		the file that's going to be opened as a project
     * @param aShowUi	whether to show the UI or not
     */
    public LoadSwingWorker(File aFile, boolean aShowUi) {
        this.showUi = aShowUi;
        this.file = aFile;
    }
	
    /**
     * Implements org.argouml.swingext.SwingWorker#construct(); this is
     * the main method for this SwingWorker.
     * In this case, it simply loads the project.
     * 
     * @param pmw	the ProgressMonitorWindow used by ProjectBrowser
     * @return		always null
     */
    public Object construct(ProgressMonitor pmw) {
        // loads the project
        ProjectBrowser.getInstance().loadProject(file, showUi, pmw);
        return null;
    }

    /**
     * Implements org.argouml.swingext.SwingWorker#initProgressMonitorWindow(); 
     * it just creates an instance of ProgressMonitorWindow.
     * 
     * @return  an instance of ProgressMonitorWindow
     */
    public ProgressMonitor initProgressMonitorWindow() {
        UIManager.put("ProgressMonitor.progressText", 
                Translator.localize("filechooser.open-project"));
        Object[] msgArgs = new Object[] {this.file.getPath()};
        return new ProgressMonitorWindow(ArgoFrame.getInstance(),
                Translator.messageFormat("dialog.openproject.title", msgArgs));
    }
    
    /**
     * Overrides the finished method of the SwingWorker class to update the GUI
     */
    public void finished() {
    	super.finished();
    	try {
    	    ProjectBrowser.getInstance().addFileSaved(file);
    	} catch (IOException exc) {
            LOG.error("Failed to save file: " + file
                    + " in most recently used list");
    	}
    }
}
