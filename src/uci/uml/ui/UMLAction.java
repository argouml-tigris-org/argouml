// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


package uci.uml.ui;

import java.util.*;
import java.beans.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.util.*;
import uci.argo.kernel.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Model_Management.*;
import uci.uml.visual.*;


public class UMLAction extends AbstractAction {

  public UMLAction(String name) { this(name, true); }
  
  public UMLAction(String name, boolean global) { 
    super(name, loadIconResource(imageName(name), name));
    putValue(Action.SHORT_DESCRIPTION, name);
    if (global) Actions._allActions.addElement(this);
  }

  protected static ImageIcon loadIconResource(String imgName, String desc) {
    ImageIcon res = null;
    try {
      java.net.URL imgURL = UMLAction.class.getResource(imgName);
      return new ImageIcon(imgURL, desc);
    }
    catch (Exception ex) {
      return new ImageIcon(desc);
    }
  }

  protected static String imageName(String name) {
    return "/uci/Images/" + stripJunk(name) + ".gif";
  }
  

  /** Perform the work the action is supposed to do. */
  // needs-more-work: should actions run in their own threads?
  public void actionPerformed(ActionEvent e) {
    System.out.println("pushed " + getValue(Action.NAME));
    StatusBar sb = ProjectBrowser.TheInstance.getStatusBar();
    sb.doFakeProgress(stripJunk(getValue(Action.NAME).toString()), 100);
    History.TheHistory.addItem("pushed " + getValue(Action.NAME));
    Actions.updateAllEnabled();
  }


  public void updateEnabled(Object target) { setEnabled(shouldBeEnabled()); }
  public void updateEnabled() { setEnabled(shouldBeEnabled()); }

  /** return true if this action should be available to the user. This
   *  method should examine the ProjectBrowser that owns it.  Sublass
   *  implementations of this method should always call
   *  super.shouldBeEnabled first. */
  public boolean shouldBeEnabled() { return true; }


  protected static String stripJunk(String s) {
    String res = "";
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      if (Character.isJavaLetterOrDigit(c)) res += c;
    }
    return res;
  }
} /* end class UMLAction */
