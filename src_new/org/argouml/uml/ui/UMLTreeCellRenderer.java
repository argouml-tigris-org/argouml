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

package org.argouml.uml.ui;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.log4j.Category;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;

/** UMTreeCellRenderer determines how the entries in the Navigationpane will be
 *  represented graphically. In particular it makes decisions about the icons
 *  to use in order to display a Navigationpane artifact depending on the kind
 *  of object to be displayed.
 */
public class UMLTreeCellRenderer extends DefaultTreeCellRenderer {
            protected static Category cat = Category.getInstance(UMLTreeCellRenderer.class);
  ////////////////////////////////////////////////////////////////
  // class variables

  protected ImageIcon _ActionStateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("ActionState");
  protected ImageIcon _StateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("State");
  protected ImageIcon _InitialStateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Initial");
  protected ImageIcon _DeepIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("DeepHistory");
  protected ImageIcon _ShallowIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("ShallowHistory");
  protected ImageIcon _ForkIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Fork");
  protected ImageIcon _JoinIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Join");
  protected ImageIcon _BranchIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Branch");
  protected ImageIcon _FinalStateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("FinalState");

  protected ImageIcon _RealizeIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Realization");

  protected ImageIcon _SignalIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("SignalSending");

  protected ImageIcon _CommentIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Note");
  
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
              //if (MPseudostateKind.FINAL.equals(kind)) icon = _FinalStateIcon;
          }
          if (value instanceof MAbstraction) {
                  icon = _RealizeIcon;
          }
          // needs more work: sending and receiving icons
          if (value instanceof MSignal) {
              icon = _SignalIcon;
          }
          
          if (value instanceof MComment) {
              icon = _CommentIcon;
          }

          if (icon == null) {
              String clsPackName = value.getClass().getName();
              if (clsPackName.startsWith("org") || clsPackName.startsWith("ru")) {
                  String cName =
                      clsPackName.substring(clsPackName.lastIndexOf(".")+1);
                  // special case "UML*" e.g. UMLClassDiagram
                  if (cName.startsWith("UML")) cName = cName.substring(3);
                  if (cName.startsWith("M"))
                      cName = cName.substring(1);
                  if (cName.endsWith("Impl"))
                      cName = cName.substring(0,cName.length() -4 );
                  icon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource(cName);
                  if (icon != null) _iconCache.put(value.getClass(), icon);
                  if (icon == null) cat.warn("UMLTreeCellRenderer: using default Icon for " + cName);
              }
          }

          if (icon != null) lab.setIcon(icon);

          String tip;
          if (value instanceof MModelElement)
              tip = ((MModelElement)value).getUMLClassName() + ": " +
                  ((MModelElement)value).getName() + " ";
	  else 
	      tip = (value == null) ? "null " : value.toString() + " ";
          lab.setToolTipText(tip);
          tree.setToolTipText(tip);

      }
      return r;
  }


} /* end class UMLTreeCellRenderer */
