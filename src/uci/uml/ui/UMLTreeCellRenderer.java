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



package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.plaf.basic.*;

import uci.gef.*;
import uci.uml.visual.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Behavioral_Elements.Collaborations.*;
import uci.uml.Model_Management.*;

public class UMLTreeCellRenderer extends BasicTreeCellRenderer {
  ////////////////////////////////////////////////////////////////
  // class variables

  protected ImageIcon _ActionStateIcon = loadIconResource("ActionState");
  protected ImageIcon _StateIcon = loadIconResource("State");
  protected ImageIcon _InitialStateIcon = loadIconResource("Initial");
  protected ImageIcon _DeepIcon = loadIconResource("DeepHistory");
  protected ImageIcon _ShallowIcon = loadIconResource("ShallowHistory");
  protected ImageIcon _ForkIcon = loadIconResource("Fork");
  protected ImageIcon _JoinIcon = loadIconResource("Join");
  protected ImageIcon _BranchIcon = loadIconResource("Branch");
  protected ImageIcon _FinalStateIcon = loadIconResource("FinalState");



  ////////////////////////////////////////////////////////////////
  // TreeCellRenderer implementation

  public Component getTreeCellRendererComponent(JTree tree, Object value,
						boolean sel,
						boolean expanded,
						boolean leaf, int row,
						boolean hasFocus) {

    Component r = super.getTreeCellRendererComponent(tree, value, sel,
						     expanded, leaf,
						     row, hasFocus);

    if (r instanceof JLabel) {
      JLabel lab = (JLabel) r;
/////////////////////////////////////////////////////////////////////      
      String stringValue = value.getClass().getName();
      //System.out.println("stringValue = " + stringValue);

      StringTokenizer st = new StringTokenizer(stringValue,".");
      String lastToken = "";
      while (st.hasMoreTokens())
        lastToken = st.nextToken();
      lastToken = lastToken.trim();
      
      // special case "UML*" e.g. UMLClassDiagram
      if (lastToken.startsWith("UML"))
        lastToken = lastToken.substring(3);

      // special case "MM*" e.g. MMClass
      if (lastToken.startsWith("MM"))
        lastToken = lastToken.substring(2);
        
      lab.setIcon(loadIconResource(lastToken));
      
      //System.out.println("lastToken = " + lastToken);
      
      if (value instanceof Pseudostate) {
	    Pseudostate ps = (Pseudostate) value;
	    PseudostateKind kind = ps.getKind();
	    if (PseudostateKind.INITIAL.equals(kind)) lab.setIcon(_InitialStateIcon);
	    if (PseudostateKind.DEEP_HISTORY.equals(kind)) lab.setIcon(_DeepIcon);
	    if (PseudostateKind.SHALLOW_HISTORY.equals(kind)) lab.setIcon(_ShallowIcon);
	    if (PseudostateKind.FORK.equals(kind)) lab.setIcon(_ForkIcon);
	    if (PseudostateKind.JOIN.equals(kind)) lab.setIcon(_JoinIcon);
	    if (PseudostateKind.BRANCH.equals(kind)) lab.setIcon(_BranchIcon);
	    if (PseudostateKind.FINAL.equals(kind)) lab.setIcon(_FinalStateIcon);
      }
//       else if (value instanceof
// 	       uci.uml.Behavioral_Elements.State_Machines.ActionState)
// 	    lab.setIcon(_ActionStateIcon);
//       else if (value instanceof State) lab.setIcon(_StateIcon);

      String tip = (value == null) ? "null" : value.toString();
      if (value instanceof ElementImpl)
	    tip = ((ElementImpl)value).getOCLTypeStr() + ": " +
	                               ((ElementImpl)value).getName().getBody();
      lab.setToolTipText(tip);
      tree.setToolTipText(tip);

    }
    return r;
  }

  ////////////////////////////////////////////////////////////////
  // utility functions

  protected static ImageIcon loadIconResource(String name) {
    String imgName = imageName(name);
    ImageIcon res = null;
    try {
      java.net.URL imgURL = UMLTreeCellRenderer.class.getResource(imgName);
      return new ImageIcon(imgURL);
    }
    catch (Exception ex) {
      return new ImageIcon(name);
    }
  }

  protected static String imageName(String name) {
    return "/uci/Images/" + stripJunk(name) + ".gif";
    //return "/uci/Images/Tree" + stripJunk(name) + ".gif";
  }

  protected static String stripJunk(String s) {
    String res = "";
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      if (Character.isJavaLetterOrDigit(c)) res += c;
    }
    return res;
  }

} /* end class UMLTreeCellRenderer */
