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
      if (value instanceof uci.uml.Foundation.Core.Class) lab.setIcon(_ClassIcon);
      if (value instanceof Package) lab.setIcon(_PackageIcon);
      if (value instanceof Association) lab.setIcon(_AssociationIcon);
      if (value instanceof Generalization) lab.setIcon(_GeneralizationIcon);
      //if (value instanceof Realization) lab.setIcon(_RealizationIcon);
      if (value instanceof Diagram) lab.setIcon(_ClassDiagramIcon);
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
