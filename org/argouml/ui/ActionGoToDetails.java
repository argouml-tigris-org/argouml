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

package org.argouml.ui;

import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import org.argouml.cognitive.ui.TabToDoTarget;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.TabModelTarget;
import org.argouml.uml.ui.UMLAction;

/**
 * Action to display a tab in the DetailsPane by name, eg "Properties".
 */
public class ActionGoToDetails extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // instance variables
    private String tabName;

    /**
     * Constructor.
     *
     * @param name The name of the tab.
     */
    public ActionGoToDetails(String name) {
	super(name, true, NO_ICON);
	tabName = name;
    }

    /**
     * Should return true if the pane where the user can navigate to supports
     * the current target.
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
        super.shouldBeEnabled();
        ProjectBrowser pb = ProjectBrowser.getInstance();       
        if (!super.shouldBeEnabled() || pb == null) return false;
        JPanel namedTab = pb.getNamedTab(tabName);
        boolean shouldBeEnabled = false;
        if (namedTab instanceof TabToDoTarget) {
            shouldBeEnabled = true;
        } else 
	    if (namedTab instanceof TabModelTarget
		&& TargetManager.getInstance().getTarget() != null)
	    {
		shouldBeEnabled =
		    ((TabModelTarget) namedTab)
		    .shouldBeEnabled(TargetManager.getInstance().getTarget());
	    } else {
		shouldBeEnabled =
		    (namedTab != null
		     && TargetManager.getInstance().getTarget() != null);
	    } 
        
	return shouldBeEnabled;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        ProjectBrowser.getInstance().selectTabNamed(tabName);
    }

} /* end class ActionGoToDetails */
