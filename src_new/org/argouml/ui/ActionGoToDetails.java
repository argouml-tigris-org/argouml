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

package org.argouml.ui;

import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import org.argouml.cognitive.ui.TabToDoTarget;
import org.argouml.uml.ui.TabModelTarget;
import org.argouml.uml.ui.UMLAction;

public class ActionGoToDetails extends UMLAction {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected String _tabName;

  ////////////////////////////////////////////////////////////////
  // constructor
  public ActionGoToDetails(String tabName) {
    super(tabName, NO_ICON);
    _tabName = tabName;
  }

    /**
     * Should return true as the pane where the user can navigate to supports
     * the current target.
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
        super.shouldBeEnabled();
        ProjectBrowser pb = ProjectBrowser.TheInstance;       
        if (!super.shouldBeEnabled() || pb == null) return false;
        JPanel namedTab = pb.getNamedTab(_tabName);
        boolean shouldBeEnabled = false;
        if (namedTab instanceof TabToDoTarget) {
            shouldBeEnabled = true;
        } else 
        if (namedTab instanceof TabModelTarget) {
            shouldBeEnabled = ((TabModelTarget)namedTab).shouldBeEnabled();
        } else {
            shouldBeEnabled = namedTab != null;
        } 
        
       return shouldBeEnabled;
    }

    public void actionPerformed(ActionEvent ae) {
        ProjectBrowser.TheInstance.selectTabNamed(_tabName);
    }

} /* end class ActionGoToDetails */
