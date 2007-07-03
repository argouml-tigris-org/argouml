// $Id: ProgressMonitorWindow.java 12455 2007-04-29 10:34:21Z mvw $
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

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.argouml.i18n.Translator;
import org.argouml.taskmgmt.ProgressEvent;

/**
 * Manages a ProgressMonitor dialog.
 * 
 * NOTE: Users of this class should use the type of the interface
 * org.argouml.swingext.ProgressMonitor wherever possible to
 * maintain GUI independance.
 * 
 * @author andrea_nironi@tigris.org
 */
public class ProgressMonitorWindow implements
        org.argouml.taskmgmt.ProgressMonitor {
    
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
        updateProgress(5);
        
    }
    
    /*
     * Report a progress to the ProgressMonitor window.
     * @see org.argouml.persistence.ProgressListener#progress(org.argouml.persistence.ProgressEvent)
     */
    public void progress(final ProgressEvent event) {
        updateProgress((int) event.getPosition());
    }
    
    /*
     * Report a progress to the ProgressMonitor window.
     * @see org.argouml.application.api.ProgressMonitor#updateProgress(int)
     */
    public void updateProgress(final int progress) {
        if (this.pbar != null) {
            pbar.setProgress(progress);
            Object[] args = new Object[]{String.valueOf(progress)};
            pbar.setNote(Translator.localize("dialog.progress.note", args));
        }
    }
    
    /*
     * @see org.argouml.application.api.ProgressMonitor#isCanceled()
     */
    public boolean isCanceled() {
        return this.pbar != null && this.pbar.isCanceled();
    }

    /*
     * @see org.argouml.application.api.ProgressMonitor#close()
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

    /*
     * @see org.argouml.application.api.ProgressMonitor#notifyMessage(java.lang.String, java.lang.String, java.lang.String)
     */
    public void notifyMessage(final String title, final String introduction, 
            final String message) {
        final String messageString = introduction + " : " + message; 
        pbar.setNote(messageString);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JDialog dialog =
                    new ExceptionDialog(
                            ArgoFrame.getInstance(),
                            title,
                            introduction,
                            message);
                dialog.setVisible(true);
            }
        });
    }

    /*
     * @see org.argouml.application.api.ProgressMonitor#notifyNullAction()
     */
    public void notifyNullAction() {
        // ignored
    }

    /*
     * @see org.argouml.application.api.ProgressMonitor#setMaximumProgress(int)
     */
    public void setMaximumProgress(int max) {
        pbar.setMaximum(max);
    }

    public void updateSubTask(String action) {
        // TODO: concatenate? - tfm
        // overwrite for now
        pbar.setNote(action);
    }

    public void updateMainTask(String name) {
        pbar.setNote(name);
    }
}
