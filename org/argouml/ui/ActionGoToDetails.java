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

import javax.swing.JTabbedPane;

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

    public boolean shouldBeEnabled() {
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        DetailsPane pane = pb.getDetailsPane();
        JTabbedPane pane2 = pane.getTabs();
        int index = pane2.indexOfTab(_tabName);
        if (index >= 0) {
            return pane2.isEnabledAt(index);
        } 
        return false;
        /*
        Project p = pb.getProject();
        if (!super.shouldBeEnabled() || pb == null) return false;
        boolean b = (pb.getNamedTab(_tabName) != null);
        return b;
        */
    }

    public void actionPerformed(ActionEvent ae) {
        ProjectBrowser.TheInstance.selectTabNamed(_tabName);
    }

} /* end class ActionGoToDetails */
