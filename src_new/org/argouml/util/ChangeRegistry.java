// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: ChangeRegistry.java
// Classes: ChangeRegistry
// Original Author: thorsten Jun 2000
// $Id$

package org.argouml.util;

import org.argouml.kernel.ProjectManager;
import org.argouml.uml.ui.ActionSaveProject;

import org.tigris.gef.graph.GraphEvent;
import org.tigris.gef.graph.GraphListener;

/** 
 * This class holds the information about the "change flag" of 
 * the current project. Initially, the "change flag" is clear, 
 * which means that there is nothing in the project to save. 
 * The "change flag" is set by every change made to its diagrams, 
 * which means that the project becomes 'dirty', i.e. it needs saving. 
 *
 * @see org.argouml.kernel.Project
 */
public class ChangeRegistry implements GraphListener
{
    /**
     * The constructor.
     * 
     */
    public ChangeRegistry() {
        setChangeFlag(false);
    }

    /**
     * Changes save state / notifies gui.
     *
     * @param newValue true means that the project needs to be saved. 
     *                 False means that there is nothing to save.
     */
    public void setChangeFlag( boolean newValue ) {
		
        boolean oldValue = ActionSaveProject.getInstance().isEnabled();
        
	if (oldValue != newValue) {
	    // notify the gui to put a * on the title bar (swing gui):
            ActionSaveProject.getInstance().setEnabled(newValue);
	    ProjectManager.getManager().notifySavePropertyChanged(newValue);
	}
                
    }

    /**
     * @return the change flag aka save state
     */
    public boolean hasChanged() {
	return ActionSaveProject.getInstance().isEnabled();
    }

    /**
     * @see org.tigris.gef.graph.GraphListener#nodeAdded(org.tigris.gef.graph.GraphEvent)
     */
    public void nodeAdded(GraphEvent e) {
	setChangeFlag( true );
    }

    /**
     * @see org.tigris.gef.graph.GraphListener#edgeAdded(org.tigris.gef.graph.GraphEvent)
     */
    public void edgeAdded(GraphEvent e) {
	setChangeFlag( true );
    }

    /**
     * @see org.tigris.gef.graph.GraphListener#nodeRemoved(org.tigris.gef.graph.GraphEvent)
     */
    public void nodeRemoved(GraphEvent e) {
	setChangeFlag( true );
    }

    /**
     * @see org.tigris.gef.graph.GraphListener#edgeRemoved(org.tigris.gef.graph.GraphEvent)
     */
    public void edgeRemoved(GraphEvent e) {
	setChangeFlag( true );
    }

    /**
     * @see org.tigris.gef.graph.GraphListener#graphChanged(org.tigris.gef.graph.GraphEvent)
     */
    public void graphChanged(GraphEvent e) {
	setChangeFlag( true );
    }

} /* end class ChangeRegistry */

