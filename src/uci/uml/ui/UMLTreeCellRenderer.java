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
import com.sun.java.util.collections.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;

import uci.gef.*;
import uci.util.Util;
import uci.uml.visual.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.model_management.*;

public class UMLTreeCellRenderer extends DefaultTreeCellRenderer {
  ////////////////////////////////////////////////////////////////
  // class variables

  protected ImageIcon _ActionStateIcon = Util.loadIconResource("ActionState");
  protected ImageIcon _StateIcon = Util.loadIconResource("State");
  protected ImageIcon _InitialStateIcon = Util.loadIconResource("Initial");
  protected ImageIcon _DeepIcon = Util.loadIconResource("DeepHistory");
  protected ImageIcon _ShallowIcon = Util.loadIconResource("ShallowHistory");
  protected ImageIcon _ForkIcon = Util.loadIconResource("Fork");
  protected ImageIcon _JoinIcon = Util.loadIconResource("Join");
  protected ImageIcon _BranchIcon = Util.loadIconResource("Branch");
  protected ImageIcon _FinalStateIcon = Util.loadIconResource("FinalState");

  protected Hashtable _iconCache = new Hashtable();



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
      Icon icon = (Icon) _iconCache.get(value.getClass());

      if (value instanceof MPseudostate) {
	MPseudostate ps = (MPseudostate) value;
	MPseudostateKind kind = ps.getKind();
	if (MPseudostateKind.INITIAL.equals(kind)) icon = _InitialStateIcon;
	if (MPseudostateKind.DEEP_HISTORY.equals(kind)) icon = _DeepIcon;
	if (MPseudostateKind.SHALLOW_HISTORY.equals(kind)) icon = _ShallowIcon;
	if (MPseudostateKind.FORK.equals(kind)) icon = _ForkIcon;
	if (MPseudostateKind.JOIN.equals(kind)) icon = _JoinIcon;
	if (MPseudostateKind.BRANCH.equals(kind)) icon = _BranchIcon;
	if (MPseudostateKind.FINAL.equals(kind)) icon = _FinalStateIcon;
      }

      if (icon == null) {
	String clsPackName = value.getClass().getName();
	if (clsPackName.startsWith("uci")) {
	  String cName = clsPackName.substring(clsPackName.lastIndexOf(".")+1);
	  // special case "UML*" e.g. UMLClassDiagram
	  if (cName.startsWith("UML")) cName = cName.substring(3);
	  // special case "MM*" e.g. MClass
	  if (cName.startsWith("M")) cName = cName.substring(2);
	  icon = Util.loadIconResource(cName);
	  if (icon != null) _iconCache.put(value.getClass(), icon);
	}
      }

      if (icon != null) lab.setIcon(icon);

      String tip = (value == null) ? "null" : value.toString();
      if (value instanceof MModelElementImpl)
	    tip = ((MModelElementImpl)value).getUMLClassName() + ": " +
	                               ((MModelElementImpl)value).getName();
      lab.setToolTipText(tip + " ");
      tree.setToolTipText(tip + " ");

    }
    return r;
  }


} /* end class UMLTreeCellRenderer */
