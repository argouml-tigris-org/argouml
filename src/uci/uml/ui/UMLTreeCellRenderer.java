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
import uci.uml.Model_Management.*;

public class UMLTreeCellRenderer extends BasicTreeCellRenderer {
  ////////////////////////////////////////////////////////////////
  // class variables
  protected ImageIcon _AttributeIcon = loadIconResource("Attribute");
  protected ImageIcon _OperationIcon = loadIconResource("Operation");
  protected ImageIcon _ClassIcon = loadIconResource("Class");
  protected ImageIcon _PackageIcon = loadIconResource("Package");
  protected ImageIcon _AssociationIcon = loadIconResource("Association");
  protected ImageIcon _AssociationIcon2 = loadIconResource("Association2");
  protected ImageIcon _AssociationIcon3 = loadIconResource("Association3");
  protected ImageIcon _AssociationIcon4 = loadIconResource("Association4");
  protected ImageIcon _AssociationIcon5 = loadIconResource("Association5");
  protected ImageIcon _GeneralizationIcon = loadIconResource("Generalization");
  protected ImageIcon _RealizationIcon = loadIconResource("Realization");
  protected ImageIcon _ClassDiagramIcon = loadIconResource("ClassDiagram");
  protected ImageIcon _UseCaseDiagramIcon = loadIconResource("UseCaseDiagram");
  protected ImageIcon _StateDiagramIcon = loadIconResource("UseCaseDiagram");


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
      if (value instanceof Attribute) lab.setIcon(_AttributeIcon);
      if (value instanceof Operation) lab.setIcon(_OperationIcon);
      if (value instanceof MMClass) lab.setIcon(_ClassIcon);
      if (value instanceof Package) lab.setIcon(_PackageIcon);
      if (value instanceof Association) lab.setIcon(_AssociationIcon);
      if (value instanceof Generalization) lab.setIcon(_GeneralizationIcon);
      //if (value instanceof Realization) lab.setIcon(_RealizationIcon);
      if (value instanceof UMLClassDiagram) lab.setIcon(_ClassDiagramIcon);
      if (value instanceof UMLUseCaseDiagram) lab.setIcon(_UseCaseDiagramIcon);
      if (value instanceof UMLStateDiagram) lab.setIcon(_StateDiagramIcon);
      lab.setToolTipText(value.toString());

      ProjectBrowser pb = ProjectBrowser.TheInstance;
      if (pb != null) {
	Object mainTarget = pb.getTarget();
	if (value != mainTarget) lab.setBorder(null);
	else lab.setBorder(LineBorder.createGrayLineBorder());
      }
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
    return "/uci/Images/Tree" + stripJunk(name) + ".gif";
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
