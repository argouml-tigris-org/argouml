// Copyright (c) 1996-01 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import org.argouml.application.api.Argo;
import org.argouml.kernel.*;
import org.argouml.ui.*;

import java.awt.Window;
import java.awt.event.*;
import java.text.MessageFormat;

import javax.swing.*;

/** Action to trigger creation of a new project.
 * @stereotype singleton
 */
public class ActionNew extends UMLAction {

  ////////////////////////////////////////////////////////////////
  // static variables

  public static ActionNew SINGLETON = new ActionNew(); 

  ////////////////////////////////////////////////////////////////
  // constructors

  protected ActionNew() { super("New"); }

  ////////////////////////////////////////////////////////////////
  // main methods

  public void actionPerformed(ActionEvent e) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = ProjectManager.getManager().getCurrentProject();

    if (p != null && p.needsSave()) {
      String t = MessageFormat.format (
          Argo.localize ("Actions", "template.new_project.save_changes_to"),
          new Object[] {p.getName()}
        );
      int response = JOptionPane.showConfirmDialog (
          pb,
          t,
          t,
          JOptionPane.YES_NO_CANCEL_OPTION
        );

      if (response == JOptionPane.CANCEL_OPTION) return;
      if (response == JOptionPane.YES_OPTION) {
        boolean safe = false;

        if (ActionSaveProject.SINGLETON.shouldBeEnabled()) {
          safe = ActionSaveProject.SINGLETON.trySave (true);
        }
        if (!safe) {
          safe = ActionSaveProjectAs.SINGLETON.trySave (false);
        }          
        if (!safe) return;
      }
      //TODO: if you cancel the save it should cancel open
      // Steffen Zschaler 01/10/2002 - Well, it does, doesn't it? trySave will
      // return false in that case...
    }
    // we should remove all open dialogs. They have as parent the ProjectBrowser
    Window[] windows = ProjectBrowser.TheInstance.getOwnedWindows();
    for (int i = 0; i < windows.length; i++) {
        windows[i].dispose();
    }
    p = ProjectManager.getManager().makeEmptyProject();
    ProjectBrowser.TheInstance.setTarget(p.getDiagrams().toArray()[0]);
  }
} /* end class ActionNew */
