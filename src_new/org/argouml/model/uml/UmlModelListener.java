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

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/**
 * A single listener that listens to all Model Element events and change
 * the NeedsSave information on the project.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class UmlModelListener implements MElementListener {

    /**
     * Log4j logging category.
     */
    private static final Logger LOG = Logger.getLogger(UmlModelListener.class);

    /**
     * Singleton instance.
     */
    private static UmlModelListener SINGLETON = new UmlModelListener();

    /**
     * Singleton instance access method.
     *
     * @return the singleton instance.
     */
    public static UmlModelListener getInstance() {
        return SINGLETON;
    }

    /**
     * Don't allow instantiation.
     */
    private UmlModelListener() {
    }

    /**
     * Handle the event.
     */
    public void listRoleItemSet(MElementEvent mee) {
        LOG.debug("listRoleItemSet(" + mee + ")");
	// TODO:  Do we need to model change notify here?
    }

    /**
     * Handle the event.
     * Provides a model change notification only if the property
     * values differ.
     */
    public void propertySet(MElementEvent mee) {
	notifyModelChanged(mee);
    }

    /**
     * Handle the event.
     */
    public void recovered(MElementEvent mee) {
        LOG.debug("recovered(" + mee + ")");
	// TODO:  Do we need to model change notify here?
    }

    /**
     * Handle the event.
     */
    public void removed(MElementEvent mee) {
        LOG.debug("removed(" + mee + ")");
	// TODO:  Do we need to model change notify here?
	// yes since we need to update the GUI
	notifyModelChanged(mee);
    }

    /**
     * Handle the event.
     * Provides a model change notification.
     */
    public void roleAdded(MElementEvent mee) {
        LOG.debug("roleAdded(" + mee + ")");
	notifyModelChanged(mee);
    }

    /**
     * Handle the event.
     * Provides a model change notification.
     */
    public void roleRemoved(MElementEvent mee) {
        LOG.debug("roleRemoved(" + mee + ")");
        
	notifyModelChanged(mee);
    }

    /**
     * Common model change notification process.
     */
    private void notifyModelChanged(MElementEvent mee) {
        // TODO: Change the project dirty flag outside this package
        //       using an event listener.

        if (mee.getAddedValue() != null
            || mee.getRemovedValue() != null
            || (mee.getNewValue() != null
            && !mee.getNewValue().equals(mee.getOldValue())))
        {
            Project cp = ProjectManager.getManager().getCurrentProject();

            if (cp != null)
                cp.setNeedsSave(true);
        }
    
    }
}

