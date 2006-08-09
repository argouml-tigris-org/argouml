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

import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;

import org.argouml.i18n.Translator;
import org.argouml.persistence.ProgressEvent;
import org.argouml.persistence.ProgressListener;

/**
 * Manages a ProgressMonitor dialog
 * 
 * @author andrea_nironi@tigris.org
 */
public class ProgressMonitorWindow implements ProgressListener {
    
    private ProgressMonitor pbar;
    
    /**
     * initializes a ProgressMonitor
     * 
     * @param parent	the Component to be set as parent
     * @param title     the (internationalized) title of the ProgressMonitor
     */
    public ProgressMonitorWindow(JFrame parent, String title) {
        this.pbar = new ProgressMonitor(parent, 
                title,
                null, 0, 100);
        this.pbar.setMillisToDecideToPopup(250);       
        this.pbar.setMillisToPopup(500);
        parent.repaint();
        progress(5);
        
    }
    
    /**
     * Report a progress to the ProgressMonitor window
     * @param event 	the ProgressEvent, containing the current position
     * 					of the monitor
     */
    public void progress(final ProgressEvent event) {
    	if (this.pbar != null) {
            int currentUpdate = Long.valueOf(event.getPosition()).intValue();
            pbar.setProgress(currentUpdate);
            Object[] args = new Object[]{String.valueOf(currentUpdate)};
            pbar.setNote(Translator.localize("dialog.progress.note", args));
    	}
    }
    
    /**
     * Report a progress to the ProgressMonitor window
     * @param progress     the progress to be set
     */
    public void progress(final int progress) {
        if (this.pbar != null) {
            pbar.setProgress(progress);
            Object[] args = new Object[]{String.valueOf(progress)};
            pbar.setNote(Translator.localize("dialog.progress.note", args));
        }
    }
    
    /**
     * Checks if the ProgressMonitor has been canceled or not
     * 
     * @return true if the user has pressed "cancel"
     */
    public boolean isCanceled() {
        return this.pbar != null && this.pbar.isCanceled();
    }

    /**
     * Close the ProgressMonitor dialog, releasing its resources
     */
    public void close() {
        this.pbar.close();
        this.pbar = null;
    }
    
    // these settings are needed to make the ProgressMonitor pop up early
    static {
        UIManager.put("ProgressBar.repaintInterval", new Integer(150));
        UIManager.put("ProgressBar.cycleTime", new Integer(1050));        
    }
}
