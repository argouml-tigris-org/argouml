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

import org.argouml.kernel.*;
import org.argouml.ui.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.util.*;

import ru.novosoft.uml.foundation.core.*;

import java.util.*;
import java.awt.event.*;
import java.text.MessageFormat;

import javax.swing.*;

public class ActionRemoveFromModel extends UMLChangeAction {
  
  ////////////////////////////////////////////////////////////////
  // static variables
  
  public static ActionRemoveFromModel SINGLETON = new ActionRemoveFromModel();
  
  ////////////////////////////////////////////////////////////////
  // constructors
  
  public ActionRemoveFromModel() {
    super (
        Localizer.localize ("CoreMenu", "Delete From Model"),
        NO_ICON
      );
  }
  
  ////////////////////////////////////////////////////////////////
  // main methods
  
  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    
    if (target instanceof MModelElement) return true;
    
    int size = 0;
    try {
      // needs-more-work: trashing diagrams
      Editor ce = Globals.curEditor();
      Vector figs = ce.getSelectionManager().getFigs();
      size = figs.size();
    }
    catch(Exception e) {}
    
    if (size > 0) return true;
    //     for (int i = 0; i < size; i++) {
    //       Fig f = (Fig) figs.elementAt(i);
    //       Object owner = f.getOwner();
    //       if (owner instanceof MModelElement) return true;
    //     }
    
    return false;
  }
  
  public void actionPerformed (ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    Project p = pb.getProject();
    
    if (target instanceof MModelElement) {
      if (sureRemove ((MModelElement) target)) {
        // System.out.println("deleting "+target+"+ "+(((MModelElement)target).getMElementListeners()).size());
        p.moveToTrash (target);
      }
    }
    // needs-more-work: trashing diagrams
    else {
      int size = 0;
      try {
        Editor ce = Globals.curEditor();
        Vector figs = ce.getSelectionManager().getFigs();
        size = figs.size();
        for (int i = 0; i < size; i++) {
          Fig f = (Fig) figs.elementAt (i);
          Object owner = f.getOwner();
          if (owner instanceof MModelElement) {
            if (! sureRemove ((MModelElement) owner)) return;
          }
        }
        
        for (int i = 0; i < size; i++) {
          Fig f = (Fig) figs.elementAt (i);
          Object owner = f.getOwner();
          if (owner == null) f.delete();
          else if (owner instanceof MModelElement) p.moveToTrash (owner);
        }
      }
      catch(Exception ex) {}
    }
    
    super.actionPerformed (ae);
  }
  
  public boolean sureRemove (MModelElement me) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    
    int count = p.getPresentationCountFor (me);
    
    boolean doAsk = false;
    String confirmStr = "";
    if (count > 1) {
      confirmStr += Localizer.localize (
          "Actions",
          "text.remove_from_model.will_remove_from_diagrams"
        );
      doAsk = true;
    }
    
    Collection beh = me.getBehaviors();
    if (beh != null && beh.size() > 0) {
	    confirmStr += Localizer.localize (
          "Actions",
          "text.remove_from_model.will_remove_subdiagram"
        );
      doAsk = true;
    }
    
    if (!doAsk) {
      return true;
    }
    
    String name = me.getName();
    if (name == null || name.equals ("")) {
      name = Localizer.localize ("Actions", "text.remove_from_model.anon_element_name");
    }
    
    confirmStr = MessageFormat.format (
        Localizer.localize (
          "Actions",
          "template.remove_from_model.confirm_delete"
        ),
        new Object[] {name, confirmStr}
      );
    int response = JOptionPane.showConfirmDialog (
        pb,
        confirmStr,
        Localizer.localize ("Actions", "text.remove_from_model.confirm_delete_title"),
        JOptionPane.YES_NO_OPTION
      );
    
    return (response == JOptionPane.YES_OPTION);
  }
} /* end class ActionRemoveFromModel */