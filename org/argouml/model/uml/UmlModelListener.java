// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;

/**
 * A single listener that listens to all UML ModelElement events and change
 * the NeedsSave information on the project.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class UmlModelListener implements PropertyChangeListener {

    /**
     * Log4j logging category.
     */
    private static final Logger LOG = Logger.getLogger(UmlModelListener.class);

    /**
     * Singleton instance.
     */
    private static UmlModelListener singleton = new UmlModelListener();

    /**
     * Singleton instance access method.
     *
     * @return the singleton instance.
     */
    public static UmlModelListener getInstance() {
        return singleton;
    }

    /**
     * Don't allow instantiation.
     */
    private UmlModelListener() {
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        notifyModelChanged(evt);
    }

    /**
     * Common model change notification process.
     */
    private void notifyModelChanged(PropertyChangeEvent pce) {
        if (pce.getNewValue() != null
            && !pce.getNewValue().equals(pce.getOldValue()))
        {
            Project cp = ProjectManager.getManager().getCurrentProject();

            if (cp != null)
                cp.setNeedsSave(true);
        }
    
    }
    
    /**
     * For every new ModelElement that has been created, we want 
     * to register for updation events.
     * 
     * @param elm the UML modelelement that has been created
     */
    public void newElement(Object elm) {
        Model.getPump().addModelEventListener(this, elm);
    }
    
    /**
     * For every ModelElement that has been deleted, we want to 
     * remove its listener.
     * 
     * @param elm the UML modelelement that has been deleted
     */
    public void deleteElement(Object elm) {
        Model.getPump().removeModelEventListener(this, elm);
    }
}

