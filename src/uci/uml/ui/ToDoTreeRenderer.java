package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.border.*;
import com.sun.java.swing.plaf.basic.*;
import com.sun.java.swing.plaf.metal.*;

import uci.argo.kernel.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;

public class ToDoTreeRenderer extends BasicTreeCellRenderer {
  ////////////////////////////////////////////////////////////////
  // class variables
  protected ImageIcon _PostIt0 = loadIconResource("PostIt0");
  protected ImageIcon _PostIt25 = loadIconResource("PostIt25");
  protected ImageIcon _PostIt50 = loadIconResource("PostIt50");
  protected ImageIcon _PostIt75 = loadIconResource("PostIt75");
  protected ImageIcon _PostIt99 = loadIconResource("PostIt99");
  protected ImageIcon _MultiPostIt = loadIconResource("MultiPostIt");
  

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
      if (value instanceof ToDoItem) {
	ToDoItem item = (ToDoItem) value;
	if (item.getProgress() == 0) lab.setIcon(_PostIt0);
	else if (item.getProgress() <= 25) lab.setIcon(_PostIt25);
	else if (item.getProgress() <= 50) lab.setIcon(_PostIt50);
	else if (item.getProgress() <= 75) lab.setIcon(_PostIt75);
	else if (item.getProgress() <= 100) lab.setIcon(_PostIt99);
      }
      if (value instanceof Decision) {
	lab.setIcon(MetalIconFactory.getTreeFolderIcon());
      }
      if (value instanceof Goal) {
	lab.setIcon(MetalIconFactory.getTreeFolderIcon());
      }
      if (value instanceof Poster) {
	lab.setIcon(MetalIconFactory.getTreeFolderIcon());
      }
      //       if (value instanceof PriorityNode) {
      //       }
      //       if (value instanceof KnowledgeType) {
      //       }
      lab.setToolTipText(value.toString());
    }
    return r;
  }

  ////////////////////////////////////////////////////////////////
  // utility functions
  
  protected static ImageIcon loadIconResource(String name) {
    String imgName = imageName(name);
    ImageIcon res = null;
    try {
      java.net.URL imgURL = ToDoTreeRenderer.class.getResource(imgName);
      return new ImageIcon(imgURL);
    }
    catch (Exception ex) {
      return new ImageIcon(name);
    }
  }

  protected static String imageName(String name) {
    return "/uci/Images/" + stripJunk(name) + ".gif";
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
  

  
} /* end class ToDoTreeRenderer */
