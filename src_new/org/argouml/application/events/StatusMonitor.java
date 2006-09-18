// $Id$
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

package org.argouml.application.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.ui.ProjectBrowser;

/**
 * Class to implement status event listener registration and
 * event notification.
 * 
 * @author Tom Morris
 *
 */
public class StatusMonitor {

    private static Collection listeners;

    private StatusMonitor() {
        listeners = new ArrayList();
    }

    /**
     * Notify all listeners of new status.
     * 
     * @param status
     */
    public static void notify(Object source, String status) {
        for (Iterator it = listeners.iterator(); it.hasNext();) {
            StatusListener l = (StatusListener) it.next();
            l.update(new StatusEvent(source, status));
        }

	// TODO: temporary until ProjectBrowser is registering
        ProjectBrowser.getInstance().getStatusBar().showStatus(status);

    }

//    public static void notify(String status) {
//        notify(null, status);
//    }

    /**
     * Register a listener to receive status updates.
     */
    public static boolean addListener(StatusListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            return true;
        }
        return false;
    }

    /**
     * Remove a registered listener
     */
    public static boolean removeListener(StatusListener listener) {
        return listeners.remove(listener);
    }
}
