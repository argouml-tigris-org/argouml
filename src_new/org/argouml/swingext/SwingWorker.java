// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.swingext;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.apache.log4j.Logger;
import org.argouml.application.api.ProgressMonitor;
import org.argouml.ui.ArgoFrame;

/**
 * This is the 3rd version of SwingWorker (also known as
 * SwingWorker 3), an abstract class that you subclass to
 * perform GUI-related work in a dedicated thread.  For
 * instructions on and examples of using this class, see:
 * 
 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 *
 * Note that the API changed slightly in the 3rd version:
 * You must now invoke start() on the SwingWorker after
 * creating it.
 */
public abstract class SwingWorker {
	
    private static final Logger LOG =
        Logger.getLogger(SwingWorker.class);
	
    private Object value;  // see getValue(), setValue()

    private GlassPane glassPane;

    private Timer timer;
    
    private ProgressMonitor pmw; 
    
    /** 
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
    private static class ThreadVar {
        private Thread thread;
        ThreadVar(Thread t) { thread = t; }
        synchronized Thread get() { return thread; }
        synchronized void clear() { thread = null; }
    }

    private ThreadVar threadVar;

    /** 
     * Get the value produced by the worker thread, or null if it 
     * hasn't been constructed yet.
     * 
     * @return 	the value produced by the worker thread
     */
    protected synchronized Object getValue() { 
        return value; 
    }

    /** 
     * Set the value produced by worker thread 
     */
    private synchronized void setValue(Object x) { 
        value = x; 
    }

    /** 
     * Compute the value to be returned by the <code>get</code> method. 
     * 
     * @param progressMonitor	the ProgressMonitorWindow class - this 
     * 	            class shall be registered as a progress listener.
     * @return 		the value to be returned
     */
    public abstract Object construct(ProgressMonitor progressMonitor);
    
    
    /** 
     * Instantiate and initialize an instance of ProgressMonitorWindow 
     * 
     * @return      an instance of ProgressMonitorWindow
     */
    public abstract ProgressMonitor initProgressMonitorWindow();
    
    /**
     * This method calls the construct(),  
     * 
     * @return		the value to be returned by the <code>get</code> method.
     */
    public Object doConstruct() {
    	activateGlassPane();
        pmw = initProgressMonitorWindow();

        ArgoFrame.getInstance().setCursor(
        		Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Object retVal = null;
        
        //Create a timer.
        timer = new Timer(25, new TimerListener());
        timer.start();
        
    	try {
    	    retVal = construct(pmw);
    	} catch (Exception exc) {
	        // what should we do here?
    	    LOG.error("Error while loading project: " + exc);
        } finally {
            pmw.close();
        }
        return retVal;
    }
    
    /**
     * The actionPerformed method in this class
     * is called each time the Timer "goes off".
     */
    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            if (pmw.isCanceled()) {
                threadVar.thread.interrupt();
                interrupt();
                timer.stop();
            }
        }
    }
    
    /**
     * Activate the capabilities of glasspane
     */
    protected void activateGlassPane() {
        // Mount the glasspane on the component window
        GlassPane aPane = GlassPane.mount(ArgoFrame.getInstance(), true);

        // keep track of the glasspane as an instance variable
        setGlassPane(aPane);

        if (getGlassPane() != null) {
            // Start interception UI interactions
            getGlassPane().setVisible(true);
        }
    }
    
    /**
     * Deactivate the glasspane
     */
    private void deactivateGlassPane() {
        if (getGlassPane() != null) {
            // Stop UI interception
            getGlassPane().setVisible(false);
        }
    }
    
    /**
     * Called on the event dispatching thread (not on the worker thread)
     * after the <code>construct</code> method has returned.
     */
    public void finished() {
    	deactivateGlassPane();
    	ArgoFrame.getInstance().setCursor(Cursor.getPredefinedCursor(
                Cursor.DEFAULT_CURSOR));
    }
    
    /**
     * Getter method for the glassPange
     *
     * @return GlassPane	the blocking glassPane
     */
    protected GlassPane getGlassPane() {
        return glassPane;
    }
    
    /**
     * Setter method
     *
     * @param newGlassPane GlassPane
     */
    protected void setGlassPane(GlassPane newGlassPane) {
        glassPane = newGlassPane;
    }
    
    /**
     * A new method that interrupts the worker thread.  Call this method
     * to force the worker to stop what it's doing.
     */
    public void interrupt() {
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }

    /**
     * Return the value created by the <code>construct</code> method.  
     * Returns null if either the constructing thread or the current
     * thread was interrupted before a value was produced.
     * 
     * @return the value created by the <code>construct</code> method
     */
    public Object get() {
        while (true) {  
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // propagate
                return null;
            }
        }
    }


    /**
     * Start a thread that will call the <code>construct</code> method
     * and then exit.
     */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {
            public void run() { 
                finished(); 
            }
        };

        Runnable doConstruct = new Runnable() { 
            public void run() {
                try {
                    setValue(doConstruct());
                }
                finally {
                    threadVar.clear();
                }

                SwingUtilities.invokeLater(doFinished);
            }
        };

        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
        
    }

    /**
     * Start the worker thread.
     */
    public void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
}
