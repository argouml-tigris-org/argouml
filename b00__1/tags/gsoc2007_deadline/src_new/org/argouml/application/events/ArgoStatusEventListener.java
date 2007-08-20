// $Id:ArgoStatusEventListener.java 13026 2007-07-10 17:54:02Z tfmorris $
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.application.events;

import org.argouml.application.api.ArgoEventListener;

/**
 * An interface that objects that are interested in StatusEvents 
 * must extend.
 *
 * @author Tom Morris <tfmorris@gmail.com>
 */
public interface ArgoStatusEventListener extends ArgoEventListener {

    /**
     * Invoked when there is a new status text to be shown, 
     * that should replace any previous one.
     * 
     * @param e <code>ArgoStatusEvent</code> describing the changed text
     */
    public void statusText(ArgoStatusEvent e);
    
    /**
     * Invoked when a previously shown status text has to be removed.
     * 
     * @param e <code>ArgoStatusEvent</code> describing the removed event
     */
    public void statusCleared(ArgoStatusEvent e);
    
    /**
     * A project has been saved.
     * 
     * @param e <code>ArgoStatusEvent</code> with the name of the project that
     *            was saved.
     */
    public void projectSaved(ArgoStatusEvent e);
    
    /**
     * A project has been loaded.
     * 
     * @param e <code>ArgoStatusEvent</code> with the name of the project that
     *            was loaded.
     */
    public void projectLoaded(ArgoStatusEvent e);
    
    /**
     * A project has been modified.
     * 
     * @param e <code>ArgoStatusEvent</code> with the name of the project that
     *            was modified (ignored for current ArgoUML implementation where
     *            there is only a single project open at a time).
     */
    public void projectModified(ArgoStatusEvent e);
}
