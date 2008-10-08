// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.util;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.argouml.model.Model;

/**
 * Helper methods for tests which need to deal with threads.  Not intended for
 * use in applications.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class ThreadHelper {

    /**
     * Wait for all events to be delivered on the MDR event thread and on the
     * AWT/Swing event thread.
     * 
     * @throws InterruptedException if we were interrupted while waiting for the
     *             AWT thread to sync
     * @throws InvocationTargetException should never happen. Indicates an
     *             internal error.
     */
    public static void synchronize() throws InterruptedException, 
    InvocationTargetException {
        
        // Wait for all Model subsystem events to be delivered
        Model.getPump().flushModelEvents();

        // Wait for all AWT events to be dispatched and processed
        Runnable doWorkRunnable = new Runnable() {
            public void run() {
            }  
        };
        SwingUtilities.invokeAndWait(doWorkRunnable);
    }
}
