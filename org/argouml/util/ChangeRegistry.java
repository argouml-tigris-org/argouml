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

import org.tigris.gef.graph.*;

import org.argouml.kernel.ProjectManager;

/** This class holds the information about the saving state of the current project.
 *  The state is changed by every change made to its diagrams.
 *
 * @see org.argouml.kernel.Project
 */

public class ChangeRegistry implements GraphListener
{
    protected boolean changeFlag;

    public ChangeRegistry() { changeFlag = false; }

    /**
     * changes save state / notifies gui.
     */
    public void setChangeFlag( boolean newValue ) {
		
	if (changeFlag != newValue) {
	    // notify the gui to put a * on the title bar (swing gui):
	    ProjectManager.getManager().notifySavePropertyChanged(newValue);
	}
                
	changeFlag = newValue;
                
    }

    public boolean hasChanged() {
	return changeFlag;
    }

    public void nodeAdded(GraphEvent e) {
	setChangeFlag( true );
    }

    public void edgeAdded(GraphEvent e) {
	setChangeFlag( true );
    }

    public void nodeRemoved(GraphEvent e) {
	setChangeFlag( true );
    }

    public void edgeRemoved(GraphEvent e) {
	setChangeFlag( true );
    }

    public void graphChanged(GraphEvent e) {
	setChangeFlag( true );
    }

} /* end class ChangeRegistry */

