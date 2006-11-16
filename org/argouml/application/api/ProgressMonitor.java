// $Id: ProgressListener.java 11208 2006-09-18 22:34:49Z tfmorris $
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.application.api;

import org.argouml.persistence.ProgressListener;

/**
 * This is a generic progress notifier. Can be used with any GUI progress bar
 * or any other progress GUI. It's this way to be independent of the GUI
 * implementation.
 * @author Bogdan Pistol
 */
public interface ProgressMonitor extends ProgressListener {
    
    /**
     * Informs the progress tool that the total progress was updated.
     * 
     * @param progress the amount of progress done so far, this is the whole
     * progress until now, not just the subtask's progress or the
     * main task's progress
     */
    void updateProgress(int progress);
    
    /**
     * Updates the subtask that is in progress.
     * @param action the name of the subtask
     */
    void updateSubTask(String action);
    
    /**
     * Updates the major task that is going on.
     * @param name the new task
     */
    void updateMainTask(String name);
    
    /**
     * Determines if the user wants to cancel the current action.
     * If this happens the current action should be stopped.
     * <p>
     * So in a long running action, you should query this periodicaly to see
     * if the user still wants to continue or he canceled the action.
     * @return true if the user canceled the action and false otherwise
     */
    boolean isCanceled();
    
    /**
     * Determines the maximum amount of progress that can be reached.
     * This should be set before the progress is updated.
     * @param max the maximum progress value or -1 if the value is unknown
     */
    void setMaximumProgress(int max);
    
    /**
     * This method notifies the GUI that the working thread determines that
     * there are no actions that could be done for various reasons.
     * The GUI should notify the user too.
     */
    void notifyNullAction();
    
    /**
     * If something happens the user should be notified.
     * @param title a title for the error/information/etc
     * @param introduction a short message that will continue with the message
     * @param message the actual message with all the details
     */
    void notifyMessage(String title, String introduction, String message);

     /** 
     * Indicate that the operation is complete.  This happens automatically
     * when the value set by setProgress is >= max, but it may be called
     * earlier if the operation ends early.
     */
    public void close();

}
